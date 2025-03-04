package de.nikey.nikeyv1.Stones;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import de.slikey.effectlib.effect.CylinderEffect;
import de.slikey.effectlib.effect.DonutEffect;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;

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

    public void castRootsAbility(Player target) {
        if (rootedPlayers.contains(target)) return;

        int abilityLength = 10;

        rootedPlayers.add(target);
        Location startLocation = target.getLocation().clone();
        startLocation.setY(startLocation.getY() - 1);

        target.setWalkSpeed(0f);
        target.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(0);
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON,abilityLength*20,5));
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_SPIDER_HURT, 0.5f, 1.2f);

        new BukkitRunnable() {
            int ticks = 0;
            final int maxTicks = 20*abilityLength;

            @Override
            public void run() {
                if (ticks >= maxTicks || target.isDead() || !target.isOnline()) {
                    explodeRoots(target);
                    rootedPlayers.remove(target);
                    cancel();
                    return;
                }

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
