package de.nikey.nikeyv1;

import de.nikey.nikeyv1.Anti_Exploits.Stone_Swapping;
import de.nikey.nikeyv1.Commands.EffectCMD;
import de.nikey.nikeyv1.Commands.stone;
import de.nikey.nikeyv1.CustomMobs.MiniWitherListener;
import de.nikey.nikeyv1.CustomMobs.WitherCage;
import de.nikey.nikeyv1.CustomMobs.WitherDash;
import de.nikey.nikeyv1.Listeners.*;
import de.nikey.nikeyv1.Stones.*;
import de.nikey.nikeyv1.Util.Items;
import de.nikey.nikeyv1.Util.Tornado;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
        //effect manager
        em = new EffectManager(EffectLib.instance());

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new Player(),this);
        manager.registerEvents(new Firestone(),this);
        manager.registerEvents(new Electrostone(),this);
        manager.registerEvents(new Waterstone(),this);
        manager.registerEvents(new Frozenstone(),this);
        manager.registerEvents(new Undeadstone(),this);
        manager.registerEvents(new Holystone(),this);
        manager.registerEvents(new CommandChenges(),this);
        manager.registerEvents(new ReloadModifyer(),this);
        manager.registerEvents(new JoinListener(),this);
        manager.registerEvents(new InventoryListener(),this);
        manager.registerEvents(new Stone_Swapping(),this);
        manager.registerEvents(new Stone_Swap(),this);
        manager.registerEvents(new Tornado(),this);
        manager.registerEvents(new InfernoBlade(),this);
        manager.registerEvents(new Elementalstone(),this);
        manager.registerEvents(new MiniWitherListener(),this);
        manager.registerEvents(new UndeadStoneListener(),this);
        manager.registerEvents(new WitherDash(),this);
        manager.registerEvents(new WitherCage(),this);
        //Command
        getCommand("stone").setExecutor(new stone());
        getCommand("effect").setExecutor(new EffectCMD());
        //resipes
        Items.EnchantedAnvil();
        Items.Soulrecepie();
        Items.switcher();
        saveDefaultConfig();

        //Code
    }

    @Override
    public void onDisable() {
        for (BukkitRunnable task : Holystone.auraTasks.values()) {
            task.cancel();
        }
        Holystone.auraTasks.clear();
        removeGiants();

        WitherCage.lockedChunks.clear();


        if (MiniWitherListener.task != null) {
            MiniWitherListener.task.cancel();
        }
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
