package ru.kvaytg.richpit.handler;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richpit.RichPit;
import java.util.Random;

public class DeathHandler extends AbstractListener {

    private final RichPit plugin;
    private final Random random = new Random();
    private final Component[] deathMessages;

    public DeathHandler(RichPit plugin) {
        super(plugin);
        this.plugin = plugin;
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();
        String[] templates = {
                "&#FFFF31Игрок &#FFAA01{victim} &#FFFF31убит игроком &#FFAA01{killer}",
                "&#FFFF31Игрок &#FFAA01{victim} &#FFFF31умер от рук игрока &#FFAA01{killer}",
                "&#FFFF31Игрок &#FFAA01{victim} &#FFFF31повержен игроком &#FFAA01{killer}",
                "&#FFFF31Игрок &#FFAA01{victim} &#FFFF31устранён игроком &#FFAA01{killer}",
                "&#FFFF31Игрок &#FFAA01{victim} &#FFFF31нейтрализован игроком &#FFAA01{killer}",
                "&#FFFF31Игрок &#FFAA01{victim} &#FFFF31выведен из строя игроком &#FFAA01{killer}"
        };
        deathMessages = new Component[templates.length];
        for (int i = 0; i < templates.length; i++) {
            String colored = ColorAPI.colorize(templates[i]);
            deathMessages[i] = serializer.deserialize(colored);
        }
    }

    private Component formatDeathMessage(String victim, String killer) {
        Component base = deathMessages[random.nextInt(deathMessages.length)];
        return base
                .replaceText(replace("{victim}", victim))
                .replaceText(replace("{killer}", killer));
    }

    private TextReplacementConfig replace(String placeholder, String replacement) {
        return TextReplacementConfig.builder()
                .matchLiteral(placeholder)
                .replacement(replacement)
                .build();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        if (killer != null && !killer.equals(victim)) {
            Component msg = formatDeathMessage(victim.getName(), killer.getName());
            plugin.getServer().broadcast(msg);
        }
    }

}