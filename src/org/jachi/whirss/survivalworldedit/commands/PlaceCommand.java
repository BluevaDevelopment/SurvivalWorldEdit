package org.jachi.whirss.survivalworldedit.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jachi.whirss.survivalworldedit.Main;

import java.util.List;

import static org.jachi.whirss.survivalworldedit.Main.blocksFromTwoPoints;

public class PlaceCommand implements CommandExecutor {

    private Main main;

    public PlaceCommand(Main main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "[SurvivalWorldEdit] You must be a player to run this command.");
            return true;
        }
        final Player p = (Player)sender;
        if(args.length > 0){
            if(sender.hasPermission("swe.*") ||
                    sender.hasPermission("swe.place")) {
                if(args.length == 1){
                    String materialtobeparsed = args[0].toUpperCase();
                    Material mat = Material.getMaterial(materialtobeparsed);
                    int amount = 0;
                    if (main.pos1.containsKey(p.getName()) && main.pos2.containsKey(p.getName())) {
                        Location one = main.pos1.get(p.getName());
                        Location two = main.pos2.get(p.getName());
                        List<Block> listblock = blocksFromTwoPoints(one, two);
                        amount = listblock.size();
                    }
                    boolean b = main.removeInventoryItems((Inventory)p.getInventory(), mat, amount);
                    if (b == true) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.filled_region")));
                        Location one = main.pos1.get(p.getName());
                        Location two = main.pos2.get(p.getName());
                        List<Block> listblock = blocksFromTwoPoints(one, two);
                        for (Block ba : listblock)
                            ba.setType(mat);
                        main.pos1.remove(p.getName());
                        main.pos2.remove(p.getName());
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.insufficient_items")));
                    }
                }
            }
        } else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.correct_use").replace("{correct_use}", "/place [BLOCK]")));
        }
        return true;
    }
}