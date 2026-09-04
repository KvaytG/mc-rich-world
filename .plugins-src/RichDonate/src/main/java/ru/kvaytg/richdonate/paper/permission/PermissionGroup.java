package ru.kvaytg.richdonate.paper.permission;

import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class PermissionGroup {

    private final String name;
    private final List<String> permissions;
    private final List<String> players;

    public PermissionGroup(String name, List<String> permissions) {
        this.name = name;
        this.permissions = new ArrayList<>(permissions);
        this.players = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player.getName());
    }

    public void addPlayer(Player player) {
        if (hasPlayer(player)) return;
        for (String permission : permissions) {
            PermissionUtil.givePermission(player, permission);
        }
        players.add(player.getName());
    }

    public void removePlayer(Player player) {
        if (!hasPlayer(player)) return;
        PermissionUtil.clear(player);
        players.remove(player.getName());
    }

}