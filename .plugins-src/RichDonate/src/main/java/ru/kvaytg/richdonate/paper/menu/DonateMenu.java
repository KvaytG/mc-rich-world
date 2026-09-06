package ru.kvaytg.richdonate.paper.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richdonate.paper.RichDonate;
import ru.kvaytg.richdonate.paper.command.VipCommand;
import java.util.List;

public class DonateMenu {

    private final Inventory menu;

    public DonateMenu(RichDonate plugin) {
        menu = Bukkit.createInventory(null, InventoryType.DISPENSER, component("Донат"));

        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(component(""));
            meta.lore(List.of(
                    component("&a VIP &#FFFF31— расширенные возможности за &#FFAA01" + VipCommand.COST + " ⛂"),
                    Component.empty(),
                    component("&#D0D0D0 • &#FFFF31Префикс &aVIP &#FFFF31в чате, табе и над головой"),
                    component("&#D0D0D0 • &#FFFF31Команда &#FFAA01/fly &#FFFF31— свободный полёт в Хабе"),
                    component("&#D0D0D0 • &#FFAA01x2 &#FFFF31монет за убийства мобов и игроков"),
                    component("&#D0D0D0 • &#FFFF31Эффект &#FFAA01Исцеления &#FFFF31в Симуляторе"),
                    Component.empty(),
                    component("&#FFFF31 Играйте с комфортом!"),
                    Component.empty(),
                    component("&#FFFF31 Получить: &#FFAA01/vip"),
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

    private Component component(String text) {
        String colorized = ColorAPI.colorize(text);
        return LegacyComponentSerializer.legacySection()
                .deserialize(colorized)
                .decoration(TextDecoration.ITALIC, false);
    }

}