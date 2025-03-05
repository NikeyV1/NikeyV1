package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import net.kyori.adventure.text.Component;
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

    private final Set<Player> rootedPlayers = new HashSet<>();

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
                        player.sendMessage(String.valueOf(Math.min(20,player.getSaturation()+1)));
                        break;
                    }
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, 100);
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

    private void explodeRoots(Player target) {
        World world = target.getWorld();
        world.spawnParticle(Particle.EXPLOSION_EMITTER, target.getLocation(), 1);
        world.createExplosion(target.getLocation(),3.5f,false,false);

        target.setWalkSpeed(0.2f);
        target.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(0.41999998688697815);

        Location originLocation = target.getLocation().clone().add(0, 1, 0);
        Vector knockback = target.getLocation().toVector().subtract(originLocation.toVector()).normalize().multiply(0.5);
        target.setVelocity(knockback);
    }
}
