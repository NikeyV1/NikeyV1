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
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("deprecation")
public class Electrostone implements Listener {
    public static ArrayList<LivingEntity> stunned = new ArrayList<>();
    private int timer;


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
                        stoneCooldows.setTime(1);
                        stoneCooldows.setStopTime(100);
                        stoneCooldows.start(p);
                        if (!config.getBoolean(p.getName()+"."+stone+".cooldown1"+".timer")){
                            timer = 20;
                            World world = p.getWorld();
                            BukkitRunnable runnable = new BukkitRunnable() {

                                @Override
                                public void run() {
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        int x = (int) (p.getLocation().getX());
                                        int z = (int) (p.getLocation().getZ());
                                        int randomX = ThreadLocalRandom.current().nextInt(x-10, x+10);
                                        int randomZ = ThreadLocalRandom.current().nextInt(z-10, z+10);
                                        int randomY = world.getHighestBlockYAt(randomX,randomZ);
                                        Location location = new Location(world,randomX,randomY+1,randomZ);
                                        LightningStrike lightningStrike = world.strikeLightning(location);
                                        for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
                                            if (e instanceof LivingEntity){
                                                LivingEntity entity = (LivingEntity) e;
                                                if (entity != p){
                                                    entity.damage(8,lightningStrike);
                                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*2,2));
                                                }
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
                            runnable.runTaskTimer(NikeyV1.getPlugin(),20,20);
                        }
                        break;
                    case 12:
                        stoneCooldows.setTime(1);
                        stoneCooldows.setStopTime(100);
                        stoneCooldows.start(p);
                        if (!config.getBoolean(p.getName()+"."+stone+".cooldown1"+".timer")){
                            timer = 20;
                            World world = p.getWorld();
                            BukkitRunnable runnable = new BukkitRunnable() {

                                @Override
                                public void run() {
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        int x = (int) (p.getLocation().getX());
                                        int z = (int) (p.getLocation().getZ());
                                        int randomX = ThreadLocalRandom.current().nextInt(x-10, x+10);
                                        int randomZ = ThreadLocalRandom.current().nextInt(z-10, z+10);
                                        int randomY = world.getHighestBlockYAt(randomX,randomZ);
                                        Location location = new Location(world,randomX,randomY+1,randomZ);
                                        LightningStrike lightningStrike = world.strikeLightning(location);
                                        for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
                                            if (e instanceof LivingEntity){
                                                LivingEntity entity = (LivingEntity) e;
                                                if (entity != p){
                                                    entity.damage(10,lightningStrike);
                                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,2));
                                                }
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
                            runnable.runTaskTimer(NikeyV1.getPlugin(),20,20);
                        }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player p = event.getPlayer();
        if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§eElektro Stein")){
            String[] arr = p.getInventory().getItemInMainHand().getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            switch (i) {
                case 15:
                    FileConfiguration config = NikeyV1.plugin.getConfig();
                    String stone = config.getString(p.getName() + ".stone");
                    if (!config.getBoolean(p.getName()+"."+stone+".cooldown2"+".timer")){

                    }

                    break;
            }
        }
    }
}
