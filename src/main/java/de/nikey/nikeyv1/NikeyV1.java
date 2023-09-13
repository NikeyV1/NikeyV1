package de.nikey.nikeyv1;

import de.nikey.nikeyv1.Commands.EffectCMD;
import de.nikey.nikeyv1.Commands.stone;
import de.nikey.nikeyv1.Listeners.Player;
import de.nikey.nikeyv1.Listeners.ReloadCommand;
import de.nikey.nikeyv1.Listeners.ReloadModifyer;
import de.nikey.nikeyv1.Stones.Electrostone;
import de.nikey.nikeyv1.Stones.Firestone;
import de.nikey.nikeyv1.Util.Items;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.HashMap;

@SuppressWarnings("ALL")
public final class NikeyV1 extends JavaPlugin {
    public static NikeyV1 plugin;
    public static EffectManager em;
    private final HashMap<org.bukkit.entity.Player, Integer> time = new HashMap<>();
    @Override
    public void onEnable() {
        em = new EffectManager(EffectLib.instance());
        plugin = this;
        reloadConfig();
        saveDefaultConfig();

        Items.EnchantedAnvil();
        Items.Soulrecepie();
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new Player(),this);
        manager.registerEvents(new Firestone(),this);
        manager.registerEvents(new Electrostone(),this);
        manager.registerEvents(new ReloadCommand(),this);
        manager.registerEvents(new ReloadModifyer(),this);
        //Command
        getCommand("stone").setExecutor(new stone());
        getCommand("effect").setExecutor(new EffectCMD());

        //Stuff



    }

    public static NikeyV1 getPlugin() {
        return plugin;
    }

}
