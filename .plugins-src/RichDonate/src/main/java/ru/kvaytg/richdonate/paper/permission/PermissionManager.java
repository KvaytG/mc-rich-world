package ru.kvaytg.richdonate.paper.permission;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.kvaytg.richdonate.paper.RichDonate;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {

    private final List<PermissionGroup> groups;

    public PermissionManager(RichDonate plugin) {
        PermissionGroup defaultGroup = new PermissionGroup("default",
                List.of(
                        "command.link",
                        "command.menu",
                        "command.help",
                        "command.balance",
                        "command.donate",
                        "command.vip",
                        "command.fly",
                        "command.up",
                        "command.upd",
                        "command.upg",
                        "command.info",
                        "command.sell",
                        "command.buy"
                )
        );

        List<String> vipPermissions = new ArrayList<>(defaultGroup.getPermissions());
        vipPermissions.addAll(List.of(
                "tab.group.vip",
                "fly.use"
        ));
        PermissionGroup vipGroup = new PermissionGroup("vip", vipPermissions);

        List<String> adminPermissions = new ArrayList<>(vipGroup.getPermissions());
        adminPermissions.add("tab.group.admin");
        PermissionGroup adminGroup = new PermissionGroup("admin", adminPermissions);

        groups = List.of(
                defaultGroup,
                vipGroup,
                adminGroup
        );

        plugin.getServer().getPluginManager().registerEvents(new Listener() {

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                Player player = event.getPlayer();
                for (PermissionGroup group : groups) {
                    group.removePlayer(player);
                }
            }

        }, plugin);
    }

    public void updatePermissions(Player player, String status) {
        PermissionGroup targetGroup = null;
        for (PermissionGroup group : groups) {
            if (group.getName().equals(status)) {
                targetGroup = group;
            } else {
                group.removePlayer(player);
            }
        }
        if (targetGroup == null) {
            System.err.println("There's no group with name: " + status);
            return;
        }
        if (targetGroup.hasPlayer(player)) return;
        targetGroup.addPlayer(player);
    }

}