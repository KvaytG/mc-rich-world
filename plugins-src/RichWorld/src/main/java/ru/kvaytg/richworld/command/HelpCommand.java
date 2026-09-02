package ru.kvaytg.richworld.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.component.AbstractCommand;
import ru.kvaytg.richworld.message.Messages;
import java.util.Arrays;

public class HelpCommand extends AbstractCommand {

    private Inventory cachedMenu;

    public HelpCommand(RichWorld plugin) {
        super(plugin, "help");
    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player player)) {
            Messages.NO_ACCESS.send(sender);
            return;
        }
        if (cachedMenu == null) {
            cachedMenu = createMenu();
        }
        player.openInventory(cachedMenu);
    }

    private Inventory createMenu() {
        Inventory menu = Bukkit.createInventory(null, 27, ColorAPI.colorize("Помощь"));
        ItemStack item1 = new ItemStack(Material.PAPER);
        ItemMeta meta1 = item1.getItemMeta();
        meta1.setDisplayName(" ");
        meta1.setLore(Arrays.asList(
                ColorAPI.colorize(" &#FFAA01Как получить монетки?"),
                "",
                ColorAPI.colorize(" &#D0D0D0• &#FFFF31Играть в мини-режимы"),
                ColorAPI.colorize(" &#D0D0D0• &#FFFF31Убивать свинок в Хабе"),
                ColorAPI.colorize(" &#D0D0D0• &#FFFF31Купить на &#FFAA01rich-world.ru "),
                ""
        ));
        item1.setItemMeta(meta1);
        ItemStack item2 = new ItemStack(Material.PAPER);
        ItemMeta meta2 = item2.getItemMeta();
        meta2.setDisplayName(" ");
        meta2.setLore(Arrays.asList(
                ColorAPI.colorize(" &#FFAA01Какие команды доступны?"),
                "",
                ColorAPI.colorize(" &#D0D0D0• &#FFAA01/menu &#D0D0D0— &#FFFF31Открыть меню"),
                ColorAPI.colorize(" &#D0D0D0• &#FFAA01/balance &#D0D0D0— &#FFFF31Узнать баланс"),
                ColorAPI.colorize(" &#D0D0D0• &#FFAA01/hub &#D0D0D0— &#FFFF31Переместиться в Хаб"),
                ColorAPI.colorize(" &#D0D0D0• &#FFAA01/donate &#D0D0D0— &#FFFF31Открыть донат-меню "),
                ColorAPI.colorize(" &#D0D0D0• &#FFAA01/vip &#D0D0D0— &#FFFF31Получить &aVIP"),
                ""
        ));
        item2.setItemMeta(meta2);
        ItemStack item3 = new ItemStack(Material.PAPER);
        ItemMeta meta3 = item3.getItemMeta();
        meta3.setDisplayName(" ");
        meta3.setLore(Arrays.asList(
                ColorAPI.colorize(" &#FFAA01Какие режимы доступны?"),
                "",
                ColorAPI.colorize(" &#FFFF31На данный момент доступны: "),
                ColorAPI.colorize(" &#D0D0D0• &#FFFF31Яма"),
                ColorAPI.colorize(" &#D0D0D0• &#FFFF31Симулятор"),
                ""
        ));
        item3.setItemMeta(meta3);
        menu.setItem(11, item1);
        menu.setItem(13, item2);
        menu.setItem(15, item3);
        return menu;
    }

}