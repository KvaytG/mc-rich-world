package ru.kvaytg.richsimulator.handler;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richsimulator.RichSimulator;

public class JoinHandler extends AbstractListener {

    private final ItemStack sickle;
    private final ItemStack bow;
    private final ItemStack guideBook;

    private final String message;

    public JoinHandler(RichSimulator plugin) {
        super(plugin);
        ItemStack item = new ItemStack(Material.IRON_HOE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Серп")
                .color(TextColor.fromHexString("#FFFF31"))
                .decoration(TextDecoration.ITALIC, false)
        );
        item.setItemMeta(meta);
        sickle = item;
        item = new ItemStack(Material.BOW);
        meta = item.getItemMeta();
        meta.displayName(Component.text("Лук")
                .color(TextColor.fromHexString("#FFFF31"))
                .decoration(TextDecoration.ITALIC, false)
        );
        item.setItemMeta(meta);
        bow = item;
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setTitle("Гайд");
        bookMeta.setAuthor("Администратор");
        bookMeta.addPage(
                "§lГде продать траву?§r\nПродавайте траву Скупщику.\n\n" +
                        "§lГде найти стрелы?§r\nИдите вперёд от Скупщика, там Лучник.\n\n" +
                        "§lГде взять еды?§r\nБегите налево от Скупщика, там Купец."
        );
        book.setItemMeta(bookMeta);
        guideBook = book;
        message = ColorAPI.colorize("&#FFFF31Используйте Серп для сбора травы");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            PlayerInventory inventory = player.getInventory();
            inventory.addItem(sickle);
            inventory.addItem(bow);
            inventory.addItem(guideBook);
            player.sendMessage(message);
        }
    }

}