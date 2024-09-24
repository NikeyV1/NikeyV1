package de.nikey.nikeyv1;

import de.nikey.nikeyv1.Anti_Exploits.StoneSwapping;
import de.nikey.nikeyv1.Commands.*;
import de.nikey.nikeyv1.Listeners.*;
import de.nikey.nikeyv1.Stones.*;
import de.nikey.nikeyv1.Util.Items;
import de.nikey.nikeyv1.Util.Tornado;
import de.nikey.nikeyv1.api.Stone;
import de.slikey.effectlib.EffectManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.WindCharge;
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


        PluginManager pm = getServer().getPluginManager();

        if (pm.getPlugin("EffectLib").isEnabled()) {
            getLogger().info("The plugin EffectLib was found successfully");
        }else {
            getLogger().warning("The plugin EffectLib could not be found!");
        }
        if (Bukkit.getServerTickManager().getTickRate() == 10) {
            Bukkit.getServerTickManager().setTickRate(20);
        }

        
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
        manager.registerEvents(new StoneSwapping(),this);
        manager.registerEvents(new Stone_Swap(),this);
        manager.registerEvents(new Tornado(),this);
        manager.registerEvents(new InfernoBlade(),this);
        manager.registerEvents(new Elementalstone(),this);
        manager.registerEvents(new UndeadStoneListener(),this);
        manager.registerEvents(new PowerBeacon(),this);
        manager.registerEvents(new ResourcePack(),this);
        manager.registerEvents(new Ghoststone(),this);
        manager.registerEvents(new GhostStoneDamageAbility(),this);
        manager.registerEvents(new Airstone(),this);
        //Command
        getCommand("stone").setExecutor(new GiveStone());
        getCommand("stone").setTabCompleter(new GiveStone());
        getCommand("peffect").setExecutor(new PEffectCMD());
        getCommand("attack").setExecutor(new AttackCommand());
        getCommand("attack").setTabCompleter(new AttackCommand());
        getCommand("board").setExecutor(new ToggleScoreboard());
        getCommand("trust").setExecutor(new Trust());
        //resipes
        Items.EnchantedAnvil();
        Items.Soulrecepie();
        Items.switcher();
        Items.PowerBeacon();
        Items.UpgradeToken();
        Items.Soulrecepie2();
        Items.Soulrecepie3();
        saveDefaultConfig();
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
                if (entity instanceof LivingEntity) {
                    if (Stone.isUndeadMaster((LivingEntity) entity)) {
                        entity.remove();
                    }
                }
            }
        }
    }

    public static NikeyV1 getPlugin() {
        return plugin;
    }
}
