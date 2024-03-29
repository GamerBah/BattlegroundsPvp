package com.battlegroundspvp.Listeners;
/* Created by GamerBah on 8/15/2016 */


import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Achievements.Achievement;
import com.battlegroundspvp.Utils.KDRatio;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardListener implements Listener {

    private Battlegrounds plugin;
    @Getter
    private Map<UUID, Integer> souls = new HashMap<>();
    @Getter
    private Map<UUID, Integer> coins = new HashMap<>();
    @Getter
    private Map<UUID, Integer> kills = new HashMap<>();
    @Getter
    private Map<UUID, Integer> deaths = new HashMap<>();
    @Getter
    private Map<UUID, String> ranks = new HashMap<>();
    @Getter
    private Map<UUID, String> kds = new HashMap<>();

    public ScoreboardListener(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("PlayerData", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        setupScoreboardTeams(player, board);
        objective.setDisplayName(BoldColor.GOLD.getColor() + "Battlegrounds");
        if (playerData != null) {
            objective.getScore(" ").setScore(10);
            // Rank
            if (playerData.getRank().equals(Rank.DEFAULT)) {
                objective.getScore(BoldColor.WHITE.getColor() + "Rank: " + playerData.getRank().getColor() + playerData.getRank().getName()).setScore(9);
                ranks.put(player.getUniqueId(), playerData.getRank().getColor() + playerData.getRank().getName());
            } else {
                objective.getScore(BoldColor.WHITE.getColor() + "Rank: " + playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase()).setScore(9);
                ranks.put(player.getUniqueId(), playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase());
            }

            objective.getScore("  ").setScore(8);

            // Kills, Deaths, KD
            objective.getScore(BoldColor.GREEN.getColor() + "Kills: " + ChatColor.GRAY + playerData.getKitPvpData().getKills()).setScore(7);
            kills.put(player.getUniqueId(), playerData.getKitPvpData().getKills());

            objective.getScore(BoldColor.RED.getColor() + "Deaths: " + ChatColor.GRAY + playerData.getKitPvpData().getDeaths()).setScore(6);
            deaths.put(player.getUniqueId(), playerData.getKitPvpData().getDeaths());

            KDRatio kdRatio = new KDRatio(plugin);
            objective.getScore(BoldColor.YELLOW.getColor() + "K/D Ratio: " + ChatColor.GRAY + "" + kdRatio.getRatio(player)).setScore(5);
            kds.put(player.getUniqueId(), ChatColor.GRAY + "" + kdRatio.getRatio(player));

            objective.getScore("   ").setScore(4);

            // Currencies
            objective.getScore(BoldColor.AQUA.getColor() + "Souls: " + ChatColor.GRAY + playerData.getKitPvpData().getSouls()).setScore(3);
            souls.put(player.getUniqueId(), playerData.getKitPvpData().getSouls());

            objective.getScore(BoldColor.PINK.getColor() + "Battle Coins: " + ChatColor.GRAY + playerData.getCoins()).setScore(2);
            souls.put(player.getUniqueId(), playerData.getKitPvpData().getSouls());
        }
        Objective healthObjective = board.registerNewObjective("showhealth", "health");
        healthObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        healthObjective.setDisplayName(ChatColor.RED + "\u2764");
        player.setHealth(player.getHealth());
        player.setScoreboard(board);
    }

    private void setupScoreboardTeams(Player player, Scoreboard board) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        player.setScoreboard(board);
        Team killer = board.registerNewTeam("killer");
        Team buzzkill = board.registerNewTeam("buzzkill");
        Team vengeful = board.registerNewTeam("vengeful");
        Team sadist = board.registerNewTeam("sadist");
        Team fragile = board.registerNewTeam("fragile");
        Team none = board.registerNewTeam("none");
        killer.setSuffix(BoldColor.GOLD.getColor() + " [KILLER]");
        buzzkill.setSuffix(BoldColor.GOLD.getColor() + " [BUZZKILL]");
        vengeful.setSuffix(BoldColor.GOLD.getColor() + " [VENGEFUL]");
        sadist.setSuffix(BoldColor.GOLD.getColor() + " [SADIST]");
        fragile.setSuffix(BoldColor.GOLD.getColor() + " [FRAGILE]");

        for (Player target : Bukkit.getOnlinePlayers()) {
            PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
            if (targetData.getKitPvpData().getTitle().equals(Achievement.Type.BRUTALITY_MASTER.toString())) {
                killer.addEntry(target.getName());
            } else if (targetData.getKitPvpData().getTitle().equals(Achievement.Type.BUZZKILL_MASTER.toString())) {
                buzzkill.addEntry(target.getName());
            } else if (targetData.getKitPvpData().getTitle().equals(Achievement.Type.VENGEFUL_MASTER.toString())) {
                vengeful.addEntry(target.getName());
            } else if (targetData.getKitPvpData().getTitle().equals(Achievement.Type.SADIST_MASTER.toString())) {
                sadist.addEntry(target.getName());
            } else if (targetData.getKitPvpData().getTitle().equals(Achievement.Type.DEATHKNELL.toString())) {
                fragile.addEntry(target.getName());
            } else if (targetData.getKitPvpData().getTitle().equals("TRAIL_NONE")) {
                none.addEntry(target.getName());
            }

            if (playerData.getKitPvpData().getTitle().equals(Achievement.Type.BRUTALITY_MASTER.toString())) {
                target.getScoreboard().getTeam("killer").addEntry(player.getName());
            } else if (playerData.getKitPvpData().getTitle().equals(Achievement.Type.BUZZKILL_MASTER.toString())) {
                target.getScoreboard().getTeam("buzzkill").addEntry(player.getName());
            } else if (playerData.getKitPvpData().getTitle().equals(Achievement.Type.VENGEFUL_MASTER.toString())) {
                target.getScoreboard().getTeam("vengeful").addEntry(player.getName());
            } else if (playerData.getKitPvpData().getTitle().equals(Achievement.Type.SADIST_MASTER.toString())) {
                target.getScoreboard().getTeam("sadist").addEntry(player.getName());
            } else if (playerData.getKitPvpData().getTitle().equals(Achievement.Type.DEATHKNELL.toString())) {
                target.getScoreboard().getTeam("fragile").addEntry(player.getName());
            } else if (playerData.getKitPvpData().getTitle().equals("TRAIL_NONE")) {
                target.getScoreboard().getTeam("none").addEntry(player.getName());
            }
        }
        for (Team team : board.getTeams()) {
            team.setAllowFriendlyFire(true);
            team.setCanSeeFriendlyInvisibles(false);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
    }

    public void reloadScoreboardTeams(Player player, Scoreboard board) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        player.setScoreboard(board);
        Team killer = board.getTeam("killer");
        Team buzzkill = board.getTeam("buzzkill");
        Team vengeful = board.getTeam("vengeful");
        Team sadist = board.getTeam("sadist");
        Team fragile = board.getTeam("fragile");
        Team none = board.getTeam("none");

        for (Team team : board.getTeams()) {
            team.setAllowFriendlyFire(true);
            team.setCanSeeFriendlyInvisibles(false);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
            if (targetData.getKitPvpData().getTitle().equals(Achievement.Type.BRUTALITY_MASTER.toString())) {
                killer.addEntry(target.getName());
            } else if (targetData.getKitPvpData().getTitle().equals(Achievement.Type.BUZZKILL_MASTER.toString())) {
                buzzkill.addEntry(target.getName());
            } else if (targetData.getKitPvpData().getTitle().equals(Achievement.Type.VENGEFUL_MASTER.toString())) {
                vengeful.addEntry(target.getName());
            } else if (targetData.getKitPvpData().getTitle().equals(Achievement.Type.SADIST_MASTER.toString())) {
                sadist.addEntry(target.getName());
            } else if (targetData.getKitPvpData().getTitle().equals(Achievement.Type.DEATHKNELL.toString())) {
                fragile.addEntry(target.getName());
            } else if (targetData.getKitPvpData().getTitle().equals("TRAIL_NONE")) {
                none.addEntry(target.getName());
            }

            if (playerData.getKitPvpData().getTitle().equals(Achievement.Type.BRUTALITY_MASTER.toString())) {
                target.getScoreboard().getTeam("killer").addEntry(player.getName());
            } else if (playerData.getKitPvpData().getTitle().equals(Achievement.Type.BUZZKILL_MASTER.toString())) {
                target.getScoreboard().getTeam("buzzkill").addEntry(player.getName());
            } else if (playerData.getKitPvpData().getTitle().equals(Achievement.Type.VENGEFUL_MASTER.toString())) {
                target.getScoreboard().getTeam("vengeful").addEntry(player.getName());
            } else if (playerData.getKitPvpData().getTitle().equals(Achievement.Type.SADIST_MASTER.toString())) {
                target.getScoreboard().getTeam("sadist").addEntry(player.getName());
            } else if (playerData.getKitPvpData().getTitle().equals(Achievement.Type.DEATHKNELL.toString())) {
                target.getScoreboard().getTeam("fragile").addEntry(player.getName());
            } else if (playerData.getKitPvpData().getTitle().equals("TRAIL_NONE")) {
                target.getScoreboard().getTeam("none").addEntry(player.getName());
            }
        }
    }

    public void updateScoreboardRank(Player player, Rank rank) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("PlayerData");
        getRanks().put(player.getUniqueId(), playerData.getRank().getColor() + (playerData.hasRank(Rank.WARRIOR)
                ? "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase() : playerData.getRank().getName()));
        playerData.setRank(rank);
        board.resetScores(BoldColor.WHITE.getColor() + "Rank: " + ranks.get(player.getUniqueId()));
        if (playerData.getRank().equals(Rank.DEFAULT)) {
            objective.getScore(BoldColor.WHITE.getColor() + "Rank: " + playerData.getRank().getColor() + playerData.getRank().getName()).setScore(9);
        } else {
            objective.getScore(BoldColor.WHITE.getColor() + "Rank: " + playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase()).setScore(9);
        }
        player.setScoreboard(board);
    }

    public void updateScoreboardKills(Player player, int amount) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("PlayerData");
        KDRatio kdRatio = new KDRatio(plugin);
        getKills().put(player.getUniqueId(), playerData.getKitPvpData().getKills());
        getKds().put(player.getUniqueId(), ChatColor.GRAY + "" + kdRatio.getRatio(player));
        playerData.getKitPvpData().addKill(amount);
        board.resetScores(BoldColor.GREEN.getColor() + "Kills: " + ChatColor.GRAY + kills.get(player.getUniqueId()));
        board.resetScores(BoldColor.YELLOW.getColor() + "K/D Ratio: " + kds.get(player.getUniqueId()));
        objective.getScore(BoldColor.GREEN.getColor() + "Kills: " + ChatColor.GRAY + playerData.getKitPvpData().getKills()).setScore(7);
        objective.getScore(BoldColor.YELLOW.getColor() + "K/D Ratio: " + ChatColor.GRAY + "" + kdRatio.getRatio(player)).setScore(5);
        player.setScoreboard(board);
    }

    public void updateScoreboardKills(Player player) {
        updateScoreboardKills(player, 1);
        plugin.getGlobalStats().addKill();
    }

    public void updateScoreboardDeaths(Player player, int amount) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("PlayerData");
        KDRatio kdRatio = new KDRatio(plugin);
        getDeaths().put(player.getUniqueId(), playerData.getKitPvpData().getDeaths());
        getKds().put(player.getUniqueId(), ChatColor.GRAY + "" + kdRatio.getRatio(player));
        playerData.getKitPvpData().addDeath(amount);
        board.resetScores(BoldColor.RED.getColor() + "Deaths: " + ChatColor.GRAY + deaths.get(player.getUniqueId()));
        board.resetScores(BoldColor.YELLOW.getColor() + "K/D Ratio: " + kds.get(player.getUniqueId()));
        objective.getScore(BoldColor.RED.getColor() + "Deaths: " + ChatColor.GRAY + playerData.getKitPvpData().getDeaths()).setScore(6);
        objective.getScore(BoldColor.YELLOW.getColor() + "K/D Ratio: " + ChatColor.GRAY + "" + kdRatio.getRatio(player)).setScore(5);
        player.setScoreboard(board);
    }

    public void updateScoreboardDeaths(Player player) {
        updateScoreboardDeaths(player, 1);
        plugin.getGlobalStats().addDeath();
    }

    public void updateScoreboardSouls(Player player, int amount) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("PlayerData");
        getSouls().put(player.getUniqueId(), playerData.getKitPvpData().getSouls());
        playerData.getKitPvpData().addSouls(amount);
        plugin.getGlobalStats().addSouls(amount);
        board.resetScores(BoldColor.AQUA.getColor() + "Souls: " + ChatColor.GRAY + souls.get(player.getUniqueId()));
        objective.getScore(BoldColor.AQUA.getColor() + "Souls: " + ChatColor.GRAY + playerData.getKitPvpData().getSouls()).setScore(3);
        player.setScoreboard(board);
    }

    public void updateScoreboardCoins(Player player, int amount) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("PlayerData");
        getCoins().put(player.getUniqueId(), playerData.getCoins());
        playerData.addCoins(amount);
        plugin.getGlobalStats().addCoins(amount);
        board.resetScores(BoldColor.PINK.getColor() + "Battle Coins: " + ChatColor.GRAY + coins.get(player.getUniqueId()));
        objective.getScore(BoldColor.PINK.getColor() + "Battle Coins: " + ChatColor.GRAY + playerData.getCoins()).setScore(2);
        player.setScoreboard(board);
    }

}
