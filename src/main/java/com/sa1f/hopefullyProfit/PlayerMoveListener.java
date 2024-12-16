package com.sa1f.hopefullyProfit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.net.http.WebSocket;
import java.util.Timer;

public class PlayerMoveListener implements Listener {
    public boolean MovementRestricted = false;
    private int gulagID = -1;
    private int remainingSeconds = 30;
    private int gulagRemainingSeconds = 10;
    Gulag gulag;
    public boolean GulagMovementRestricted = false;
    startCommand startCommand = new startCommand();

    public PlayerMoveListener(Gulag gulag) {
        this.gulag = gulag;
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (MovementRestricted) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
                event.setCancelled(true);
            }
        }
        if(gulag.ActiveGulagMatch.contains(player) && GulagMovementRestricted){
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()){
                event.setCancelled(true);
            }

        }
    }

    public void startCountdown(){
        MovementRestricted = true;
        int[] countdownID = new int[1];
        countdownID[0] =  Bukkit.getScheduler().runTaskTimer(HopefullyProfit.getPlugin(HopefullyProfit.class), () -> {
            if(remainingSeconds > 0){
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.sendTitle(
                            ChatColor.GREEN + "Game starting in ",
                            ChatColor.GOLD + String.valueOf(remainingSeconds) + ChatColor.GREEN + " seconds",
                            0,
                            20,
                            0
                    );
                }remainingSeconds--;
            }else {
                MovementRestricted = false;
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 1, 1);
                }
                Bukkit.getScheduler().cancelTask(countdownID[0]);
                countdownID[0] = -1;

            }

        }, 0L, 20L).getTaskId();
    }

    public void startGulagCountdown(Player player1, Player player2, Runnable callback){
        GulagMovementRestricted = true;
        gulagID = Bukkit.getScheduler().runTaskTimer(HopefullyProfit.getPlugin(HopefullyProfit.class), new Runnable(){
            @Override
            public void run(){
                if(gulagRemainingSeconds > 0){
                    player1.sendMessage(ChatColor.GREEN + "Gulag match starting in " + gulagRemainingSeconds + " seconds");
                    player2.sendMessage(ChatColor.GREEN + "Gulag match starting in " + gulagRemainingSeconds + " seconds");
                    gulagRemainingSeconds--;
                    if(gulagRemainingSeconds <= 3){
                        player1.playSound(player1.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        player2.playSound(player2.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    }
                }else{
                    GulagMovementRestricted = false;
                    player1.sendMessage(ChatColor.GREEN + "Fight!");
                    player2.sendMessage(ChatColor.GREEN + "Fight!");
                    callback.run();
                    Bukkit.getScheduler().cancelTask(gulagID);
                    gulagID = -1; //reset task ID

                }
            }

        }, 0L, 20L).getTaskId();

    }


}
