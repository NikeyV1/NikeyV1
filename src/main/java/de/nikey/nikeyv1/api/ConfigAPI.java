package de.nikey.nikeyv1.api;

import de.nikey.nikeyv1.NikeyV1;

public class ConfigAPI {
    public static Integer getStartLevel() {
        return NikeyV1.getPlugin().getConfig().getInt("start-level");
    }
}
