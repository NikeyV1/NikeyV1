package de.nikey.nikeyv1.Util;

import de.nikey.nikeyv1.NikeyV1;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.FireworkEffectMeta;
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
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(soul);
        }else {
            player.getWorld().dropItem(player.getLocation(),soul);
        }
        
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

        ShapedRecipe soulrecipe = new ShapedRecipe(new NamespacedKey(NikeyV1.getPlugin(),"soulofstrenght"),soul);
        soulrecipe.shape("EIE","IWI","EIE");
        soulrecipe.setIngredient('I',Material.DIAMOND_BLOCK);
        soulrecipe.setIngredient('W',Material.BEACON);
        soulrecipe.setIngredient('E', new RecipeChoice.ExactChoice(essence));
        NikeyV1.getPlugin().getServer().addRecipe(soulrecipe);
    }
    
    public static void Firestone(Player player , Integer level){
        ItemStack lavastein = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta = lavastein.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        FireworkEffect aa = FireworkEffect.builder().withColor(Color.fromRGB(230,107,99)).build();
        metaFw.setEffect(aa);
        metaFw.setDisplayName(ChatColor.of("#e66b63")+"Fire Stone");
        metaFw.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        lavastein.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7As hot as §clava");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        metaFw.setLore(lore);
        lavastein.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(lavastein);
        }else {
            player.getInventory().setItemInOffHand(lavastein);
        }
    }
    public static void Electrostone(Player player , Integer level){
        ItemStack elektrostein = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  elektrostein.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        FireworkEffect aa = FireworkEffect.builder().withColor(Color.YELLOW).build();
        metaFw.setEffect(aa);
        metaFw.setDisplayName("§eElectric Stone");
        metaFw.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        elektrostein.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.of("#B1A012")+ "Overloaded with electricity");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        metaFw.setLore(lore);
        elektrostein.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(elektrostein);
        }else {
            player.getInventory().setItemInOffHand(elektrostein);
        }
    }
    public static void Waterstone(Player player , Integer level){
        ItemStack waterstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  waterstone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        FireworkEffect aa = FireworkEffect.builder().withColor(Color.BLUE).build();
        metaFw.setEffect(aa);
        metaFw.setDisplayName("§9Water Stone");
        metaFw.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        waterstone.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§1A storm rages in this stone");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        metaFw.setLore(lore);
        waterstone.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(waterstone);
        }else {
            player.getInventory().setItemInOffHand(waterstone);
        }
    }
    public static void Frozenstone(Player player , Integer level){
        ItemStack frozenstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  frozenstone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        FireworkEffect aa = FireworkEffect.builder().withColor(Color.fromRGB(43690)).build();
        metaFw.setEffect(aa);
        metaFw.setDisplayName("§3Frozen Stone");
        metaFw.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        frozenstone.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§bCold enough to freeze fire");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        metaFw.setLore(lore);
        frozenstone.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(frozenstone);
        }else {
            player.getInventory().setItemInOffHand(frozenstone);
        }
    }
    public static void Undeadstone(Player player , Integer level){
        ItemStack undeadstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  undeadstone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        FireworkEffect aa = FireworkEffect.builder().withColor(Color.fromRGB(16,6,19)).build();
        metaFw.setEffect(aa);
        metaFw.setDisplayName(ChatColor.of("#100613")+"Undead Stone");
        metaFw.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        undeadstone.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.of("#221726")+"Souls wander around in this stone");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        metaFw.setLore(lore);
        undeadstone.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(undeadstone);
        }else {
            player.getInventory().setItemInOffHand(undeadstone);
        }
    }
    public static void Holystone(Player player , Integer level){
        ItemStack holystone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  holystone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        FireworkEffect aa = FireworkEffect.builder().withColor(Color.fromRGB(5635925)).build();
        metaFw.setEffect(aa);
        metaFw.setDisplayName("§aHoly Stone");
        metaFw.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        holystone.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§2Forged by Farys");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        metaFw.setLore(lore);
        holystone.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(holystone);
        }else {
            player.getInventory().setItemInOffHand(holystone);
        }
    }

    public static void Ghoststone(Player player , Integer level){
        ItemStack ghoststone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  ghoststone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        FireworkEffect aa = FireworkEffect.builder().withColor(Color.fromRGB(14540253)).build();
        metaFw.setEffect(aa);
        metaFw.setDisplayName(ChatColor.of("#dddddd")+"Ghost Stone");
        metaFw.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ghoststone.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§fHaunted by ghosts");
        lore.add(ChatColor.of("#00FFAA")+"Level:"+level);
        metaFw.setLore(lore);
        ghoststone.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(ghoststone);
        }else {
            player.getInventory().setItemInOffHand(ghoststone);
        }
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
        lore.add("§7This Item has the Power to trick your §8Stone");
        lore.add("§7into swapping itself for a outher random §8Stone");
        meta.setLore(lore);
        anvil.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(NikeyV1.getPlugin(),"stoneswitcher"),anvil);
        recipe.shape("YTY","TWT","EYE");
        recipe.setIngredient('T',Material.TOTEM_OF_UNDYING);
        recipe.setIngredient('W',Material.RECOVERY_COMPASS);
        recipe.setIngredient('Y',Material.NETHERITE_INGOT);
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(soul));
        NikeyV1.getPlugin().getServer().addRecipe(recipe);
    }

    public static void GiveSwitcher(Player player) {
        ItemStack anvil = new ItemStack(Material.PAPER);
        ItemMeta meta = anvil.getItemMeta();
        meta.setDisplayName("§3Stone Switcher");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        anvil.addUnsafeEnchantment(Enchantment.DIG_SPEED,1);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7This Item has the Power to trick your §8Stone");
        lore.add("§7into swapping itself for a outher random §8Stone");
        meta.setLore(lore);
        anvil.setItemMeta(meta);
        player.getInventory().addItem(anvil);
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
        NamespacedKey key = new NamespacedKey(NikeyV1.getPlugin(),"enchantedanvil");
        ShapedRecipe recipe = new ShapedRecipe(key,anvil);
        recipe.shape("NWN","ENE","NEN");
        recipe.setIngredient('N',Material.NETHERITE_SCRAP);
        recipe.setIngredient('W',Material.IRON_BLOCK);
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
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(sword);
        }else {
            player.getInventory().setItem(9,sword);
        }
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
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(elementalstone);
        }else {
            player.getInventory().setItemInOffHand(elementalstone);
        }
    }

    public static void PowerBeacon(){
        ItemStack beacon = new ItemStack(Material.BEACON);
        ItemMeta bmeta = beacon.getItemMeta();
        bmeta.setDisplayName(ChatColor.of("#f59542")+"Power Beacon");
        beacon.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        bmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> plore = new ArrayList<>();
        plore.add("§aA friend of every stone");
        bmeta.setLore(plore);
        beacon.setItemMeta(bmeta);
        //
        ItemStack soul = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta meta = soul.getItemMeta();
        meta.setDisplayName("§3Soul of Strenght");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§7A §3Soul §7that contains the §cStrenght §7of 1000 Players");
        meta.setLore(lore);
        soul.setItemMeta(meta);
        //
        NamespacedKey key = new NamespacedKey(NikeyV1.getPlugin(),"powerbeacon");
        ShapedRecipe recipe = new ShapedRecipe(key,beacon);
        recipe.shape("SHS","SBS","SES");
        recipe.setIngredient('H',Material.HEART_OF_THE_SEA);
        recipe.setIngredient('E',Material.END_CRYSTAL);
        recipe.setIngredient('B',Material.BEACON);
        recipe.setIngredient('S', new RecipeChoice.ExactChoice(soul));
        NikeyV1.getPlugin().getServer().addRecipe(recipe);
    }

    public static void GivePowerBeacon(Player player){
        ItemStack beacon = new ItemStack(Material.BEACON);
        ItemMeta bmeta = beacon.getItemMeta();
        bmeta.setDisplayName(ChatColor.of("#f59542")+"Power Beacon");
        beacon.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        bmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> plore = new ArrayList<>();
        plore.add("§aA friend of every stone");
        bmeta.setLore(plore);
        beacon.setItemMeta(bmeta);
        player.getInventory().addItem(beacon);
    }

    public static void GiveUpgradeToken(Player player){
        ItemStack beacon = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta bmeta = beacon.getItemMeta();
        bmeta.setDisplayName(ChatColor.of("#D0B4F4")+"Upgrade Token");
        beacon.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        bmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> plore = new ArrayList<>();
        plore.add("§6The strenght core");
        bmeta.setLore(plore);
        beacon.setItemMeta(bmeta);
        player.getInventory().addItem(beacon);
    }

    public static void UpgradeToken(){
        ItemStack beacon = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta bmeta = beacon.getItemMeta();
        bmeta.setDisplayName(ChatColor.of("#D0B4F4")+"Upgrade Token");
        beacon.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        bmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> plore = new ArrayList<>();
        plore.add("§6The strenght core");
        bmeta.setLore(plore);
        beacon.setItemMeta(bmeta);

        ItemStack essence = new ItemStack(Material.DRAGON_BREATH);
        ItemMeta smeta = essence.getItemMeta();
        smeta.setDisplayName("§dEnchanted Essence");
        essence.addUnsafeEnchantment(Enchantment.CHANNELING,1);
        smeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        essence.setItemMeta(smeta);

        ShapelessRecipe recipe = new ShapelessRecipe(beacon);
        recipe.addIngredient(Material.AMETHYST_SHARD);
        recipe.addIngredient(Material.COPPER_INGOT);
        recipe.addIngredient(Material.EMERALD);
        recipe.addIngredient(Material.GOLD_INGOT);
        recipe.addIngredient(Material.IRON_INGOT);
        recipe.addIngredient(Material.LAPIS_LAZULI);
        recipe.addIngredient(Material.QUARTZ);
        recipe.addIngredient(Material.REDSTONE);
        recipe.addIngredient(new RecipeChoice.ExactChoice(essence));
        NikeyV1.getPlugin().getServer().addRecipe(recipe);
        NikeyV1.getPlugin().getLogger().info("Recipe active");
    }
}
