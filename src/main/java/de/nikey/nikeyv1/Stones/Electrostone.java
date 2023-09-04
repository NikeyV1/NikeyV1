package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.TornadoEffect;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class Electrostone implements Listener {
    private int timer;
    private int countdown;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eElektro Stein")&&
                event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.plugin.getConfig();
            config.set(p.getName()+".elektro",null);
            config.set(p.getName()+".level",i);
            NikeyV1.plugin.saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                StoneCooldown1 stoneCooldows = new StoneCooldown1();
                switch (i){
                    case 1:
                    case 2:
                    case 3:
                        break;
                    case 10:
                        timer = 20;
                        countdown = 10;
                        World world = p.getWorld();
                        BukkitRunnable runnable = new BukkitRunnable() {

                            @Override
                            public void run() {
                                if (timer == 0){
                                    cancel();
                                    return;
                                }else {
                                    int randomX = ThreadLocalRandom.current().nextInt(-10, 10);
                                    int randomZ = ThreadLocalRandom.current().nextInt(-10, 10);
                                    int randomY = world.getHighestBlockYAt(randomX,randomZ);
                                    Location location = new Location(world,randomX,randomY,randomZ);
                                    LightningStrike lightningStrike = world.strikeLightning(location);
                                    for (Entity e : location.getWorld().getNearbyEntities(p.getLocation(),4,4,4)){
                                        if (e instanceof LivingEntity){
                                            LivingEntity entity = (LivingEntity) e;
                                            entity.damage(8,lightningStrike);
                                        }
                                    }
                                    TornadoEffect effect = new TornadoEffect(NikeyV1.em);
                                    effect.setLocation(location);
                                    effect.maxTornadoRadius = 3F;
                                    effect.tornadoParticle = Particle.ELECTRIC_SPARK;
                                    effect.start();
                                }
                                timer--;
                            }
                        };
                        runnable.runTaskTimer(NikeyV1.getPlugin(),0,20);
                        break;
                }
            }
        }
    }
}
