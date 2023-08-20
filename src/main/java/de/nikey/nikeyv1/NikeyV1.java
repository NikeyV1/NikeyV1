package de.nikey.nikeyv1;

import de.nikey.nikeyv1.Listeners.Player;
import de.nikey.nikeyv1.Timer.Timer;
import de.nikey.nikeyv1.Util.Items;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NikeyV1 extends JavaPlugin {
    public static NikeyV1 plugin;

    @Override
    public void onEnable() {
        plugin = this;
        reloadConfig();
        saveDefaultConfig();
        Items.EnchantedAnvil();
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new Player(),this);

    }

    @Override
    public void onDisable() {

    }

    public static NikeyV1 getPlugin() {
        return plugin;
    }

}
