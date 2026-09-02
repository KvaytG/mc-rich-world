package ru.kvaytg.richanarchy.command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richanarchy.RichAnarchy;
import ru.kvaytg.richdonate.paper.RichDonate;

public class BuyCommand extends AbstractCommand {

    private final RichDonate donateApi;

    private final String messageNoAccess;
    private final String messageHelp;
    private final String messageNotEnough;
    private final String messageSuccess;

    public BuyCommand(RichAnarchy plugin) {
        super(plugin, "buy");
        donateApi = RichDonate.getInstance();
        messageNoAccess = ColorAPI.colorize(
                "&aSorry. but this command is ONLY for players"
        );
        messageHelp = ColorAPI.colorize(
                "&#FFFF31Неверный тип зелья. Доступны: speed, strength, jump"
        );
        messageNotEnough = ColorAPI.colorize(
                "&#FFFF31У вас &#FF0000недостаточно &#FFFF31монеток для покупки"
        );
        messageSuccess = ColorAPI.colorize(
                "&#FFFF31Вы купили зелье"
        );
    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messageNoAccess);
            return;
        }
        if (args.length == 0) {
            player.sendMessage(messageHelp);
            return;
        }
        String type = args[0].toLowerCase();
        int price = switch(type) {
            case "speed" -> 50;
            case "strength" -> 70;
            case "jump" -> 40;
            default -> -1;
        };
        if (price < 0) {
            player.sendMessage(messageHelp);
            return;
        }
        if (donateApi.takeCoins(player, price)) {
            ItemStack potion = createPotion(type);
            player.getInventory().addItem(potion);
            player.sendMessage(messageSuccess);
        } else {
            player.sendMessage(messageNotEnough);
        }
    }

    private ItemStack createPotion(String type) {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        switch (type.toLowerCase()) {
            case "speed" -> {
                meta.setDisplayName("§bЗелье скорости");
                meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 180, 1), true);
            }
            case "strength" -> {
                meta.setDisplayName("§cЗелье силы");
                meta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 180, 0), true);
            }
            case "jump" -> {
                meta.setDisplayName("§aЗелье прыгучести");
                meta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 180, 1), true);
            }
            default -> throw new IllegalArgumentException("Неверный тип зелья: " + type);
        }

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        potion.setItemMeta(meta);
        return potion;
    }


}