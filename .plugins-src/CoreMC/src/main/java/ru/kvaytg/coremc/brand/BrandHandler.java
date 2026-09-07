package ru.kvaytg.coremc.brand;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.brand.packet.ReflectionPayloadPacket;
import ru.kvaytg.coremc.component.AbstractHandler;

public class BrandHandler extends AbstractHandler {

    private final ReflectionPayloadPacket packet;

    public BrandHandler(CoreMc plugin) {
        super(plugin, true);
        this.packet = new ReflectionPayloadPacket(plugin);
    }

    private void sendBrand(Player player) {
        packet.send(player, ServerBrand.get());
    }

    @EventHandler(priority =  EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        sendBrand(event.getPlayer());
    }

}