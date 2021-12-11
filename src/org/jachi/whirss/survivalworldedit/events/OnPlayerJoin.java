package org.jachi.whirss.survivalworldedit.events;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jachi.whirss.survivalworldedit.Main;
import org.jachi.whirss.survivalworldedit.UpdateChecker;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OnPlayerJoin implements Listener {

    private Main main;

    public OnPlayerJoin(Main main) {
        this.main = main;
    }

    @EventHandler
    public void OnPlayerJoin(@NotNull PlayerJoinEvent event) {
        if(event.getPlayer().isOp() || event.getPlayer().hasPermission("swe.admin")) {
            new UpdateChecker(main, 98035).getVersion(version -> {
                if (!main.getDescription().getVersion().equals(version)) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "A new version of Survival World Edit is available.");
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Download: https://www.spigotmc.org/resources/98035/");
                }
            });
        }
    }
}
