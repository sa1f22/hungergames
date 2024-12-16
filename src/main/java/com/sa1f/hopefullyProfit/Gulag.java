package com.sa1f.hopefullyProfit;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Gulag implements Listener {
    Set<Player> gulagWinners = new HashSet<>();
    Queue<Player> gulagQueue = new LinkedList<>();
    HashSet<Player> ActiveGulagMatch = new HashSet<>();
    PlayerMoveListener playerMoveListener;
    int healthmonitorID = -1;
    int gulagTimeLimitID = -1;
    int playersReductionScoreboard = 0;

    public Gulag(PlayerMoveListener playerMoveListener) {
        this.playerMoveListener = playerMoveListener;
    }

    public Gulag() {
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();


        if (gulagWinners.contains(player)) {
            gulagWinners.remove(player);
            player.setGameMode(GameMode.SPECTATOR);
            Bukkit.broadcastMessage((player.getDisplayName() + ChatColor.RED + " has been eliminated!"));
            playersReductionScoreboard++;
            return;
        } else {
            gulagQueue.add(player);
            Bukkit.broadcastMessage((player.getDisplayName() + ChatColor.RED + " has been sent to the Gulag!"));
        }


        Location gulagLocation = LocationStorage.getGulagWaiting();
        player.setHealth(20.0); // Reset health
        player.setFoodLevel(20); // Reset food level
        player.teleport(gulagLocation);
        player.sendTitle(ChatColor.RED + "GULAG", ChatColor.GREEN + "YOU'RE IN QUEUE!", 10, 70, 20);
        postioninQueueTimer(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("HopefullyProfit"), 1L);

        if (gulagQueue.size() >= 2 && ActiveGulagMatch.isEmpty()) {
            Player player1 = gulagQueue.poll();
            Player player2 = gulagQueue.poll();

            ActiveGulagMatch.add(player1);
            ActiveGulagMatch.add(player2);

            startGulagMatch(player1, player2);

        }


    }

    public void startGulagMatch(Player player1, Player player2) {


        Location fightLocation1 = LocationStorage.getFightLocation1();
        Location fightLocation2 = LocationStorage.getFightLocation2();

        player1.teleport(fightLocation1);
        player2.teleport(fightLocation2);

        ItemStack snowballs = new ItemStack(Material.SNOWBALL, 10);
        player1.getInventory().addItem(snowballs);
        player2.getInventory().addItem(snowballs);

        player1.sendMessage(ChatColor.GREEN + "Fight to win your second chance!");
        player2.sendMessage(ChatColor.GREEN + "Fight to win your second chance!");
        player1.sendMessage(ChatColor.RED + "You have 2 minutes to kill your opponent. If you both survive, you are both eliminated.");
        player2.sendMessage(ChatColor.RED + "You have 2 minutes to kill your opponent. If you both survive, you are both eliminated.");

        Bukkit.broadcastMessage(ChatColor.GREEN + player1.getDisplayName() + " is fighting " + player2.getDisplayName() + " in the Gulag!");
        playerMoveListener.startGulagCountdown(player1, player2, () -> gulagTimeLimit(player1, player2));


        startHealthMonitor(player1, player2);

    }

    public void startHealthMonitor(Player player1, Player player2) {
        healthmonitorID = new BukkitRunnable() {
            @Override
            public void run() {
                if (player1.getHealth() <= 0) {
                    Bukkit.broadcastMessage(ChatColor.RED + player1.getDisplayName() + " lost the Gulag fight against " + player2.getDisplayName() + "and was eliminated!");
                    player1.sendMessage(ChatColor.RED + "You have been eliminated!");
                    playersReductionScoreboard++;
                    player2.sendMessage(ChatColor.GREEN + "Congraulations! You have won the Gulag fight and have been given a second chance!");
                    gulagWinners.add(player2);
                    player1.setGameMode(GameMode.SPECTATOR);
                    player2.teleport(LocationStorage.getSpawnLocation());
                    player2.setHealth(20.0);
                    player2.setFoodLevel(20);
                    player2.getInventory().clear();
                    ActiveGulagMatch.remove(player1);
                    ActiveGulagMatch.remove(player2);
                    Bukkit.getScheduler().cancelTask(healthmonitorID);
                }
                if (player2.getHealth() <= 0) {
                    Bukkit.broadcastMessage(ChatColor.RED + player2.getDisplayName() + " lost the Gulag fight against " + player1.getDisplayName() + "and was eliminated!");
                    player2.sendMessage(ChatColor.RED + "You have been eliminated!");
                    player1.sendMessage(ChatColor.GREEN + "Congraulations! You have won the Gulag fight and have been given a second chance!");
                    playersReductionScoreboard++;
                    gulagWinners.add(player1);
                    player2.setGameMode(GameMode.SPECTATOR);
                    player1.teleport(LocationStorage.getSpawnLocation());
                    player1.setHealth(20.0);
                    player1.setFoodLevel(20);
                    player1.getInventory().clear();
                    ActiveGulagMatch.remove(player1);
                    ActiveGulagMatch.remove(player2);
                    Bukkit.getScheduler().cancelTask(healthmonitorID);
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("HopefullyProfit"), 0L, 20L).getTaskId();
    }

    public void postioninQueueTimer(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (gulagQueue.size() > 2) {
                    int position = gulagQueue.size() - 2;
                    player.sendMessage(ChatColor.YELLOW + "You are in position " + position + " in the Gulag queue.");
                }
                if (gulagQueue.size() < 2) {
                    player.sendMessage(ChatColor.GREEN + "Status: Waiting for an opponent...");
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("HopefullyProfit"), 100L, 60 * 20L);
    }

    public void gulagTimeLimit(Player player1, Player player2) {
        gulagTimeLimitID = new BukkitRunnable() {
            @Override
            public void run() {
                if (player1.getHealth() > 0 && player2.getHealth() > 0) {
                    player1.sendMessage(ChatColor.RED + "Time's up! You both lose!");
                    player2.sendMessage(ChatColor.RED + "Time's up! You both lose!");
                    playersReductionScoreboard += 2;
                    player1.setGameMode(GameMode.SPECTATOR);
                    player2.setGameMode(GameMode.SPECTATOR);
                    player1.teleport(LocationStorage.getSpawnLocation());
                    player2.teleport(LocationStorage.getSpawnLocation());
                    ActiveGulagMatch.remove(player1);
                    ActiveGulagMatch.remove(player2);
                    Bukkit.getScheduler().cancelTask(gulagTimeLimitID);
                    gulagTimeLimitID = -1;
                }
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("HopefullyProfit"), 2 * 60 * 20L).getTaskId();


    }
}
