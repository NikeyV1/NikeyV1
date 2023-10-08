package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.nikey.nikeyv1.Stones.Electrostone;
import de.nikey.nikeyv1.Timer.TimerBuild;
import de.nikey.nikeyv1.Util.Items;
import io.papermc.paper.event.entity.EntityMoveEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
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
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ALL")
public class Player implements Listener {
    int a;
    int b;
    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        org.bukkit.entity.Player p = event.getPlayer();
    }

    public static Inventory inv = Bukkit.createInventory(null, 27, "Enchanted Anvil");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        org.bukkit.entity.Player p = event.getPlayer();
        if (config.contains(p.getName())){
            String stone = config.getString(p.getName() + ".stone");
            if (stone.equalsIgnoreCase("Holy")&&p.getMaxHealth() >20)p.setMaxHealth(20);
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

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause == EntityDamageEvent.DamageCause.FREEZE){
            event.setDamage(event.getDamage()+1.5);
        }
        if (Electrostone.stunned.contains(entity)){
            int damg = (int) (event.getDamage() + (event.getDamage() * (20 / 100)));
            event.setDamage(damg);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item itemDrop = event.getItemDrop();
        org.bukkit.entity.Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        if (itemDrop.getItemStack().getType() == Material.FIREWORK_STAR||itemDrop.getItemStack().getItemMeta().hasLore()){
            String[] arr = itemDrop.getItemStack().getLore().get(1).split(":");
            String a = arr[0];
            if (a.equalsIgnoreCase(ChatColor.of("#00FFAA")+"Level")){
               event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        org.bukkit.entity.Player p = event.getPlayer();
        ItemMeta meta = event.getItemInHand().getItemMeta();
        if (meta.getDisplayName().equalsIgnoreCase("§dEnchanted Anvil")){
            event.setCancelled(true);
            de.nikey.nikeyv1.Util.Inventory.anvilinv(inv);
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
                player.getInventory().addItem(contents);
                inventory.clear();
            }
        }
    }


    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        int expLevelCost = event.getExpLevelCost();
        if (expLevelCost >8 ){
            Random rand = new Random();
            int nextInt = rand.nextInt(25);
            if (nextInt == 20){
                org.bukkit.entity.Player p = event.getEnchanter();
                ItemStack essence = new ItemStack(Material.DRAGON_BREATH);
                ItemMeta meta = essence.getItemMeta();
                meta.setDisplayName("§dEnchanted Essence");
                essence.addUnsafeEnchantment(Enchantment.CHANNELING,1);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                essence.setItemMeta(meta);
                p.getInventory().addItem(essence);
                p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF,1,1);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        PlayerRespawnEvent.RespawnReason respawnReason = event.getRespawnReason();
        if (respawnReason == PlayerRespawnEvent.RespawnReason.DEATH){
            FileConfiguration config = NikeyV1.plugin.getConfig();
            String stone = config.getString(player.getName() + ".stone");
            int level = config.getInt(player.getName() + ".level");
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
        FileConfiguration config = NikeyV1.plugin.getConfig();
        int i = config.getInt(player.getName() + ".level");
        if (i > 1){
            for (ItemStack drop : drops) {
                if (drop.getType() == Material.FIREWORK_STAR && drop.getItemMeta().hasLore()) {
                    drop.setAmount(0);
                    return;
                }
            }
            config.set(player.getName() +".level",i-1);
            String stone = config.getString(player.getName() + ".stone");
            if (player.getKiller() != null&&player.getKiller() instanceof org.bukkit.entity.Player){
                Items.SoulofStrenght(player.getKiller());
            }
        }else {
            for (ItemStack drop : drops) {
                drop.setAmount(0);
                return;
            }
            Date date = new Date(System.currentTimeMillis()+1440*60*1000);
            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "§cYour stone is out of strength!",date,"Game");
            player.kickPlayer("§cYour stone is out of strength!");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();
        Inventory bottom = event.getView().getBottomInventory();
        InventoryAction action = event.getAction();
        if (action ==InventoryAction.HOTBAR_SWAP||action == InventoryAction.HOTBAR_MOVE_AND_READD ) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)event.setCancelled(true);
        }
        if (event.getCurrentItem().getType() == Material.FIREWORK_STAR && event.getCurrentItem().getItemMeta().hasLore()) {
            String[] arr = event.getCurrentItem().getLore().get(1).split(":");
            String a = arr[0];
            if (a.equalsIgnoreCase(ChatColor.of("#00FFAA")+"Level") && !(top.getType() == InventoryType.CRAFTING)){
                if (!event.getWhoClicked().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§dEnchanted Anvil")) {
                    event.setCancelled(true);
                }
                if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
                    event.getCurrentItem().setAmount(0);
                }
            }
        }
        if (event.getWhoClicked() instanceof org.bukkit.entity.Player && event.getInventory() == inv){
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) event.getWhoClicked();
            Inventory inventory = event.getInventory();
            if (event.getCurrentItem() == null){
                return;
            }
            else if (event.getSlot() == 15 && event.getCurrentItem().getType() == Material.ANVIL || event.getCurrentItem().getType() == Material.PURPLE_STAINED_GLASS_PANE){
                event.setCancelled(true);
                if (inventory.getItem(13).getLore() != null){
                    ItemStack item = inventory.getItem(13);
                    String[] arr = item.getLore().get(1).split(":");
                    String a = arr[0];
                    if (inventory.getItem(13).getLore().get(1).contains(a)){
                        int num = Integer.parseInt(arr[1]);
                        FileConfiguration config = NikeyV1.plugin.getConfig();
                        config.set(p.getName()+".level",num+1);
                        NikeyV1.plugin.saveConfig();
                        TimerBuild timerBuild = new TimerBuild();
                        if (num <= 2){
                            if (!timerBuild.isRunning() || !config.getBoolean(p.getName()+".time")){
                                if (p.getLevel() > 30 || p.getGameMode() == GameMode.CREATIVE){
                                    inventory.setItem(13,null);
                                    p.closeInventory();
                                    timerBuild.setLevel(num+1);
                                    timerBuild.setStopTime(60*30);
                                    timerBuild.setTime(1);
                                    timerBuild.start(p);
                                    p.sendMessage("§aUpgrading!");
                                    if (p.getGameMode() != GameMode.CREATIVE){
                                        p.setLevel(p.getLevel()-30);
                                    }
                                }else {
                                    p.sendMessage("You dont have 30 levels!");
                                }
                            }
                        }else if (num <= 4){
                            if (!timerBuild.isRunning() || !config.getBoolean(p.getName()+".time")){
                                if (p.getLevel() > 50 || p.getGameMode() == GameMode.CREATIVE){
                                    inventory.setItem(13,null);
                                    p.closeInventory();
                                    timerBuild.setLevel(num+1);
                                    timerBuild.setStopTime(60*60);
                                    timerBuild.setTime(1);
                                    timerBuild.start(p);
                                    if (p.getGameMode() != GameMode.CREATIVE){
                                        p.setLevel(p.getLevel()-50);
                                    }
                                    p.sendMessage("§aUpgrading!");
                                }else {
                                    p.sendMessage("You dont have 50 levels!");
                                }
                            }
                        }else if (num <= 9){
                            if (!timerBuild.isRunning() || !config.getBoolean(p.getName()+".time")){
                                if (p.getLevel() > 50 || p.getGameMode() == GameMode.CREATIVE){
                                    inventory.setItem(13,null);
                                    p.closeInventory();
                                    timerBuild.setLevel(num+1);
                                    timerBuild.setStopTime(60*120);
                                    timerBuild.setTime(1);
                                    timerBuild.start(p);
                                    if (p.getGameMode() != GameMode.CREATIVE){
                                        p.setLevel(p.getLevel()-50);
                                    }
                                    p.sendMessage("§aUpgrading!");
                                }else {
                                    p.sendMessage("You dont have 50 levels!");
                                }
                            }
                        }else if (num <= 14){
                            if (!timerBuild.isRunning() || !config.getBoolean(p.getName()+".time")){
                                if (p.getLevel() > 50 || p.getGameMode() == GameMode.CREATIVE){
                                    inventory.setItem(13,null);
                                    p.closeInventory();
                                    timerBuild.setLevel(num+1);
                                    timerBuild.setStopTime(60*120);
                                    timerBuild.setTime(1);
                                    timerBuild.start(p);
                                    if (p.getGameMode() != GameMode.CREATIVE){
                                        p.setLevel(p.getLevel()-50);
                                    }
                                    p.sendMessage("§aUpgrading!");
                                }else {
                                    p.sendMessage("You dont have 50 levels!");
                                }
                            }
                        }else if (num <= 19){
                            if (!timerBuild.isRunning() || !config.getBoolean(p.getName()+".time")){
                                if (p.getLevel() > 50 || p.getGameMode() == GameMode.CREATIVE){
                                    inventory.setItem(13,null);
                                    p.closeInventory();
                                    timerBuild.setLevel(num+1);
                                    timerBuild.setStopTime(60*120);
                                    timerBuild.setTime(1);
                                    timerBuild.start(p);
                                    if (p.getGameMode() != GameMode.CREATIVE){
                                        p.setLevel(p.getLevel()-50);
                                    }
                                    p.sendMessage("§aUpgrading!");
                                }else {
                                    p.sendMessage("You dont have 50 levels!");
                                }
                            }
                        } else if (num == 20) {
                            if (!timerBuild.isRunning() || !config.getBoolean(p.getName()+".time")){
                                if (p.getLevel() > 50 || p.getGameMode() == GameMode.CREATIVE){
                                    inventory.setItem(13,null);
                                    p.closeInventory();
                                    timerBuild.setLevel(num+1);
                                    timerBuild.setStopTime(60*120);
                                    timerBuild.setTime(1);
                                    timerBuild.start(p);
                                    if (p.getGameMode() != GameMode.CREATIVE){
                                        p.setLevel(p.getLevel()-50);
                                    }
                                    p.sendMessage("§aUpgrading!");
                                }else {
                                    p.sendMessage("You dont have 50 levels!");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() != null && event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == Material.FIREWORK_STAR && event.getPlayer().getItemInHand().getItemMeta().hasLore()) {
                event.setCancelled(true);
                event.getRightClicked().remove();
            }
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Player) {
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) event.getEntity();
            Item item = event.getItem();
            if (item.getItemStack().getType() == Material.FIREWORK_STAR && item.getItemStack().getItemMeta().hasLore()) {
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
}