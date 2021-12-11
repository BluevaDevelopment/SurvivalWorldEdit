package org.jachi.whirss.survivalworldedit.events;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jachi.whirss.survivalworldedit.Main;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OnPlayerInteract implements Listener {

    private Main main;

    public OnPlayerInteract(Main main) {
        this.main = main;
    }

    @EventHandler
    public void OnPlayerInteract(@NotNull PlayerInteractEvent event) {
        Location loc = new Location(event.getPlayer().getWorld(), event.getPlayer().getEyeLocation().getBlockX(), event.getPlayer().getEyeLocation().getBlockY(), event.getPlayer().getEyeLocation().getBlockZ());
        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL &&
                event.getPlayer().getItemInHand().getType() == Material.STICK) {
            if(main.worldguard) {
                if(main.isRegionProtected(loc)) {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.protected_area")));

                } else {
                    if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                        main.pos1.put(event.getPlayer().getName(), event.getClickedBlock().getLocation());
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.first_position")));
                    } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK ) {
                        main.pos2.put(event.getPlayer().getName(), event.getClickedBlock().getLocation());
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.second_position")));
                    }
                    if (main.pos1.containsKey(event.getPlayer().getName()) && main.pos2.containsKey(event.getPlayer().getName())) {
                        int amount = 0;
                        Location one = main.pos1.get(event.getPlayer().getName());
                        Location two = main.pos2.get(event.getPlayer().getName());
                        List<Block> listblock = main.blocksFromTwoPoints(one, two);
                        amount = listblock.size();
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.selected_region").replace("{amount}", ""+amount)));
                    }
                }
            } else {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    main.pos1.put(event.getPlayer().getName(), event.getClickedBlock().getLocation());
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.first_position")));
                } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK ) {
                    main.pos2.put(event.getPlayer().getName(), event.getClickedBlock().getLocation());
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.second_position")));
                }
                if (main.pos1.containsKey(event.getPlayer().getName()) && main.pos2.containsKey(event.getPlayer().getName())) {
                    int amount = 0;
                    Location one = main.pos1.get(event.getPlayer().getName());
                    Location two = main.pos2.get(event.getPlayer().getName());
                    List<Block> listblock = main.blocksFromTwoPoints(one, two);
                    amount = listblock.size();
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', main.getLanguages().getString("messages.selected_region").replace("{amount}", ""+amount)));
                }
            }
        }
    }
}
