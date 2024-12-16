package com.sa1f.hopefullyProfit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

public final class HopefullyProfit extends JavaPlugin {
    private PlayerMoveListener playerMoveListener;
    private Gulag gulag;
    PlayerFightListener playerFightListener;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        gulag = new Gulag(playerMoveListener);
        playerFightListener = new PlayerFightListener();
        playerMoveListener = new PlayerMoveListener(gulag);
        Scoreboard1 scoreboard1 = new Scoreboard1(gulag);
        PlayerJoinAndLeave playerJoinAndLeave = new PlayerJoinAndLeave(scoreboard1);

        getCommand("gamesspawn").setExecutor(new setspawnCommand());
        getCommand("start").setExecutor(new startCommand(this, playerMoveListener, scoreboard1, playerFightListener));
        getCommand("gulag").setExecutor(new GulagCommands());



        Bukkit.getPluginManager().registerEvents(playerJoinAndLeave, this);
        Bukkit.getPluginManager().registerEvents(gulag, this);
        Bukkit.getPluginManager().registerEvents(playerMoveListener, this);
        Bukkit.getPluginManager().registerEvents(playerFightListener, this);

        scoreboard1.setScoreBoard();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
