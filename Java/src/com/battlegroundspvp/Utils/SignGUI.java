package com.battlegroundspvp.Utils;

import com.battlegroundspvp.Utils.Packets.Signs.PacketOpenSignEditor;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.entity.Player;

public class SignGUI {

    public static void open(Player player) {
        PacketOpenSignEditor openSignEditor = new PacketOpenSignEditor();
        openSignEditor.setLocation(new BlockPosition(0, 0, 0));
        openSignEditor.sendPacket(player);
    }
}