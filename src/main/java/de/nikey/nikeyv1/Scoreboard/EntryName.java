package de.nikey.nikeyv1.Scoreboard;

import org.bukkit.ChatColor;

public enum EntryName {
    ENTRY_0(0, ChatColor.DARK_PURPLE.toString()),
    ENTRY_1(1, ChatColor.DARK_AQUA.toString()),
    ENTRY_2(2, ChatColor.DARK_BLUE.toString()),
    ENTRY_3(3, ChatColor.DARK_GRAY.toString()),
    ENTRY_4(4, ChatColor.DARK_GREEN.toString()),
    ENTRY_5(5, ChatColor.DARK_RED.toString()),
    ENTRY_6(6, ChatColor.BOLD.toString());

    private int entry;
    private String entryName;

    EntryName(int entry,String entryName) {
        this.entry = entry;
        this.entryName = entryName;
    }

    public int getEntry() {
        return entry;
    }

    public String getEntryName() {
        return entryName;
    }
}
