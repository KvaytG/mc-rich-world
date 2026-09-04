package ru.kvaytg.richdonate.paper.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.kvaytg.richdonate.paper.RichDonate;
import ru.kvaytg.richdonate.paper.donate.coins.CoinsManager;
import ru.kvaytg.richdonate.paper.donate.status.StatusManager;

/*
 *
 * PlaceholderAPI-расширение на стороне Paper.
 *
 */
public class RichDonateExpansion extends PlaceholderExpansion {

    private final RichDonate plugin;

    public RichDonateExpansion(RichDonate plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return plugin.getDescription().getName().toLowerCase();
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(@NotNull Player player, @NotNull String params) {
        params = params.toLowerCase();
        if (params.equals("coins")) {
            return String.valueOf(CoinsManager.INSTANCE.getBalance(player));
        } else if (params.equals("status")) {
            return StatusManager.INSTANCE.getStatus(player);
        }
        return null;
    }

}