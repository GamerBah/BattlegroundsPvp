package me.gamerbah.Administration.Data;
/* Created by GamerBah on 8/22/2016 */


import me.gamerbah.Administration.Donations.Essence;
import me.gamerbah.Battlegrounds;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EssenceData {

    private Battlegrounds plugin;
    private MySQL sql = Battlegrounds.getSql();

    public EssenceData(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void setEssence(Player player, Essence.Type type, int amount) {
        sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT, amount, player.getUniqueId().toString(), type.toString());
        plugin.getEssenceData(type).put(player.getUniqueId(), amount);
    }

    public void setEssence(UUID uuid, Essence.Type type, int amount) {
        sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT, amount, uuid.toString(), type.toString());
        plugin.getEssenceData(type).put(uuid, amount);
    }

    public void removeEssence(Player player, Essence.Type type) {
        sql.executeUpdate(Query.DELETE_ESSENCE_DATA, player.getUniqueId().toString(), type.toString());
        plugin.getEssenceData(type).put(player.getUniqueId(), 0);
    }
}
