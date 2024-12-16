package com.sa1f.hopefullyProfit;

import org.bukkit.Location;

public class LocationStorage {
    static Location spawnLocation;

    public static Location getSpawnLocation() {
        return spawnLocation;
    }

    public static void setSpawnLocation(Location location) {
        spawnLocation = location;
    }

    static Location fightLocation1;

    public static Location getFightLocation1() {
        return fightLocation1;
    }

    public static void setFightLocation1(Location location) {
        fightLocation1 = location;
    }

    static Location fightLocation2;

    public static Location getFightLocation2() {
        return fightLocation2;
    }

    public static void setFightLocation2(Location location) {
        fightLocation2 = location;
    }

    static Location gulagWaiting;

    public static Location getGulagWaiting() {
        return gulagWaiting;
    }

    public static void setGulagWaiting(Location location) {
        gulagWaiting = location;
    }
}
