package de.nikey.nikeyv1.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AbilityCooldownEndEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final String stone;
    private final int ability;
    private Player player;

    public AbilityCooldownEndEvent(String stone, int ability, Player player) {
        this.stone = stone;
        this.ability = ability;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public String getStone() {
        return stone;
    }

    public Player getPlayer() {
        return player;
    }

    public int getAbility() {
        return ability;
    }
}
