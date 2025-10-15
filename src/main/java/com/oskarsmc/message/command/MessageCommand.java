package com.oskarsmc.message.command;

import org.incendo.cloud.Command;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.velocity.VelocityCommandManager;
import org.incendo.cloud.velocity.parser.PlayerParser;
import com.google.inject.Inject;
import com.oskarsmc.message.configuration.MessageSettings;
import com.oskarsmc.message.event.MessageEvent;
import com.oskarsmc.message.logic.MessageHandler;
import com.oskarsmc.message.util.DefaultPermission;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

/**
 * The message command class.
 */
public final class MessageCommand {
    /**
     * Construct the message command.
     * @param messageSettings Message Settings
     * @param commandManager Command Manager
     * @param proxyServer Proxy Server
     * @param messageHandler Message Handler
     */
    @Inject
    public MessageCommand(@NotNull MessageSettings messageSettings, @NotNull VelocityCommandManager<CommandSource> commandManager, ProxyServer proxyServer, MessageHandler messageHandler) {
        Command.Builder<CommandSource> builder = commandManager.commandBuilder("message", messageSettings.messageAliases().toArray(new String[0]));

        commandManager.command(builder
                .required("player", PlayerParser.playerParser(), RichDescription.translatable("oskarsmc.message.command.message.argument.player-argument"))
                .required("message", StringParser.greedyStringParser(), RichDescription.translatable("oskarsmc.message.command.common.argument.message-description"))
                .permission(new DefaultPermission("osmc.message.send"))
                .handler(context -> {
                    Player receiver = context.get("player");

                    proxyServer.getEventManager().fire(new MessageEvent(
                            context.sender(),
                            receiver,
                            context.get("message")
                    )).thenAccept(messageHandler::handleMessageEvent);
                })
        );
    }
}
