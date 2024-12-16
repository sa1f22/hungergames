package com.sa1f.hopefullyProfit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerFightListener implements Listener {
    public boolean fightGracePeriod = false;


    @EventHandler
    public void onPlayerFightGracePeriod(EntityDamageByEntityEvent event){
        Player player = (Player) event.getEntity();
        player.sendMessage("hello");
        if(fightGracePeriod){
            if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
                player.sendMessage("You are in a fight grace period. You cannot fight other players.");
                event.setCancelled(true);
            }
        }
    }




}
