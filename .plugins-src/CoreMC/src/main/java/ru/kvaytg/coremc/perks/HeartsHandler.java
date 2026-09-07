package ru.kvaytg.coremc.perks;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kvaytg.coremc.CoreMc;

public class HeartsHandler extends PerkHandler {

    private int maxHealth;
    private boolean restore;

    public HeartsHandler(CoreMc plugin) {
        super(plugin, "maxHearts");
    }

    @Override
    public void onInit() {
        maxHealth = Math.max(getConfigInt("value"), 1);
        restore = getConfigBoolean("restore");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) healthAttribute.setBaseValue(maxHealth);
        if (restore) player.setHealth(maxHealth);
    }

}