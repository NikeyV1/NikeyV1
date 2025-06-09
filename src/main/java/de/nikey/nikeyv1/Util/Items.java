package de.nikey.nikeyv1.Util;

import de.nikey.nikeyv1.NikeyV1;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import java.util.List;

@SuppressWarnings("ALL")
public class Items {
    public static void SoulofStrenght(Player player) {
        ItemStack soul = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta meta = soul.getItemMeta();
        meta.displayName(Component.text("Soul of Strength").color(NamedTextColor.DARK_AQUA));
        meta.lore(List.of(
                Component.text("A ", NamedTextColor.GRAY)
                        .append(Component.text("Soul", NamedTextColor.DARK_AQUA))
                        .append(Component.text(" that contains the ", NamedTextColor.GRAY))
                        .append(Component.text("Strength", NamedTextColor.RED))
                        .append(Component.text(" of 1000 Players", NamedTextColor.GRAY))
        ));
        soul.setItemMeta(meta);
        player.give(soul);
    }

    public static void Soulrecepie(){
        ItemStack soul = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta meta = soul.getItemMeta();
        meta.displayName(Component.text("Soul of Strength").color(NamedTextColor.DARK_AQUA));
        meta.lore(List.of(
                Component.text("A ", NamedTextColor.GRAY)
                        .append(Component.text("Soul", NamedTextColor.DARK_AQUA))
                        .append(Component.text(" that contains the ", NamedTextColor.GRAY))
                        .append(Component.text("Strength", NamedTextColor.RED))
                        .append(Component.text(" of 1000 Players", NamedTextColor.GRAY))
        ));
        soul.setItemMeta(meta);

        //Needed
        ItemStack essence = new ItemStack(Material.DRAGON_BREATH);
        ItemMeta smeta = essence.getItemMeta();
        smeta.displayName(Component.text("Enchanted Essence").color(NamedTextColor.LIGHT_PURPLE));
        essence.setItemMeta(smeta);
        //Recipe

        ShapedRecipe soulrecipe = new ShapedRecipe(new NamespacedKey(NikeyV1.getPlugin(),"soulofstrenght"),soul);
        soulrecipe.shape("EIE","IWI","EIE");
        soulrecipe.setIngredient('I',Material.DIAMOND_BLOCK);
        soulrecipe.setIngredient('W',Material.BEACON);
        soulrecipe.setIngredient('E', new RecipeChoice.ExactChoice(essence));
        NikeyV1.getPlugin().getServer().addRecipe(soulrecipe);
    }

    public static void Soulrecepie2(){
        ItemStack soul = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta meta = soul.getItemMeta();
        meta.displayName(Component.text("Soul of Strength").color(NamedTextColor.DARK_AQUA));
        meta.lore(List.of(
                Component.text("A ", NamedTextColor.GRAY)
                        .append(Component.text("Soul", NamedTextColor.DARK_AQUA))
                        .append(Component.text(" that contains the ", NamedTextColor.GRAY))
                        .append(Component.text("Strength", NamedTextColor.RED))
                        .append(Component.text(" of 1000 Players", NamedTextColor.GRAY))
        ));
        soul.setItemMeta(meta);

        //Needed
        ItemStack essence = new ItemStack(Material.DRAGON_BREATH);
        ItemMeta smeta = essence.getItemMeta();
        smeta.displayName(Component.text("Enchanted Essence").color(NamedTextColor.LIGHT_PURPLE));
        essence.setItemMeta(smeta);
        //Recipe

        ShapedRecipe soulrecipe = new ShapedRecipe(new NamespacedKey(NikeyV1.getPlugin(),"soulofstrenght2"),soul);
        soulrecipe.shape("EIE","IWI","EIE");
        soulrecipe.setIngredient('I',Material.DIAMOND_BLOCK);
        soulrecipe.setIngredient('W',Material.CONDUIT);
        soulrecipe.setIngredient('E', new RecipeChoice.ExactChoice(essence));
        NikeyV1.getPlugin().getServer().addRecipe(soulrecipe);
    }

    public static void Soulrecepie3(){
        ItemStack soul = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta meta = soul.getItemMeta();
        meta.displayName(Component.text("Soul of Strength").color(NamedTextColor.DARK_AQUA));
        meta.lore(List.of(
                Component.text("A ", NamedTextColor.GRAY)
                        .append(Component.text("Soul", NamedTextColor.DARK_AQUA))
                        .append(Component.text(" that contains the ", NamedTextColor.GRAY))
                        .append(Component.text("Strength", NamedTextColor.RED))
                        .append(Component.text(" of 1000 Players", NamedTextColor.GRAY))
        ));
        soul.setItemMeta(meta);

        //Needed
        ItemStack essence = new ItemStack(Material.DRAGON_BREATH);
        ItemMeta smeta = essence.getItemMeta();
        smeta.displayName(Component.text("Enchanted Essence").color(NamedTextColor.LIGHT_PURPLE));
        essence.setItemMeta(smeta);
        //Recipe

        ShapedRecipe soulrecipe = new ShapedRecipe(new NamespacedKey(NikeyV1.getPlugin(),"soulofstrenght3"),soul);
        soulrecipe.shape("EIE","IWI","EIE");
        soulrecipe.setIngredient('I',Material.DIAMOND_BLOCK);
        soulrecipe.setIngredient('E', new RecipeChoice.ExactChoice(essence));

        ItemStack enchantedGoldenApple1 = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ItemStack enchantedGoldenApple2 = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        RecipeChoice.ExactChoice enchantedGoldenApples = new RecipeChoice.ExactChoice(enchantedGoldenApple1, enchantedGoldenApple2);

        soulrecipe.setIngredient('W', enchantedGoldenApples);
        NikeyV1.getPlugin().getServer().addRecipe(soulrecipe);
    }
    
    public static void Firestone(Player player , Integer level){
        ItemStack lavastein = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta = lavastein.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        FireworkEffect aa = FireworkEffect.builder().withColor(Color.fromRGB(230,107,99)).build();
        metaFw.setEffect(aa);
        metaFw.displayName(Component.text("Fire Stone").color(TextColor.color(230, 107, 99)));

        metaFw.lore(List.of(Component.text("As hot as lava", NamedTextColor.RED),
                Component.text("Level:" + level).color(NamedTextColor.AQUA)));

        lavastein.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(lavastein);
        } else {
            player.getInventory().setItemInOffHand(lavastein);
        }
    }
    public static void Electrostone(Player player , Integer level){
        ItemStack elektrostein = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  elektrostein.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        metaFw.setEffect(FireworkEffect.builder().withColor(Color.YELLOW).build());
        metaFw.displayName(Component.text("Electric Stone").color(NamedTextColor.YELLOW));

        metaFw.lore(List.of(Component.text("Overloaded with electricity", TextColor.color(177, 160, 18)),
                Component.text("Level:" + level).color(NamedTextColor.AQUA)));
        elektrostein.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(elektrostein);
        } else {
            player.getInventory().setItemInOffHand(elektrostein);
        }
    }
    public static void Waterstone(Player player , Integer level){
        ItemStack waterstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  waterstone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        metaFw.setEffect(FireworkEffect.builder().withColor(Color.BLUE).build());
        metaFw.displayName(Component.text("Water Stone").color(NamedTextColor.BLUE));

        metaFw.lore(List.of(Component.text("A storm rages in this stone", TextColor.color(65,65,137)),
                Component.text("Level:" + level).color(NamedTextColor.AQUA)));
        waterstone.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(waterstone);
        } else {
            player.getInventory().setItemInOffHand(waterstone);
        }
    }
    public static void Frozenstone(Player player , Integer level){
        ItemStack frozenstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  frozenstone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        metaFw.setEffect(FireworkEffect.builder().withColor(Color.fromRGB(43690)).build());
        metaFw.displayName(Component.text("Frozen Stone").color(NamedTextColor.DARK_AQUA));
        metaFw.lore(List.of(Component.text("Cold enough to freeze fire", NamedTextColor.AQUA),
                Component.text("Level:" + level).color(NamedTextColor.AQUA)));
        frozenstone.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(frozenstone);
        } else {
            player.getInventory().setItemInOffHand(frozenstone);
        }
    }
    public static void Undeadstone(Player player , Integer level){
        ItemStack undeadstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  undeadstone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        metaFw.setEffect(FireworkEffect.builder().withColor(Color.fromRGB(16,6,19)).build());
        metaFw.displayName(Component.text("Undead Stone").color(TextColor.color(16,6,19)));
        metaFw.lore(List.of(Component.text("Souls wander around in this stone", TextColor.color(34, 23, 38)),
                Component.text("Level:" + level).color(NamedTextColor.AQUA)));
        undeadstone.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(undeadstone);
        } else {
            player.getInventory().setItemInOffHand(undeadstone);
        }
    }
    public static void Holystone(Player player , Integer level){
        ItemStack holystone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  holystone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        metaFw.setEffect(FireworkEffect.builder().withColor(Color.fromRGB(5635925)).build());
        metaFw.displayName(Component.text("Holy Stone").color(NamedTextColor.GREEN));
        metaFw.lore(List.of(Component.text("Forged by Farys", NamedTextColor.DARK_GREEN),
                Component.text("Level:" + level).color(NamedTextColor.AQUA)));
        holystone.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(holystone);
        } else {
            player.getInventory().setItemInOffHand(holystone);
        }
    }

    public static void Ghoststone(Player player , Integer level){
        ItemStack ghoststone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  ghoststone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        metaFw.setEffect(FireworkEffect.builder().withColor(Color.fromRGB(14540253)).build());
        metaFw.displayName(Component.text("Ghost Stone").color(TextColor.color(221,221,221)));
        metaFw.lore(List.of(Component.text("Haunted by ghosts", NamedTextColor.WHITE),
                Component.text("Level:" + level).color(NamedTextColor.AQUA)));
        ghoststone.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(ghoststone);
        } else {
            player.getInventory().setItemInOffHand(ghoststone);
        }
    }

    public static void Airstone(Player player , Integer level){
        ItemStack airstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta =  airstone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
        metaFw.setEffect(FireworkEffect.builder().withColor(Color.fromRGB(180, 212, 206)).build());
        metaFw.displayName(Component.text("Air Stone").color(TextColor.color(180,212,206)));
        metaFw.lore(List.of(Component.text("Strong winds storm in this stone", NamedTextColor.WHITE),
                Component.text("Level:" + level).color(NamedTextColor.AQUA)));
        airstone.setItemMeta(metaFw);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(airstone);
        } else {
            player.getInventory().setItemInOffHand(airstone);
        }
    }

    public static void Naturestone(Player player , Integer level){
        ItemStack natureStone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta = natureStone.getItemMeta();
        FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;

        metaFw.setEffect(FireworkEffect.builder().withColor(Color.fromRGB(34, 139, 34)).build());
        metaFw.displayName(Component.text("Nature Stone").color(TextColor.color(34,139,34)));

        metaFw.lore(List.of(Component.text("The heart of the forest pulses in this stone", TextColor.color(117, 209, 117)),
                Component.text("Level:" + level).color(NamedTextColor.AQUA)));

        natureStone.setItemMeta(metaFw);

        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(natureStone);
        } else {
            player.getInventory().setItemInOffHand(natureStone);
        }
    }

    public static void switcher() {
        ItemStack soul = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta smeta = soul.getItemMeta();
        smeta.displayName(Component.text("Soul of Strength").color(NamedTextColor.DARK_AQUA));
        smeta.lore(List.of(
                Component.text("A ", NamedTextColor.GRAY)
                        .append(Component.text("Soul", NamedTextColor.DARK_AQUA))
                        .append(Component.text(" that contains the ", NamedTextColor.GRAY))
                        .append(Component.text("Strength", NamedTextColor.RED))
                        .append(Component.text(" of 1000 Players", NamedTextColor.GRAY))
        ));
        soul.setItemMeta(smeta);

        ItemStack beacon = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta bmeta = beacon.getItemMeta();
        bmeta.displayName(Component.text("Upgrade Token").color(TextColor.color(208, 180, 244)));
        bmeta.lore(List.of(Component.text("The strength core").color(NamedTextColor.GOLD)));
        beacon.setItemMeta(bmeta);

        ItemStack anvil = new ItemStack(Material.PAPER);
        ItemMeta meta = anvil.getItemMeta();
        meta.displayName(Component.text("Stone Switcher").color(NamedTextColor.DARK_AQUA));
        meta.lore(List.of(
                Component.text("This Item has the Power to trick your ", NamedTextColor.GRAY)
                        .append(Component.text("Stone", NamedTextColor.DARK_GRAY)),
                Component.text("into swapping itself for a outher random ", NamedTextColor.GRAY)
                        .append(Component.text("Stone", NamedTextColor.DARK_GRAY))
        ));

        anvil.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(NikeyV1.getPlugin(),"stoneswitcher"),anvil);
        recipe.shape("YTY","TWT","YEY");
        recipe.setIngredient('T',Material.TOTEM_OF_UNDYING);
        recipe.setIngredient('W',Material.RECOVERY_COMPASS);
        recipe.setIngredient('Y',new RecipeChoice.ExactChoice(beacon));
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(soul));
        NikeyV1.getPlugin().getServer().addRecipe((Recipe) recipe);
    }

    public static void GiveSwitcher(Player player) {
        ItemStack anvil = new ItemStack(Material.PAPER);
        ItemMeta meta = anvil.getItemMeta();
        meta.displayName(Component.text("Stone Switcher").color(NamedTextColor.DARK_AQUA));
        meta.lore(List.of(
                Component.text("This Item has the Power to trick your ", NamedTextColor.GRAY)
                        .append(Component.text("Stone", NamedTextColor.DARK_GRAY)),
                Component.text("into swapping itself for a outher random ", NamedTextColor.GRAY)
                        .append(Component.text("Stone", NamedTextColor.DARK_GRAY))
        ));

        anvil.setItemMeta(meta);
        player.give(anvil);
    }

    public static void EnchantedAnvil(){
        ItemStack essence = new ItemStack(Material.DRAGON_BREATH);
        ItemMeta smeta = essence.getItemMeta();
        smeta.displayName(Component.text("Enchanted Essence").color(NamedTextColor.LIGHT_PURPLE));
        essence.setItemMeta(smeta);
        //
        ItemStack anvil = new ItemStack(Material.ANVIL);
        ItemMeta meta = anvil.getItemMeta();
        meta.displayName(Component.text("Enchanted Anvil").color(NamedTextColor.LIGHT_PURPLE));
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
        ItemMeta smeta = essence.getItemMeta();
        smeta.displayName(Component.text("Enchanted Essence").color(NamedTextColor.LIGHT_PURPLE));
        essence.setItemMeta(smeta);
        //
        ItemStack anvil = new ItemStack(Material.ANVIL);
        ItemMeta meta = anvil.getItemMeta();
        meta.displayName(Component.text("Enchanted Anvil").color(NamedTextColor.LIGHT_PURPLE));
        anvil.setItemMeta(meta);
        player.getInventory().addItem(anvil);
    }


    public static void GiveInfernoBlade(Player player) {
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta itemMeta = sword.getItemMeta();
        itemMeta.displayName(Component.text("Inferno Blade").color(NamedTextColor.LIGHT_PURPLE));
        itemMeta.lore(List.of(
                Component.empty(),
                Component.text("What will you do?", NamedTextColor.GRAY),
                Component.text("Death is inevitable", NamedTextColor.GRAY, TextDecoration.OBFUSCATED)
        ));

        itemMeta.setUnbreakable(true);
        itemMeta.setCustomModelData(1);
        sword.setItemMeta(itemMeta);
        sword.addUnsafeEnchantment(Enchantment.SHARPNESS,7);
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(sword);
        }else {
            player.getInventory().setItem(9,sword);
        }
    }

    public static void GiveElementalStone(Player player) {
        ItemStack elementalstone = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta meta = elementalstone.getItemMeta();
        meta.displayName(Component.text("Elemental Stone", NamedTextColor.WHITE));
        meta.lore(List.of(
                Component.text("The combined power of all ", NamedTextColor.WHITE)
                        .append(Component.text("stones", NamedTextColor.DARK_GRAY))
        ));
        elementalstone.setItemMeta(meta);

        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(elementalstone);
        } else {
            player.getInventory().setItemInOffHand(elementalstone);
        }
    }

    public static void PowerBeacon() {
        ItemStack beacon = new ItemStack(Material.BEACON);
        ItemMeta bmeta = beacon.getItemMeta();
        bmeta.displayName(Component.text("Power Beacon", TextColor.fromHexString("#f59542")));
        bmeta.lore(List.of(
                Component.text("A friend of every stone", NamedTextColor.GREEN)
        ));
        beacon.setItemMeta(bmeta);

        ItemStack soul = new ItemStack(Material.SOUL_LANTERN);
        ItemMeta meta = soul.getItemMeta();
        meta.displayName(Component.text("Soul of Strenght", NamedTextColor.DARK_AQUA));
        meta.lore(List.of(
                Component.text("A ", NamedTextColor.GRAY)
                        .append(Component.text("Soul ", NamedTextColor.DARK_AQUA))
                        .append(Component.text("that contains the ", NamedTextColor.GRAY))
                        .append(Component.text("Strenght ", NamedTextColor.RED))
                        .append(Component.text("of 1000 Players", NamedTextColor.GRAY))
        ));
        soul.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(NikeyV1.getPlugin(), "powerbeacon");
        ShapedRecipe recipe = new ShapedRecipe(key, beacon);
        recipe.shape("SHS", "SBS", "SHS");
        recipe.setIngredient('H', new RecipeChoice.ExactChoice(soul));
        recipe.setIngredient('B', Material.NETHER_STAR);
        recipe.setIngredient('S', Material.TOTEM_OF_UNDYING);
        NikeyV1.getPlugin().getServer().addRecipe(recipe);
    }

    public static void GivePowerBeacon(Player player) {
        ItemStack beacon = new ItemStack(Material.BEACON);
        ItemMeta bmeta = beacon.getItemMeta();
        bmeta.displayName(Component.text("Power Beacon", TextColor.fromHexString("#f59542")));
        bmeta.lore(List.of(
                Component.text("A friend of every stone", NamedTextColor.GREEN)
        ));
        beacon.setItemMeta(bmeta);
        player.getInventory().addItem(beacon);
    }

    public static void GiveUpgradeToken(Player player) {
        ItemStack beacon = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta bmeta = beacon.getItemMeta();
        bmeta.displayName(Component.text("Upgrade Token", TextColor.fromHexString("#D0B4F4")));
        bmeta.lore(List.of(
                Component.text("The strenght core", NamedTextColor.GOLD)
        ));
        beacon.setItemMeta(bmeta);
        player.getInventory().addItem(beacon);
    }

    public static void UpgradeToken(){
        ItemStack beacon = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta bmeta = beacon.getItemMeta();
        bmeta.displayName(Component.text("Upgrade Token", TextColor.fromHexString("#D0B4F4")));
        bmeta.lore(List.of(
                Component.text("The strenght core", NamedTextColor.GOLD)
        ));
        beacon.setItemMeta(bmeta);

        ItemStack essence = new ItemStack(Material.DRAGON_BREATH);
        ItemMeta smeta = essence.getItemMeta();
        smeta.displayName(Component.text("Enchanted Essence", NamedTextColor.LIGHT_PURPLE));
        essence.setItemMeta(smeta);

        NamespacedKey key = new NamespacedKey(NikeyV1.getPlugin(),"upgradetoken");
        ShapelessRecipe recipe = new ShapelessRecipe(key,beacon);
        recipe.addIngredient(Material.AMETHYST_SHARD);
        recipe.addIngredient(Material.COPPER_INGOT);
        recipe.addIngredient(Material.EMERALD);
        recipe.addIngredient(Material.GOLD_INGOT);
        recipe.addIngredient(Material.IRON_INGOT);
        recipe.addIngredient(Material.LAPIS_LAZULI);
        recipe.addIngredient(Material.QUARTZ);
        recipe.addIngredient(Material.REDSTONE);
        recipe.addIngredient(new RecipeChoice.ExactChoice(essence));
        NikeyV1.getPlugin().getServer().addRecipe((Recipe)recipe);
    }
}
