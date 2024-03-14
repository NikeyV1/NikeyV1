package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class UndeadStoneListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            event.getDamager().sendMessage(String.valueOf(event.getDamage()));
            String stone = NikeyV1.getPlugin().getConfig().getString(event.getDamager().getName() + ".stone");
            int level = NikeyV1.getPlugin().getConfig().getInt(event.getDamager().getName() + ".level");
            if (stone.equalsIgnoreCase("Undead")) {
                if (!(event.getEntity() instanceof Player)) {
                    if (level == 7) {
                        event.setDamage(event.getDamage() * 1.075);
                    } else if (level == 8) {
                        event.setDamage(event.getDamage() * 1.1);
                    } else if (level >= 9) {
                        event.setDamage(event.getDamage() * 1.125);
                    }
                }
            }
        }
    }
}
