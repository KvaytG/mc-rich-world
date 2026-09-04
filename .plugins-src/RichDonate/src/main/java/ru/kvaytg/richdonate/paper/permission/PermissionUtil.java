package ru.kvaytg.richdonate.paper.permission;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import ru.kvaytg.richdonate.paper.RichDonate;
import java.util.HashMap;
import java.util.Map;

public class PermissionUtil {

    private static final Map<String, PermissionAttachment> attachments = new HashMap<>();

    public static void givePermission(Player player, String permission) {
        String name = player.getName();
        PermissionAttachment attachment = attachments.computeIfAbsent(
                name,
                k -> player.addAttachment(RichDonate.getInstance())
        );
        attachment.setPermission(permission, true);
    }

    public static void clear(Player player) {
        String name = player.getName();
        PermissionAttachment attachment = attachments.remove(name);
        if (attachment != null) {
            player.removeAttachment(attachment);
        }
    }

}