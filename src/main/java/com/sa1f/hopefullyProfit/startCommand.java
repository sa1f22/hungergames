package com.sa1f.hopefullyProfit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.sa1f.hopefullyProfit.HungerGamesLoot.*;

public class startCommand implements CommandExecutor {

    public PlayerFightListener playerFightListener;
    HopefullyProfit main;
    private PlayerMoveListener playerMoveListener;
    Scoreboard1 scoreboard1;

    int gracePeriodID = -1;
    int gracePeriodID1 = -1;
    int hungerGamesTimerID = -1;


    public startCommand(HopefullyProfit main, PlayerMoveListener playerMoveListener, Scoreboard1 scoreboard1, PlayerFightListener playerFightListener) {
        this.playerFightListener = playerFightListener;
        this.scoreboard1 = scoreboard1;
        this.main = main;
        this.playerMoveListener = playerMoveListener;
    }

    public startCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location spawnLocation = LocationStorage.getSpawnLocation();
            if (spawnLocation == null) {
                player.sendMessage("Spawn location is not set");
                return false;
            }

            spawnRandomChests(spawnLocation);
            putPlayersintoCircle();
            playerMoveListener.startCountdown();
            startHungerGamesTimer();


        }
        return false;

    }

    public void putPlayersintoCircle() {
        int radiusOfCircle = main.getConfig().getInt("radiusOfCircle");
        int playerCount = Bukkit.getOnlinePlayers().size();
        int maxPlayersPerCircle = (int) (2 * Math.PI * radiusOfCircle / 3);
        if (playerCount == 0) {
            main.getLogger().warning("No players online to put into the circle.");
            return;
        }

        int layers = (int) Math.ceil((double) playerCount / maxPlayersPerCircle);
        double angleIncrement = 360 / playerCount;

        int i = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            int layer = i / maxPlayersPerCircle;
            double angle = (i % maxPlayersPerCircle) * angleIncrement;
            double x = radiusOfCircle * Math.cos(Math.toRadians(angle));
            double z = radiusOfCircle * Math.sin(Math.toRadians(angle));
            double y = layer * 3;
            Location location = LocationStorage.getSpawnLocation().clone().add(x, y + 1, z);
            player.teleport(location);
            Location locationpod = LocationStorage.getSpawnLocation().clone().add(x, y, z);
            buildpod(locationpod);
            i++;
        }
    }

    public void spawnRandomChests(Location location) {
        Random random = new Random();
        int numberofChests = 25;
        List<Location> chestLocations = new ArrayList<>();
        List<String> blocksAllowed = main.getConfig().getStringList("allowedChestSpawnBlocks");

        int minX = main.getConfig().getInt("minX");
        int minZ = main.getConfig().getInt("minZ");
        int maxX = main.getConfig().getInt("maxX");
        int maxZ = main.getConfig().getInt("maxZ");
        int minY = main.getConfig().getInt("minY");
        int maxY = main.getConfig().getInt("maxY");


        for (int i = 0; i < numberofChests; i++) {

            double x = random.nextInt(maxX - minX + 1) + minX;
            double z = random.nextInt(maxZ - minZ + 1) + minZ;
            double y = random.nextInt(maxY - minY + 1) + minY;

            Location chestLocation = new Location(location.getWorld(), x, y, z);
            //check if the block is air
            boolean isTooClose = false;

            for (Location loc : chestLocations) {
                if (loc.distance(chestLocation) < 15) {
                    isTooClose = true;
                    break; // Exit the loop as we've found a nearby chest
                }
            }

            if (isTooClose) {
                continue; // Skip this iteration of the outer loop
            }

            Material blockbelowChest = chestLocation.clone().subtract(0, 1, 0).getBlock().getType();
            if (!blocksAllowed.contains(blockbelowChest.toString())) {
                continue;
            }

            // If we reach here, the chest location is valid
            chestLocations.add(chestLocation);


            if (chestLocation.getBlock().getType() == Material.AIR) {
                chestLocation.getBlock().setType(Material.CHEST);
            }
            if (chestLocation.getBlock().getState() instanceof Chest chest) {
                fillChestswithRandomLoot(chest.getInventory());
            }

        }
    }

    public void fillChestswithRandomLoot(Inventory inventory) {
        Random random = new Random();

        for (int i = 0; i < 12; i++) {
            if (random.nextBoolean()) {
                inventory.setItem(i, getRandomLoot());
            }
        }
    }

    private static ItemStack getRandomLoot() {
        Random random = new Random();
        int chance = random.nextInt(100);

        if (chance < 65) { // 50% chance for common loot
            return COMMON_LOOT.get(random.nextInt(COMMON_LOOT.size()));
        } else if (chance < 90) { // 25% chance for rare loot
            return RARE_LOOT.get(random.nextInt(RARE_LOOT.size()));
        } else { // 10% chance for legendary loot
            return LEGENDARY_LOOT.get(random.nextInt(LEGENDARY_LOOT.size()));
        }
    }

    private void buildpod(Location location) {
        Material stairMaterial = Material.OAK_STAIRS;
        location.getBlock().setType(Material.GLOWSTONE);

        location.clone().add(1, 0, 0).getBlock().setType(stairMaterial);
        location.clone().add(-1, 0, 0).getBlock().setType(stairMaterial);
        location.clone().add(0, 0, 1).getBlock().setType(stairMaterial);
        location.clone().add(0, 0, -1).getBlock().setType(stairMaterial);

        location.clone().add(1, 0, 0).getBlock().setBlockData(Bukkit.createBlockData("minecraft:oak_stairs[facing=west]"));
        location.clone().add(-1, 0, 0).getBlock().setBlockData(Bukkit.createBlockData("minecraft:oak_stairs[facing=east]"));
        location.clone().add(0, 0, 1).getBlock().setBlockData(Bukkit.createBlockData("minecraft:oak_stairs[facing=north]"));
        location.clone().add(0, 0, -1).getBlock().setBlockData(Bukkit.createBlockData("minecraft:oak_stairs[facing=south]"));


    }

    public void startGracePeriod() {
        playerFightListener.fightGracePeriod = true;
        final int[] gracePeriod = {main.getConfig().getInt("gracePeriod")};


        gracePeriodID = Bukkit.getScheduler().runTaskTimer(HopefullyProfit.getPlugin(HopefullyProfit.class), () -> {
            if (gracePeriod[0] > 0) {
                gracePeriod[0]--;
            } else {
                Bukkit.broadcastMessage(ChatColor.RED + "Grace period has ended! You can now attack other players!");
                Bukkit.getScheduler().cancelTask(gracePeriodID);
                playerFightListener.fightGracePeriod = false;
            }
        }, 0L, 20L).getTaskId();


        gracePeriodID1 = Bukkit.getScheduler().runTaskTimer(HopefullyProfit.getPlugin(HopefullyProfit.class), () -> {
            if (gracePeriod[0] > 0) {
                Bukkit.broadcastMessage("Grace period ends in " + gracePeriod[0] + 1 + " seconds");
            } else {
                Bukkit.getScheduler().cancelTask(gracePeriodID1);
            }
        }, 0L, 100L).getTaskId();
    }

    public void startHungerGamesTimer() {
        int time[] = {main.getConfig().getInt("time")};

        Bukkit.getScheduler().runTaskTimer(HopefullyProfit.getPlugin(HopefullyProfit.class), () -> {
            if (time[0] > 0) {
                Bukkit.getLogger().info("HungerGames timer tick: " + time[0]);

                time[0]--;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Scoreboard scoreboard = player.getScoreboard();
                    scoreboard.getObjective("hungergames").getScore(ChatColor.AQUA + "Time Left: ").setScore(time[0]);
                }
            } else {


            }
        }, 0L, 600L);
    }
}

