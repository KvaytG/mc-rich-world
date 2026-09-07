package ru.kvaytg.coremc.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kvaytg.coremc.CoreMc;

public class SpeedHandler extends PerkHandler {

    private float walkSpeed;

    public SpeedHandler(CoreMc plugin) {
        super(plugin, "walkSpeed");
    }

    @Override
    public void onInit() {
        walkSpeed = (float) Math.max(-1.0, Math.min(getConfigFloat("value"), 1.0));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setWalkSpeed(walkSpeed);
    }

}