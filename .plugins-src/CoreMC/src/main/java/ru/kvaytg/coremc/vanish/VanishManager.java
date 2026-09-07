package ru.kvaytg.coremc.vanish;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.utils.BukkitUtils;
import java.util.HashSet;
import java.util.Set;

public enum VanishManager {

    INSTANCE;

    private CoreMc plugin;
    private Set<Player> vanishedPlayers;

    public void init(CoreMc plugin) {
        this.plugin = plugin;
        this.vanishedPlayers = new HashSet<>();
        BukkitUtils.registerHandler(new VanishHandler(plugin, this), plugin);
    }

    public void clear() {
        for (Player vanishedPlayer : new HashSet<>(vanishedPlayers)) {
            show(vanishedPlayer);
        }
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player);
    }

    public void hideVanishedFor(Player player) {
        vanishedPlayers.forEach(vanished -> player.hidePlayer(plugin, vanished));
    }

    public void hide(Player player) {
        if (!vanishedPlayers.add(player)) return;
        for (Player user : Bukkit.getOnlinePlayers()) {
            user.hidePlayer(plugin, player);
        }
    }

    public void show(Player player) {
        if (!vanishedPlayers.remove(player)) return;
        for (Player user : Bukkit.getOnlinePlayers()) {
            user.showPlayer(plugin, player);
        }
    }

    public VanishStatus switchVanish(Player player) {
        if (isVanished(player)) {
            show(player);
            return VanishStatus.DISABLED;
        } else {
            hide(player);
            return VanishStatus.ENABLED;
        }
    }

}