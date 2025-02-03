package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import de.slikey.effectlib.effect.CircleEffect;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class Holystone implements Listener {
    public static ArrayList<Entity> stunned = new ArrayList<>();
    public static ArrayList<Player> hitted = new ArrayList<>();
    private final List<UUID> selectedPlayers = new ArrayList<>();
    public static final Set<Player> vanishedPlayers = new HashSet<>();
    public static final Map<Player, BukkitRunnable> auraTasks = new HashMap<>();
    public static HashMap<Player, BukkitTask> repairing = new HashMap<>();
    
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();


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

    public static void startRepairing(Player player) {
        int level = Stone.getLevel(player);
        String stone = Stone.getName(player);

        if (!stone.equalsIgnoreCase("Holy"))return;
        if (!(level >= 7))return;

        int period = 40;
        if (level == 8) {
            period = 20;
        } else if (level >= 9) {
            period = 10;
        }

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!Stone.getName(player).equalsIgnoreCase("Holy")) {
                    repairing.remove(player);
                    cancel();
                    return;
                }

                if (level != Stone.getLevel(player)) {
                    startRepairing(player);
                    repairing.remove(player);
                    cancel();
                    return;
                }

                repairRandomArmorPiece(player);
            }
        }.runTaskTimer(NikeyV1.getPlugin(),0,period);

        repairing.put(player,task);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p =  event.getPlayer();
        startRepairing(p);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (Stone.whatStone(item).equalsIgnoreCase("Holy")){
            int level = Stone.getLevelFromItem(item);
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (level >= 10){
                    if (!(cooldown.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (100 * 1000));

                        applyEffect(p,30*30);

                        if (level >= 13) {
                            p.removePotionEffect(PotionEffectType.WEAKNESS);
                            p.removePotionEffect(PotionEffectType.POISON);
                            p.removePotionEffect(PotionEffectType.DARKNESS);
                            p.removePotionEffect(PotionEffectType.LEVITATION);
                            p.removePotionEffect(PotionEffectType.BLINDNESS);
                            p.removePotionEffect(PotionEffectType.SLOWNESS);
                            p.removePotionEffect(PotionEffectType.NAUSEA);
                            p.removePotionEffect(PotionEffectType.WITHER);
                        }
                    }            
                }
            }else if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR){
                if (!p.isSneaking()) {
                    double radius = 7.5;
                    if (level >= 16)radius = 10;
                    double multiplyer = 1.6;
                    if (level >= 18)multiplyer = 1.9;
                    int extraDamage = 5;
                    if (level >= 17)extraDamage = 10;
                    int extraLevel = 0;
                    if (level >= 19)extraLevel = 1;

                    if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));

                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
                        for (Player player : p.getWorld().getNearbyPlayers(p.getLocation(),radius)) {
                            double armor = player.getAttribute(Attribute.ARMOR).getValue();
                            armor = armor * multiplyer;

                            double damage;

                            damage = armor + extraDamage;
                            player.damage(damage, p);

                            if (damage < 20) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, extraLevel));
                            } else if (damage >= 20 && damage < 30) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 15 * 20, 1+extraLevel));
                            } else {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 2+extraLevel));
                            }

                            break;
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


    public void applyEffect(Player player, int taskDelay) {
        int level = Stone.getLevel(player);

        int hearts = 8;

        if (level == 11) {
            hearts = 12;
        } else if (level == 12) {
            hearts = 16;
        }else if (level >= 14) {
            hearts = 20;
        }

        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20+hearts);

        player.heal(40, EntityRegainHealthEvent.RegainReason.CUSTOM);

        CircleEffect effect = new CircleEffect(NikeyV1.em);
        effect.setEntity(player);
        effect.duration = taskDelay;
        effect.start();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20);
                }
            }
        }.runTaskLater(NikeyV1.getPlugin(), taskDelay);
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

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (Stone.whatStone(item).equalsIgnoreCase("Holy")){
            int level = Stone.getLevel(p);
            if (level == 20 || level == 21){
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
        if (event.getDamager() instanceof Player damager) {
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
                                }else {
                                    Location location = entity.getLocation().add(0,2.5F,0);
                                    entity.getWorld().spawnParticle(Particle.WHITE_ASH,location,5);
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0,3);
                    }
                }
                damager.heal(event.getFinalDamage() * 0.5, EntityRegainHealthEvent.RegainReason.CUSTOM);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedInventory != null && event.getView().title() == Component.text("Select Players to Buff").color(NamedTextColor.GREEN)) {
            event.setCancelled(true);

            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                UUID selectedPlayerUUID = Bukkit.getOfflinePlayer(clickedItem.getItemMeta().customName().toString()).getUniqueId();
                if (selectedPlayers.contains(selectedPlayerUUID)) {
                    selectedPlayers.remove(selectedPlayerUUID);
                } else {
                    if (selectedPlayers.size() < 5) {
                        selectedPlayers.add(selectedPlayerUUID);
                    } else {
                        player.sendMessage(Component.text("You can only select up to 5 players!").color(NamedTextColor.RED));
                        return;
                    }
                }

                updateItemLore(clickedItem, selectedPlayers.contains(selectedPlayerUUID));

                player.updateInventory();
            }
        }
    }

    private void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(player, 27, Component.text("Select Players to Buff").color(NamedTextColor.GREEN));

        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player onlinePlayer : onlinePlayers) {
            ItemStack playerHead = getPlayerHeadItem(onlinePlayer);
            updateItemLore(playerHead, selectedPlayers.contains(onlinePlayer.getUniqueId()));
            menu.addItem(playerHead);
        }

        player.openInventory(menu);
    }

    private ItemStack getPlayerHeadItem(Player player) {
        ItemStack headItem = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta headMeta = (SkullMeta) headItem.getItemMeta();
        headMeta.displayName(Component.text(player.getName()).color(NamedTextColor.GRAY));
        headMeta.setPlayerProfile(player.getPlayerProfile());
        headItem.setItemMeta(headMeta);
        return headItem;
    }

    private void updateItemLore(ItemStack item, boolean isSelected) {
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = new ArrayList<>();
        if (isSelected) {
            lore.add(Component.text("Selected").color(NamedTextColor.GREEN));
        } else {
            lore.add(Component.text("Not Selected").color(NamedTextColor.RED));
        }
        meta.lore(lore);
        item.setItemMeta(meta);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getView().title() == Component.text("Select Players to Buff").color(NamedTextColor.GREEN)) {
            int level = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
            for (UUID selectedPlayerUUID : selectedPlayers) {
                Player selectedPlayer = Bukkit.getPlayer(selectedPlayerUUID);
                if (selectedPlayer != null) {
                    selectedPlayer.getWorld().playSound(selectedPlayer.getLocation(),Sound.BLOCK_AMETHYST_BLOCK_BREAK,1.2F,1);
                    if (level == 20) {
                        selectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20*30, 1)); // Strength effect
                        selectedPlayer.heal(100, EntityRegainHealthEvent.RegainReason.CUSTOM);
                        selectedPlayer.setFoodLevel(20);
                        selectedPlayer.setSaturation(20);
                        selectedPlayer.setFireTicks(0);

                        CircleEffect effect = new CircleEffect(NikeyV1.em);
                        effect.setEntity(selectedPlayer);
                        effect.duration=1000*30;
                        effect.enableRotation=false;
                        effect.particle=Particle.INSTANT_EFFECT;
                        effect.start();

                        selectedPlayer.sendMessage(Component.text(player.getName()).color(NamedTextColor.BLUE)
                                .append(Component.text(" buffed you").color(NamedTextColor.GREEN)));
                        //Remove Buff
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                selectedPlayers.remove(selectedPlayerUUID);
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*30);
                    } else if (level == 21) {
                        selectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20*45, 1));
                        selectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*45, 1));
                        selectedPlayer.heal(100, EntityRegainHealthEvent.RegainReason.CUSTOM);
                        selectedPlayer.setFoodLevel(20);
                        selectedPlayer.setSaturation(20);
                        selectedPlayer.setFireTicks(0);
                        //Effect
                        CircleEffect effect = new CircleEffect(NikeyV1.em);
                        effect.setEntity(selectedPlayer);
                        effect.duration=1000*45;
                        effect.enableRotation=false;
                        effect.particle=Particle.INSTANT_EFFECT;
                        effect.start();
                        selectedPlayer.sendMessage(Component.text(player.getName()).color(NamedTextColor.BLUE)
                                .append(Component.text(" buffed you").color(NamedTextColor.GREEN)));

                        //Remove Buff
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                selectedPlayers.remove(selectedPlayerUUID);
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*45);

                        for (ItemStack armor : selectedPlayer.getInventory().getArmorContents()) {
                            if (armor instanceof Damageable damageable) {
                                damageable.resetDamage();
                                armor.setItemMeta(damageable);
                            }
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

                            int delayTicks;
                            if (level == 20) {
                                delayTicks = 20 * 15; // 20 seconds
                            } else if (level == 21) {
                                delayTicks = 20 * 25; // 30 seconds
                            } else {
                                continue;
                            }
                            vanishedPlayers.add(player);

                            Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> {
                                onlinePlayer.showPlayer(NikeyV1.getPlugin(), selectedPlayer);
                                vanishedPlayers.remove(player);
                                player.sendMessage(Component.text("You are now visible!").color(NamedTextColor.AQUA));
                            }, delayTicks);
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

    private static void repairRandomArmorPiece(Player player) {
        ItemStack[] nonRepairedArmorPieces = getNonRepairedArmorPieces(player.getInventory().getArmorContents());

        for (ItemStack armor : nonRepairedArmorPieces) {
            ItemMeta meta = armor.getItemMeta();
            if (meta instanceof Damageable damageable) {
                if (damageable.hasDamage()) {
                    int damage = damageable.getDamage();
                    if (damage > 1) {
                        damageable.setDamage(damage - 1);
                    } else {
                        damageable.resetDamage();
                    }

                    armor.setItemMeta(damageable);
                }
            }
        }

    }

    private static ItemStack[] getNonRepairedArmorPieces(ItemStack[] armorContents) {
        List<ItemStack> nonRepairedPieces = new ArrayList<>();
        for (ItemStack armorPiece : armorContents) {
            if (armorPiece.getItemMeta() instanceof Damageable damageable) {
                if (damageable.hasDamage()) nonRepairedPieces.add(armorPiece);
            }
        }
        return nonRepairedPieces.toArray(new ItemStack[0]);
    }
}
