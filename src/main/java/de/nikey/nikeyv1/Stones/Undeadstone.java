package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ALL")
public class Undeadstone implements Listener {
    public ArrayList<LivingEntity> Soul = new ArrayList<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;
    private Player player;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stone")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Undead");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_AIR||event.getAction() == Action.RIGHT_CLICK_BLOCK){
                if (!Soul.isEmpty()){
                    EntityType type = Soul.get(0).getType();
                    if (type.equals(EntityType.WITHER)||type.equals(EntityType.ENDER_DRAGON)||type.equals(EntityType.EVOKER)){
                    }else {
                        if (i == 10||i == 11){
                            LivingEntity summoned = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), type);
                            summoned.setCustomName(p.getDisplayName()+"'s "+type.getName());
                            Soul.clear();
                            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
                            ItemStack hand = p.getInventory().getItemInMainHand();
                            ItemMeta meta = hand.getItemMeta();
                            ArrayList<String> list = new ArrayList<>();
                            list.add(ChatColor.of("#221726")+"Souls wander around in this stone");
                            list.add(ChatColor.of("#00FFAA")+"Level:"+i);
                            meta.setLore(list);
                            hand.setItemMeta(meta);
                        }else if (i >= 12){
                            LivingEntity summoned = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), type);
                            summoned.setCustomName(p.getDisplayName()+"'s "+type.getName());
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,0));
                            Soul.clear();
                            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
                            ItemStack hand = p.getInventory().getItemInMainHand();
                            ItemMeta meta = hand.getItemMeta();
                            ArrayList<String> list = new ArrayList<>();
                            list.add(ChatColor.of("#221726")+"Souls wander around in this stone");
                            list.add(ChatColor.of("#00FFAA")+"Level:"+i);
                            meta.setLore(list);
                            hand.setItemMeta(meta);
                        }
                    }
                }
            }else if (event.getAction() == Action.LEFT_CLICK_AIR ||event.getAction() == Action.LEFT_CLICK_BLOCK){
                if (p.isSneaking()){
                    if (i == 15||i == 16||i == 17){
                        if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                            event.setCancelled(true);
                            p.updateInventory();
                            remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                        }else {
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ability.remove(p.getUniqueId());
                                    cancel();
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 180);
                            //cooldown-ability
                            timer = 40;
                            player = p;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    timer--;
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        int x = (int) (p.getLocation().getX());
                                        int z = (int) (p.getLocation().getZ());
                                        int randomX = ThreadLocalRandom.current().nextInt(x-6, x+6);
                                        int randomZ = ThreadLocalRandom.current().nextInt(z-6, z+6);
                                        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
                                        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
                                        Random random = new Random();
                                        int r = random.nextInt(3);
                                        if (r ==0){
                                            Zombie zombie = location.getWorld().spawn(location, Zombie.class);
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            zombie.setCustomName(p.getDisplayName()+"'s "+zombie.getType().getName());
                                        }else if (r ==1){
                                            ZombieVillager spawn = location.getWorld().spawn(location, ZombieVillager.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else if (r == 2){
                                            WitherSkeleton spawn = location.getWorld().spawn(location, WitherSkeleton.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else{
                                            Zombie spawn = location.getWorld().spawn(location, Zombie.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            spawn.setArmsRaised(true);
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }
                                    }
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),10,10);
                        }
                    }else if (i >=18){
                        if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                            event.setCancelled(true);
                            p.updateInventory();
                            remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                        }else {
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ability.remove(p.getUniqueId());
                                    cancel();
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 180);
                            //cooldown-ability
                            timer = 60;
                            player = p;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    timer--;
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        int x = (int) (p.getLocation().getX());
                                        int z = (int) (p.getLocation().getZ());
                                        int randomX = ThreadLocalRandom.current().nextInt(x-6, x+6);
                                        int randomZ = ThreadLocalRandom.current().nextInt(z-6, z+6);
                                        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
                                        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
                                        Random random = new Random();
                                        int r = random.nextInt(3);
                                        if (r ==0){
                                            Zombie zombie = location.getWorld().spawn(location, Zombie.class);
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            zombie.setCustomName(p.getDisplayName()+"'s "+zombie.getType().getName());
                                        }else if (r ==1){
                                            ZombieVillager spawn = location.getWorld().spawn(location, ZombieVillager.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                            spawn.setArmsRaised(true);
                                        }else if (r == 2){
                                            WitherSkeleton spawn = location.getWorld().spawn(location, WitherSkeleton.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else{
                                            Zombie spawn = location.getWorld().spawn(location, Zombie.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            spawn.setArmsRaised(true);
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }
                                    }
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),10,10);
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getKiller() instanceof Player && !(entity instanceof Player)){
            Player p = (Player) entity.getKiller();
            if (p.getInventory().getItemInMainHand().getItemMeta() == null)return;
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stone")){
                String[] arr = p.getItemInHand().getLore().get(1).split(":");
                int i = Integer.parseInt(arr[1]);
                if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                    p.updateInventory();
                    remainingTime1 = cooldown.get(p.getUniqueId()) - System.currentTimeMillis();
                }else {
                    cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (100 * 1000));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cooldown.remove(p.getUniqueId());
                            cancel();
                        }
                    }.runTaskLater(NikeyV1.getPlugin(), 20 * 100);
                    //cooldown-ability
                    ItemStack hand = p.getInventory().getItemInMainHand();
                    ItemMeta meta = hand.getItemMeta();
                    ArrayList<String> list = new ArrayList<>();
                    list.add(ChatColor.of("#221726")+"Souls wander around in this stone");
                    list.add(ChatColor.of("#00FFAA")+"Level:"+i);
                    list.add("§e");
                    list.add("§7Absorbed Soul:§3 "+entity.getType());
                    meta.setLore(list);
                    hand.setItemMeta(meta);
                    Soul.clear();
                    Soul.add(entity);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (stone.equalsIgnoreCase("Undead")) {
                Monster damager = (Monster) event.getDamager();
                // Überprüfen, ob der Spieler von einen Entity geschlagen wurde
                if (damager != null) {
                    // Aggro-Mechanismus aktivieren
                    HelpUtil.triggerEntityAggro(damager,p);
                }
            }
        }

        if (event.getDamager() instanceof Player){
            Player p = (Player) event.getDamager();
            if (event.getEntity() instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) event.getEntity();
                FileConfiguration config = NikeyV1.getPlugin().getConfig();
                String stone = config.getString(p.getName() + ".stone");

                if (stone.equalsIgnoreCase("Undead")) {
                    Monster damagedEntity = (Monster) event.getEntity();
                    // Überprüfen, ob der Spieler ein Entity schlägt
                    if (damagedEntity != null) {
                        // Aggro-Mechanismus aktivieren
                        HelpUtil.triggerEntityAggro(damagedEntity,p);
                    }
                }

                if (entity.getCategory() == EntityCategory.UNDEAD && stone.equalsIgnoreCase("Undead")&&config.getInt(p.getName()+".level")>=5){
                    event.setDamage(event.getDamage()+1);
                }
            }
            if (p.getInventory().getItemInMainHand().getItemMeta() == null)return;
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stone")){
                if (cooldown.containsKey(p.getUniqueId())&& cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                    event.setCancelled(true);
                }else {
                    event.setDamage(4);
                    }
                }
            }
        }
    @EventHandler
    public void onEntityTarget(EntityTargetEvent event){
        if (event.getTarget() instanceof Player){
            if (event.getEntity() instanceof Monster || event.getEntity() instanceof IronGolem || event.getEntity() instanceof Warden){
                if (event.getEntity().getCustomName().equalsIgnoreCase(((Player) event.getTarget()).getDisplayName()+"'s "+event.getEntityType().getName())){
                    event.setCancelled(true);
                }
            }
        }
    }
}
