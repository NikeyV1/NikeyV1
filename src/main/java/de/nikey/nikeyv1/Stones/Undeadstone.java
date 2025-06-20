package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.StoneHandler;
import de.slikey.effectlib.effect.SmokeEffect;
import io.papermc.paper.entity.TeleportFlag;
import io.papermc.paper.event.entity.EntityMoveEvent;
import io.papermc.paper.event.entity.WardenAngerChangeEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ALL")
public class Undeadstone implements Listener {
    public ArrayList<LivingEntity> Soul = new ArrayList<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();

    private HashMap<Player, Integer> timer = new HashMap<>();
    private final Map<UUID, BukkitRunnable> invulnerableTasks = new HashMap<>();
    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (!item.hasItemMeta())return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stone")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Undead");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (!Soul.isEmpty()) {
                    EntityType type = Soul.get(0).getType();
                    if (!(type == EntityType.WITHER || type == EntityType.ENDER_DRAGON || type == EntityType.EVOKER || type == EntityType.GIANT)) {

                        LivingEntity summoned = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), type);
                        summoned.setCustomName(p.getDisplayName() + "'s " + type.getName());

                        if (i >= 11) {
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 400, 1));
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 1));
                        }
                        if (i >= 12) {
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 19200, 0));
                        }
                        if (i >= 13) {
                            summoned.setAbsorptionAmount(4);
                        }
                        if (i >= 14) {
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 19200, 0));
                        }

                        Soul.clear();
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1, 1);

                        ItemStack hand = p.getInventory().getItemInMainHand();
                        ItemMeta meta = hand.getItemMeta();
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.of("#221726") + "Souls wander around in this stone");
                        lore.add(ChatColor.of("#00FFAA") + "Level:" + i);
                        meta.setLore(lore);
                        hand.setItemMeta(meta);
                    }
                }
            } else if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && p.isSneaking()) {
                if (i >= 15 && ability.getOrDefault(p.getUniqueId(), 0L) <= System.currentTimeMillis()) {
                    int duration = (i >= 18) ? 50 : 40;
                    ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                    timer.put(p, duration);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (timer.get(p) == 0) {
                                timer.remove(p);
                                cancel();
                                return;
                            }

                            int x = p.getLocation().getBlockX();
                            int z = p.getLocation().getBlockZ();
                            int randomX = ThreadLocalRandom.current().nextInt(x - 6, x + 6);
                            int randomZ = ThreadLocalRandom.current().nextInt(z - 6, z + 6);
                            int randomY = p.getWorld().getHighestBlockYAt(randomX, randomZ);
                            Location location = new Location(p.getWorld(), randomX, randomY + 1, randomZ);

                            // SmokeEffect
                            SmokeEffect effect = new SmokeEffect(NikeyV1.em);
                            effect.setLocation(location);
                            effect.particle = Particle.SOUL;
                            effect.duration = 100;
                            effect.particles = 3;
                            effect.start();

                            // Mob-Auswahl
                            Random random = new Random();
                            int r = (i == 15) ? random.nextInt(2) : random.nextInt(3); // nur 0-1 bei Level 15

                            LivingEntity mob;
                            switch (r) {
                                case 1:
                                    mob = location.getWorld().spawn(location, ZombieVillager.class);
                                    break;
                                case 2:
                                    mob = location.getWorld().spawn(location, WitherSkeleton.class);
                                    break;
                                default:
                                    mob = location.getWorld().spawn(location, Zombie.class);
                                    break;
                            }

                            // Effekte
                            mob.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 400, 0));
                            mob.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 4));
                            int fireDuration = (i >= 17) ? 12000 : 400;
                            mob.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, fireDuration, 0));

                            if (i >= 19) {
                                mob.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 1));
                                mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 1));
                            }

                            mob.setCustomName(p.getDisplayName() + "'s " + mob.getType().getName());
                            timer.replace(p, timer.get(p) - 1);
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(), 10, 10);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (item == null) return;
        if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stone")&& event.getItemDrop().getItemStack().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Undead");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (i == 20 || i == 21){
                if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                    cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));

                    spawnRidingHusk(p);
                }
            }
        }
    }


    private void spawnRidingHusk(Player player) {
        Location added = player.getLocation().getWorld().getHighestBlockAt(player.getLocation()).getLocation();
        Location l = added.clone().add(0,1,0);
        Zombie zombie = (Zombie) player.getLocation().getWorld().spawnEntity(l, EntityType.ZOMBIE);
        zombie.setMaxHealth(300);
        zombie.setHealth(300);
        zombie.setShouldBurnInDay(false);
        zombie.getAttribute(Attribute.SCALE).setBaseValue(3.5F);
        zombie.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(8);
        zombie.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(6);
        zombie.getAttribute(Attribute.SAFE_FALL_DISTANCE).setBaseValue(7);
        zombie.getAttribute(Attribute.ARMOR).setBaseValue(10);
        zombie.getAttribute(Attribute.ARMOR_TOUGHNESS).setBaseValue(6);
        zombie.getAttribute(Attribute.KNOCKBACK_RESISTANCE).setBaseValue(10);
        zombie.getAttribute(Attribute.FOLLOW_RANGE).setBaseValue(100);
        zombie.setMetadata("master", new FixedMetadataValue(NikeyV1.getPlugin(), true));
        zombie.setCanBreakDoors(true);
        zombie.setAdult();
        zombie.getEquipment().setItem(EquipmentSlot.HAND,new ItemStack(Material.DIAMOND_SWORD));
        int level = StoneHandler.getStoneLevel(player);
        if (level == 20) {
            zombie.setCustomName(player.getName()+"'s "+zombie.getType().getName());
            zombie.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.35F);
        } else if (level == 21) {
            zombie.setCustomName(player.getName()+"'s "+zombie.getType().getName()+" strong");
            zombie.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.5F);
        }
        zombie.setCustomNameVisible(false);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (zombie.isValid()) {
                    zombie.remove();
                }
            }
        }.runTaskLater(NikeyV1.getPlugin(), 5 * 60 * 20);
    }

    @EventHandler
    public void onEntityTransform(EntityTransformEvent event) {
        if (!(event.getEntity() instanceof Zombie)) return;
        if (event.getTransformReason() == EntityTransformEvent.TransformReason.DROWNED && StoneHandler.isUndeadMaster((LivingEntity) event.getEntity())) {
            Zombie living = (Zombie) event.getEntity();
            event.setCancelled(true);
            living.setConversionTime(2400);
        }
    }


    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        if (StoneHandler.isUndeadMaster((LivingEntity) entity)) {
            Zombie zombie = (Zombie) entity;
            if (event.getFinalDamage() > 20) {
                event.setDamage(20);
            }
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && zombie.getCustomName().contains("low")) {
                for (Entity nearbyEntity : zombie.getNearbyEntities(30, 30, 30)) {
                    if (nearbyEntity instanceof LivingEntity) {
                        String[] arr = entity.getCustomName().split("'");
                        Player p = Bukkit.getPlayer(arr[0]);
                        if (HelpUtil.shouldDamageEntity((LivingEntity) nearbyEntity,p)) {
                            LivingEntity player = (LivingEntity) nearbyEntity;
                            Vector dir = player.getLocation().toVector().subtract(zombie.getLocation().toVector()).normalize();
                            player.setVelocity(dir.multiply(-2.5F).add(new Vector(0,1.5F,0)));
                            player.damage(player.getHealth(),zombie);
                        }
                    }
                }
                zombie.getWorld().playSound(zombie,Sound.ENTITY_WITHER_BREAK_BLOCK,1,1);
                event.setCancelled(true);
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,6000,0));
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,6000,0));
            }
        }
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity))return;
        if (StoneHandler.isUndeadMaster((LivingEntity) entity)) {
            double eyeLocation = ((LivingEntity) entity).getEyeLocation().getY() -5;
            Location loc = new Location(entity.getWorld(),entity.getX(),eyeLocation,entity.getZ());
            for (Entity entitys : loc.getNearbyEntities(3,2,3)) {
                if (!(entitys instanceof LivingEntity)) return;
                LivingEntity living = (LivingEntity) entitys;
                String[] arr = entity.getCustomName().split("'");
                Player p = Bukkit.getPlayer(arr[0]);
                if (p == null) return;
                if (StoneHandler.isSummoned(p,event.getEntity()) && HelpUtil.shouldDamageEntity(living,p) ) {
                    if (living != event.getEntity() && !StoneHandler.isSummoned(p,living)) {
                        HelpUtil.spawnParticles(loc,3,0,-2,0,Particle.LARGE_SMOKE,0);
                        if (!entity.getCustomName().contains("low")) {
                            if (!entity.getCustomName().contains("strong")) {
                                double armor = living.getAttribute(Attribute.ARMOR).getValue();
                                armor = armor*0.10;
                                living.damage(armor+1.5F,entity);
                            }else{
                                double armor = living.getAttribute(Attribute.ARMOR).getValue();
                                armor = armor*0.15;
                                living.damage(armor+3.5F,entity);
                            }
                        }else {
                            if (!entity.getCustomName().contains("strong")) {
                                double armor = living.getAttribute(Attribute.ARMOR).getValue();
                                armor = armor*0.1;
                                living.damage(armor+2.5F,entity);
                            }else{
                                double armor = living.getAttribute(Attribute.ARMOR).getValue();
                                armor = armor*0.15;
                                living.damage(armor+4.5F,entity);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            int level = StoneHandler.getStoneLevel(player);
            if (StoneHandler.getStoneName(player).equalsIgnoreCase("undead")) {
                if (player.getGameMode() == GameMode.SURVIVAL && level >= 3) {
                    player.getWorld().spawnParticle(Particle.RAID_OMEN, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20*6, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*6, 1));
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new net.md_5.bungee.api.chat.TextComponent(org.bukkit.ChatColor.DARK_RED + "You are in a rush!"));

                    if (level >= 4) {
                        player.setInvulnerable(true);

                        // Cancel the previous task if it exists
                        if (invulnerableTasks.containsKey(player.getUniqueId())) {
                            invulnerableTasks.get(player.getUniqueId()).cancel();
                        }

                        // Create a new task
                        BukkitRunnable task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.setInvulnerable(false);
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(org.bukkit.ChatColor.DARK_RED + "Your rush has worn off!"));
                                invulnerableTasks.remove(player.getUniqueId());
                            }
                        };
                        task.runTaskLater(NikeyV1.getPlugin(), 20*6);
                        invulnerableTasks.put(player.getUniqueId(), task);
                    }

                    if (level >= 5) {
                        player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue());
                        player.setFoodLevel(20);
                        player.setSaturation(20);
                    }
                }
            }
        }

        if (entity.getCustomName() != null && entity.getCustomName().contains("'s")) {
            event.getDrops().clear();
        }
        if (entity.getKiller() instanceof Player && !(entity instanceof Player) && !(entity instanceof Giant)){
            Player p = (Player) entity.getKiller();
            if (p.getInventory().getItemInMainHand().getItemMeta() == null)return;
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stone")){
                String[] arr = p.getItemInHand().getLore().get(1).split(":");
                int i = Integer.parseInt(arr[1]);
                if (!(cooldown.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                    cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (100 * 1000));
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

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getHitEntity() != null) {
            Entity hitEntity = event.getHitEntity();
            if (hitEntity instanceof LivingEntity) {
                if (StoneHandler.isUndeadMaster((LivingEntity) hitEntity) && hitEntity.getCustomName().contains("low") ) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean isUndead(LivingEntity entity) {
        EntityType type = entity.getType();
        return type == EntityType.ZOMBIE || type == EntityType.SKELETON || type == EntityType.ZOGLIN|| type == EntityType.ZOMBIFIED_PIGLIN|| type == EntityType.ZOMBIE_VILLAGER|| type == EntityType.PHANTOM || type == EntityType.DROWNED  || type == EntityType.WITHER_SKELETON || type == EntityType.STRAY || type == EntityType.HUSK;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            String stone = StoneHandler.getStoneName(p);
            if (stone.equalsIgnoreCase("Undead") ) {
                if (event.getDamager() instanceof Projectile) {
                    Projectile projectile = (Projectile) event.getDamager();
                    ProjectileSource shooter = projectile.getShooter();
                    if (shooter instanceof LivingEntity) {
                        HelpUtil.triggerEntityAggro((LivingEntity) shooter,p);
                    }
                }else {
                    LivingEntity damager = (LivingEntity) event.getDamager();
                    HelpUtil.triggerEntityAggro(damager,p);
                }
            }

            int level = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            if (stone.equalsIgnoreCase("Undead") && level >= 6) {
                if (event.getDamager() instanceof LivingEntity) {
                    if (isUndead((LivingEntity) event.getDamager())) {
                        double damageReductionPercentage = 15;
                        double damage = event.getDamage();
                        double reducedDamage = damage - (damage * (damageReductionPercentage / 100));
                        event.setDamage(reducedDamage);
                    }
                }
            }
        }

        if (event.getEntity() instanceof LivingEntity) {
            if (StoneHandler.isUndeadMaster((LivingEntity) event.getEntity())) {
                Zombie giant = (Zombie) event.getEntity();
                double health = giant.getHealth();
                if (health <= 100) {
                    if (!(event.getCause() == EntityDamageEvent.DamageCause.FALL)) {

                        if (!giant.getCustomName().contains("low") && giant.getHealth() <100) {
                            Block h = giant.getWorld().getHighestBlockAt(giant.getLocation());
                            Location loc = h.getLocation().add(0, 50, 0);
                            int y = (int) giant.getLocation().getY();
                            y += 50;
                            int x = (int) giant.getLocation().getX();
                            int z = (int) giant.getLocation().getZ();
                            giant.teleport(new Location(giant.getWorld(),x,y,z), PlayerTeleportEvent.TeleportCause.PLUGIN, TeleportFlag.EntityState.RETAIN_PASSENGERS);
                            giant.setCustomName(giant.getCustomName() +" low");
                            giant.setHealth(200);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    giant.setVelocity(new Vector(0, -1, 0));
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20L);
                        }
                    }
                }
            }
        }

        if (event.getDamager() instanceof Player){
            Player p = (Player) event.getDamager();
            if (event.getEntity() instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) event.getEntity();
                String stone = config.getString(p.getName() + ".stone");

                if (stone.equalsIgnoreCase("Undead")) {
                    if (entity != null) {
                        HelpUtil.triggerEntityAggro(entity,p);
                    }
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
    public void onWardenAngerChange(WardenAngerChangeEvent event) {
        Warden entity = event.getEntity();
        Entity target = event.getTarget();
        if (event.getTarget() instanceof Player){
            if (event.getEntity().getCustomName() != null) {
                if (event.getEntity().getCustomName().equalsIgnoreCase(((Player) event.getTarget()).getDisplayName()+"'s "+event.getEntityType().getName())){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event){
        if (event.getTarget() instanceof Player && event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();
            Player player = (Player) event.getTarget();
            if (StoneHandler.isUndeadMaster(zombie)) {
                String[] arr = zombie.getCustomName().split("'");
                if (player.getName().equalsIgnoreCase(arr[0])) {
                    event.setCancelled(true);
                }
            }
        }


        if (event.getTarget() instanceof Player && event.getEntity() instanceof LivingEntity) {
            if (event.getEntity().getCustomName() != null) {
                LivingEntity entity = (LivingEntity) event.getEntity();
                String[] arr = entity.getCustomName().split("'");
                Player player = Bukkit.getPlayer(arr[0]);
                if (player == null) {
                    return;
                }
                if (HelpUtil.isTrusted(player, (Player) event.getTarget())) {
                    event.setCancelled(true);
                }
            }
        }

        if (event.getTarget() instanceof Player){
            if (event.getEntity() instanceof LivingEntity){
                if (event.getEntity().getCustomName() != null) {
                    if (event.getEntity().getCustomName().equalsIgnoreCase(((Player) event.getTarget()).getDisplayName()+"'s "+event.getEntityType().getName())){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            String stone = NikeyV1.getPlugin().getConfig().getString(event.getDamager().getName() + ".stone");
            int level = NikeyV1.getPlugin().getConfig().getInt(event.getDamager().getName() + ".level");
            if (stone.equalsIgnoreCase("Undead")) {
                if (!(event.getEntity() instanceof Player)) {
                    if (level == 7) {
                        event.setDamage(event.getDamage() * 1.075);
                    } else if (level == 8) {
                        event.setDamage(event.getDamage() * 1.15);
                    } else if (level >= 9) {
                        event.setDamage(event.getDamage() * 1.225);
                    }
                }
            }
        }
    }
}