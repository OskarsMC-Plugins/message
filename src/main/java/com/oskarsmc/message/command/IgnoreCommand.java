package com.oskarsmc.message.command;

import cloud.commandframework.Command;
import cloud.commandframework.minecraft.extras.RichDescription;
import cloud.commandframework.velocity.VelocityCommandManager;
import cloud.commandframework.velocity.arguments.PlayerArgument;
import com.google.inject.Inject;
import com.oskarsmc.message.configuration.MessageSettings;
import com.oskarsmc.message.logic.IgnoreManager;
import com.oskarsmc.message.util.DefaultPermission;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * /ignore command with subcommands: on, off, list, and toggle
 * Player being ignored doesnt knows
 */
public final class IgnoreCommand {
    private final IgnoreManager ignoreManager;
    private final ProxyServer proxyServer;

    @Inject
    public IgnoreCommand(@NotNull VelocityCommandManager<CommandSource> commandManager,
                         @NotNull MessageSettings messageSettings,
                         @NotNull IgnoreManager ignoreManager,
                         @NotNull ProxyServer proxyServer) {
        this.ignoreManager = ignoreManager;
        this.proxyServer = proxyServer;

        Command.Builder<CommandSource> builder = commandManager.commandBuilder("ignore",
                        messageSettings.ignoreAliases().toArray(new String[0]))
                .senderType(Player.class)
                .permission(new DefaultPermission("osmc.message.ignore"));

        // /ignore <player> toggle
        commandManager.command(builder
                .argument(PlayerArgument.of("player"), RichDescription.translatable("oskarsmc.message.command.ignore.argument.player"))
                .handler(context -> handleToggle((Player) context.getSender(), context.get("player")))
        );

        // /ignore on <player>
        commandManager.command(builder
                .literal("on")
                .argument(PlayerArgument.of("player"), RichDescription.translatable("oskarsmc.message.command.ignore.argument.player"))
                .handler(context -> handleOn((Player) context.getSender(), context.get("player")))
        );

        // /ignore off <player>
        commandManager.command(builder
                .literal("off")
                .argument(PlayerArgument.of("player"), RichDescription.translatable("oskarsmc.message.command.ignore.argument.player"))
                .handler(context -> handleOff((Player) context.getSender(), context.get("player")))
        );

        // /ignore list
        commandManager.command(builder
                .literal("list")
                .handler(context -> handleList((Player) context.getSender()))
        );
    }

    private void handleToggle(Player ignorer, Player target) {
        if (ignorer.getUniqueId().equals(target.getUniqueId())) {
            ignorer.sendMessage(Component.translatable("oskarsmc.message.command.ignore.self-error", NamedTextColor.RED));
            return;
        }
        boolean nowIgnoring = ignoreManager.toggleIgnore(ignorer, target);
        sendFeedback(ignorer, target, nowIgnoring);
    }

    private void handleOn(Player ignorer, Player target) {
        if (ignorer.getUniqueId().equals(target.getUniqueId())) {
            ignorer.sendMessage(Component.translatable("oskarsmc.message.command.ignore.self-error", NamedTextColor.RED));
            return;
        }
        ignoreManager.ignore(ignorer, target);
        sendFeedback(ignorer, target, true);
    }

    private void handleOff(Player ignorer, Player target) {
        if (ignorer.getUniqueId().equals(target.getUniqueId())) {
            ignorer.sendMessage(Component.translatable("oskarsmc.message.command.ignore.self-error", NamedTextColor.RED));
            return;
        }
        ignoreManager.unignore(ignorer, target);
        sendFeedback(ignorer, target, false);
    }

    private void handleList(Player ignorer) {
        Set<UUID> ignored = ignoreManager.getIgnored(ignorer);
        if (ignored.isEmpty()) {
            ignorer.sendMessage(Component.translatable("oskarsmc.message.command.ignore.list.empty"));
            return;
        }

        ignorer.sendMessage(Component.translatable("oskarsmc.message.command.ignore.list.header"));
        for (UUID uuid : ignored) {
            String name = proxyServer.getPlayer(uuid)
                    .map(Player::getUsername)
                    .orElse(uuid.toString());
            ignorer.sendMessage(Component.translatable("oskarsmc.message.command.ignore.list.entry", Component.text(name)));
        }
    }

    private void sendFeedback(Player ignorer, Player target, boolean nowIgnoring) {
        if (nowIgnoring) {
            ignorer.sendMessage(Component.translatable("oskarsmc.message.command.ignore.on", Component.text(target.getUsername())));
        } else {
            ignorer.sendMessage(Component.translatable("oskarsmc.message.command.ignore.off", Component.text(target.getUsername())));
        }
    }
}
