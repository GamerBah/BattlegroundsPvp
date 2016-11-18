package com.battlegroundspvp.Administration.Commands;
/* Created by GamerBah on 8/18/2016 */


import com.battlegroundspvp.Administration.Data.EssenceData;
import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Donations.DonationMessages;
import com.battlegroundspvp.Administration.Donations.Essence;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import net.gpedro.integrations.slack.SlackMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DonationCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public DonationCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            plugin.sendNoPermission((Player) sender);
            return true;
        }

        if (args.length == 0) {
            plugin.getLogger().severe("Error Processing Donation Request: Invalid Arguments!");
            plugin.slackErrorReporting.call(new SlackMessage("Invalid Arguments while attempting to process new donation!\nIssue: 0 arguments"));
            return true;
        }

        if (args.length > 0) {
            if (args[0].equals("rank")) {
                if (args.length != 3) {
                    plugin.getLogger().severe("Error Processing Donation Request: \"Rank\" donation set incorrectly filled");
                    plugin.slackErrorReporting.call(new SlackMessage("Invalid Arguments while attempting to process new donation!\nIssue: \"Rank\" info not filled out correctly."));
                    return true;
                }
                PlayerData playerData = plugin.getPlayerData(UUID.fromString(args[1]));
                if (playerData == null) {
                    plugin.getLogger().severe("Error Processing Donation Request: Requested UUID not found in database!");
                    plugin.slackErrorReporting.call(new SlackMessage("Error while attempting to process new donation!\nIssue: Requested UUID was not found in server database."));
                    return true;
                }

                for (Rank rank : Rank.values()) {
                    if (!args[2].equalsIgnoreCase(rank.getName())) {
                        plugin.getLogger().severe("Error Processing Donation Request: Requested rank change invalid");
                        plugin.slackErrorReporting.call(new SlackMessage("Error while attempting to process new donation!\nIssue: Requested Rank was invalid."));
                        return true;
                    }
                }
                plugin.getLogger().info("Success! Donation for Rank registered!");
                Rank rank = Rank.valueOf(args[2]);
                playerData.setRank(rank);
                plugin.slackDonations.call(new SlackMessage(">>> _*" + playerData.getName() + "* purchased the *" + rank.getName() + "* rank!_"));
                Player player = plugin.getServer().getPlayer(playerData.getUuid());
                if (player.isOnline()) {
                    DonationMessages donationMessages = new DonationMessages(plugin);
                    donationMessages.sendRankPurchaseMessage(player, rank);
                }
                return true;
            }
            if (args[0].equals("essence")) {
                if (args.length != 4) {
                    plugin.getLogger().severe("Error Processing Donation Request: \"Essence\" donation set incorrectly filled");
                    plugin.slackErrorReporting.call(new SlackMessage("Invalid Arguments while attempting to process new donation!\nIssue: \"Essence\" info not filled out correctly."));
                    return true;
                }
                PlayerData playerData = plugin.getPlayerData(UUID.fromString(args[1]));
                if (playerData == null) {
                    plugin.getLogger().severe("Error Processing Donation Request: Requested UUID not found in database!");
                    plugin.slackErrorReporting.call(new SlackMessage("Error while attempting to process new donation!\nIssue: Requested UUID was not found in server database."));
                    return true;
                }
                if (!args[1].matches("[1,3,6]")) {
                    plugin.getLogger().severe("Error Processing Donation Request: Time value invalid");
                    plugin.slackErrorReporting.call(new SlackMessage("Error while attempting to process new donation!\nIssue: Requested Essence time was invalid."));
                    return true;
                }

                if (Integer.parseInt(args[2]) != 50 && Integer.parseInt(args[2]) != 100 && Integer.parseInt(args[2]) != 150) {
                    plugin.getLogger().severe("Error Processing Donation Request: Invalid increase");
                    plugin.slackErrorReporting.call(new SlackMessage("Error while attempting to process new donation!\nIssue: Requested Essence increase was invalid."));
                    return true;
                }
                Essence.Type eType = null;
                for (Essence.Type type : Essence.Type.values()) {
                    if (Integer.parseInt(args[1]) == type.getTime() && Integer.parseInt(args[2]) == type.getIncrease()) {
                        eType = type;
                    }
                }
                if (eType == null) {
                    plugin.getLogger().severe("Fatal Error Processing Donation Request!: Essence Type invalid!");
                    plugin.slackErrorReporting.call(new SlackMessage("Error while attempting to process new donation!\nIssue: Essence Type was invalid."));
                    return true;
                }
                EssenceData essenceData = new EssenceData(plugin);
                if (!plugin.getEssenceData(eType).get(playerData.getUuid()).equals(0)) {
                    essenceData.setEssence(playerData.getUuid(), eType, plugin.getEssenceData(eType).get(playerData.getUuid()) + 1);
                } else {
                    plugin.createEssenceData(playerData.getUuid(), eType);
                }
                plugin.getLogger().info("Success! Donation for Rank registered!");
                Player player = plugin.getServer().getPlayer(playerData.getUuid());
                plugin.slackDonations.call(new SlackMessage(">>> _*" + player.getName() + "* purchased a *" + eType.getTime() + " hour (+" + eType.getIncrease() + "%) Battle Essence!*_"));
                if (player.isOnline()) {
                    DonationMessages donationMessages = new DonationMessages(plugin);
                    donationMessages.sendEssensePurchaseMessage(player, eType);
                }
            }
        }
        return true;
    }
}
