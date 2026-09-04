package ru.kvaytg.richdonate.paper.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.kvaytg.richdonate.paper.RichDonate;
import ru.kvaytg.richdonate.paper.command.VipCommand;
import java.util.List;

public class DonateMenu {

    private final Inventory menu;

    public DonateMenu(RichDonate plugin) {
        menu = Bukkit.createInventory(null, InventoryType.DISPENSER, Component.text("Донат")
                .decoration(TextDecoration.ITALIC, false));
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.empty().decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(
                    Component.text(" VIP ", NamedTextColor.GREEN)
                            .append(Component.text("— расширенные возможности за ", TextColor.color(0xFFFF31)))
                            .append(Component.text(VipCommand.COST + " ⛂", TextColor.color(0xFFAA01)))
                            .decoration(TextDecoration.ITALIC, false),
                    Component.empty(),
                    Component.text(" • ", TextColor.color(0xD0D0D0))
                            .append(Component.text("Префикс ", TextColor.color(0xFFFF31)))
                            .append(Component.text("VIP ", NamedTextColor.GREEN))
                            .append(Component.text("в чате, табе и над головой", TextColor.color(0xFFFF31)))
                            .decoration(TextDecoration.ITALIC, false),
                    Component.text(" • ", TextColor.color(0xD0D0D0))
                            .append(Component.text("Команда ", TextColor.color(0xFFFF31)))
                            .append(Component.text("/fly ", TextColor.color(0xFFAA01)))
                            .append(Component.text("— свободный полёт в Хабе", TextColor.color(0xFFFF31)))
                            .decoration(TextDecoration.ITALIC, false),
                    Component.text(" • ", TextColor.color(0xD0D0D0))
                            .append(Component.text("x2 ", TextColor.color(0xFFAA01)))
                            .append(Component.text("монет за убийства мобов и игроков", TextColor.color(0xFFFF31)))
                            .decoration(TextDecoration.ITALIC, false),
                    Component.text(" • ", TextColor.color(0xD0D0D0))
                            .append(Component.text("Эффект ", TextColor.color(0xFFFF31)))
                            .append(Component.text("Исцеления ", TextColor.color(0xFFAA01)))
                            .append(Component.text("в Симуляторе", TextColor.color(0xFFFF31)))
                            .decoration(TextDecoration.ITALIC, false),
                    Component.empty(),
                    Component.text(" Играйте с комфортом!", TextColor.color(0xFFFF31))
                            .decoration(TextDecoration.ITALIC, false),
                    Component.empty(),
                    Component.text(" Получить: ", TextColor.color(0xFFFF31))
                            .append(Component.text("/vip", TextColor.color(0xFFAA01)))
                            .decoration(TextDecoration.ITALIC, false),
                    Component.empty()
            ));
            item.setItemMeta(meta);
        }
        menu.setItem(4, item);
        plugin.getServer().getPluginManager().registerEvents(new Listener() {

            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (!event.getInventory().equals(menu)) return;
                event.setCancelled(true);
                if (event.getRawSlot() == 4 && event.getWhoClicked() instanceof Player player) {
                    player.chat("/vip");
                    player.closeInventory();
                }
            }

            @EventHandler
            public void onInventoryDrag(InventoryDragEvent event) {
                if (event.getInventory().equals(menu)) {
                    event.setCancelled(true);
                }
            }

        }, plugin);
    }

    public void showToPlayer(Player player) {
        player.openInventory(menu);
    }

}