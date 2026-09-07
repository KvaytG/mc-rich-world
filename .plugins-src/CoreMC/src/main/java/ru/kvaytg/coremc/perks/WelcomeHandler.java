package ru.kvaytg.coremc.perks;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kvaytg.coremc.CoreMc;

public class WelcomeHandler extends PerkHandler {

    public WelcomeHandler(CoreMc plugin) {
        super(plugin, "welcomeSequence");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }

}