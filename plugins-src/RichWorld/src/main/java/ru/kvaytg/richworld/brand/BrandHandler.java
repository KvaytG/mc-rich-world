package ru.kvaytg.richworld.brand;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.brand.packet.ReflectionPayloadPacket;
import ru.kvaytg.richworld.component.AbstractHandler;

public class BrandHandler extends AbstractHandler {

    private final ReflectionPayloadPacket packet;

    public BrandHandler(RichWorld plugin) {
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