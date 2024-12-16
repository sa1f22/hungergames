package com.sa1f.hopefullyProfit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HungerGamesLoot {
    private static final Random random = new Random();

    // Loot tables for different chest tiers
    public static final List<ItemStack> COMMON_LOOT = new ArrayList<>();
    public static final List<ItemStack> RARE_LOOT = new ArrayList<>();
    public static final List<ItemStack> LEGENDARY_LOOT = new ArrayList<>();

    // Static block to initialize loot tables
    static {
        // Common Loot
        COMMON_LOOT.add(new ItemStack(Material.WOODEN_SWORD));
        COMMON_LOOT.add(new ItemStack(Material.BREAD, randomAmount(1, 3)));
        RARE_LOOT.add(new ItemStack(Material.COOKED_BEEF, randomAmount(2, 4)));
        COMMON_LOOT.add(new ItemStack(Material.APPLE, randomAmount(1, 3)));
        COMMON_LOOT.add(new ItemStack(Material.LEATHER_HELMET));
        COMMON_LOOT.add(new ItemStack(Material.ARROW, randomAmount(3, 6)));
        COMMON_LOOT.add(new ItemStack(Material.STICK, randomAmount(1, 2)));
        COMMON_LOOT.add(new ItemStack(Material.FLINT));
        COMMON_LOOT.add(new ItemStack(Material.LEATHER_HELMET));
        COMMON_LOOT.add(new ItemStack(Material.LEATHER_CHESTPLATE));
        COMMON_LOOT.add(new ItemStack(Material.LEATHER_LEGGINGS));
        COMMON_LOOT.add(new ItemStack(Material.LEATHER_BOOTS));
        COMMON_LOOT.add(new ItemStack(Material.STONE_AXE));
        COMMON_LOOT.add(new ItemStack(Material.FISHING_ROD));

        // Rare Loot
        RARE_LOOT.add(new ItemStack(Material.STONE_SWORD));
        RARE_LOOT.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        RARE_LOOT.add(new ItemStack(Material.BOW));
        RARE_LOOT.add(new ItemStack(Material.IRON_INGOT));
        RARE_LOOT.add(new ItemStack(Material.TNT));
        RARE_LOOT.add(new ItemStack(Material.CHAINMAIL_HELMET));
        RARE_LOOT.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        RARE_LOOT.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        RARE_LOOT.add(new ItemStack(Material.CHAINMAIL_BOOTS));
        RARE_LOOT.add(new ItemStack(Material.IRON_HELMET));
        RARE_LOOT.add(new ItemStack(Material.IRON_LEGGINGS));
        RARE_LOOT.add(new ItemStack(Material.IRON_BOOTS));

        // Legendary Loot
        LEGENDARY_LOOT.add(new ItemStack(Material.DIAMOND_SWORD));
        LEGENDARY_LOOT.add(new ItemStack(Material.IRON_AXE));
        LEGENDARY_LOOT.add(new ItemStack(Material.DIAMOND, randomAmount(1, 3)));
        LEGENDARY_LOOT.add(new ItemStack(Material.IRON_SWORD));
        LEGENDARY_LOOT.add(new ItemStack(Material.GOLDEN_APPLE));
        LEGENDARY_LOOT.add(new ItemStack(Material.DIAMOND));
        LEGENDARY_LOOT.add(new ItemStack(Material.LAVA_BUCKET));
        LEGENDARY_LOOT.add(new ItemStack(Material.ENCHANTED_BOOK)); // Can further customize enchantments
        LEGENDARY_LOOT.add(new ItemStack(Material.ENDER_PEARL));
        RARE_LOOT.add(new ItemStack(Material.IRON_CHESTPLATE));

    }

    private static int randomAmount(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
