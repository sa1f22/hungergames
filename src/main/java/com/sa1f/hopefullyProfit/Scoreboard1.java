package com.sa1f.hopefullyProfit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Scoreboard1 {
    private ScoreboardManager manager;
    private org.bukkit.scoreboard.Scoreboard board;
    private Objective objective;
    Gulag gulag;

    public Scoreboard1(Gulag gulag){
        this.gulag = gulag;
    }

    public void setScoreBoard(){
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        objective = board.registerNewObjective("hungergames", "dummy", ChatColor.RED + "Hunger Games");
        objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);

        org.bukkit.scoreboard.Score playeramount = objective.getScore(ChatColor.AQUA + "Players Left: ");
        Score kills = objective.getScore(ChatColor.AQUA + "Kills: ");
        kills.setScore(0);
        int playersLeft = Bukkit.getOnlinePlayers().size();
        playeramount.setScore(playersLeft);

        for(org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()){
            player.setScoreboard(board);
        }
    }

    public void updateScoreBoard(Player player){
        int playersLeft = Bukkit.getOnlinePlayers().size() - gulag.playersReductionScoreboard;
        objective.getScore(ChatColor.AQUA + "Players Left: ").setScore(playersLeft);

        for(Player player1 : Bukkit.getOnlinePlayers()){
            player1.setScoreboard(board);
        }

    }

    public void updatePlayerKills(Player player){
        Scoreboard board = player.getScoreboard();
        Score score = objective.getScore(player.getName());
        board.getObjective("hungergames").getScore(ChatColor.AQUA + "Kills: ").setScore(score.getScore() + 1);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer != null){
            updatePlayerKills(killer);
            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has been killed by " + killer.getName());

        }
    }
}
