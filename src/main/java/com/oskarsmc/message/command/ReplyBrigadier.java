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
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.HashMap;
import java.util.Map;

public class ReplyBrigadier {
    public HashMap<Player, Player> playerConversations = new HashMap<Player, Player>();

    public ReplyBrigadier(ProxyServer proxyServer, MessageSettings messageSettings) {
        LiteralCommandNode<CommandSource> replyCommand = LiteralArgumentBuilder
                .<CommandSource>literal("reply")
                .executes(context -> {
                    context.getSource().sendMessage(messageSettings.getReplyUsageComponent());
                    return 1;
                })
                .build();

        ArgumentCommandNode<CommandSource, String> messageNode = RequiredArgumentBuilder
                .<CommandSource, String>argument("message",  StringArgumentType.greedyString())
                .executes(context -> {
                    if (context.getSource().getPermissionValue("osmc.message.reply") != Tristate.FALSE) {
                        if (context.getSource() instanceof Player) {
                            Player player = (Player) context.getSource();

                            if (playerConversations.containsKey(player)) {
                                proxyServer.getEventManager().fire(new MessageEvent(player, playerConversations.get(player), context.getArgument("message", String.class)));
                                return 1;
                            } else {
                                context.getSource().sendMessage(messageSettings.getReplyNoPlayerFoundComponent());
                            }
                        } else {
                            // Something here i guess?
                        }
                    } else {
                        context.getSource().sendMessage(messageSettings.getNoPermissionComponent());
                    }
                    return 0;
                }).build();

        replyCommand.addChild(messageNode);

        BrigadierCommand messageBrigadier = new BrigadierCommand(replyCommand);

        CommandMeta.Builder metaBuilder = proxyServer.getCommandManager().metaBuilder(messageBrigadier);

        for (String alias : messageSettings.getReplyAlias()) {
            metaBuilder.aliases(alias);
        }

        CommandMeta meta = metaBuilder.build();

        proxyServer.getCommandManager().register(meta, messageBrigadier);
    }

    @Subscribe
    public void messageEvent(MessageEvent event) {
        if (event.getSender() instanceof Player) {
            Player sender = (Player) event.getSender();
            playerConversations.remove(event.getRecipient());

            playerConversations.put(event.getRecipient(), sender);
        }
    }

    @Subscribe
    public void quitEvent(DisconnectEvent event) {
        playerConversations.remove(event.getPlayer());

        for (Map.Entry<Player, Player> entry : playerConversations.entrySet()) {
            if (entry.getValue().equals(event.getPlayer())) {
                playerConversations.remove(entry.getKey());
            }

        }
    }
}
