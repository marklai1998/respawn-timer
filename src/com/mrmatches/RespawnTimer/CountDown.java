package com.mrmatches.RespawnTimer;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class CountDown extends BukkitRunnable {
    private UUID uuid;
    private int timeRemain;

    CountDown(UUID uuid, int timeRemain) {
        this.uuid = uuid;
        this.timeRemain = timeRemain;
        Utils.displayMessage(uuid, timeRemain);
    }

    public void run() {
        if (timeRemain == 0) {
            clearTimer();
            return;
        }
        timeRemain--;
        Utils.displayMessage(uuid, timeRemain);
    }

    protected void clearTimer() {
        Utils.respawn(uuid);
        RespawnTimer.countDowns.remove(this);
        cancel();
    }

    protected UUID getUUID() {
        return uuid;
    }
}
