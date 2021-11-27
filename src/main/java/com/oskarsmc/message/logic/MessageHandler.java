package com.oskarsmc.message.logic;

import com.oskarsmc.message.configuration.MessageSettings;
import com.oskarsmc.message.event.MessageEvent;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.platform.PlayerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class MessageHandler {
    private final MessageSettings messageSettings;
    private final MiniMessage miniMessage = MiniMessage.get();
    private final ConcurrentHashMap<Player, Player> conversations = new ConcurrentHashMap<>();
    public final List<CommandSource> conversationWatchers = new ArrayList<>();

    public MessageHandler(MessageSettings messageSettings) {
        this.messageSettings = messageSettings;
    }

    public void handleMessageEvent(@NotNull MessageEvent event) {
        if (messageSettings.selfMessageSending() && event.sender() == event.recipient()) {
            event.sender().sendMessage(Component.translatable("oskarsmc.message.command.common.self-sending-error", NamedTextColor.RED));
            return;
        }
        
        if (event.getResult().isAllowed()) {
            Component senderName = Component.text("UNKNOWN");
            Component receiverName = Component.text(event.recipient().getUsername());
            if (event.sender() instanceof Player) senderName = Component.text(((Player) event.sender()).getUsername());

            List<Template> templates = new ArrayList<>(List.of(
                    Template.of("sender", senderName),
                    Template.of("receiver", receiverName),
                    Template.of("message", Component.text(event.message()))
            ));

            if (messageSettings.luckpermsIntegration()) {
                LuckPerms luckPerms = LuckPermsProvider.get();

                PlayerAdapter<Player> playerAdapter = luckPerms.getPlayerAdapter(Player.class);

                CachedMetaData senderMetaData = null;
                if (event.sender() instanceof Player) {
                    senderMetaData = playerAdapter.getUser(((Player) event.sender())).getCachedData().getMetaData();
                }

                CachedMetaData recipientMetaData = playerAdapter.getUser(event.recipient()).getCachedData().getMetaData();

                templates.addAll(craftLuckpermsTemplates("sender", senderMetaData));
                templates.addAll(craftLuckpermsTemplates("receiver", recipientMetaData));
            }

            for (Map.Entry<String, Component> entry : event.extraPlaceholders().entrySet()) {
                templates.add(Template.of(entry.getKey(), entry.getValue()));
            }

            Component senderMessage = miniMessage.parse(messageSettings.messageSentMiniMessage(), templates);
            Component receiverMessage = miniMessage.parse(messageSettings.messageReceivedMiniMessage(), templates);

            event.sender().sendMessage(senderMessage);
            event.recipient().sendMessage(receiverMessage);

            if (event.sender() instanceof Player) {
                conversations.remove(event.recipient());
                conversations.put(event.recipient(), ((Player) event.sender()));
            }

            Component socialSpyComponent = miniMessage.parse(messageSettings.messageSocialSpyMiniMessage(), templates);
            for (CommandSource watcher : conversationWatchers) {
                watcher.sendMessage(socialSpyComponent);
            }
        }
    }

    private @Unmodifiable List<Template> craftLuckpermsTemplates(String role, CachedMetaData cachedMetaData) {
        List<Template> templates;
        if (cachedMetaData == null) {
            templates = List.of(
                    Template.of(role + "_prefix", Component.empty()),
                    Template.of(role + "_suffix", Component.empty()),
                    Template.of(role + "_group", Component.empty())
            );
        } else {
            templates = List.of(
                    Template.of(role + "_prefix", LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNullElse(cachedMetaData.getPrefix(), ""))),
                    Template.of(role + "_suffix", LegacyComponentSerializer.legacyAmpersand().deserialize(Objects.requireNonNullElse(cachedMetaData.getSuffix(), ""))),
                    Template.of(role + "_group", Component.text(Objects.requireNonNullElse(cachedMetaData.getPrimaryGroup(), "")))
            );
        }
        return templates;
    }

    public @NotNull @UnmodifiableView Map<Player, Player> conversations() {
        return Collections.unmodifiableMap(conversations);
    }
}
