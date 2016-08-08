package me.gamerbah;
/* Created by GamerBah on 8/7/2016 */


import lombok.Getter;
import me.gamerbah.Data.MySQL;
import me.gamerbah.Data.PlayerData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public class Battlegrounds extends JavaPlugin {

    @Getter
    private static MySQL sql = null;
    private HashSet<PlayerData> playerData = new HashSet<>();

    public void onEnable() {

        sql = new MySQL(this);

    }

    public void onDisable() {

    }

}
