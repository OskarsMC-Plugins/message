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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocialSpyBrigadier {
    private MessageSettings messageSettings;
    public List<CommandSource> watching = new ArrayList<CommandSource>();

    public SocialSpyBrigadier(ProxyServer proxyServer, MessageSettings messageSettings) {
        this.messageSettings = messageSettings;

        LiteralCommandNode<CommandSource> socialSpyCommand = LiteralArgumentBuilder
                .<CommandSource>literal("socialspy")
                .executes(context -> {
                    return 1;
                })
                .build();

        ArgumentCommandNode<CommandSource, String> actionNode = RequiredArgumentBuilder
                .<CommandSource, String>argument("action", StringArgumentType.word())
                .suggests((context, builder) -> {
                    builder.suggest("on");
                    builder.suggest("off");
                    builder.suggest("toggle");
                    return builder.buildFuture();
                })
                .executes(context -> {
                    if (context.getSource().hasPermission("osmc.message.socialspy")) {
                        if (context.getArgument("action", String.class).equals("on")) {
                            watching.add(context.getSource());
                        } else if (context.getArgument("action", String.class).equals("off")) {
                            watching.remove(context.getSource());
                        } else if (context.getArgument("action", String.class).equals("toggle")) {
                            if (watching.contains(context.getSource())) {
                                watching.remove(context.getSource());
                            } else {
                                watching.add(context.getSource());
                            }
                        }
                    }
                    return 0;
                })
                .build();

        socialSpyCommand.addChild(actionNode);

        BrigadierCommand socialSpyBrigadier = new BrigadierCommand(socialSpyCommand);

        CommandMeta.Builder metaBuilder = proxyServer.getCommandManager().metaBuilder(socialSpyBrigadier);

        for (String alias : messageSettings.getSocialSpyAlias()) {
            metaBuilder.aliases(alias);
        }

        CommandMeta meta = metaBuilder.build();

        proxyServer.getCommandManager().register(meta, socialSpyBrigadier);
    }

    @Subscribe
    public void onMessage(MessageEvent event) {
        String senderName;
        String receiverName = event.recipient().getUsername();

        if (event.sender() instanceof Player) {
            Player sender = (Player) event.sender();
            senderName = sender.getUsername();
        } else {
            senderName = "UNKNOWN";
        }

        MiniMessage miniMessage = MiniMessage.get();

        Map<String, String> map = new HashMap<String, String>();

        map.put("sender", senderName);
        map.put("receiver", receiverName);
        map.put("message", event.message());

        Component socialSpyMessageComponent = miniMessage.parse(messageSettings.getMessageSocialSpyMiniMessage(), map);

        for (CommandSource watcher : watching) {
            watcher.sendMessage(socialSpyMessageComponent);
        }
    }
}
