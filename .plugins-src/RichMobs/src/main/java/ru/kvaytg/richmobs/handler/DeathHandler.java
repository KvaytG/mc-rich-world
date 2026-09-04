package ru.kvaytg.richmobs.handler;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.kvaytg.richdonate.paper.RichDonate;
import java.util.concurrent.ThreadLocalRandom;

public class DeathHandler implements Listener {

    private final RichDonate donateApi;

    public DeathHandler() {
        donateApi = RichDonate.getInstance();
    }

    private int getRandomInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private int applyAmplifier(Player player, int amount) {
        boolean boosted = !"default".equals(donateApi.getStatus(player));
        if (amount == 1 && boosted) return 2;
        return boosted ? amount * 2 : amount;
    }

    /*
     *
     * Даёт монетки игроку за убийство
     *
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer != null) {
            int coins = entity instanceof Player
                ? getRandomInRange(3, 5)
                : getRandomInRange(1, 2);
            coins = applyAmplifier(killer, coins);
            donateApi.giveCoins(killer, coins);
        }
    }

}