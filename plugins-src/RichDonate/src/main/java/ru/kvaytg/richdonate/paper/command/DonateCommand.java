package ru.kvaytg.richdonate.paper.command;

import org.bukkit.entity.Player;
import ru.kvaytg.richdonate.paper.RichDonate;
import ru.kvaytg.richdonate.paper.menu.DonateMenu;

public class DonateCommand extends AbstractCommand {

    private final DonateMenu donateMenu;

    public DonateCommand(RichDonate plugin) {
        super(plugin, "donate");
        donateMenu = new DonateMenu(plugin);
    }

    @Override
    public void onExecute(Player player, String alias, String[] args) {
        donateMenu.showToPlayer(player);
    }

}