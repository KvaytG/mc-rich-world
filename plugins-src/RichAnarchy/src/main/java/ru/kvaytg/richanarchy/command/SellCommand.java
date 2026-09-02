package ru.kvaytg.richanarchy.command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richanarchy.RichAnarchy;
import ru.kvaytg.richdonate.paper.RichDonate;

public class SellCommand extends AbstractCommand {

    private final RichDonate donateApi;

    private final String messageNoAccess;
    private final String messageNoItem;
    private final String messageUnsoldItem;
    private final String messageSuccess;

    public SellCommand(RichAnarchy plugin) {
        super(plugin, "sell");
        donateApi = RichDonate.getInstance();
        messageNoAccess = ColorAPI.colorize(
                "&aSorry. but this command is ONLY for players"
        );
        messageNoItem = ColorAPI.colorize(
                "&#FFFF31Возьмите предмет для продажи в ведущую руку!"
        );
        messageUnsoldItem = ColorAPI.colorize(
                "&#FFFF31Этот предмет нельзя продать"
        );
        messageSuccess = ColorAPI.colorize(
                "&#FFFF31Вы продали предмет"
        );
    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messageNoAccess);
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage(messageNoItem);
            return;
        }
        int amount = item.getAmount();
        int price;
        switch (item.getType()) {
            case DIAMOND:
                price = 3 * amount;
                break;
            case NETHERITE_INGOT:
                price = 10 * amount;
                break;
            default:
                player.sendMessage(messageUnsoldItem);
                return;
        }
        player.getInventory().setItemInMainHand(null);
        donateApi.giveCoins(player, price);
        player.sendMessage(messageSuccess);
    }

}