package de.nikey.nikeyv1.Listeners;

import io.papermc.paper.ban.BanListType;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.data.loot.packs.UpdateOneTwentyOneLootTableProvider;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PowerBeacon implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("ol");
        ItemStack itemInHand = event.getItemInHand();
        ItemMeta meta = event.getItemInHand().getItemMeta();
        if (meta.getDisplayName().equalsIgnoreCase(ChatColor.of("#f59542")+"Power Beacon")) {
            player.sendMessage("12");
            itemInHand.setAmount(itemInHand.getAmount()-1);
            openBanMenu(player);
            event.setCancelled(true);
            player.sendMessage("odddwqdwq");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory != null && clickedInventory.equals(event.getView().getTopInventory())) {
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                SkullMeta skullMeta = (SkullMeta) clickedItem.getItemMeta();
                OfflinePlayer targetPlayer = skullMeta.getOwningPlayer();

                if (targetPlayer != null) {
                    BanList<?> banList = Bukkit.getBanList(BanList.Type.PROFILE);

                    if (banList.isBanned(targetPlayer.getName())) {
                        banList.pardon(targetPlayer.getName());
                        Bukkit.broadcast(Component.text(targetPlayer.getName()+"`s stone got power again"));
                    } else {
                        player.sendMessage("Der Spieler " + targetPlayer.getName() + " ist nicht gebannt.");
                    }

                    player.closeInventory();
                }
            }
        }
    }

    private void openBanMenu(Player player) {
        BanList<?> banList = Bukkit.getBanList(BanList.Type.PROFILE);
        @NotNull Set<BanEntry> bannedPlayers = banList.getBanEntries();

        int size = Math.min(54, (bannedPlayers.size() / 9 + 1) * 9); // Dynamically adjust inventory size
        String title = "Gebannte Spieler";
        Inventory menu = Bukkit.createInventory(player, size, title);

        for (OfflinePlayer bannedPlayer : bannedPlayers) {
            ItemStack head = getPlayerHead(bannedPlayer.getName());
            menu.addItem(head);
        }

        player.openInventory(menu);
    }

    private ItemStack getPlayerHead(String playerName) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        head.setItemMeta(meta);
        return head;
    }
}
