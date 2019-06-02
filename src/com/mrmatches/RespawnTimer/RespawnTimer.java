package com.mrmatches.RespawnTimer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RespawnTimer extends JavaPlugin implements Listener {
    protected static List<CountDown> countDowns = new ArrayList<>();
    protected static String scoreBoardTag = "DeathPlayer";
    private int timeDelay = 0;
    private boolean whitelistOP = false;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        timeDelay = getConfig().getInt("TimeDelay");
        scoreBoardTag = getConfig().getString("ScoreBoardTag");
        whitelistOP = getConfig().getBoolean("WhitelistOP");
    }

    @Override
    public void onDisable() {
        while (countDowns.size() > 0) {
            //timer will remove itself from the list, thus always get index 0
            CountDown timer = countDowns.get(0);
            timer.clearTimer();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (whitelistOP && player.isOp()) return;

        //prevent double dying
        if (Utils.hasTag(player)) return;

        UUID uuid = player.getUniqueId();
        Utils.death(uuid);
        CountDown timer = new CountDown(uuid, timeDelay);
        timer.runTaskTimer(this, 0L, 20L);
        countDowns.add(timer);
    }

    @EventHandler
    //prevent player movement
    public void onMove(PlayerMoveEvent e) {
        if (Utils.hasTag(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    //prevent damage taking while player died in the void
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (Utils.hasTag(player)) e.setCancelled(true);
    }

    @EventHandler
    //respawn player if there is no timer running for that player
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!Utils.hasTag(player)) {
            return;
        }
        UUID uuid = player.getUniqueId();
        for (CountDown timer : countDowns) {
            if (timer.getUUID().compareTo(uuid) == 0) return;
        }
        Utils.respawn(uuid);
    }

    @EventHandler
    //prevent player teleportation in DIED spectator mode
    public void onTeleport(PlayerTeleportEvent e) {
        if (!Utils.hasTag(e.getPlayer())) return;
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) e.setCancelled(true);
    }
}
