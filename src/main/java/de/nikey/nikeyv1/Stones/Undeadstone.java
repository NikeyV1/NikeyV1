package de.nikey.nikeyv1.Stones;

import com.sun.java.accessibility.util.internal.CheckboxTranslator;
import de.nikey.nikeyv1.NikeyV1;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("ALL")
public class Undeadstone implements Listener {
    public ArrayList<LivingEntity> Soul = new ArrayList<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stein")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.plugin.getConfig();
            config.set(p.getName()+".stone","Undead");
            config.set(p.getName()+".level",i);
            NikeyV1.plugin.saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_AIR||event.getAction() == Action.RIGHT_CLICK_BLOCK){
                if (!Soul.isEmpty()){
                    EntityType type = Soul.get(0).getType();
                    if (!type.getName().equalsIgnoreCase("Wither")||!type.getName().equalsIgnoreCase("Ender_dragon")||!type.getName().equalsIgnoreCase("Evoker")){
                        LivingEntity summoned = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), type);
                        summoned.setCustomName(p.getDisplayName()+"'s "+type.getName());
                        Soul.clear();
                        p.getWorld().playSound(p.getLocation(),Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
                        ItemStack hand = p.getInventory().getItemInMainHand();
                        ItemMeta meta = hand.getItemMeta();
                        ArrayList<String> list = new ArrayList<>();
                        list.add(ChatColor.of("#221726")+"Souls wander around in this stone");
                        list.add(ChatColor.of("#00FFAA")+"Level:"+i);
                        meta.setLore(list);
                        hand.setItemMeta(meta);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getKiller() instanceof Player && !(entity instanceof Player)){
            Player p = (Player) entity.getKiller();
            if (p.getInventory().getItemInMainHand().getItemMeta() == null)return;
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stein")){
                String[] arr = p.getItemInHand().getLore().get(1).split(":");
                int i = Integer.parseInt(arr[1]);
                if (i == 10||i == 11) {
                    if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                        p.updateInventory();
                        remainingTime1 = cooldown.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (100 * 1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown.remove(p.getUniqueId());
                                cancel();
                            }
                        }.runTaskLater(NikeyV1.getPlugin(), 20 * 100);
                        //cooldown-ability
                        ItemStack hand = p.getInventory().getItemInMainHand();
                        ItemMeta meta = hand.getItemMeta();
                        ArrayList<String> list = new ArrayList<>();
                        list.add(ChatColor.of("#221726")+"Souls wander around in this stone");
                        list.add(ChatColor.of("#00FFAA")+"Level:"+i);
                        list.add("§e");
                        list.add("§7Absorbed Soul:§3 "+entity.getType());
                        meta.setLore(list);
                        hand.setItemMeta(meta);
                        Soul.clear();
                        Soul.add(entity);
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player){
            Player p = (Player) event.getDamager();
            if (p.getInventory().getItemInMainHand().getItemMeta() == null)return;
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stein")){
                if (cooldown.containsKey(p.getUniqueId())&& cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                    event.setCancelled(true);
                }else {
                    event.setDamage(5);
                }
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event){
        if (event.getTarget() instanceof Player){
            if (event.getEntity() instanceof Monster || event.getEntity() instanceof IronGolem){
                if (event.getEntity().getCustomName().equalsIgnoreCase(((Player) event.getTarget()).getDisplayName()+"'s "+event.getEntityType().getName())){
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
