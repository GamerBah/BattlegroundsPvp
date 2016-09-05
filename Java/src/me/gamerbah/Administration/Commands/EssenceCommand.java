package me.gamerbah.Administration.Commands;
/* Created by GamerBah on 8/18/2016 */


import me.gamerbah.Administration.Data.EssenceData;
import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Donations.DonationMessages;
import me.gamerbah.Administration.Donations.Essence;
import me.gamerbah.Battlegrounds;
import net.gpedro.integrations.slack.SlackMessage;
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

        if (args.length != 4) {
            sender.sendMessage("Required Arguments: <uuid> <time> <increase> <amount>");
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

        if (!args[3].matches("[0-9]+") || Integer.parseInt(args[3]) == 0) {
            sender.sendMessage("Can only give amounts greater than 0!");
            return true;
        }

        int amount = Integer.parseInt(args[3]);
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

        EssenceData essenceData = new EssenceData(plugin);
        if (!plugin.getEssenceData(eType).get(playerData.getUuid()).equals(0)) {
            essenceData.setEssence(playerData.getUuid(), eType, plugin.getEssenceData(eType).get(playerData.getUuid()) + amount);
        } else {
            plugin.createEssenceData(playerData.getUuid(), eType, amount);
        }
        sender.sendMessage("Success! Donation registered.");
        Player player = plugin.getServer().getPlayer(UUID.fromString(args[0]));

        plugin.slackDonations.call(new SlackMessage(">>> _*" + player.getName() + "* purchased a *" + eType.getTime() + " hour (+" + eType.getIncrease() + "%) Battle Essence!*_"));
        if (player.isOnline()) {
            DonationMessages donationMessages = new DonationMessages(plugin);
            donationMessages.sendEssensePurchaseMessage(player, eType);
        }
        return true;
    }
}
