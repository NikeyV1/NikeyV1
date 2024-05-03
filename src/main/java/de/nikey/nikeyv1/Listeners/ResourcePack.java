package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ResourcePack implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                event.getPlayer().setResourcePack("https://download.mc-packs.net/pack/1ae3a52c4b0096559c567b176d433b59af81a588.zip","1ae3a52c4b0096559c567b176d433b59af81a588",false);
            }
        }.runTaskLater(NikeyV1.getPlugin(),10);
    }
}
