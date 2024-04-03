package de.nikey.nikeyv1.CustomMobs;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class WitherDash implements Listener {
    private int witherHealth;

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Wither) {
            if (event.getEntity().getCustomName().equalsIgnoreCase("Mini-Wither")) {
                Wither wither = (Wither) event.getEntity();
                Random random = new Random();
                int i = random.nextInt(10);
                if (i == 6) {
                    Player nearestPlayer;
                    String spawnerName = wither.getPersistentDataContainer().get(new NamespacedKey(NikeyV1.getPlugin(), "Spawner"), PersistentDataType.STRING);
                    assert spawnerName != null;
                    Player player = Bukkit.getPlayer(spawnerName);
                    nearestPlayer = HelpUtil.getNearestPlayer(player);

                    // Dash towards nearest player
                    event.getDamager().sendMessage("§aDash1 incoming");
                    if (nearestPlayer != null) {
                        player.sendMessage("§aDash incoming");
                        dashTowardsLocation(wither, nearestPlayer);
                    }
                }
            }
        }
    }
    int dticks;

    private void dashTowardsLocation(Wither wither, Player player) {
        Location witherLocation = wither.getLocation();
        Location targetLocation = player.getLocation();

        HelpUtil.boostEntityTowards(wither,player,2.6);
        witherHealth = (int) wither.getHealth();

        wither.setInvulnerableTicks(100);
        wither.setTarget(null);

        // Destroy blocks around the Wither
        dticks = 0;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (dticks >= 20 && !wither.isDead()) {
                    destroyBlocksAroundWither(wither.getLocation());
                    wither.getLocation().getWorld().playSound(wither.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK,0.4F,0.4F);
                }else {
                    cancel();
                }
                dticks++;
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0,5L);

        // Restore wither's health after 8 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!wither.isDead()) {
                    wither.setHealth(Math.min(witherHealth, wither.getMaxHealth()));
                }
            }
        }.runTaskLater(NikeyV1.getPlugin(), 100L); // 160 ticks = 8 seconds
    }

    private void destroyBlocksAroundWither(Location witherLocation) {
        // Loop through blocks in a 6x8x6 volume around the Wither
        for (int x = -3; x <= 3; x++) {
            for (int y = -4; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    Location blockLocation = witherLocation.clone().add(x, y, z);
                    if (blockLocation.getBlock().getType() != Material.OBSIDIAN || blockLocation.getBlock().getType() != Material.BEDROCK || blockLocation.getBlock().getType() != Material.END_PORTAL_FRAME) {
                        blockLocation.getBlock().breakNaturally(new ItemStack(Material.NETHERITE_PICKAXE));
                    }
                }
            }
        }
    }
}
