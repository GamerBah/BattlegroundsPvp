package me.gamerbah.Utils.Teams;

import me.gamerbah.Battlegrounds;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;

public class TeamUtils {
    private Battlegrounds plugin;

    public TeamUtils(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public static void createPendingRequest(Player target, Player sender) {
        Battlegrounds.pendingTeams.put(target.getName(), sender.getName());
    }

    public static void removePendingRequest(Player target) {
        if (Battlegrounds.pendingTeams.isEmpty() || !Battlegrounds.pendingTeams.containsKey(target.getName())) {
            return;
        } else {
            Battlegrounds.pendingTeams.remove(target.getName());
        }
    }

    public boolean hasPendingRequest(Player target) {
        return Battlegrounds.pendingTeams.containsKey(target.getName());
    }

    public static void createTeam(Player target, Player sender) {
        Battlegrounds.currentTeams.put(target.getName(), sender.getName());
        removePendingRequest(target);
        GlowAPI.setGlowing(sender, GlowAPI.Color.WHITE, target);
        GlowAPI.setGlowing(target, GlowAPI.Color.WHITE, sender);
    }

    public static void removeTeam(Player player, Player target) {
        if (Battlegrounds.currentTeams.get(target.getName()) == player.getName()) {
            Battlegrounds.currentTeams.remove(target.getName());
        }
        if (Battlegrounds.currentTeams.get(player.getName()) == target.getName()) {
            Battlegrounds.currentTeams.remove(player.getName());
        } else {
            return;
        }
        GlowAPI.setGlowing(player, null, target);
        GlowAPI.setGlowing(target, null, player);
    }

    public boolean isTeaming(Player target) {
        for (Player sender : plugin.getServer().getOnlinePlayers()) {
            if (Battlegrounds.currentTeams.containsKey(target.getName()) || Battlegrounds.currentTeams.containsValue(target.getName())) {
                return true;
            }
        }
        return false;
    }

    public Player getRequester(Player target) {
        return plugin.getServer().getPlayer(Battlegrounds.pendingTeams.get(target.getName()));
    }

}
