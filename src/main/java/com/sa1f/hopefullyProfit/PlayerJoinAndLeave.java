package com.sa1f.hopefullyProfit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Score;

public class PlayerJoinAndLeave implements Listener {
    Scoreboard1 scoreboard1;

    PlayerJoinAndLeave(Scoreboard1 scoreboard1){
        this.scoreboard1 = scoreboard1;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        scoreboard1.updateScoreBoard(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        scoreboard1.updateScoreBoard(player);
    }



}
