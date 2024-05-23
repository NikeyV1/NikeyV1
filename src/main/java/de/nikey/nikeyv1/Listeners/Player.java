package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.nikey.nikeyv1.Stones.Electrostone;
import de.nikey.nikeyv1.Stones.Holystone;
import de.nikey.nikeyv1.Timer.TimerBuild;
import de.nikey.nikeyv1.Util.Items;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


@SuppressWarnings("ALL")
public class Player implements Listener {
    int a;
    int b;
    int times;
    public static Inventory inv = Bukkit.createInventory(null, 27, "Enchanted Anvil");


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        org.bukkit.entity.Player p = event.getPlayer();
        if (config.contains(p.getName())){
            String stone = config.getString(p.getName() + ".stone");
            Integer level = config.getInt(p.getName() + ".level");
            p.setWalkSpeed(0.2F);
            p.setFlySpeed(0.1f);
            if (stone.equalsIgnoreCase("Electric")) {
                if (level == 3){
                    p.setWalkSpeed(0.21F);
                } else if (level == 4) {
                    p.setWalkSpeed(0.22F);
                }else if (level == 5) {
                    p.setWalkSpeed(0.23F);
                }else if (level >= 6) p.setWalkSpeed(0.24F);
            }
            Holystone.hitted.remove(p);
            p.setMaxHealth(20);
            TimerBuild timerBuild = new TimerBuild();
            if (config.getBoolean(p.getName()+".time")){
                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        config.set(p.getName()+".time",false);
                        if (!config.getBoolean(p.getName()+".time")){
                            timerBuild.setLevel(config.getInt(p.getName()+".level"));
                            timerBuild.setTime(config.getInt(p.getName()+".timer"));
                            timerBuild.setStopTime(config.getInt(p.getName()+".stoptime"));
                            timerBuild.start(p);
                            p.sendMessage("§aYour upgrade continues!");
                        }else {
                            p.sendMessage("§cPlugin Error: "+event.getEventName()+".continue.upgrade");
                        }
                    }
                };
                runnable.runTaskLater(NikeyV1.getPlugin(),20);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if (Electrostone.stunned.contains(entity)){
            Integer i = config.getInt(damager.getName() + ".level");
            if (i == 15) {
                float damg = (float) ((float) event.getDamage()*1.4);
                event.setDamage(damg);
            } else if (i == 16 || i == 17) {
                float damg = (float) ((float) event.getDamage()*1.5);
                event.setDamage(damg);
            }else if (i >= 18) {
                float damg = (float) ((float) event.getDamage()*1.6);
                event.setDamage(damg);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop() != null) {
            Item itemDrop = event.getItemDrop();
            org.bukkit.entity.Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            if (itemDrop.getItemStack().getType() == Material.FIREWORK_STAR){
                event.setCancelled(true);
                if (!isInventoryFull(player)) {
                    event.setCancelled(true);
                }else {
                    ItemStack droppedItem = event.getItemDrop().getItemStack();
                    player.getInventory().setItemInOffHand(droppedItem);
                    event.setCancelled(true);
                }
            }else {
                if (itemDrop.getItemStack().getType() == Material.NETHERITE_SWORD && itemDrop.getItemStack().getItemMeta().hasLore()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean isInventoryFull(org.bukkit.entity.Player player) {
        Inventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType().isAir()) {
                return false;
            }
        }
        return true;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        org.bukkit.entity.Player p = event.getPlayer();
        ItemMeta meta = event.getItemInHand().getItemMeta();
        if (meta.getDisplayName().equalsIgnoreCase("§dEnchanted Anvil")){
            event.setCancelled(true);
            ItemStack glass = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
            ItemMeta itemMeta = glass.getItemMeta();
            itemMeta.setDisplayName("§c");
            glass.setItemMeta(itemMeta);
            ItemStack a = new ItemStack(Material.ANVIL);
            ItemMeta m = a.getItemMeta();
            m.setDisplayName("§dUpgrade");
            a.setItemMeta(m);
            //Invsetup
            inv.clear();
            inv.setItem(3,glass);
            inv.setItem(4,glass);
            inv.setItem(5,glass);
            inv.setItem(12,glass);
            inv.setItem(14,glass);
            inv.setItem(15,a);
            inv.setItem(21,glass);
            inv.setItem(22,glass);
            inv.setItem(23,glass);
            p.openInventory(inv);
        }else if (meta.getDisplayName().equalsIgnoreCase("§3Soul of Strenght")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() == inv){
            Inventory inventory = event.getInventory();
            for (ItemStack contents : inventory.getContents()){
                if(contents == null || contents.getType() == Material.AIR || contents.getItemMeta().getDisplayName().equalsIgnoreCase("§dUpgrade") || contents.getType() == Material.PURPLE_STAINED_GLASS_PANE) continue;
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getPlayer();
                if (contents.getType() == Material.FIREWORK_STAR && contents.getItemMeta().hasLore()) {
                    int i = player.getInventory().firstEmpty();
                    if (i == -1) {
                        player.getInventory().setItemInOffHand(contents);
                    }else {
                        player.getInventory().addItem(contents);
                    }
                    inventory.clear();
                }else {
                    player.getInventory().addItem(contents);
                    inventory.clear();
                }
            }
        }
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Random rand = new Random();
        int nextInt = rand.nextInt(20);
        if (nextInt == 15){
            org.bukkit.entity.Player p = event.getEnchanter();
            ItemStack essence = new ItemStack(Material.DRAGON_BREATH);
            ItemMeta meta = essence.getItemMeta();
            meta.setDisplayName("§dEnchanted Essence");
            essence.addUnsafeEnchantment(Enchantment.CHANNELING,1);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            essence.setItemMeta(meta);
            if (p.getInventory().firstEmpty() != -1){
                p.getInventory().addItem(essence);
            }else {
                p.getWorld().dropItem(p.getLocation(),essence);
            }
            p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF,1,1);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        PlayerRespawnEvent.RespawnReason respawnReason = event.getRespawnReason();
        if (respawnReason == PlayerRespawnEvent.RespawnReason.DEATH){
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            String stone = config.getString(player.getName() + ".stone");
            int level = config.getInt(player.getName() + ".level");
            boolean o = NikeyV1.getPlugin().getConfig().getBoolean(player.getName() + ".time");
            if (!o) {
                if (stone.equalsIgnoreCase("Fire")) {
                    Items.Firestone(player,level);
                }else if (stone.equalsIgnoreCase("Electric")){
                    Items.Electrostone(player,level);
                }else if (stone.equalsIgnoreCase("Water")){
                    Items.Waterstone(player,level);
                }else if (stone.equalsIgnoreCase("Frozen")){
                    Items.Frozenstone(player,level);
                }else if (stone.equalsIgnoreCase("Undead")){
                    Items.Undeadstone(player,level);
                }else if (stone.equalsIgnoreCase("Holy")){
                    Items.Holystone(player,level);
                }
            }


            new BukkitRunnable() {
                @Override
                public void run() {
                    new ServerScoreboard(player);
                }
            }.runTaskLater(NikeyV1.getPlugin(),10);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        List<ItemStack> drops = event.getDrops();
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        int i = config.getInt(player.getName() + ".level");
        //Changes to only drop if level is higher than 10
        if (player.getKiller() != null&&player.getKiller() instanceof org.bukkit.entity.Player){
            if (i >= 10) {
                Items.SoulofStrenght(player.getKiller());
            }
        }
        if (i > 1){
            config.set(player.getName() +".level",i-1);
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    if (item.getType() == Material.FIREWORK_STAR || item.getType() == Material.NETHERITE_SWORD && item.getItemMeta().hasLore()) {
                        // Stone entfernen
                        event.getDrops().remove(item);
                        boolean buffed = config.getBoolean(player.getName() + ".buffed");
                        if (buffed) {
                            config.set(player.getName() + ".buffed", false);
                            NikeyV1.getPlugin().saveConfig();
                            ItemStack itemStack = new ItemStack(Material.DRAGON_EGG);
                            Item spawn = player.getWorld().spawn(player.getLocation(), Item.class);
                            spawn.setThrower(player.getUniqueId());
                            spawn.setItemStack(itemStack);
                        }
                    }
                }
            }
        }else {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    if (item.getType() == Material.FIREWORK_STAR || item.getType() == Material.NETHERITE_SWORD) {
                        if (item.getItemMeta().hasLore()) {
                            event.getDrops().remove(item);
                        }
                    }
                }
            }
            player.getInventory().clear();
            player.kickPlayer("§cYour stone is out of strength!");
            Date date = new Date(System.currentTimeMillis()+1440*60*1000);
            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "§cYour stone is out of strength!",date,"Game");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FREEZE){
            event.setDamage(event.getDamage()+1.5);
        }
        if (event.getEntityType() == EntityType.PLAYER ){
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FREEZE){
                FileConfiguration config = NikeyV1.getPlugin().getConfig();
                String stone = config.getString(p.getName() + ".stone");
                int level = config.getInt(p.getName() + ".level");
                if (stone.equalsIgnoreCase("Frozen") && level>=3){
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean isChest(Inventory inventory) {
        return inventory.getHolder() instanceof org.bukkit.block.Chest ||
                inventory.getHolder() instanceof org.bukkit.block.DoubleChest||
                inventory.getHolder() instanceof org.bukkit.block.Barrel||
                inventory.getHolder() instanceof org.bukkit.block.BlastFurnace||
                inventory.getHolder() instanceof org.bukkit.block.BrewingStand||
                inventory.getHolder() instanceof org.bukkit.block.Dispenser||
                inventory.getHolder() instanceof org.bukkit.block.Dropper||
                inventory.getHolder() instanceof org.bukkit.block.EnderChest||
                inventory.getHolder() instanceof org.bukkit.block.Furnace||
                inventory.getHolder() instanceof org.bukkit.block.EnchantingTable||
                inventory.getHolder() instanceof org.bukkit.block.Hopper||
                inventory.getHolder() instanceof org.bukkit.block.ShulkerBox||
                inventory.getHolder() instanceof org.bukkit.block.Container||
                inventory.getHolder() instanceof org.bukkit.block.Smoker||
                inventory.getHolder() instanceof org.bukkit.entity.minecart.HopperMinecart;
    }
    // Überprüfen, ob es sich um ein Hopper- oder Trichter-Minecart-Inventar handelt
    private boolean isMinecartInventory(Inventory inventory) {
        return inventory != null && (isHopperMinecart(inventory) || isChestMinecart(inventory));
    }

    // Überprüfen, ob es sich um ein Hopper-Minecart-Inventar handelt
    private boolean isHopperMinecart(Inventory inventory) {
        return inventory.getHolder() instanceof org.bukkit.entity.minecart.HopperMinecart;
    }

    // Überprüfen, ob es sich um ein Trichter-Minecart-Inventar handelt
    private boolean isChestMinecart(Inventory inventory) {
        return inventory.getHolder() instanceof org.bukkit.entity.minecart.HopperMinecart;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        int hotbarButton = event.getHotbarButton();
        if (event.getInventory() == inv) {
            if (event.getWhoClicked() instanceof org.bukkit.entity.Player ) {
                org.bukkit.entity.Player p = (org.bukkit.entity.Player) event.getWhoClicked();
                Inventory inventory = event.getInventory();
                if (event.getCurrentItem() == null) ;
                else if (event.getSlot() == 15 && event.getCurrentItem().getType() == Material.ANVIL || event.getCurrentItem().getType() == Material.PURPLE_STAINED_GLASS_PANE) {
                    event.setCancelled(true);
                    if (inventory.getItem(13) != null) {
                        if (inventory.getItem(13).getLore() != null) {
                            ItemStack item = inventory.getItem(13);
                            String[] arr = item.getLore().get(1).split(":");
                            String a = arr[0];
                            if (inventory.getItem(13).getLore().get(1).contains(net.md_5.bungee.api.ChatColor.of("#00FFAA") + "Level")) {
                                int num = Integer.parseInt(arr[1]);
                                FileConfiguration config = NikeyV1.getPlugin().getConfig();
                                NikeyV1.getPlugin().saveConfig();
                                times = 0;
                                int amountToTax = 1;
                                if (times == 0) {
                                    if (num == 1 || num == 2) {
                                        TimerBuild timerBuild = new TimerBuild();
                                        if (!timerBuild.isRunning() || !config.getBoolean(p.getName() + ".time")) {
                                            if (p.getLevel() >= 10 || p.getGameMode() == GameMode.CREATIVE) {
                                                inventory.setItem(13, null);
                                                p.closeInventory();
                                                timerBuild.setLevel(num + 1);
                                                timerBuild.setStopTime(60 * 10);
                                                timerBuild.setTime(1);
                                                timerBuild.start(p);
                                                p.sendMessage("§aUpgrading!");
                                                config.set(p.getName() + ".level", num + 1);
                                                NikeyV1.getPlugin().saveConfig();
                                                if (p.getGameMode() != GameMode.CREATIVE) {
                                                    p.setLevel(p.getLevel() - 10);
                                                }
                                            } else {
                                                p.sendMessage("You dont have 10 levels!");
                                            }
                                        }
                                    } else if (num == 3 || num == 4 || num == 5) {
                                        TimerBuild timerBuild = new TimerBuild();
                                        if (!timerBuild.isRunning() || !config.getBoolean(p.getName() + ".time")) {
                                            for (ItemStack soul : p.getInventory().getContents()) {
                                                if (soul != null && soul.getType() == Material.PRISMARINE_SHARD && soul.getItemMeta().hasLore()) {
                                                    if (p.getLevel() >= 20 || p.getGameMode() == GameMode.CREATIVE) {
                                                        inventory.setItem(13, null);
                                                        p.closeInventory();
                                                        timerBuild.setLevel(num + 1);
                                                        timerBuild.setStopTime(60 * 20);
                                                        timerBuild.setTime(1);
                                                        timerBuild.start(p);
                                                        config.set(p.getName() + ".level", num + 1);
                                                        NikeyV1.getPlugin().saveConfig();
                                                        if (p.getGameMode() != GameMode.CREATIVE) {
                                                            p.setLevel(p.getLevel() - 20);
                                                        }
                                                        p.sendMessage("§aUpgrading!");
                                                        if (soul.getAmount() >= amountToTax) {
                                                            soul.setAmount(soul.getAmount() - 1);
                                                            break;
                                                        } else {
                                                            p.getInventory().remove(soul);
                                                            break;
                                                        }
                                                    } else {
                                                        p.sendMessage("You dont have 20 levels!");
                                                    }
                                                }
                                            }
                                        }
                                    } else if (num == 6 || num == 7 || num == 8 || num == 9) {
                                        TimerBuild timerBuild = new TimerBuild();
                                        if (!timerBuild.isRunning() || !config.getBoolean(p.getName() + ".time")) {
                                            for (ItemStack soul : p.getInventory().getContents()) {
                                                if (soul != null && soul.getType() == Material.PRISMARINE_SHARD && soul.getItemMeta().hasLore()) {
                                                    if (p.getLevel() >= 30 || p.getGameMode() == GameMode.CREATIVE) {
                                                        inventory.setItem(13, null);
                                                        p.closeInventory();
                                                        timerBuild.setLevel(num + 1);
                                                        timerBuild.setStopTime(60 * 30);
                                                        timerBuild.setTime(1);
                                                        timerBuild.start(p);
                                                        config.set(p.getName() + ".level", num + 1);
                                                        NikeyV1.getPlugin().saveConfig();
                                                        if (p.getGameMode() != GameMode.CREATIVE)
                                                            p.setLevel(p.getLevel() - 30);
                                                        p.sendMessage("§aUpgrading!");
                                                        if (soul.getAmount() >= amountToTax) {
                                                            soul.setAmount(soul.getAmount() - 1);
                                                            break;
                                                        } else {
                                                            p.getInventory().remove(soul);
                                                            break;
                                                        }
                                                    } else {
                                                        p.sendMessage("You dont have 30 levels!");
                                                    }
                                                }
                                            }
                                        }
                                    } else if (num == 10 ||num == 11 || num == 12 || num == 13 || num == 14) {
                                        TimerBuild timerBuild = new TimerBuild();
                                        if (!timerBuild.isRunning() || !config.getBoolean(p.getName() + ".time")) {
                                            for (ItemStack soul : p.getInventory().getContents()) {
                                                if (soul != null && soul.getType() == Material.PRISMARINE_SHARD && soul.getItemMeta().hasLore()) {
                                                    if (p.getLevel() >= 40 || p.getGameMode() == GameMode.CREATIVE) {
                                                        inventory.setItem(13, null);
                                                        p.closeInventory();
                                                        timerBuild.setLevel(num + 1);
                                                        timerBuild.setStopTime(60 * 40);
                                                        timerBuild.setTime(1);
                                                        timerBuild.start(p);
                                                        config.set(p.getName() + ".level", num + 1);
                                                        NikeyV1.getPlugin().saveConfig();
                                                        if (p.getGameMode() != GameMode.CREATIVE) {
                                                            p.setLevel(p.getLevel() - 40);
                                                        }
                                                        p.sendMessage("§aUpgrading!");
                                                        if (soul.getAmount() >= amountToTax) {
                                                            soul.setAmount(soul.getAmount() - 1);
                                                            break;
                                                        } else {
                                                            p.getInventory().remove(soul);
                                                            break;
                                                        }
                                                    } else {
                                                        p.sendMessage("You dont have 40 levels!");
                                                    }
                                                }
                                            }
                                        }
                                    } else if (num == 15 ||  num == 16 || num == 17 || num == 18 || num == 19) {
                                        TimerBuild timerBuild = new TimerBuild();
                                        if (!timerBuild.isRunning() || !config.getBoolean(p.getName() + ".time")) {
                                            for (ItemStack soul : p.getInventory().getContents()) {
                                                if (soul != null && soul.getType() == Material.SOUL_LANTERN && soul.getItemMeta().hasLore()) {
                                                    if (p.getLevel() >= 50 || p.getGameMode() == GameMode.CREATIVE) {
                                                        inventory.setItem(13, null);
                                                        p.closeInventory();
                                                        timerBuild.setLevel(num + 1);
                                                        timerBuild.setStopTime(60 * 50);
                                                        timerBuild.setTime(1);
                                                        timerBuild.start(p);
                                                        config.set(p.getName() + ".level", num + 1);
                                                        NikeyV1.getPlugin().saveConfig();
                                                        if (p.getGameMode() != GameMode.CREATIVE) {
                                                            p.setLevel(p.getLevel() - 50);
                                                        }
                                                        p.sendMessage("§aUpgrading!");
                                                        new ServerScoreboard(p);
                                                        if (soul.getAmount() >= amountToTax) {
                                                            soul.setAmount(soul.getAmount() - 1);
                                                            break;
                                                        } else {
                                                            p.getInventory().remove(soul);
                                                            break;
                                                        }
                                                    } else {
                                                        p.sendMessage("You dont have 50 levels!");
                                                    }
                                                }
                                            }
                                        }
                                    } else if (num == 20) {
                                        TimerBuild timerBuild = new TimerBuild();
                                        if (!timerBuild.isRunning() || !config.getBoolean(p.getName() + ".time")) {
                                            // Durchlaufe das Inventar des Spielers und entferne die Soul of Strenght
                                            for (ItemStack soul : p.getInventory().getContents()) {
                                                if (soul != null && soul.getType() == Material.SOUL_LANTERN && soul.getItemMeta().hasLore()) {
                                                    if (p.getLevel() >= 100 || p.getGameMode() == GameMode.CREATIVE) {
                                                        inventory.setItem(13, null);
                                                        p.closeInventory();
                                                        timerBuild.setLevel(num + 1);
                                                        timerBuild.setStopTime(60 * 120);
                                                        timerBuild.setTime(1);
                                                        timerBuild.start(p);
                                                        config.set(p.getName() + ".level", num + 1);
                                                        NikeyV1.getPlugin().saveConfig();
                                                        if (p.getGameMode() != GameMode.CREATIVE) {
                                                            p.setLevel(p.getLevel() - 100);
                                                        }
                                                        p.sendMessage("§aUpgrading!");
                                                        if (soul.getAmount() >= amountToTax) {
                                                            soul.setAmount(soul.getAmount() - 1);
                                                            break;
                                                        } else {
                                                            p.getInventory().remove(soul);
                                                            break;
                                                        }
                                                    } else {
                                                        p.sendMessage("You dont have 100 levels!");
                                                    }
                                                }
                                            }
                                        }
                                    } else if (num == 21) {
                                        TimerBuild timerBuild = new TimerBuild();
                                        if (!timerBuild.isRunning() || !config.getBoolean(p.getName() + ".time")) {
                                            // Durchlaufe das Inventar des Spielers und entferne die Soul of Strenght
                                            for (ItemStack soul : p.getInventory().getContents()) {
                                                if (soul != null && soul.getType() == Material.DRAGON_EGG) {
                                                    boolean buffed = config.getBoolean(p.getName() + ".buffed");
                                                    if (!buffed) {
                                                        p.getInventory().remove(soul);
                                                        inventory.setItem(13, null);
                                                        p.closeInventory();
                                                        config.set(p.getName() + ".buffed", true);
                                                        NikeyV1.getPlugin().saveConfig();
                                                        Items.GiveInfernoBlade(p);
                                                        Items.GiveElementalStone(p);
                                                        new ServerScoreboard(p);

                                                        for (org.bukkit.entity.Player players : Bukkit.getOnlinePlayers()) {
                                                            players.playSound(players.getLocation(),Sound.BLOCK_BEACON_POWER_SELECT,2,2);
                                                            players.playEffect(EntityEffect.TOTEM_RESURRECT);
                                                        }
                                                    } else {
                                                        p.sendMessage("§cError: You are already buffed!");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else {
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getWhoClicked();
            Inventory clickedInventory = event.getClickedInventory();
            Material itemMaterial = event.getCursor().getType();

            if (isMinecartInventory(clickedInventory)) {
                if (itemMaterial == Material.FIREWORK_STAR || itemMaterial == Material.NETHERITE_SWORD && event.getCursor().getItemMeta().hasLore()) {
                    event.setCancelled(true);
                    player.sendMessage("§cYou are not allowed to do that!");
                    player.damage(4);
                }
            }

            // Überprüfen, ob es sich um eine Truhe handelt und der Spieler versucht, Diamanten zu legen
            if (clickedInventory != null && isChest(clickedInventory)) {
                if ( itemMaterial == Material.FIREWORK_STAR || itemMaterial == Material.NETHERITE_SWORD && event.getCursor().getItemMeta().hasLore()) {
                    event.setCancelled(true);
                    player.sendMessage("§cYou are not allowed to do that!");
                    player.damage(4);
                }
            }

            Inventory top = event.getView().getTopInventory();
            Inventory bottom = event.getView().getBottomInventory();


            if (bottom.getType() == InventoryType.PLAYER) {
                if (event.getCurrentItem() != null) {
                    if ( event.getCurrentItem().getType() == Material.FIREWORK_STAR ||event.getCurrentItem().getType() == Material.NETHERITE_SWORD && event.getCurrentItem().getItemMeta().hasLore()) {
                        if (top.getType() == InventoryType.CHEST  || event.getInventory().getType() == InventoryType.ANVIL|| event.getInventory().getType() == InventoryType.BARREL|| event.getInventory().getType() == InventoryType.BEACON|| event.getInventory().getType() == InventoryType.BLAST_FURNACE|| event.getInventory().getType() == InventoryType.BREWING|| event.getInventory().getType() == InventoryType.CARTOGRAPHY|| event.getInventory().getType() == InventoryType.LOOM
                                || event.getInventory().getType() == InventoryType.SMOKER|| event.getInventory().getType() == InventoryType.ENDER_CHEST|| event.getInventory().getType() == InventoryType.STONECUTTER|| event.getInventory().getType() == InventoryType.SHULKER_BOX|| event.getInventory().getType() == InventoryType.SMITHING|| event.getInventory().getType() == InventoryType.GRINDSTONE|| event.getInventory().getType() == InventoryType.FURNACE|| event.getInventory().getType() == InventoryType.ENCHANTING|| event.getInventory().getType() == InventoryType.HOPPER|| event.getInventory().getType() == InventoryType.DROPPER|| event.getInventory().getType() == InventoryType.DISPENSER
                        ) {
                            if (event.getRawSlot() > 26) {
                                event.setCancelled(true);
                                player.damage(4);
                                player.sendMessage("§cYou are not allowed to do that!");
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if (event.getItem() != null) {
            if (event.getItem().getType() == Material.FIREWORK_STAR) {
                event.setCancelled(true);
            }
            if (event.getItem().getType() == Material.FIREWORK_STAR || event.getItem().getType() == Material.NETHERITE_SWORD && event.getItem().getItemMeta().hasLore()){
                if (clickedBlock.getType() == Material.DECORATED_POT && event.getItem().getItemMeta().hasLore()) {
                    event.setCancelled(true);
                    event.getPlayer().damage(4);
                    event.getPlayer().sendMessage("§cYou are not allowed to do that!");
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == Material.FIREWORK_STAR || event.getPlayer().getItemInHand().getType() == Material.NETHERITE_SWORD && event.getPlayer().getItemInHand().getItemMeta().hasLore()) {
            if (event.getRightClicked() != null && event.getRightClicked().getType() == EntityType.ITEM_FRAME || event.getRightClicked() != null && event.getRightClicked().getType() == EntityType.GLOW_ITEM_FRAME) {
                event.getPlayer().damage(4);
                event.getPlayer().sendMessage("§cYou are not allowed to do that!");
                event.setCancelled(true);
                event.getRightClicked().remove();
            } else if (event.getRightClicked().getType() == EntityType.ALLAY ) {
                event.getPlayer().damage(4);
                event.getPlayer().sendMessage("§cYou are not allowed to do that!");
                event.setCancelled(true);
                event.getPlayer().damage(4);
                event.getRightClicked().remove();
            }
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Player) {
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) event.getEntity();
            Item item = event.getItem();
            if (item.getItemStack().getType() == Material.FIREWORK_STAR||item.getItemStack().getType() == Material.NETHERITE_SWORD && item.getItemStack().getItemMeta().hasLore()) {
                event.getEntity().damage(4);
                event.getEntity().sendMessage("§cYou are not allowed to do that!");
                Bukkit.broadcastMessage(ChatColor.RED+ event.getEntity().getName() +" triggered Stone-SMP anti Exploit!");
                event.setCancelled(true);
                item.remove();
            }
        }
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();new ServerScoreboard(player);
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.isInWaterOrRain())entity.setVisualFire(false);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        org.bukkit.entity.Player entity = event.getPlayer();
        if (entity.isInWaterOrRain())entity.setVisualFire(false);
    }
}
