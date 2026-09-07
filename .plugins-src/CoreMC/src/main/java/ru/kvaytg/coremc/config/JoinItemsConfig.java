package ru.kvaytg.coremc.config;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.joinitems.JoinItem;
import ru.kvaytg.coremc.utils.ConfigUtils;
import ru.kvaytg.coremc.utils.StringUtils;
import ru.kvaytg.coremc.utils.other.DotPath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class JoinItemsConfig extends AbstractConfig {

    private final Logger logger;
    private final boolean clearInventoryOnJoin;
    private final List<JoinItem> joinItems;

    public JoinItemsConfig(CoreMc plugin) {
        super(plugin, "items.yml");
        this.logger = plugin.getLogger();
        this.clearInventoryOnJoin = getConfig().getBoolean("clearInventoryOnJoin", false);
        this.joinItems = new ArrayList<>();
        load();
    }

    public boolean shouldClearInventoryOnJoin() {
        return clearInventoryOnJoin;
    }

    public List<JoinItem> getJoinItems() {
        return Collections.unmodifiableList(joinItems);
    }

    private void load() {
        ConfigurationSection section = getConfig().getConfigurationSection("items");
        if (section == null) return;
        for (String joinItemName : section.getKeys(false)) {
            DotPath joinItemPath = new DotPath("items", joinItemName);
            Material material = parseMaterial(joinItemPath.add("material").getPath(), joinItemName);
            if (material == null) continue;
            String name = parseString(joinItemPath.add("name").getPath(), joinItemName);
            if (name == null) continue;
            int slot = parseSlot(joinItemPath.add("slot").getPath(), joinItemName);
            if (slot == -1) continue;
            String command = parseString(joinItemPath.add("command").getPath(), joinItemName);
            if (command == null) continue;
            boolean blockDrop = getConfig().getBoolean(joinItemPath.add("blockDrop").getPath(), false);
            boolean blockMovement = getConfig().getBoolean(joinItemPath.add("blockMovement").getPath(), false);
            Set<World> allowedWorlds = ConfigUtils.getWorlds(this, joinItemPath.add("allowedWorlds"));
            joinItems.add(new JoinItem(material, name, slot, command, blockDrop, blockMovement, allowedWorlds));
        }
    }

    private Material parseMaterial(String path, String joinItemName) {
        String materialName = getConfig().getString(path);
        if (StringUtils.isNullOrBlank(materialName)) {
            logInvalidParameter(joinItemName, "material");
            return null;
        }
        Material material = Material.getMaterial(materialName.toUpperCase());
        if (material == null) {
            logInvalidParameter(joinItemName, "material (unknown material)");
        }
        return material;
    }

    private String parseString(String path, String joinItemName) {
        String value = getConfig().getString(path);
        if (StringUtils.isNullOrBlank(value)) {
            logInvalidParameter(joinItemName, "name");
            return null;
        }
        return value;
    }

    private int parseSlot(String path, String joinItemName) {
        int slot = getConfig().getInt(path, -1);
        if (slot <= 0 || slot > 9) {
            logInvalidParameter(joinItemName, "slot (must be 1-9)");
            return -1;
        }
        return slot - 1;
    }

    private void logInvalidParameter(String joinItem, String parameter) {
        logger.warning(String.format("JoinItem %s has an invalid %s", joinItem, parameter));
    }

}