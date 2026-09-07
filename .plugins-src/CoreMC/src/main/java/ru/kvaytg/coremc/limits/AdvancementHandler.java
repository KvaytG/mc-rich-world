package ru.kvaytg.coremc.limits;

import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import ru.kvaytg.coremc.CoreMc;

public class AdvancementHandler extends LimitHandler {

    public AdvancementHandler(CoreMc plugin) {
        super(plugin, "advancements");
    }

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        if (!getWorlds().contains(player.getWorld())) return;
        Advancement advancement = event.getAdvancement();
        for (String criteria : advancement.getCriteria()) {
            player.getAdvancementProgress(advancement).revokeCriteria(criteria);
        }
    }

}