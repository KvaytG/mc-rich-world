package ru.kvaytg.coremc.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.warp.Warp;
import ru.kvaytg.coremc.utils.StringUtils;
import ru.kvaytg.coremc.utils.other.DotPath;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class WarpConfig extends AbstractConfig {

    private final Set<Warp> warps;

    public WarpConfig(CoreMc plugin) {
        super(plugin, "warps.yml");
        this.warps = new HashSet<>();
        load(plugin.getLogger());
    }

    private Location parseLocation(String locationString) {
        try {
            String[] parts = locationString.split(" ");
            if (parts.length != 6) {
                return null;
            }
            World world = Bukkit.getWorld(parts[0]);
            if (world == null) {
                return null;
            }
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);
            return new Location(world, x, y, z, yaw, pitch);
        } catch(NumberFormatException ignored) {
            return null;
        }
    }

    private void load(Logger logger) {
        String sectionName = "warps";
        ConfigurationSection section = getConfig().getConfigurationSection(sectionName);
        if (section == null) return;
        for (String warpName : section.getKeys(false)) {
            String warpLocation = getString(new DotPath(sectionName, warpName));
            if (!StringUtils.isNullOrBlank(warpLocation)) {
                Location location = parseLocation(warpLocation);
                if (location != null) {
                    warps.add(new Warp(warpName.toLowerCase(), location));
                    continue;
                }
            }
            logger.warning(String.format("Warp '%s' has an invalid location", warpName));
        }
    }

    public Warp getWarp(String name) {
        for (Warp warp : warps) {
            if (warp.name().equalsIgnoreCase(name)) {
                return warp;
            }
        }
        return null;
    }

}