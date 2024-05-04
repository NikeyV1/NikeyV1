package de.nikey.nikeyv1;

import de.nikey.nikeyv1.Anti_Exploits.Stone_Swapping;
import de.nikey.nikeyv1.Commands.EffectCMD;
import de.nikey.nikeyv1.Commands.stone;
import de.nikey.nikeyv1.Listeners.*;
import de.nikey.nikeyv1.Stones.*;
import de.nikey.nikeyv1.Util.Items;
import de.nikey.nikeyv1.Util.Tornado;
import de.slikey.effectlib.EffectManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("ALL")
public final class NikeyV1 extends JavaPlugin{
    public static EffectManager em;
    private static NikeyV1 plugin;
    @Override
    public void onEnable() {
        plugin = this;
        //Effect Manager
        em = new EffectManager(this);

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new Player(),this);
        manager.registerEvents(new Firestone(),this);
        manager.registerEvents(new Electrostone(),this);
        manager.registerEvents(new Waterstone(),this);
        manager.registerEvents(new Frozenstone(),this);
        manager.registerEvents(new Undeadstone(),this);
        manager.registerEvents(new Holystone(),this);
        manager.registerEvents(new CommandChanges(),this);
        manager.registerEvents(new ReloadModifyer(),this);
        manager.registerEvents(new JoinListener(),this);
        manager.registerEvents(new InventoryListener(),this);
        manager.registerEvents(new Stone_Swapping(),this);
        manager.registerEvents(new Stone_Swap(),this);
        manager.registerEvents(new Tornado(),this);
        manager.registerEvents(new InfernoBlade(),this);
        manager.registerEvents(new Elementalstone(),this);
        manager.registerEvents(new UndeadStoneListener(),this);
        manager.registerEvents(new PowerBeacon(),this);
        manager.registerEvents(new ResourcePack(),this);
        //Command
        getCommand("stone").setExecutor(new stone());
        getCommand("effect").setExecutor(new EffectCMD());
        //resipes
        Items.EnchantedAnvil();
        Items.Soulrecepie();
        Items.switcher();
        Items.PowerBeacon();
        saveDefaultConfig();

        //Code
    }

    @Override
    public void onDisable() {
        em.disposeAll();
        HandlerList.unregisterAll();

        for (BukkitRunnable task : Holystone.auraTasks.values()) {
            task.cancel();
        }
        Holystone.auraTasks.clear();
        removeGiants();

    }

    private void removeGiants() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == EntityType.GIANT) {
                    entity.getPassenger().remove();
                    entity.remove();
                }
            }
        }
    }

    public static NikeyV1 getPlugin() {
        return plugin;
    }

}
