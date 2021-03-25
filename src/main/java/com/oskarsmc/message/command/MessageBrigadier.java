package com.oskarsmc.message.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.oskarsmc.message.configuration.MessageSettings;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageBrigadier {
    public MessageBrigadier(ProxyServer proxyServer, MessageSettings messageSettings) {
        LiteralCommandNode<CommandSource> messageCommand = LiteralArgumentBuilder
                .<CommandSource>literal("message")
                .executes(context -> {
                    return 1;
                })
                .build();

        ArgumentCommandNode<CommandSource, String> messageNode = RequiredArgumentBuilder
                .<CommandSource, String>argument("message",  StringArgumentType.greedyString())
                .executes(context -> {
                    if (context.getSource().hasPermission("osmc.message.send")) {
                        Optional<Player> playerOptional = proxyServer.getPlayer(context.getArgument("player", String.class));

                        if (playerOptional.isPresent()) {
                            String senderName;
                            String receiverName;

                            if (context.getSource() instanceof Player) {
                                Player sender = (Player) context.getSource();
                                senderName = sender.getUsername();
                            } else {
                                senderName = "UNKNOWN";
                            }

                            Player receiver = playerOptional.get();
                            receiverName = receiver.getUsername();

                            MiniMessage miniMessage = MiniMessage.get();

                            Map<String, String> map = new HashMap<String, String>();

                            map.put("sender", senderName);
                            map.put("receiver", receiverName);
                            map.put("message", context.getArgument("message", String.class));

                            Component senderMessage = miniMessage.parse(messageSettings.getMessageSentMiniMessage(), map);
                            Component receiverMessage = miniMessage.parse(messageSettings.getMessageReceivedMiniMessage(), map);

                            context.getSource().sendMessage(senderMessage);
                            receiver.sendMessage(receiverMessage);

                            return 1;
                        }
                    }
                    return 0;
                }).build();

        ArgumentCommandNode<CommandSource, String> playerNode = RequiredArgumentBuilder
                .<CommandSource, String>argument("player", StringArgumentType.word())
                .suggests((context, builder) -> {
                    for (Player player : proxyServer.getAllPlayers()) {
                        builder.suggest(player.getGameProfile().getName());
                    }
                    return builder.buildFuture();
                })
                .build();

        playerNode.addChild(messageNode);
        messageCommand.addChild(playerNode);

        BrigadierCommand messageBrigadier = new BrigadierCommand(messageCommand);


        CommandMeta meta = proxyServer.getCommandManager().metaBuilder(messageBrigadier)
                .aliases("msg") // Use seperate alias plugin or add configuration for aliases??
                .build();

        proxyServer.getCommandManager().register(meta, messageBrigadier);
    }
}
