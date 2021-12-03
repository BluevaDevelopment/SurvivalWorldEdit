package org.jachi.whirss.survivalworldedit.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jachi.whirss.survivalworldedit.Main;

public class StickCommand implements CommandExecutor {

    private Main main;

    public StickCommand(Main main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        //player:
        if ((sender instanceof Player)) {
            if (args.length > 0) {
                if (sender.hasPermission("swe.*") ||
                        sender.hasPermission("swe.stick") ||
                        sender.hasPermission("swe.stick.others") ||
                        sender.hasPermission("swe.admin")) {
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            ItemStack itemStack = new ItemStack(Material.STICK);
                            ItemMeta meta = itemStack.getItemMeta();
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("items.stick_name")));
                            itemStack.setItemMeta(meta);
                            target.getInventory().addItem(itemStack);
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.stick_obtained")));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.stick_delivered").replace("{player}", target.getName())));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.offline_player")));
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.offline_player")));
                }
            } else {
                if (sender.hasPermission("swe.*") ||
                        sender.hasPermission("swe.stick") ||
                        sender.hasPermission("swe.admin"))  {
                    ItemStack itemStack = new ItemStack(Material.STICK);
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("items.stick_name")));
                    itemStack.setItemMeta(meta);
                    ((Player) sender).getInventory().addItem(itemStack);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.stick_obtained")));
                }
            }
        } else {

            //console:
            if (args.length > 0) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        ItemStack itemStack = new ItemStack(Material.STICK);
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("items.stick_name")));
                        itemStack.setItemMeta(meta);
                        target.getInventory().addItem(itemStack);
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.stick_obtained")));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.stick_delivered").replace("{player}", target.getName())));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.offline_player")));
                    }
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.correct_use").replace("{correct_use}", "/stick [PLAYER]")));
            }
        }
        return true;
    }
}