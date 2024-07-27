package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.net.URI;

public class ResourcePack implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                sendOptionalResourcePack(event.getPlayer());
            }
        }.runTaskLater(NikeyV1.getPlugin(),10);
    }

    private static final ResourcePackInfo PACK_INFO = ResourcePackInfo.resourcePackInfo()
            .uri(URI.create("https://download.mc-packs.net/pack/1ae3a52c4b0096559c567b176d433b59af81a588.zip"))
            .hash("1ae3a52c4b0096559c567b176d433b59af81a588")
            .build();

    public void sendOptionalResourcePack(final @NonNull Audience target) {
        final ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
                .packs(PACK_INFO)
                .prompt(Component.text("Please download the stone-smp resource pack!"))
                .required(false)
                .build();

        // Send the resource pack request to the target audience
        target.sendResourcePacks(request);
    }
}
