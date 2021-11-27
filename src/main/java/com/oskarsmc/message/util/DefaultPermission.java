package com.oskarsmc.message.util;

import cloud.commandframework.permission.PredicatePermission;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import org.jetbrains.annotations.NotNull;

public final record DefaultPermission(String permission) implements PredicatePermission<CommandSource> {
    @Override
    public boolean hasPermission(@NotNull CommandSource sender) {
        return sender.getPermissionValue(permission) != Tristate.FALSE;
    }
}
