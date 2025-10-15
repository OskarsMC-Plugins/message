package com.oskarsmc.message.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PermissionResult;
import org.incendo.cloud.permission.PredicatePermission;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;

/**
 * Default permission PredicatePermission, as it's not present in CLOUD.
 */
public record DefaultPermission(Permission permission) implements PredicatePermission<CommandSource> {
  public DefaultPermission(String permission) {
    this(Permission.of(permission));
  }

  @Override
  public @NonNull PermissionResult testPermission(@NonNull CommandSource sender) {
    return PermissionResult.of(sender.getPermissionValue(permission.permissionString()) != Tristate.FALSE, permission);
  }
}
