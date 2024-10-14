package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.EntityTypeDamage;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

@SuppressWarnings("ALL")
public class Holystone implements Listener {
    public static ArrayList<Entity> stunned = new ArrayList<>();
    public static ArrayList<Player> hitted = new ArrayList<>();
    private final List<UUID> selectedPlayers = new ArrayList<>();
    public static final Set<Player> vanishedPlayers = new HashSet<>();
    public static final Map<Player, BukkitRunnable> auraTasks = new HashMap<>();
    public static HashMap<Player, Boolean> repairing = new HashMap<>();
    
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();

    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;


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

    public static void repairfunc(Player player) {
        int level = Stone.getStoneLevel(player);
        String stone = Stone.getStoneName(player);

        if (stone.equalsIgnoreCase("Holy") && !repairing.containsKey(player)){
                if (level == 7){
                    repairing.put(player,true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(stone.equalsIgnoreCase("Holy") && level == 7 && player.isValid()){
                                repairRandomArmorPiece(player);
                            }else{
                                repairfunc(player);
                                repairing.remove(player);
                                cancel();
                            }
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0,40);
                }else if (level == 8){
                    repairing.put(player,true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(stone.equalsIgnoreCase("Holy") && level == 8 && player.isValid()){
                                repairRandomArmorPiece(player);
                            }else{
                                repairfunc(player);
                                repairing.remove(player);
                                cancel();
                            }
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0,20);
                }else if (level >= 9){
                    repairing.put(player,true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(stone.equalsIgnoreCase("Holy") && level >= 9 && player.isValid()){
                                repairRandomArmorPiece(player);
                            }else{
                                repairfunc(player);
                                repairing.remove(player);
                                cancel();
                            }
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0,10);
                }
            }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        repairfunc(event.getPlayer());
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p =  event.getPlayer();
        repairfunc(p);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (Stone.isStone(item) && Stone.whatStone(item).equalsIgnoreCase("Holy")){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Holy");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (i >= 10){
                    if (!(cooldown.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (100 * 1000));
                        //Ability

                        switch (i) {
                            case 10:
                                applyEffect(p, 20, 8, 1, 30 * 30);
                                break;
                            case 11:
                                applyEffect(p, 20, 18, 1, 30 * 30);
                                break;
                            case 12:
                                applyEffect(p, 25, 18, 2, 30 * 30);
                                break;
                            case 13:
                                applyEffect(p, 25, 18, 2, 20 * 30);
                                p.removePotionEffect(PotionEffectType.WEAKNESS);
                                p.removePotionEffect(PotionEffectType.POISON);
                                p.removePotionEffect(PotionEffectType.DARKNESS);
                                p.removePotionEffect(PotionEffectType.LEVITATION);
                                p.removePotionEffect(PotionEffectType.BLINDNESS);
                                p.removePotionEffect(PotionEffectType.SLOWNESS);
                                p.removePotionEffect(PotionEffectType.NAUSEA);
                                p.removePotionEffect(PotionEffectType.WITHER);
                                break;
                            default:
                                if (i >= 14) {
                                    applyEffect(p, 25, 18, 2, 20 * 40);
                                    p.removePotionEffect(PotionEffectType.WEAKNESS);
                                    p.removePotionEffect(PotionEffectType.POISON);
                                    p.removePotionEffect(PotionEffectType.DARKNESS);
                                    p.removePotionEffect(PotionEffectType.LEVITATION);
                                    p.removePotionEffect(PotionEffectType.BLINDNESS);
                                    p.removePotionEffect(PotionEffectType.SLOWNESS);
                                    p.removePotionEffect(PotionEffectType.NAUSEA);
                                    p.removePotionEffect(PotionEffectType.WITHER);
                                }
                                break;
                        }
                    }            
                }
            }else if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR){
                String damageEntityType = EntityTypeDamage.getDamageEntityType(p);
                if (!p.isSneaking()) {
                    if (i == 15){
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(10,10,10)){
                                if (e instanceof LivingEntity) {
                                    if (HelpUtil.shouldDamageEntity((LivingEntity) e,p)) {
                                        LivingEntity player =(LivingEntity) e;
                                        double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*1.8;
                                        int players = p.getNearbyEntities(10, 10, 10).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        if (player instanceof Player) {
                                            player.damage(armor+5,p);
                                        }else {
                                            DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                            player.damage(armor+10,source);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (i == 16) {
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(15,15,15)){
                                if (e instanceof LivingEntity) {
                                    if (HelpUtil.shouldDamageEntity((LivingEntity) e,p)) {
                                        LivingEntity player =(LivingEntity) e;
                                        double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*1.8;
                                        int players = p.getNearbyEntities(15, 15, 15).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        if (player instanceof Player) {
                                            player.damage(armor+5,p);
                                        }else {
                                            DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                            player.damage(armor+10,source);
                                        }
                                    }
                                }
                            }
                        }
                    }else if (i == 17) {
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(15,15,15)){
                                if (e instanceof LivingEntity) {
                                    if (HelpUtil.shouldDamageEntity((LivingEntity) e,p)) {
                                        LivingEntity player =(LivingEntity) e;
                                        double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*1.8;
                                        int players = p.getNearbyEntities(15, 15, 15).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        if (player instanceof Player) {
                                            player.damage(armor+10,p);
                                        }else {
                                            DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                            player.damage(armor+15,source);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (i == 18) {
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(15,15,15)){
                                if (e instanceof LivingEntity) {
                                    if (HelpUtil.shouldDamageEntity((LivingEntity) e,p)) {
                                        LivingEntity player =(LivingEntity) e;
                                        double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.1;
                                        int players = p.getNearbyEntities(15, 15, 15).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }
                                        if (player instanceof Player) {
                                            player.damage(armor+10,p);
                                        }else {
                                            DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                            player.damage(armor+15,source);
                                        }
                                    }
                                }
                            }
                        }
                    }else if (i >= 19) {
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                            for (Entity e : p.getNearbyEntities(15,15,15)){
                                if (e instanceof LivingEntity) {
                                    if (HelpUtil.shouldDamageEntity((LivingEntity) e,p)) {
                                        LivingEntity player =(LivingEntity) e;
                                        double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*2.1;
                                        int players = p.getNearbyEntities(15, 15, 15).size();
                                        if (players == 1){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                        } else if (players  == 2 || players == 3 || players == 4) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                        }else {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,3));
                                        }
                                        if (player instanceof Player) {
                                            player.damage(armor+10,p);
                                        }else {
                                            DamageSource source = DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build();
                                            player.damage(armor+15,source);
                                        }
                                    }
                                }
                            }
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


    public void applyEffect(Player p, int radius, int maxPlayers, int yOffset, int taskDelay) {
        World world = p.getWorld();
        int players = Math.min(p.getNearbyEntities(radius, radius, radius).size(), maxPlayers);
        p.setMaxHealth(22 + players);
        p.setHealth(22 + players);

        Location location = p.getLocation().add(0, yOffset, 0);
        p.spawnParticle(Particle.HEART, location, 3);

        CircleEffect effect = new CircleEffect(NikeyV1.em);
        effect.setEntity(p);
        effect.start();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (p.isOnline()) p.setMaxHealth(20);
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
            if (i == 20 || i == 21){
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
        if (event.getDamager() instanceof Player ) {
            Player damager = (Player) event.getDamager();
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
                                    return;
                                }else {
                                    Location location = entity.getLocation().add(0,2.5F,0);
                                    entity.getWorld().spawnParticle(Particle.WHITE_ASH,location,5);
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0,3);
                    }
                }
                if (damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() >= damager.getHealth() + event.getFinalDamage()*0.5) {
                    damager.setHealth(damager.getHealth() + event.getFinalDamage()*0.5);
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
                        player.sendMessage("§cYou can only select up to 5 players!");
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
            int level = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
            // Apply strength effect to selected players
            for (UUID selectedPlayerUUID : selectedPlayers) {
                Player selectedPlayer = Bukkit.getPlayer(selectedPlayerUUID);
                if (selectedPlayer != null) {
                    selectedPlayer.getWorld().playSound(selectedPlayer.getLocation(),Sound.BLOCK_AMETHYST_BLOCK_BREAK,1.2F,1);
                    if (level == 20) {
                        selectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20*30, 0)); // Strength effect
                        selectedPlayer.setHealth(selectedPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                        selectedPlayer.setFoodLevel(20);
                        selectedPlayer.setSaturation(20);
                        selectedPlayer.setFireTicks(0);
                        //Effect
                        CircleEffect effect = new CircleEffect(NikeyV1.em);
                        effect.setEntity(selectedPlayer);
                        effect.duration=1000*30;
                        effect.enableRotation=false;
                        effect.particle=Particle.INSTANT_EFFECT;
                        effect.start();
                        selectedPlayer.sendMessage(ChatColor.BLUE+player.getName()+ChatColor.GREEN+" buffed you");
                        //Remove Buff
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                selectedPlayers.remove(selectedPlayerUUID);
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*30);
                    } else if (level == 21) {
                        selectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20*45, 0)); // Strength effect
                        selectedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*45, 1));
                        selectedPlayer.setHealth(selectedPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
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
                        Component textComponent = Component.text(player.getName()).color(NamedTextColor.BLUE).append(Component.text(" buffed you").color(NamedTextColor.GREEN));
                        player.sendActionBar(textComponent);
                        //Remove Buff
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                selectedPlayers.remove(selectedPlayerUUID);
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*45);
                        //Repair Armor
                        for (ItemStack armor : selectedPlayer.getInventory().getArmorContents()) {
                            armor.setDurability((short) 0);
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
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    onlinePlayer.showPlayer(NikeyV1.getPlugin(), selectedPlayer);
                                }
                                vanishedPlayers.remove(player);
                                selectedPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new net.md_5.bungee.api.chat.TextComponent(org.bukkit.ChatColor.AQUA + "You are now visible!"));
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

        if (nonRepairedArmorPieces.length == 0) {
            // Alle Rüstungsteile sind bereits vollständig repariert
            return;
        }

        // Wähle ein zufälliges nicht repariertes Rüstungsteil aus
        Random random = new Random();
        ItemStack armorToRepair = nonRepairedArmorPieces[random.nextInt(nonRepairedArmorPieces.length)];

        // Repariere das ausgewählte Rüstungsteil um 1 Haltbarkeitspunkt
        short currentDurability = armorToRepair.getDurability();
        short maxDurability = armorToRepair.getType().getMaxDurability();
        if (currentDurability < maxDurability) {
            armorToRepair.setDurability((short) (currentDurability - 1));
        }else {
            repairRandomArmorPiece(player);
        }
    }

    // Methode zum Abrufen der nicht reparierten Rüstungsteile eines Spielers
    private static ItemStack[] getNonRepairedArmorPieces(ItemStack[] armorContents) {
        List<ItemStack> nonRepairedPieces = new ArrayList<>();
        for (ItemStack armorPiece : armorContents) {
            if (armorPiece != null && armorPiece.getDurability() < armorPiece.getType().getMaxDurability()) {
                nonRepairedPieces.add(armorPiece);
            }
        }
        return nonRepairedPieces.toArray(new ItemStack[0]);
    }
}
