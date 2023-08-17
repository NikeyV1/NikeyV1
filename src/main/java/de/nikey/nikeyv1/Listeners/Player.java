package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
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
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

@SuppressWarnings("deprecation")
public class Player implements Listener {
    public static Inventory inv = Bukkit.createInventory(null, 9, "Enchanted Anvil");
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        new ServerScoreboard(player);
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
            ItemStack a = new ItemStack(Material.ANVIL);
            ItemMeta m = a.getItemMeta();
            m.setDisplayName("§dUpgrade");
            a.setItemMeta(m);
            inv.clear();
            inv.setItem(4, a);
            p.openInventory(inv);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() == inv){
            Inventory inventory = event.getInventory();
            for (ItemStack contents : inventory.getContents()){
                if(contents == null || contents.getType() == Material.AIR || contents.getItemMeta().getDisplayName().equalsIgnoreCase("§dUpgrade")) continue;
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getPlayer();
                player.getInventory().addItem(contents);
                inventory.clear();


            }

        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Player){
            org.bukkit.entity.Player p = (org.bukkit.entity.Player) event.getEntity();
            ItemStack item = event.getItem().getItemStack();
            if (item.getLore().get(1).contains(ChatColor.of("#00FFAA")+"Level:")){
                FileConfiguration config = NikeyV1.plugin.getConfig();
                config.set(p.getName()+".Name",item.getItemMeta().getDisplayName());
                NikeyV1.plugin.saveConfig();
                p.sendMessage("Your stone got registered!");
                for (ItemStack contents : p.getInventory().getContents()){
                    if(contents == null || contents.getType() == Material.AIR) continue;
                    String[] arr = contents.getLore().get(1).split(":");
                    String s = arr[0];
                    if(contents.getLore().get(1).contains(s)){
                        event.setCancelled(true);
                        if (contents.getAmount() != 1){
                            p.sendMessage("g");
                            contents.setAmount(1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        event.getPlayer().sendMessage("ff");
        String[] arr = event.getOffHandItem().getLore().get(1).split(":");
        String[] split = event.getMainHandItem().getLore().get(1).split(":");
        String s = arr[0];
        String s2 = split[0];
        event.getPlayer().sendMessage(s2);
        if (event.getOffHandItem().getLore().get(1).contains(s)){
            ItemStack item = event.getOffHandItem();
            event.getPlayer().sendMessage("dd");
            if (item.getAmount() != 1){
                item.setAmount(1);
            }
        }
        if (event.getMainHandItem().getLore().get(1).contains(s2)){
            ItemStack item = event.getMainHandItem();
            event.getPlayer().sendMessage("dd");
            if (item.getAmount() != 1){
                item.setAmount(1);
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
            if (event.getSlot() == 4 && event.getCurrentItem().getType() == Material.ANVIL){
                event.setCancelled(true);
                if (inventory.getItem(3).getLore().get(1).contains(ChatColor.of("#00FFAA")+"Level:")){
                    String s = inventory.getItem(3).getLore().get(1);
                    String[] arr = s.split(":");
                    int num = Integer.parseInt(arr[1]);
                    if (num <= 3){
                        NikeyV1.plugin.getConfig().set("stone.",p.getName());
                        p.sendMessage("gg");
                    }
                }
            }
        }
    }
}
