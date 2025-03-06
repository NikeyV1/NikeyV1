package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

public class Naturestone implements Listener {
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();

    //Ability 1
    private final Set<Player> rootedPlayers = new HashSet<>();

    //Master Ability
    private static final Set<SanctuaryZone> activeZones = new HashSet<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (Stone.whatStone(item).equalsIgnoreCase("Nature")) {
            int level = Stone.getStoneLevelFromItem(item);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(player.getName()+".stone","Nature");
            config.set(player.getName()+".level",level);
            NikeyV1.getPlugin().saveConfig();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (level >= 10){
                    if (!(cooldown.getOrDefault(player.getUniqueId(),0L) > System.currentTimeMillis())){
                        //cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (100 * 1000));

                        for (Player target : player.getWorld().getNearbyPlayers(player.getLocation(), 9)) {
                            if (HelpUtil.shouldDamageEntity(target, player)) {
                                Location baseLocation = target.getLocation().clone().add(0, 1.1, 0);
                                BlockData branchBlock = Material.MANGROVE_LOG.createBlockData();
                                Random random = new Random();

                                int branchCount = 4;
                                List<List<BlockDisplay>> allBranches = new ArrayList<>();

                                for (int i = 0; i < branchCount; i++) {
                                    double angleOffset = Math.toRadians(random.nextInt(360)); // Zufälliger Startwinkel
                                    List<BlockDisplay> branch = new ArrayList<>();
                                    double radius = 0.35; // Wie eng die Äste um die Beine wachsen

                                    new BukkitRunnable() {
                                        int j = 0; // Zähler für die Schleifeniteration

                                        @Override
                                        public void run() {
                                            if (j >= 5) {
                                                cancel();
                                                return;
                                            }

                                            double height = j * 0.12;
                                            double angle = angleOffset + j * Math.PI / 4;

                                            double xOffset = Math.cos(angle) * radius;
                                            double zOffset = Math.sin(angle) * radius;

                                            Location segmentLoc = baseLocation.clone().add(xOffset, height, zOffset);
                                            BlockDisplay segment = (BlockDisplay) target.getWorld().spawnEntity(segmentLoc, EntityType.BLOCK_DISPLAY);
                                            segment.setBlock(branchBlock);
                                            segment.setTransformation(new Transformation(
                                                    new Vector3f(0, -1.2f, 0),
                                                    new Quaternionf(),
                                                    new Vector3f(0.15f, 0.15f, 0.15f),
                                                    new Quaternionf()
                                            ));
                                            branch.add(segment);

                                            j++;
                                        }
                                    }.runTaskTimer(NikeyV1.getPlugin(), 0L, 2L);

                                    allBranches.add(branch);
                                }
                                castRootsAbility(target);

                                new BukkitRunnable() {
                                    int ticks = 0;

                                    @Override
                                    public void run() {
                                        if (ticks >= 100) {
                                            for (List<BlockDisplay> branch : allBranches) {
                                                for (BlockDisplay segment : branch) {
                                                    segment.remove();
                                                }
                                            }
                                            cancel();
                                            return;
                                        }

                                        if (ticks >= 80) {
                                            for (List<BlockDisplay> branch : allBranches) {
                                                for (BlockDisplay segment : branch) {
                                                    segment.teleport(segment.getLocation().add(0, -0.02, 0));
                                                }
                                            }
                                        }

                                        ticks++;
                                    }
                                }.runTaskTimer(NikeyV1.getPlugin(), 0, 1);
                            }
                        }
                    }
                }
            }else if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
                if (!player.isSneaking() && level >= 15) {
                    if (!(ability.getOrDefault(player.getUniqueId(),0L) > System.currentTimeMillis())){
                        ability.put(player.getUniqueId(), System.currentTimeMillis() + (180 * 1000));

                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!Stone.getStoneName(player).equalsIgnoreCase("Nature"))return;
        if (Stone.getStoneLevel(player) < 3)return;

        Biome biome = player.getLocation().getBlock().getBiome();

        double reduction;

        int level = Stone.getStoneLevel(player);

        if (level == 3) {
            reduction = 0.95;
        }else if (level == 4) {
            reduction = 0.9;
        }else {
            reduction = 0.85;
        }
        if (biome.getKey().value().contains("forest")) {
            event.setDamage(event.getDamage() * reduction);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!Stone.getStoneName(player).equalsIgnoreCase("Nature"))return;
        if (Stone.getStoneLevel(player) < 6)return;

        int level = Stone.getStoneLevel(player);

        int radius = 0;

        if (level == 6){
            radius = 5;
        }else if (level == 7){
            radius = 6;
        }else if (level == 8){
            radius = 7;
        }else if (level >= 9){
            radius = 8;
        }
        int finalRadius = radius;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || Stone.getStoneLevel(player) < 6) {
                    cancel();
                    return;
                }

                for (Entity entity : player.getNearbyEntities(finalRadius,finalRadius,finalRadius)) {
                    if (entity instanceof Animals) {
                        player.setSaturation(Math.min(20,player.getSaturation()+1));
                        player.sendHealthUpdate();
                        break;
                    }
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, 20*3);
    }



    public void castRootsAbility(Player target) {
        if (rootedPlayers.contains(target)) return;

        int level = Stone.getStoneLevel(target);
        int abilityLength = 8;
        if (level == 11 || level == 12 || level == 13) {
            abilityLength = 10;
        }else if (level >= 14) {
            abilityLength = 12;
        }

        rootedPlayers.add(target);
        Location startLocation = target.getLocation().clone();
        startLocation.setY(startLocation.getY() - 1);

        target.setWalkSpeed(0f);
        target.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(0);
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON,abilityLength*20,5));

        if (level >= 12) {
            for (PotionEffect effect : target.getActivePotionEffects()) {
                if (effect.getType().getCategory() == PotionEffectTypeCategory.BENEFICIAL) {
                    target.removePotionEffect(effect.getType());
                }
            }
        }

        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_SPIDER_HURT, 0.5f, 1.2f);

        int damage = 3;

        if (level >= 13) {
            damage = 4;
        }
        int finalAbilityLength = abilityLength;
        int finalDamage = damage;
        new BukkitRunnable() {
            int ticks = 0;
            final int maxTicks = 20* finalAbilityLength;

            @Override
            public void run() {
                if (ticks >= maxTicks || target.isDead() || !target.isOnline()) {
                    explodeRoots(target);
                    rootedPlayers.remove(target);
                    cancel();
                    return;
                }
                target.damage(finalDamage);

                ticks += 5;
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, 5);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (Stone.whatStone(item).equalsIgnoreCase("nature")){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Nature");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            if (i == 20 || i == 21){
                if (p.isSneaking()) {
                    if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));

                        createSanctuaryZone(p);
                    }
                }
            }
        }
    }

    public static void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (activeZones.isEmpty()) return;

                activeZones.removeIf(SanctuaryZone::update);
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, 20);
    }

    private void createSanctuaryZone(Player player) {
        int level = Stone.getStoneLevel(player);
        SanctuaryZone zone;
        if (level == 21) {
            zone = new SanctuaryZone(player, player.getLocation(), 40, 45);
        }else {
            zone = new SanctuaryZone(player, player.getLocation(), 30, 30);
        }
        activeZones.add(zone);
        player.showTitle(Title.title(Component.empty(),Component.text("Sanctuary Bloom placed!").color(NamedTextColor.GREEN)));
    }

    @EventHandler
    public void onPlayerResurrect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!event.isCancelled())return;

        for (SanctuaryZone zone : activeZones) {
            if (zone.isInZone(player) && !HelpUtil.shouldDamageEntity(player,zone.p) && zone.tryResurrect(player)) {
                event.setCancelled(false);
            }
        }
    }


    private static class SanctuaryZone {
        private final Location location;
        private final int radius;
        private final int maxTicks;
        private final Set<UUID> resurrectedPlayers = new HashSet<>();
        private int ticks = 0;
        private final Player p;

        public SanctuaryZone(Player player,Location location, int radius, int durationSeconds) {
            this.p = player;
            this.location = location;
            this.radius = radius;
            this.maxTicks = durationSeconds * 20;

            location.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location, 100, 2, 1, 2);
            location.getWorld().playSound(location, Sound.BLOCK_BEACON_ACTIVATE, 3, 1);
        }

        public boolean update() {
            if (ticks >= maxTicks) {
                location.getWorld().playSound(location, Sound.BLOCK_BEACON_DEACTIVATE, 3, 1);
                location.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 50, 2, 1, 2);
                return true;
            }

            for (Player player : location.getWorld().getNearbyPlayers(location,radius)) {
                if (isInZone(player) && !HelpUtil.shouldDamageEntity(player,p)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20*90, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*90, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*480, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*22, 1));
                }
            }

            if (ticks % 80 == 0) {
                triggerPulse();
            }

            ticks += 20;
            return false;
        }

        private void triggerPulse() {
            World world = location.getWorld();
            world.playSound(location, Sound.BLOCK_CONDUIT_ACTIVATE, 1, 1);
            for (int i = 0; i <= radius; i++) {
                int later;
                if (Stone.getStoneLevel(p) == 21) {
                    later = i;
                }else {
                    later = i*2;
                }
                int finalI = i;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (double angle = 0; angle < 360; angle += 10) {
                            double radians = Math.toRadians(angle);
                            double x = Math.cos(radians) * finalI;
                            double z = Math.sin(radians) * finalI;

                            Location particleLoc = location.clone().add(x, 1, z);
                            world.spawnParticle(Particle.DUST_COLOR_TRANSITION, particleLoc, 1, new Particle.DustTransition(Color.GREEN, Color.YELLOW, 1.5f));
                        }

                        if (finalI == radius) {
                            healTeammatesInZone();
                        }
                    }
                }.runTaskLater(NikeyV1.getPlugin(), later);
            }
        }

        private void healTeammatesInZone() {
            for (Player player : location.getWorld().getPlayers()) {
                if (isInZone(player) && !HelpUtil.shouldDamageEntity(player,p)) {
                    if (Stone.getStoneLevel(player) == 21) {
                        player.heal(5, EntityRegainHealthEvent.RegainReason.MAGIC);
                        player.applyMending(12);
                    }else {
                        player.heal(3, EntityRegainHealthEvent.RegainReason.MAGIC);
                        player.applyMending(6);
                    }
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1, 1);
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 3, 0.5, 0.5, 0.5);
                }
            }
        }

        public boolean isInZone(Player player) {
            return player.getWorld().equals(location.getWorld()) && player.getLocation().distance(location) <= radius;
        }

        public boolean tryResurrect(Player player) {
            if (resurrectedPlayers.contains(player.getUniqueId())) return false;

            if (Stone.getStoneLevel(player) == 21) {
                player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
            }else {
                player.setHealth(8);
            }
            resurrectedPlayers.add(player.getUniqueId());

            player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
            player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 50, 0.5, 1, 0.5);
            player.sendMessage(Component.text("You have been resurrected by Sanctuary Bloom!").color(NamedTextColor.GOLD));

            return true;
        }
    }

    private void explodeRoots(Player target) {
        World world = target.getWorld();
        world.spawnParticle(Particle.EXPLOSION_EMITTER, target.getLocation(), 1);
        world.createExplosion(target.getLocation(),2.3f,false,false);

        target.setWalkSpeed(0.2f);
        target.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(0.41999998688697815);

        Location originLocation = target.getLocation().clone().add(0, 1, 0);
        Vector knockback = target.getLocation().toVector().subtract(originLocation.toVector()).normalize().multiply(0.5);
        target.setVelocity(knockback);
    }
}
