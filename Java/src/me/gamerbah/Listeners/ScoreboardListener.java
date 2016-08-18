package me.gamerbah.Listeners;
/* Created by GamerBah on 8/15/2016 */


import lombok.Getter;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.BoldColor;
import me.gamerbah.Utils.KDRatio;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

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
        player.setScoreboard(board);
        Objective objective = board.registerNewObjective("Player Data", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        board.registerNewTeam("Team");
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
            objective.getScore(BoldColor.GREEN.getColor() + "Kills: " + ChatColor.GRAY + playerData.getKills()).setScore(7);
            kills.put(player.getUniqueId(), playerData.getKills());

            objective.getScore(BoldColor.RED.getColor() + "Deaths: " + ChatColor.GRAY + playerData.getDeaths()).setScore(6);
            deaths.put(player.getUniqueId(), playerData.getDeaths());

            KDRatio kdRatio = new KDRatio(plugin);
            objective.getScore(BoldColor.YELLOW.getColor() + "K/D Ratio: " + ChatColor.GRAY + "" + kdRatio.getRatio(player)).setScore(5);
            kds.put(player.getUniqueId(), ChatColor.GRAY + "" + kdRatio.getRatio(player));

            objective.getScore("   ").setScore(4);

            // Currencies
            objective.getScore(BoldColor.AQUA.getColor() + "Souls: " + ChatColor.GRAY + playerData.getSouls()).setScore(3);
            souls.put(player.getUniqueId(), playerData.getSouls());

            objective.getScore(BoldColor.PINK.getColor() + "Battle Coins: " + ChatColor.GRAY + playerData.getCoins()).setScore(2);
            souls.put(player.getUniqueId(), playerData.getSouls());
        }
        Objective healthObjective = board.registerNewObjective("showhealth", "health");
        healthObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        healthObjective.setDisplayName(ChatColor.RED + "\u2764");
        player.setHealth(player.getHealth());
        player.setScoreboard(board);
    }

    public void updateScoreboardRank(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("Player Data");
        if (ranks.containsKey(player.getUniqueId())) {
            board.resetScores(BoldColor.WHITE.getColor() + "Rank: " + ranks.get(player.getUniqueId()));
            if (playerData.getRank().equals(Rank.DEFAULT)) {
                objective.getScore(BoldColor.WHITE.getColor() + "Rank: " + playerData.getRank().getColor() + playerData.getRank().getName()).setScore(9);
            } else {
                objective.getScore(BoldColor.WHITE.getColor() + "Rank: " + playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase()).setScore(9);
            }
        }
        player.setScoreboard(board);
    }

    public void updateScoreboardKills(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("Player Data");
        if (kills.containsKey(player.getUniqueId())) {
            board.resetScores(BoldColor.GREEN.getColor() + "Kills: " + ChatColor.GRAY + kills.get(player.getUniqueId()));
            objective.getScore(BoldColor.GREEN.getColor() + "Kills: " + ChatColor.GRAY + playerData.getKills()).setScore(7);
        }
        player.setScoreboard(board);
    }

    public void updateScoreboardDeaths(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("Player Data");
        if (deaths.containsKey(player.getUniqueId())) {
            board.resetScores(BoldColor.RED.getColor() + "Deaths: " + ChatColor.GRAY + deaths.get(player.getUniqueId()));
            objective.getScore(BoldColor.RED.getColor() + "Deaths: " + ChatColor.GRAY + playerData.getDeaths()).setScore(6);
        }
        player.setScoreboard(board);
    }

    public void updateScoreboardRatio(Player player) {
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("Player Data");
        if (kds.containsKey(player.getUniqueId())) {
            KDRatio kdRatio = new KDRatio(plugin);
            board.resetScores(BoldColor.YELLOW.getColor() + "K/D Ratio: " + kds.get(player.getUniqueId()));
            objective.getScore(BoldColor.YELLOW.getColor() + "K/D Ratio: " + ChatColor.GRAY + "" + kdRatio.getRatio(player)).setScore(5);
        }
        player.setScoreboard(board);
    }

    public void updateScoreboardSouls(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("Player Data");
        if (souls.containsKey(player.getUniqueId())) {
            board.resetScores(BoldColor.AQUA.getColor() + "Souls: " + ChatColor.GRAY + souls.get(player.getUniqueId()));
            objective.getScore(BoldColor.AQUA.getColor() + "Souls: " + ChatColor.GRAY + playerData.getSouls()).setScore(3);
        }
        player.setScoreboard(board);
    }

    public void updateScoreboardCoins(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("Player Data");
        if (coins.containsKey(player.getUniqueId())) {
            board.resetScores(BoldColor.PINK.getColor() + "Battle Coins: " + ChatColor.GRAY + coins.get(player.getUniqueId()));
            objective.getScore(BoldColor.PINK.getColor() + "Battle Coins: " + ChatColor.GRAY + playerData.getCoins()).setScore(2);
        }
        player.setScoreboard(board);
    }

}
