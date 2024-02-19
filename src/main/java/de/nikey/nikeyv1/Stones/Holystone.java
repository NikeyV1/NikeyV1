package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.CircleEffect;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@SuppressWarnings("ALL")
public class Holystone implements Listener {
    public static ArrayList<Entity> stunned = new ArrayList<>();
    private final List<UUID> selectedPlayers = new ArrayList<>();
    private final Set<Player> vanishedPlayers = new HashSet<>();
    public static final Map<Player, BukkitRunnable> auraTasks = new HashMap<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;


    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        if (event.getEntity() instanceof Player){
            Player p = (Player) event.getEntity();
            String stone = config.getString(p.getName() + ".stone");
            if (stone.equalsIgnoreCase("Holy")){
                if (config.getInt(p.getName()+".level") == 3||config.getInt(p.getName()+".level") == 4){
                    event.setAmount(event.getAmount()+0.4);
                }else if (config.getInt(p.getName()+".level") >= 5){
                    event.setAmount(event.getAmount()+0.6);
                }
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
                    if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                        event.setCancelled(true);
                        p.updateInventory();
                        remainingTime1 = cooldown.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        cooldown.put(p.getUniqueId(),System.currentTimeMillis() + (100*1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown.remove(p.getUniqueId());
                                cancel();
                                return;
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*100);
                        if (i ==10 || i == 11){
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
                            }.runTaskLater(NikeyV1.getPlugin(),20*40);
                        }else if (i >= 12) {
                            World world = p.getWorld();
                            int players = p.getNearbyEntities(20, 20, 20).size();
                            if (players >18)players = 18;
                            p.removePotionEffect(PotionEffectType.WEAKNESS);
                            p.removePotionEffect(PotionEffectType.POISON);
                            p.removePotionEffect(PotionEffectType.DARKNESS);
                            p.removePotionEffect(PotionEffectType.LEVITATION);
                            p.removePotionEffect(PotionEffectType.BLINDNESS);
                            p.removePotionEffect(PotionEffectType.SLOW);
                            p.removePotionEffect(PotionEffectType.CONFUSION);
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
                if (!p.isSneaking()) {
                    if (i == 15||i == 16||i == 17){
                        if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                            p.updateInventory();
                            remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                        }else {
                            ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ability.remove(p.getUniqueId());
                                    cancel();
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*180);
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(20,20,20)){
                                if (e instanceof Player) {
                                    Player player =(Player) e;
                                    double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                    armor = armor*2;
                                    int players = p.getNearbyEntities(20, 20, 20).size();
                                    if (players <= 2){
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                    } else if (players  == 3||players == 4 || players == 5) {
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                    }else {
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,3));
                                    }
                                    player.damage(armor+10,p);
                                }
                                if (e instanceof LivingEntity){
                                    LivingEntity entity = (LivingEntity) e;
                                    entity.damage(12,p);
                                }
                            }
                        }
                    } else if (i >= 18) {
                        if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                            p.updateInventory();
                            remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                        }else {
                            ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ability.remove(p.getUniqueId());
                                    cancel();
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*180);
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(25,25,25)){
                                if (e instanceof Player) {
                                    Player player =(Player) e;
                                    double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                    armor = armor*2.6;
                                    int players = p.getNearbyEntities(25, 25, 25).size();
                                    if (players <= 2){
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                    } else if (players  == 3||players == 4 || players == 5) {
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                    }else {
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,3));
                                    }
                                    player.damage(armor+18,p);
                                }
                                if (e instanceof LivingEntity){
                                    LivingEntity entity = (LivingEntity) e;
                                    entity.damage(15,p);
                                }
                            }
                        }
                    }
                }
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
            if (i == 20 || i==21){
                if (p.isSneaking()) {
                    if (cooldown2.containsKey(p.getUniqueId()) && cooldown2.get(p.getUniqueId()) > System.currentTimeMillis()){
                        p.updateInventory();
                        remainingTime3 = cooldown2.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown2.remove(p.getUniqueId());
                                cancel();
                            }
                        }.runTaskLater(NikeyV1.getPlugin(), 20 * 300);
                        //Cooldown-Ability
                        openMenu(p);
                    }
                }
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
                        player.sendMessage("You can only select up to 5 players!");
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
            // Apply strength effect to selected players
            for (UUID selectedPlayerUUID : selectedPlayers) {
                Player selectedPlayer = Bukkit.getPlayer(selectedPlayerUUID);
                if (selectedPlayer != null) {
                    selectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*45, 1)); // Strength effect

                }
            }
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!selectedPlayers.contains(onlinePlayer.getUniqueId()) && onlinePlayer != player) {
                    for (UUID selectedPlayerUUID : selectedPlayers) {
                        Player selectedPlayer = Bukkit.getPlayer(selectedPlayerUUID);
                        if (selectedPlayer != null) {
                            startAura(selectedPlayer);
                            onlinePlayer.hidePlayer(NikeyV1.getPlugin(), selectedPlayer);
                            selectedPlayer.sendMessage(ChatColor.GREEN+player.getName()+" buffed you");
                            Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> {
                                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                    onlinePlayer.showPlayer(NikeyV1.getPlugin(), selectedPlayer  );
                                }
                                vanishedPlayers.remove(player);
                            }, 200L); // 10 seconds (20 ticks per second)

                            vanishedPlayers.add(player);
                        }
                    }
                }
            }
        }
    }

    private void startAura(Player player) {
        BukkitRunnable auraTask = new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                ticks++;
                if (ticks >= 200) { // 10 seconds (20 ticks per second)
                    cancel();
                    auraTasks.remove(player);
                }
                Location loc = player.getLocation().clone();
                spawnParticleCircle(loc);
            }
        };
        auraTask.runTaskTimer(NikeyV1.getPlugin(), 0, 1); // Run every tick
        auraTasks.put(player, auraTask);
    }

    private void spawnParticleCircle(Location center) {
        double radius = 2.0;
        double x, z;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 16) {
            x = radius * Math.cos(theta);
            z = radius * Math.sin(theta);
            center.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, center.getX() + x, center.getY(), center.getZ() + z, 1);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (vanishedPlayers.contains(player)) {
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                p.showPlayer(NikeyV1.getPlugin(), player);
            }
            vanishedPlayers.remove(player);
        }
    }


}
