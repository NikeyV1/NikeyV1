package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.nikey.nikeyv1.Stones.StoneCooldown1;
import de.nikey.nikeyv1.Timer.Timer;
import de.nikey.nikeyv1.Timer.TimerBuild;
import de.nikey.nikeyv1.Util.Items;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.aether.impl.RepositoryEventDispatcher;

import java.util.Collection;
import java.util.Random;

@SuppressWarnings("deprecation")
public class Player implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        NikeyV1.getPlugin().reloadConfig();
        TimerBuild timerBuild = new TimerBuild();
        org.bukkit.entity.Player p = event.getPlayer();
    }

    public static Inventory inv = Bukkit.createInventory(null, 27, "Enchanted Anvil");
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        new ServerScoreboard(player);
        NikeyV1.getPlugin().reloadConfig();
        FileConfiguration config = NikeyV1.plugin.getConfig();
        if (!(config.getInt(player.getName()+".level") >= 3) ||!config.getString(player.getName() +".stone").equalsIgnoreCase("Fire")){
            if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE); player.sendMessage("You flagged!");
        }
        if (config.contains(player.getName())){
            String stone = config.getString(player.getName() + ".stone");
            if (config.getBoolean(player.getName()+"."+stone+".cooldown1"+".timer")){
                BukkitRunnable runnable = new BukkitRunnable() {

                    @Override
                    public void run() {
                        config.set(player.getName()+"."+stone+".cooldown1"+".timer",false);
                        if (!config.getBoolean(player.getName()+"."+stone+".cooldown1"+".timer")){
                            StoneCooldown1 stoneCooldown1 = new StoneCooldown1();
                            stoneCooldown1.setTime(config.getInt(player.getName()+"."+stone+".cooldown1"+".time"));
                            stoneCooldown1.setStopTime(config.getInt(player.getName()+"."+stone+".cooldown1"+".stoptime"));
                            stoneCooldown1.start(player);
                        }else {
                            player.sendMessage("§cPlugin Error: "+event.getEventName()+".continue.cooldown");
                        }
                    }
                };
                runnable.runTaskLater(NikeyV1.getPlugin(),20);
            }
            TimerBuild timerBuild = new TimerBuild();
            if (config.getBoolean(player.getName()+".time")){
                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        config.set(player.getName()+".time",false);
                        if (!config.getBoolean(player.getName()+".time")){
                            timerBuild.setLevel(config.getInt(player.getName()+".level"));
                            timerBuild.setTime(config.getInt(player.getName()+".timer"));
                            timerBuild.setStopTime(config.getInt(player.getName()+".stoptime"));
                            timerBuild.start(player);
                            player.sendMessage("§aYour upgrade continues!");
                        }else {
                            player.sendMessage("§cPlugin Error: "+event.getEventName()+".continue.upgrade");
                        }
                    }
                };
                runnable.runTaskLater(NikeyV1.getPlugin(),20);
            }
        }else {
            Random random = new Random();
            int i = random.nextInt(2);
            Items.Firestone(player,1);
            player.sendTitle("§d§kR§r§cLava Stone§r§d§kR","");
            config.set(player.getName()+".stone", ChatColor.RED +"Fire");
            config.set(player.getName()+".level",1);
            config.set(player.getName()+".time",false);
            NikeyV1.plugin.saveConfig();
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
            int upperbound = 25;
            int nextInt = rand.nextInt(upperbound);
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
    public void onPlayerDeath(PlayerDeathEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        //was war hier nochmal?
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof org.bukkit.entity.Player && event.getInventory() == inv){
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) event.getWhoClicked();
            Inventory inventory = event.getInventory();
            if (event.getCurrentItem() == null){

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

                        }else if (num <= 19){

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();new ServerScoreboard(player);
    }
}
