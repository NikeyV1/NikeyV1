package de.nikey.nikeyv1.Util;

import de.nikey.nikeyv1.NikeyV1;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Items {

    public static void SoulofStrenght(Player player) {
        ItemStack soul = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta meta = soul.getItemMeta();
        meta.setDisplayName("§3Soul of Strenght");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7A §3Soul §7that contains the §cStrenght §7of 1000 Players");
        meta.setLore(lore);
        soul.setItemMeta(meta);
        player.getInventory().addItem(soul);
        
    }
    public static void Soulrecepie(){
        ItemStack soul = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta meta = soul.getItemMeta();
        meta.setDisplayName("§3Soul of Strenght");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7A §3Soul §7that contains the §cStrenght §7of 1000 Players");
        meta.setLore(lore);
        soul.setItemMeta(meta);

        //Needed
        ItemStack essence = new ItemStack(Material.DRAGON_BREATH);
        ItemMeta smeta = essence.getItemMeta();
        smeta.setDisplayName("§dEnchanted Essence");
        essence.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        smeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        essence.setItemMeta(smeta);
        //Recipe

        ShapedRecipe soulrecipe = new ShapedRecipe(soul);
        soulrecipe.shape("EIE","IWI","EIE");
        soulrecipe.setIngredient('I',Material.DIAMOND_BLOCK);
        soulrecipe.setIngredient('W',Material.BEACON);
        soulrecipe.setIngredient('E', new RecipeChoice.ExactChoice(essence));
        NikeyV1.getPlugin().getServer().addRecipe(soulrecipe);
    }
    
    public static void Firestone(Player player , Integer level){
        ItemStack lavastein = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta = lavastein.getItemMeta();
        meta.setDisplayName(ChatColor.of("#e66b63")+"Fire Stone");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        lavastein.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7As hot as §clava");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        meta.setLore(lore);
        lavastein.setItemMeta(meta);
        player.getInventory().addItem(lavastein);
    }
    public static void Electrostone(Player player , Integer level){
        ItemStack elektrostein = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  elektrostein.getItemMeta();
        meta.setDisplayName("§eElectric Stone");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        elektrostein.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.of("#B1A012")+ "Overloaded with electricity");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        meta.setLore(lore);
        elektrostein.setItemMeta(meta);
        player.getInventory().addItem(elektrostein);
    }
    public static void Waterstone(Player player , Integer level){
        ItemStack waterstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  waterstone.getItemMeta();
        meta.setDisplayName("§9Water Stone");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        waterstone.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§1A storm rages in this stone");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        meta.setLore(lore);
        waterstone.setItemMeta(meta);
        player.getInventory().addItem(waterstone);
    }
    public static void Frozenstone(Player player , Integer level){
        ItemStack frozenstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  frozenstone.getItemMeta();
        meta.setDisplayName("§3Frozen Stone");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        frozenstone.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§bCold enough to freeze fire");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        meta.setLore(lore);
        frozenstone.setItemMeta(meta);
        player.getInventory().addItem(frozenstone);
    }
    public static void Undeadstone(Player player , Integer level){
        ItemStack undeadstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  undeadstone.getItemMeta();
        meta.setDisplayName(ChatColor.of("#100613")+"Undead Stone");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        undeadstone.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.of("#221726")+"Souls wander around in this stone");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        meta.setLore(lore);
        undeadstone.setItemMeta(meta);
        player.getInventory().addItem(undeadstone);
    }
    public static void Holystone(Player player , Integer level){
        ItemStack holystone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  holystone.getItemMeta();
        meta.setDisplayName("§aHoly Stone");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        holystone.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§2Forged by Farys");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        meta.setLore(lore);
        holystone.setItemMeta(meta);
        player.getInventory().addItem(holystone);
    }

    public static void switcher() {
        ItemStack soul = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta smeta = soul.getItemMeta();
        smeta.setDisplayName("§3Soul of Strenght");
        ArrayList<String> slore = new ArrayList<>();
        slore.add("§7A §3Soul §7that contains the §cStrenght §7of 1000 Players");
        smeta.setLore(slore);
        soul.setItemMeta(smeta);

        ItemStack anvil = new ItemStack(Material.PAPER);
        ItemMeta meta = anvil.getItemMeta();
        meta.setDisplayName("§3Stone Switcher");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        anvil.addUnsafeEnchantment(Enchantment.DIG_SPEED,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7This Item has the Power to trick your §8Stone §7into swapping itself for a outher random §8Stone");
        meta.setLore(lore);
        anvil.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(anvil);
        recipe.shape("YTY","TWT","EYE");
        recipe.setIngredient('T',Material.TOTEM_OF_UNDYING);
        recipe.setIngredient('W',Material.RECOVERY_COMPASS);
        recipe.setIngredient('Y',Material.NETHERITE_INGOT);
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(soul));
        NikeyV1.getPlugin().getServer().addRecipe(recipe);
    }
    public static void EnchantedAnvil(){
        ItemStack essence = new ItemStack(Material.DRAGON_BREATH);
        ItemMeta emeta = essence.getItemMeta();
        emeta.setDisplayName("§dEnchanted Essence");
        essence.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        emeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        essence.setItemMeta(emeta);
        //
        ItemStack anvil = new ItemStack(Material.ANVIL);
        ItemMeta meta = anvil.getItemMeta();
        meta.setDisplayName("§dEnchanted Anvil");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        anvil.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        anvil.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(anvil);
        recipe.shape("NWN","ENE","NEN");
        recipe.setIngredient('N',Material.NETHERITE_SCRAP);
        recipe.setIngredient('W',Material.NETHER_STAR);
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(essence));
        NikeyV1.getPlugin().getServer().addRecipe(recipe);
    }
    public static void GiveEnchantedAnvil(Player player){
        ItemStack essence = new ItemStack(Material.DRAGON_BREATH);
        ItemMeta emeta = essence.getItemMeta();
        emeta.setDisplayName("§dEnchanted Essence");
        essence.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        emeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        essence.setItemMeta(emeta);
        //
        ItemStack anvil = new ItemStack(Material.ANVIL);
        ItemMeta meta = anvil.getItemMeta();
        meta.setDisplayName("§dEnchanted Anvil");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        anvil.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        anvil.setItemMeta(meta);
        player.getInventory().addItem(anvil);
    }


    public static void GiveInfernoBlade(Player player) {
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta itemMeta = sword.getItemMeta();
        itemMeta.setDisplayName("§dInferno Blade");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(null);
        lore.add("§7What will you do?");
        lore.add("§7§kDeath is inevitable");
        itemMeta.setLore(lore);
        itemMeta.setUnbreakable(true);
        itemMeta.setCustomModelData(1);
        sword.setItemMeta(itemMeta);
        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,7);
        player.getInventory().addItem(sword);
    }

    public static void GiveElementalStone(Player player){
        ItemStack elementalstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  elementalstone.getItemMeta();
        meta.setDisplayName("§fElemental Stone");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        elementalstone.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§fThe combined power of all §8stones");
        meta.setLore(lore);
        elementalstone.setItemMeta(meta);
        player.getInventory().addItem(elementalstone);
    }
}
