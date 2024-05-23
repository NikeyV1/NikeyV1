package de.nikey.nikeyv1.api;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class EntityTypeDamage {
    public static String getDamageEntityType(Player player) {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        String string = config.getString(player.getName() + ".det");
        if (!string.isEmpty()) {
            return string;
        }else {
            return "";
        }
    }
}
