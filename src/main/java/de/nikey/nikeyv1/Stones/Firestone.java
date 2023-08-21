package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Firestone implements Listener {
    int timer;

    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        for (ItemStack contents : player.getInventory().getContents()){
            if(contents == null || contents.getType() == Material.AIR) continue;
            if (contents.getType() == Material.FIREWORK_STAR && contents.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#e66b63")+"Lava Stein")){
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,Integer.MAX_VALUE,1,true,false));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#e66b63")+"Lava Stein")&&
        event.getItem().getType() == Material.FIREWORK_STAR){
            Player p = event.getPlayer();
            ItemStack item = event.getItem();
            ItemMeta meta = item.getItemMeta();
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){
                if (i <= 4) {
                    for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(),20,10,20)){
                        e.setVisualFire(true);
                        if (e instanceof LivingEntity){
                            LivingEntity entity = (LivingEntity) e;
                            timer = 0;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (timer == 15){
                                        p.sendMessage("Gay");
                                        e.setVisualFire(false);
                                        cancel();
                                    } else if (timer < 15) {
                                        p.sendMessage("TT");
                                        e.setVisualFire(true);
                                        timer++;
                                        entity.damage(2);
                                    }
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),20,20);
                        }
                    }
                }else if (i <= 9){

                }else if (i <= 14){

                }else if (i <= 19){

                }
            }else if (event.getAction() == Action.LEFT_CLICK_AIR ||event.getAction() == Action.LEFT_CLICK_BLOCK){
                if (i <= 4) {

                }else if (i <= 9){

                }else if (i <= 14){

                }else if (i <= 19){

                }
            }

        }
    }
    public void run(){

    }
}
