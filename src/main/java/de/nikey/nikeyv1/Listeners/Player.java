package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.nikey.nikeyv1.Timer.Timer;
import de.nikey.nikeyv1.Util.Items;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

@SuppressWarnings("deprecation")
public class Player implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        org.bukkit.entity.Player p = event.getPlayer();
        if (NikeyV1.getPlugin().getTimer().isRunning()){
            NikeyV1.getPlugin().getTimer().setRunning(false);
            NikeyV1.plugin.getConfig().set(p.getName()+"timer", NikeyV1.getPlugin().getTimer().getTime());
            NikeyV1.plugin.saveConfig();
        }
    }

    public static Inventory inv = Bukkit.createInventory(null, 27, "Enchanted Anvil");
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        new ServerScoreboard(player);
        FileConfiguration config = NikeyV1.plugin.getConfig();
        if (!config.contains(player.getName())){
            Random random = new Random();
            int i = random.nextInt(2);
            if (i == 0){
                Items.Firestone(player,1);
                player.sendMessage("You got Firestone");
                config.set(player.getName()+".stone","Fire");
                config.set(player.getName()+".level",1);
                NikeyV1.plugin.saveConfig();
            }
        }else{
            if (config.getInt(player.getName()+".time") != 0){
                int time = config.getInt(player.getName() + ".time");
                NikeyV1.getPlugin().getTimer().setTime(time);
                NikeyV1.getPlugin().getTimer().setRunning(true);
            }
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        org.bukkit.entity.Player p = entity.getKiller();
        if (entity.getType() == EntityType.MAGMA_CUBE && entity.getKiller() != null){
            Random rand = new Random();
            int upperbound = 10;
            int ende = rand.nextInt(upperbound);
            if (ende == 5){
                p.sendTitle("§d§kR§r§cLava Stone!§r§d§kR","");
                Items.Firestone(p, 1);
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

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof org.bukkit.entity.Player && event.getInventory() == inv){
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) event.getWhoClicked();
            Inventory inventory = event.getInventory();
            if (event.getSlot() == 15 && event.getCurrentItem().getType() == Material.ANVIL || event.getCurrentItem().getType() == Material.PURPLE_STAINED_GLASS_PANE){
                event.setCancelled(true);
                if (inventory.getItem(14).getLore().get(1).contains(ChatColor.of("#00FFAA")+"Level:")){
                    String s = inventory.getItem(14).getLore().get(1);
                    ItemStack item = inventory.getItem(14);
                    String[] arr = s.split(":");
                    int num = Integer.parseInt(arr[1]);
                    NikeyV1.plugin.getConfig().set(p.getName()+"level",num);
                    NikeyV1.plugin.saveConfig();
                    if (num <= 3){

                    }else if (num <= 5){

                    }else if (num <= 10){

                    }else if (num <= 15){

                    }else if (num <= 20){

                    }
                }
            }
        }
    }
}
