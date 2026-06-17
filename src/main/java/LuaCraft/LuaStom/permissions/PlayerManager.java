package LuaCraft.LuaStom.permissions;

import java.util.ArrayList;
import java.util.List;

import LuaCraft.LuaStom.permissions.GroupManager.GroupData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.tag.Tag;

public class PlayerManager extends Player {
    private GroupManager manager;

    public static final Tag<List<String>> GROUP = Tag.String("luacraft_groups").list().defaultValue(List.of());
    public static final Tag<List<String>> PERMISSIONS = Tag.String("luacraft_permissions").list().defaultValue(List.of());

    public PlayerManager(PlayerConnection playerConnection, GameProfile gameProfile, GroupManager manager) {
        super(playerConnection, gameProfile);

        this.manager = manager;
    }

    public boolean hasPermission(String permission) {
        List<String> perms = super.getTag(PERMISSIONS);

        if (perms.contains("*") || perms.contains(permission)) return true;

        List<String> groups = super.getTag(GROUP);

        return groups.stream().anyMatch(groupName -> {
            GroupData groupData = manager.getPermissionGroups().get(groupName);
            if (groupData == null) return false;

            return groupData.getPermissions().contains("*") || groupData.getPermissions().contains(permission);
        });
    }

    public void addPermission(String permission) {
        List<String> perms = new ArrayList<>(super.getTag(PERMISSIONS));

        if (permission.equals("*")) {
            super.sendMessage("[WARNING] Adding this permission to a player grants them access to bypass any permission restriction. This should ONLY be granted to a server operator (the owner)");
            super.sendMessage(Component.text("[SUGGESTION] LuaStom suggests you provide other users specific permissions such as mycommand.usage as a 'minimum permission' to maintain server security and safety. ")
                .append(Component.text("See why by clicking this link")
                .clickEvent(ClickEvent.openUrl("https://learn.microsoft.com/en-us/entra/identity-platform/secure-least-privileged-access"))
                .decorate(TextDecoration.UNDERLINED)
                .color(NamedTextColor.AQUA)));
        }

        perms.add(permission);

        super.setTag(PERMISSIONS, perms);
    }

    public boolean hasGroupAssigned(String group) {
        // Check if group exists here first

        return super.getTag(GROUP).contains(group);
    }

    public void assignGroup(String group) {
        // Check if group exists here first

        List<String> groups = new ArrayList<>(super.getTag(GROUP));

        if (groups.contains(group)) return;

        groups.add(group);

        super.setTag(GROUP, groups);
    }

    public void unAssignGroup(String group) {
        // Check if group exists here first

        List<String> groups = new ArrayList<>(super.getTag(GROUP));

        if (!groups.contains(group)) return;

        groups.remove(group);

        super.setTag(GROUP, groups);
    }
}
