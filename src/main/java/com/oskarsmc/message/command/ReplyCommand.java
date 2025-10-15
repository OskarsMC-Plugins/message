package com.oskarsmc.message.command;

import org.incendo.cloud.Command;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.velocity.VelocityCommandManager;
import com.google.inject.Inject;
import com.oskarsmc.message.configuration.MessageSettings;
import com.oskarsmc.message.event.MessageEvent;
import com.oskarsmc.message.logic.MessageHandler;
import com.oskarsmc.message.util.DefaultPermission;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Reply Command
 */
public final class ReplyCommand {
    /**
     * Construct the reply command
     * @param messageSettings Message Settings
     * @param commandManager Command Manager
     * @param proxyServer Proxy Server
     * @param messageHandler Message Handler.
     */
    @Inject
    public ReplyCommand(@NotNull MessageSettings messageSettings, @NotNull VelocityCommandManager<CommandSource> commandManager, ProxyServer proxyServer, MessageHandler messageHandler) {
        Command.Builder<CommandSource> builder = commandManager.commandBuilder("reply", messageSettings.replyAliases().toArray(new String[0]));

        commandManager.command(builder
                .senderType(Player.class)
                .permission(new DefaultPermission("osmc.message.reply"))
                .required("message", StringParser.greedyStringParser(), RichDescription.translatable("oskarsmc.message.command.common.argument.message-description"))
                .handler(context -> {
                    Map<Player, Player> conversations = messageHandler.conversations();

                    Player receiver = conversations.get(context.sender());

                    proxyServer.getEventManager().fire(new MessageEvent(
                            context.sender(),
                            receiver,
                            context.get("message")
                    )).thenAccept(messageHandler::handleMessageEvent);
                })
        );
    }
}
