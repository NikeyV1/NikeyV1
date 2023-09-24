package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.nikey.nikeyv1.Stones.Electrostone;
import de.nikey.nikeyv1.Stones.Firestone;
import de.nikey.nikeyv1.Timer.TimerBuild;
import de.nikey.nikeyv1.Util.Items;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Date;
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
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.bukkit.entity.Player p = event.getPlayer();
        new ServerScoreboard(p);
        NikeyV1.getPlugin().reloadConfig();
        FileConfiguration config = NikeyV1.plugin.getConfig();
        if (config.contains(p.getName())){
            String stone = config.getString(p.getName() + ".stone");
            if (config.getBoolean(p.getName()+"."+stone+".cooldown1.timer")){
                config.set(p.getName()+"."+stone+".cooldown1.timer",false);
                NikeyV1.plugin.saveConfig();
                if (stone.equalsIgnoreCase("Fire")){
                    a = config.getInt(p.getName() + "." + stone + ".cooldown1.time");
                    Firestone.cooldown.put(p,a);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            a++;
                            if (!p.isOnline()){
                                config.set(p.getName()+"."+stone+"."+"cooldown1.time",a);
                                config.set(p.getName()+"."+stone+"."+"cooldown1.timer",true);
                                NikeyV1.getPlugin().saveConfig();
                                cancel();
                            }
                            if (a < 100){
                            Firestone.cooldown.replace(p,a);
                            }else {
                                a=0;
                                Firestone.cooldown.remove(p);
                                cancel();
                            }
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0L,20);
                }else if (stone.equalsIgnoreCase("Elektro")){
                    a = config.getInt(p.getName() + "." + stone + ".cooldown1.time");
                    Electrostone.cooldown.put(p.getUniqueId(), (long) a);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            a++;
                            if (!p.isOnline()){
                                config.set(p.getName()+"."+stone+"."+"cooldown1.time",a);
                                config.set(p.getName()+"."+stone+"."+"cooldown1.timer",true);
                                NikeyV1.getPlugin().saveConfig();
                                cancel();
                            }
                            if (a < 100){
                                Electrostone.cooldown.replace(p.getUniqueId(), (long) a);
                            }else {
                                a=0;
                                Electrostone.cooldown.remove(p);
                                cancel();
                            }
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0L,20);
                }
            }
            if (config.getBoolean(p.getName()+"."+stone+".cooldown2.timer")){
                config.set(p.getName()+"."+stone+".cooldown2.timer",false);
                NikeyV1.plugin.saveConfig();
                if (stone.equalsIgnoreCase("Elektro")){
                    b = config.getInt(p.getName() + "." + stone + ".cooldown1.time");
                    Electrostone.ability.put(p,b);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            b++;
                            if (!p.isOnline()){
                                config.set(p.getName()+"."+stone+"."+"cooldown1.time",b);
                                config.set(p.getName()+"."+stone+"."+"cooldown1.timer",true);
                                NikeyV1.getPlugin().saveConfig();
                                cancel();
                            }
                            if (b < 180){
                                Electrostone.ability.replace(p,b);
                            }else {
                                b=0;
                                Electrostone.ability.remove(b);
                                cancel();
                            }
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0L,20);
                }
            }
            for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()){

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
            }
        }else {
            Random random = new Random();
            int i = random.nextInt(3);
            if (i == 0){
                Items.Firestone(p,1);
                p.sendTitle("§d§kR§r§cLava Stone§r§d§kR","");
                config.set(p.getName()+".stone", "Fire");
                config.set(p.getName()+".level",1);
                config.set(p.getName()+".time",false);
                NikeyV1.plugin.saveConfig();
            }else if (i == 1){
                Items.Electrostone(p,1);
                p.sendTitle("§d§kR§r§eElektro Stone§r§d§kR","");
                config.set(p.getName()+".stone", "Elektro");
                config.set(p.getName()+".level",1);
                config.set(p.getName()+".time",false);
                NikeyV1.plugin.saveConfig();
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
    public void onPlayerDeath(PlayerDeathEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        if (player.getKiller() != null){
            org.bukkit.entity.Player killer = player.getKiller();
            FileConfiguration config = NikeyV1.plugin.getConfig();
            int i = config.getInt(player.getName() + ".level");
            if (i > 1){
                config.set(player.getName() +".level",i-1);
                String stone = config.getString(player.getName() + ".stone");
                assert stone != null;
                if (stone.equals("Fire")) {
                    for (ItemStack contents : player.getInventory().getContents()) {
                        if (contents == null || contents.getType() == Material.AIR) continue;
                        if (contents.getItemMeta().getDisplayName().equalsIgnoreCase(net.md_5.bungee.api.ChatColor.of("#e66b63") + "Lava Stein")) {
                            String[] arr = contents.getLore().get(1).split(":");
                            String a = arr[1];
                            if (a.equalsIgnoreCase(String.valueOf(i))) {
                                int l = Integer.parseInt(a);
                                l = l - 1;
                                ArrayList<String> lore = new ArrayList<>();
                                lore.add("§7As hot as §clava");
                                lore.add(net.md_5.bungee.api.ChatColor.of("#00FFAA") + "Level:" + l);
                                contents.setLore(lore);
                                new ServerScoreboard(player);
                                Items.SoulofStrenght(killer);
                            } else {
                                config.set(player.getName() + ".level", Integer.parseInt(a));
                            }
                        }
                    }
                }else {
                    if (stone.equals("elektro")) {
                        for (ItemStack contents : player.getInventory().getContents()) {
                            if (contents == null || contents.getType() == Material.AIR) continue;
                            if (contents.getItemMeta().getDisplayName().equalsIgnoreCase(net.md_5.bungee.api.ChatColor.of("#e66b63") + "Lava Stein")) {
                                String[] arr = contents.getLore().get(1).split(":");
                                String a = arr[1];
                                if (a.equalsIgnoreCase(String.valueOf(i))) {
                                    int l = Integer.parseInt(a);
                                    l = l - 1;
                                    ArrayList<String> lore = new ArrayList<>();
                                    lore.add(net.md_5.bungee.api.ChatColor.of("#B1A012")+ "Überladen mit elektrizität");
                                    lore.add(net.md_5.bungee.api.ChatColor.of("#00FFAA")+"Level:"+l);
                                    contents.setLore(lore);
                                    new ServerScoreboard(player);
                                    Items.SoulofStrenght(killer);
                                } else {
                                    config.set(player.getName() + ".level", Integer.parseInt(a));
                                }
                            }
                        }
                    }
                }
            }else {
                Date date = new Date(System.currentTimeMillis()+1440*60*1000);
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "§cYour stone is out of strength!",date,"Game");
                player.kickPlayer("§cYour stone is out of strength!");
            }
        }
        //Wenn spieler gekillt dann gebe SoulofStrenght
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
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
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();new ServerScoreboard(player);
    }

}
