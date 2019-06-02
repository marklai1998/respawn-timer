package com.mrmatches.RespawnTimer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Utils {
    protected static void displayMessage(UUID uuid, int time) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        player.sendTitle("你將在" + time + "秒後復活", null, 0, 20, 0);
    }

    protected static void respawn(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        player.setGameMode(GameMode.SURVIVAL);
        player.spigot().respawn();
        player.teleport(player.getWorld().getSpawnLocation());
        player.removeScoreboardTag(RespawnTimer.scoreBoardTag);
    }

    protected static void death(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        player.setHealth(20.0D);
        player.setGameMode(GameMode.SPECTATOR);
        player.addScoreboardTag(RespawnTimer.scoreBoardTag);
    }

    protected static boolean hasTag(Player player) {
        return player.getScoreboardTags().contains(RespawnTimer.scoreBoardTag);
    }
}
