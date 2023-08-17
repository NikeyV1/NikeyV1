package de.nikey.nikeyv1;

import de.nikey.nikeyv1.Listeners.Player;
import de.nikey.nikeyv1.Util.Items;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NikeyV1 extends JavaPlugin {
    public static NikeyV1 plugin;



    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        reloadConfig();
        Items.EnchantedAnvil();
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new Player(),this);

        //Config

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static NikeyV1 getPlugin() {
        return plugin;
    }

}
