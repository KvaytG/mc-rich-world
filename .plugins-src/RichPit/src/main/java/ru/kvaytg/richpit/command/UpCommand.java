package ru.kvaytg.richpit.command;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richdonate.paper.RichDonate;
import ru.kvaytg.richpit.item.ItemImprover;
import ru.kvaytg.richpit.RichPit;

public class UpCommand implements CommandExecutor {

    private final ItemImprover itemImprover;

    private final RichDonate donateAPI;

    private final String messageNoAccess;
    private final String messageNoItem;
    private final String messageItemAlreadyMax;
    private final String messageItemImproved;
    private final String messageEnough;
    private final String messageNotEnough;

    public UpCommand(RichPit plugin) {
        itemImprover = plugin.getItemImprover();
        donateAPI = RichDonate.getInstance();
        messageNoAccess = ColorAPI.colorize(
                "&aSorry. but this command is ONLY for players"
        );
        messageNoItem = ColorAPI.colorize(
                "&#FFFF31Возьмите предмет для улучшения в ведущую руку!"
        );
        messageItemAlreadyMax = ColorAPI.colorize(
                "&#FFFF31Этот предмет нельзя улучшить"
        );
        messageItemImproved = ColorAPI.colorize(
                "&#FFFF31Вы улучшили предмет"
        );
        String messageInfo = "&#FFFF31Для улучшения вам нужно &#FFAA01%d ⛂\n";
        messageEnough = ColorAPI.colorize(
                messageInfo +
                "&#FFFF31У вас &aдостаточно &#FFFF31монеток для покупки\n" +
                "Введите &#FFAA01/%s confirm &#FFFF31для подтверждения"
        );
        messageNotEnough = ColorAPI.colorize(
                messageInfo +
                "&#FFFF31У вас &#FF0000недостаточно &#FFFF31монеток для покупки"
        );
    }

    private void replaceItemInHand(Player player, Material material) {
        player.getInventory().getItemInMainHand().setType(material);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String alias,
                             @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messageNoAccess);
            return true;
        }
        Material material = player.getInventory().getItemInMainHand().getType();
        if (material.isAir()) {
            player.sendMessage(messageNoItem);
            return true;
        }
        int cost = itemImprover.getImprovementCost(material);
        if (cost == 0) {
            player.sendMessage(messageItemAlreadyMax);
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            if (donateAPI.takeCoins(player, cost)) {
                replaceItemInHand(player, itemImprover.improveItem(material));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                player.sendMessage(messageItemImproved);
            } else {
                player.sendMessage(String.format(messageNotEnough, cost));
            }
        } else {
            boolean isEnough = donateAPI.getCoins(player) >= cost;
            String message = isEnough
                    ? String.format(messageEnough, cost, alias)
                    : String.format(messageNotEnough, cost);
            player.sendMessage(message);
        }
        return true;
    }

}