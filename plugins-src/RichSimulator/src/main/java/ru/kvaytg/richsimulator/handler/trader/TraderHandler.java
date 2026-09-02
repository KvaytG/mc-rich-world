package ru.kvaytg.richsimulator.handler.trader;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import ru.kvaytg.richsimulator.RichSimulator;
import ru.kvaytg.richsimulator.handler.AbstractListener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class TraderHandler extends AbstractListener {

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long CLICK_DELAY = 1000;

    public TraderHandler(RichSimulator plugin) {
        super(plugin);
    }

    public boolean isTraderWithName(Entity entity, String name) {
        if (!(entity instanceof Villager villager)) return false;
        return name.equals(villager.getCustomName());
    }

    protected boolean checkAndApplyCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        if (cooldowns.containsKey(uuid)) {
            long elapsed = currentTime - cooldowns.get(uuid);
            if (elapsed < CLICK_DELAY) {
                return true;
            }
        }
        cooldowns.put(uuid, currentTime);
        return false;
    }

}