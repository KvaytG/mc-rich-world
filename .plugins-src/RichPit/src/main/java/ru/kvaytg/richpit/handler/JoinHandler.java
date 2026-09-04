package ru.kvaytg.richpit.handler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.kvaytg.richpit.RichPit;

public class JoinHandler extends AbstractListener {

    private final ItemStack woodenSword;
    private final ItemStack leatherHelmet;
    private final ItemStack leatherBreastplate;
    private final ItemStack leatherLeggings;
    private final ItemStack leatherBoots;

    public JoinHandler(RichPit plugin) {
        super(plugin);
        woodenSword = new ItemStack(Material.WOODEN_SWORD);
        leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        leatherBreastplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
    }

    private void giveEquipment(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.addItem(woodenSword);
        inventory.setHelmet(leatherHelmet);
        inventory.setChestplate(leatherBreastplate);
        inventory.setLeggings(leatherLeggings);
        inventory.setBoots(leatherBoots);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            giveEquipment(player);
        }
    }

}