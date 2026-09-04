package ru.kvaytg.richanarchy.handler;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.kvaytg.richanarchy.RichAnarchy;

public class MonsterDamageHandler extends AbstractListener {

    public MonsterDamageHandler(RichAnarchy plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getDamager() instanceof Monster)) return;
        double damage = event.getDamage();
        int level = player.getLevel();
        if (level == 0) {
            event.setDamage(damage * 0.8);
        } else if (level > 0) {
            event.setDamage(damage * 1.2);
        }
    }

}