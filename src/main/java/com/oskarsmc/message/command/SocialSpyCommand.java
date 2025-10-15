package com.oskarsmc.message.command;

import org.incendo.cloud.Command;
import org.incendo.cloud.velocity.VelocityCommandManager;
import com.google.inject.Inject;
import com.oskarsmc.message.configuration.MessageSettings;
import com.oskarsmc.message.logic.MessageHandler;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Social Spy command
 */
public final class SocialSpyCommand {
    @Inject
    private MessageHandler messageHandler;

    /**
     * Construct the social spy command.
     *
     * @param messageSettings Message Settings
     * @param commandManager  Command Manager
     */
    @Inject
    public SocialSpyCommand(@NotNull MessageSettings messageSettings, @NotNull VelocityCommandManager<CommandSource> commandManager) {
        Command.Builder<CommandSource> builder = commandManager.commandBuilder("socialspy", messageSettings.socialSpyAliases().toArray(new String[0])).permission("osmc.message.socialspy");

        commandManager.command(builder
                .literal("on")
                .handler(context -> addWatcher(context.sender()))
        );

        commandManager.command(builder
                .literal("off")
                .handler(context -> removeWatcher(context.sender()))
        );

        commandManager.command(builder
                .literal("toggle")
                .handler(context -> {
                    if (messageHandler.conversationWatchers.contains(context.sender())) {
                        removeWatcher(context.sender());
                    } else {
                        addWatcher(context.sender());
                    }
                })
        );
    }

    private void addWatcher(CommandSource source) {
        if (!messageHandler.conversationWatchers.contains(source)) {
            messageHandler.conversationWatchers.add(source);
        }
        source.sendMessage(Component.translatable("oskarsmc.message.command.socialspy.on"));
    }

    private void removeWatcher(CommandSource source) {
        messageHandler.conversationWatchers.remove(source);
        source.sendMessage(Component.translatable("oskarsmc.message.command.socialspy.off"));
    }
}
