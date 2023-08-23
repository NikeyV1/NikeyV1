package de.nikey.nikeyv1.Util;

import de.nikey.nikeyv1.NikeyV1;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Items {

    public static void SoulofStrenght(Player player) {
        ItemStack soul = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta meta = soul.getItemMeta();
        meta.setDisplayName("§3Soul of Strenght");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7A §3Soul §7 that contains the §cStrenght §7of 1000 Players")
        meta.setLore(lore);
        soul.setItemMeta(meta);
        player.getInventory().addItem(soul);

        ShapedRecipe soulrecipe = new ShapedRecipe(soul);
        recipe.shape("ENE","IWI","EIE");
        recipe.setIngredient('I',Material.IRON_BLOCK);
        recipe.setIngredient('N',Material.NETHERITE_BLOCK);
        recipe.setIngredient('W',Material.NETHER_STAR);
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(essence));
        NikeyV1.plugin.getServer().addRecipe(recipe);
        
    }
    
    public static void Firestone(Player player , Integer level){
        ItemStack lavastein = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  lavastein.getItemMeta();
        meta.setDisplayName(ChatColor.of("#e66b63")+"Lava Stein");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lavastein.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7As hot as §clava");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        meta.setLore(lore);
        lavastein.setItemMeta(meta);
        player.getInventory().addItem(lavastein);
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
        recipe.shape("IWI","ENE","IEI");
        recipe.setIngredient('I',Material.IRON_BLOCK);
        recipe.setIngredient('N',Material.NETHERITE_BLOCK);
        recipe.setIngredient('W',Material.NETHER_STAR);
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(essence));
        NikeyV1.plugin.getServer().addRecipe(recipe);
    }

}
