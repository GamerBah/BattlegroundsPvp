package me.gamerbah.Administration.Commands;
/* Created by GamerBah on 8/18/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.Donations.DonationMessages;
import me.gamerbah.Utils.Donations.Essence;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EssenceCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public EssenceCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            plugin.sendNoPermission((Player) sender);
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage("<UUID> <Time> <Increase>");
            return true;
        }

        PlayerData playerData = plugin.getPlayerData(UUID.fromString(args[0]));
        if (playerData == null) {
            sender.sendMessage("User hasn't joined before!");
            return true;
        }

        if (!args[1].matches("[1,3,6]")) {
            sender.sendMessage("Can only assign time values of 1, 3, or 6");
            return true;
        }

        if (Integer.parseInt(args[2]) != 50 && Integer.parseInt(args[2]) != 100 && Integer.parseInt(args[2]) != 150) {
            sender.sendMessage("Can only assign increase values of 50, 100, or 150");
            return true;
        }


        Essence.Type eType = null;

        for (Essence.Type type : Essence.Type.values()) {
            if (Integer.parseInt(args[1]) == type.getTime() && Integer.parseInt(args[2]) == type.getIncrease()) {
                eType = type;
            }
        }

        if (eType == null) {
            sender.sendMessage("Fatal Error processing request: Unknown essence type.");
            return true;
        }

        playerData.setEssences(playerData.getEssences() + eType.getDisplayName() + ",");
        sender.sendMessage("Success! Donation registered.");
        Player player = plugin.getServer().getPlayer(UUID.fromString(args[0]));
        DonationMessages donationMessages = new DonationMessages(plugin);
        donationMessages.sendEssensePurchaseMessage(player, eType);
        return true;
    }
}
