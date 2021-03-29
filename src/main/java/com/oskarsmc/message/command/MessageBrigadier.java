package com.oskarsmc.message.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.oskarsmc.message.configuration.MessageSettings;
import com.oskarsmc.message.event.MessageEvent;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageBrigadier {
    private final MessageSettings messageSettings;

    public MessageBrigadier(ProxyServer proxyServer, MessageSettings messageSettings) {
        this.messageSettings = messageSettings;

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
                            Player receiver = playerOptional.get();

                            proxyServer.getEventManager().fire(new MessageEvent(context.getSource(), receiver, context.getArgument("message", String.class)));

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


        CommandMeta.Builder metaBuilder = proxyServer.getCommandManager().metaBuilder(messageBrigadier);

        for (String alias : messageSettings.getMessageAlias()) {
            metaBuilder.aliases(alias);
        }

        CommandMeta meta = metaBuilder.build();

        proxyServer.getCommandManager().register(meta, messageBrigadier);
    }

    @Subscribe
    public void onMessage(MessageEvent event) {
        String senderName;
        String receiverName;

        if (event.getSender() instanceof Player) {
            Player sender = (Player) event.getSender();
            senderName = sender.getUsername();
        } else {
            senderName = "UNKNOWN";
        }


        receiverName = event.getRecipient().getUsername();

        MiniMessage miniMessage = MiniMessage.get();

        Map<String, String> map = new HashMap<String, String>();

        map.put("sender", senderName);
        map.put("receiver", receiverName);
        map.put("message", event.getMessage());

        Component senderMessage = miniMessage.parse(messageSettings.getMessageSentMiniMessage(), map);
        Component receiverMessage = miniMessage.parse(messageSettings.getMessageReceivedMiniMessage(), map);

        event.getSender().sendMessage(senderMessage);
        event.getRecipient().sendMessage(receiverMessage);
    }
}
