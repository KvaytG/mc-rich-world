package ru.kvaytg.richsimulator.handler;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import ru.kvaytg.richsimulator.RichSimulator;
import java.util.HashMap;
import java.util.Map;

public class VillagerHandler extends AbstractListener {

    private final Map<String, Long> lastClickTimes = new HashMap<>();
    private static final long DELAY_MS = 1000;

    public VillagerHandler(RichSimulator plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        if (!(entity instanceof Villager)) return;
        String playerName = player.getName();
        long currentTime = System.currentTimeMillis();
        if (lastClickTimes.containsKey(playerName)) {
            long timeSinceLastClick = currentTime - lastClickTimes.get(playerName);
            if (timeSinceLastClick < DELAY_MS) {
                event.setCancelled(true);
                return;
            }
        }
        lastClickTimes.put(playerName, currentTime);
    }

}