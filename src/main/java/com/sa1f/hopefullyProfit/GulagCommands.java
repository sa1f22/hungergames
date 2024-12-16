package com.sa1f.hopefullyProfit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class GulagCommands implements CommandExecutor{


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "No subcommand provided. Please use 'fightlocation1', 'fightlocation2', or 'gulagwaiting'.");
                return false;
            }

            switch (args[0]){
                case "fightlocation1":
                    Location fightLocation1 = player.getLocation();
                    LocationStorage.setFightLocation1(fightLocation1);
                    break;
                case "fightlocation2":
                    Location fightLocation2 = player.getLocation();
                    LocationStorage.setFightLocation2(fightLocation2);
                    break;
                case "gulagwaiting":
                    Location gulagWaiting = player.getLocation();
                    LocationStorage.setGulagWaiting(gulagWaiting);
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Invalid command. Please use 'fightlocation1', 'fightlocation2', or 'gulagwaiting'.");
                    break;
            }
        }

        return false;
    }
}
