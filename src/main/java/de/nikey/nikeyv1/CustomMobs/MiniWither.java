package de.nikey.nikeyv1.CustomMobs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Wither;

public class MiniWither extends WitherBoss {

    public MiniWither(Location loc) {
        super(EntityType.WITHER, ((CraftWorld) loc.getWorld()).getHandle());
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        this.setCustomName(Component.literal("Mini-Wither")); // Optional: Namen setzen
        this.setCustomNameVisible(true); // Optional: Namen sichtbar machen
        this.goalSelector.getAvailableGoals().forEach(goal -> this.goalSelector.removeGoal(goal));
    }

}
