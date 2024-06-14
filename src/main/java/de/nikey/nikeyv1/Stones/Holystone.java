package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.EntityTypeDamage;
import de.slikey.effectlib.effect.CircleEffect;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

@SuppressWarnings("ALL")
public class Holystone implements Listener {
    public static ArrayList<Entity> stunned = new ArrayList<>();
    public static ArrayList<Player> hitted = new ArrayList<>();
    private final List<UUID> selectedPlayers = new ArrayList<>();
    private final Set<Player> vanishedPlayers = new HashSet<>();
    public static final Map<Player, BukkitRunnable> auraTasks = new HashMap<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();

    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;


    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        if (event.getEntity() instanceof Player){
            Player p = (Player) event.getEntity();
            String stone = config.getString(p.getName() + ".stone");
            int level = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            if (stone.equalsIgnoreCase("Holy")){
                if (level == 3){
                    event.setAmount(event.getAmount()+0.2);
                }else if (level == 4){
                    event.setAmount(event.getAmount()+0.3);
                }else if (level == 5){
                    event.setAmount(event.getAmount()+0.4);
                }else if (level >= 6){
                    event.setAmount(event.getAmount()+0.5);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
            Player p =  event.getPlayer();
            String stone = NikeyV1.getPlugin().getConfig().getString(p.getName() + ".stone");
            int level = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            if (stone.equalsIgnoreCase("Holy")){
                if (level == 7){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            repairRandomArmorPiece(p);
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0,160);
                }else if (level == 8){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            repairRandomArmorPiece(p);
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0,100);
                }else if (level >= 9){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            repairRandomArmorPiece(p);
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0,60);
                }
            }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aHoly Stone")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Holy");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (i >= 10){
                    if (!(cooldown.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (100 * 1000));
                        //Ability
                        if (i ==10 ){
                            World world = p.getWorld();
                            int players = p.getNearbyEntities(20, 20, 20).size();
                            if (players >8){
                                players = 8;
                            }
                            p.setMaxHealth(22+players);
                            p.setHealth(20);
                            Location location = p.getLocation().add(0,1,0);
                            p.spawnParticle(Particle.HEART,location,3);
                            CircleEffect effect = new CircleEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.start();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.setMaxHealth(20);
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),30*30);
                        } else if (i == 11) {
                            World world = p.getWorld();
                            int players = p.getNearbyEntities(20, 20, 20).size();
                            if (players >18){
                                players = 18;
                            }
                            p.setMaxHealth(22+players);
                            p.setHealth(20);
                            Location location = p.getLocation().add(0,1,0);
                            p.spawnParticle(Particle.HEART,location,3);
                            CircleEffect effect = new CircleEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.start();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.setMaxHealth(20);
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),30*30);
                        } else if (i == 12) {
                            World world = p.getWorld();
                            int players = p.getNearbyEntities(25, 25, 25).size();
                            if (players >18)players = 18;
                            p.setMaxHealth(22+players);
                            p.setHealth(20);
                            Location location = p.getLocation().add(0,2,0);
                            CircleEffect effect = new CircleEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.start();
                            p.spawnParticle(Particle.HEART,location,3);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.setMaxHealth(20);
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),30*30);
                        }else if (i == 13) {
                            World world = p.getWorld();
                            int players = p.getNearbyEntities(25, 25, 25).size();
                            if (players >18)players = 18;
                            p.removePotionEffect(PotionEffectType.WEAKNESS);
                            p.removePotionEffect(PotionEffectType.POISON);
                            p.removePotionEffect(PotionEffectType.DARKNESS);
                            p.removePotionEffect(PotionEffectType.LEVITATION);
                            p.removePotionEffect(PotionEffectType.BLINDNESS);
                            p.removePotionEffect(PotionEffectType.SLOW);
                            p.removePotionEffect(PotionEffectType.CONFUSION);
                            p.removePotionEffect(PotionEffectType.WITHER);
                            p.setMaxHealth(22+players);
                            p.setHealth(20);
                            Location location = p.getLocation().add(0,2,0);
                            CircleEffect effect = new CircleEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.start();
                            p.spawnParticle(Particle.HEART,location,3);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.setMaxHealth(20);
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*30);
                        }else if (i >= 14) {
                            World world = p.getWorld();
                            int players = p.getNearbyEntities(25, 25, 25).size();
                            if (players >18)players = 18;
                            p.removePotionEffect(PotionEffectType.WEAKNESS);
                            p.removePotionEffect(PotionEffectType.POISON);
                            p.removePotionEffect(PotionEffectType.DARKNESS);
                            p.removePotionEffect(PotionEffectType.LEVITATION);
                            p.removePotionEffect(PotionEffectType.BLINDNESS);
                            p.removePotionEffect(PotionEffectType.SLOW);
                            p.removePotionEffect(PotionEffectType.CONFUSION);
                            p.removePotionEffect(PotionEffectType.WITHER);
                            p.setMaxHealth(22+players);
                            p.setHealth(20);
                            Location location = p.getLocation().add(0,2,0);
                            CircleEffect effect = new CircleEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.start();
                            p.spawnParticle(Particle.HEART,location,3);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.setMaxHealth(20);
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*40);
                        }
                    }            
                }
            }else if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR){
                String damageEntityType = EntityTypeDamage.getDamageEntityType(p);
                if (!p.isSneaking()) {
                    if (i == 15){
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(20,20,20)){

                                if (damageEntityType.equalsIgnoreCase("all")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player = (Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.1;
                                            int players = p.getNearbyEntities(20, 20, 20).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+5,p);
                                        }
                                    }else if (e instanceof LivingEntity){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.1;
                                        int players = p.getNearbyEntities(20, 20, 20).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+10,source);
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("players")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.1;
                                            int players = p.getNearbyEntities(20, 20, 20).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+5,p);
                                        }
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                                    if (e instanceof Monster){
                                        Monster entity = (Monster) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.1;
                                        int players = p.getNearbyEntities(20, 20, 20).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+10,source);
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.1;
                                            int players = p.getNearbyEntities(20, 20, 20).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+5,p);
                                        }
                                    }else if (e instanceof Monster){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.1;
                                        int players = p.getNearbyEntities(20, 20, 20).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+10,source);
                                    }
                                }
                            }
                        }
                    } else if (i == 16) {
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(25,25,25)){

                                if (damageEntityType.equalsIgnoreCase("all")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.1;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+5,p);
                                        }
                                    }else if (e instanceof LivingEntity){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.1;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+10,source);
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("players")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.1;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+5,p);
                                        }
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                                    if (e instanceof Monster){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.1;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+10,source);
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.1;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+5,p);
                                        }
                                    }else if (e instanceof Monster){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.1;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+10,source);
                                    }
                                }
                            }
                        }
                    }else if (i == 17) {
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(25,25,25)){

                                if (damageEntityType.equalsIgnoreCase("all")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.1;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+10,p);
                                        }
                                    }else if (e instanceof LivingEntity){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.1;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+15,source);
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("players")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.1;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+10,p);
                                        }
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                                    if (e instanceof Monster){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.1;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+15,source);
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.1;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+10,p);
                                        }
                                    }else if (e instanceof Monster){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.1;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+15,source);
                                    }
                                }
                            }
                        }
                    } else if (i == 18) {
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(25,25,25)){

                                if (damageEntityType.equalsIgnoreCase("all")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.4;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+10,p);
                                        }
                                    }else if (e instanceof LivingEntity){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.4;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+15,source);
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("players")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.4;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+10,p);
                                        }
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                                    if (e instanceof Monster){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.4;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+15,source);
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.4;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }
                                            player.damage(armor+10,p);
                                        }
                                    }else if (e instanceof Monster){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.4;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+15,source);
                                    }
                                }
                            }
                        }
                    }else if (i >= 19) {
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(25,25,25)){

                                if (damageEntityType.equalsIgnoreCase("all")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.4;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,3));
                                            }
                                            player.damage(armor+10,p);
                                        }
                                    }else if (e instanceof LivingEntity){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.4;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,3));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+15,source);
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("players")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.4;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,3));
                                            }
                                            player.damage(armor+10,p);
                                        }
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                                    if (e instanceof Monster){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.4;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,3));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+15,source);
                                    }
                                }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                                    if (e instanceof Player) {
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player =(Player) e;
                                            double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                            armor = armor*2.4;
                                            int players = p.getNearbyEntities(25, 25, 25).size();
                                            if (players == 1){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                            } else if (players  == 2 || players == 3 || players == 4) {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                            }else {
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,3));
                                            }
                                            player.damage(armor+10,p);
                                        }
                                    }else if (e instanceof Monster){
                                        LivingEntity entity = (LivingEntity) e;
                                        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.4;
                                        int players = p.getNearbyEntities(25, 25, 25).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,3));
                                        }
                                        DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                        entity.damage(armor+15,source);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (hitted.contains(event.getPlayer())) {
            if (event.getItem() != null && event.getItem().getType() == Material.SHIELD) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (hitted.contains(player)) {
                event.setCancelled(true);
                hitted.remove(player);
            }
        }
    }

    //Master Ability
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (item == null) return;
        if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("§aHoly Stone")&& event.getItemDrop().getItemStack().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Holy");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (i == 20 || i == 21){
                if (p.isSneaking()) {
                    if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));
                        openMenu(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player ) {
            Player damager = (Player) event.getDamager();
            if (selectedPlayers.contains(damager.getUniqueId())) {
                if (event.getEntity() instanceof Player) {
                    if (!hitted.contains((Player) event.getEntity())) {
                        Player entity = (Player) event.getEntity();
                        hitted.add(entity);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                hitted.remove(entity);
                                cancel();
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*3);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!hitted.contains(entity)){
                                    cancel();
                                    return;
                                }else {
                                    Location location = entity.getLocation().add(0,2.5F,0);
                                    entity.getWorld().spawnParticle(Particle.TOWN_AURA,location,5);
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0,3);
                    }
                }
                damager.setHealth(damager.getHealth() + event.getFinalDamage()*0.5);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedInventory != null && event.getView().getTitle().equals("§aSelect Players to Buff")) {
            event.setCancelled(true); // Prevents players from taking items out of the inventory

            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                // Toggle player selection
                UUID selectedPlayerUUID = Bukkit.getOfflinePlayer(clickedItem.getItemMeta().getDisplayName()).getUniqueId();
                if (selectedPlayers.contains(selectedPlayerUUID)) {
                    selectedPlayers.remove(selectedPlayerUUID);
                } else {
                    if (selectedPlayers.size() < 5) {
                        selectedPlayers.add(selectedPlayerUUID);
                    } else {
                        player.sendMessage("§cYou can only select up to 5 players!");
                        return;
                    }
                }

                // Update lore to reflect selection status
                updateItemLore(clickedItem, selectedPlayers.contains(selectedPlayerUUID));

                player.updateInventory();
            }
        }
    }

    private void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(player, 27, "§aSelect Players to Buff");

        // Get online players
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());

        // Populate the menu with online players
        for (Player onlinePlayer : onlinePlayers) {
            ItemStack playerHead = getPlayerHeadItem(onlinePlayer);
            updateItemLore(playerHead, selectedPlayers.contains(onlinePlayer.getUniqueId())); // Update lore based on selection status
            menu.addItem(playerHead);
        }

        player.openInventory(menu);
    }

    private ItemStack getPlayerHeadItem(Player player) {
        ItemStack headItem = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta headMeta = (SkullMeta) headItem.getItemMeta();
        headMeta.setDisplayName("§r"+player.getName());
        headMeta.setOwningPlayer(player);
        headItem.setItemMeta(headMeta);
        return headItem;
    }

    private void updateItemLore(ItemStack item, boolean isSelected) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (isSelected) {
            lore.add("§aSelected"); // Add lore indicating selected status
        } else {
            lore.add("§cNot Selected"); // Add lore indicating not selected status
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getView().getTitle().equals("§aSelect Players to Buff")) {
            int level = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
            // Apply strength effect to selected players
            for (UUID selectedPlayerUUID : selectedPlayers) {
                Player selectedPlayer = Bukkit.getPlayer(selectedPlayerUUID);
                if (selectedPlayer != null) {
                    selectedPlayer.getWorld().playSound(selectedPlayer.getLocation(),Sound.BLOCK_AMETHYST_BLOCK_BREAK,1,1);
                    if (level == 20) {
                        selectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*30, 0)); // Strength effect
                        selectedPlayer.setHealth(20);
                        selectedPlayer.setFoodLevel(20);
                        selectedPlayer.setSaturation(20);
                        selectedPlayer.setFireTicks(0);
                        //Effect
                        CircleEffect effect = new CircleEffect(NikeyV1.em);
                        effect.setEntity(selectedPlayer);
                        effect.duration=1000*30;
                        effect.enableRotation=false;
                        effect.particle=Particle.SPELL_INSTANT;
                        effect.start();
                        //Remove Buff
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                selectedPlayers.remove(selectedPlayerUUID);
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*30);
                    } else if (level == 21) {
                        selectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*45, 0)); // Strength effect
                        selectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*45, 1));
                        selectedPlayer.setHealth(20);
                        selectedPlayer.setFoodLevel(20);
                        selectedPlayer.setSaturation(20);
                        selectedPlayer.setFireTicks(0);
                        //Effect
                        CircleEffect effect = new CircleEffect(NikeyV1.em);
                        effect.setEntity(selectedPlayer);
                        effect.duration=1000*45;
                        effect.enableRotation=false;
                        effect.particle=Particle.SPELL_INSTANT;
                        effect.start();
                        //Remove Buff
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                selectedPlayers.remove(selectedPlayerUUID);
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*45);
                        //Repair Armor
                        for (ItemStack armor : selectedPlayer.getInventory().getArmorContents()) {
                            armor.setDurability((short) 0);
                        }
                    }
                }
            }
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!selectedPlayers.contains(onlinePlayer.getUniqueId()) && onlinePlayer != player) {
                    for (UUID selectedPlayerUUID : selectedPlayers) {
                        Player selectedPlayer = Bukkit.getPlayer(selectedPlayerUUID);
                        if (selectedPlayer != null) {
                            onlinePlayer.hidePlayer(NikeyV1.getPlugin(), selectedPlayer);
                            selectedPlayer.sendMessage(ChatColor.BLUE+player.getName()+ChatColor.GREEN+" buffed you");
                            if (level == 20) {
                                Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> {
                                    for (Player p : getServer().getOnlinePlayers()) {
                                        onlinePlayer.showPlayer(NikeyV1.getPlugin(), selectedPlayer  );
                                    }
                                    vanishedPlayers.remove(player);
                                }, 20*20); // 20 seconds (20 ticks per second)
                            } else if (level == 21) {
                                Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> {
                                    for (Player p : getServer().getOnlinePlayers()) {
                                        onlinePlayer.showPlayer(NikeyV1.getPlugin(), selectedPlayer  );
                                    }
                                    vanishedPlayers.remove(player);
                                }, 20*30); // 30 seconds (20 ticks per second)
                            }
                            vanishedPlayers.add(player);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (vanishedPlayers.contains(player)) {
            for (Player p : getServer().getOnlinePlayers()) {
                p.showPlayer(NikeyV1.getPlugin(), player);
            }
            vanishedPlayers.remove(player);
        }
    }

    private void repairRandomArmorPiece(Player player) {
        ItemStack[] armorContents = player.getInventory().getArmorContents();
        ItemStack[] nonRepairedArmorPieces = getNonRepairedArmorPieces(armorContents);

        if (nonRepairedArmorPieces.length == 0) {
            // Alle Rüstungsteile sind bereits vollständig repariert
            return;
        }

        // Wähle ein zufälliges nicht repariertes Rüstungsteil aus
        Random random = new Random();
        ItemStack armorToRepair = nonRepairedArmorPieces[random.nextInt(nonRepairedArmorPieces.length)];

        // Repariere das ausgewählte Rüstungsteil um 1 Haltbarkeitspunkt
        short currentDurability = armorToRepair.getDurability();
        short maxDurability = armorToRepair.getType().getMaxDurability();
        if (currentDurability < maxDurability) {
            armorToRepair.setDurability((short) (currentDurability - 1));
        }
    }

    // Methode zum Abrufen der nicht reparierten Rüstungsteile eines Spielers
    private ItemStack[] getNonRepairedArmorPieces(ItemStack[] armorContents) {
        List<ItemStack> nonRepairedPieces = new ArrayList<>();
        for (ItemStack armorPiece : armorContents) {
            if (armorPiece != null && armorPiece.getDurability() < armorPiece.getType().getMaxDurability()) {
                nonRepairedPieces.add(armorPiece);
            }
        }
        return nonRepairedPieces.toArray(new ItemStack[0]);
    }
}
