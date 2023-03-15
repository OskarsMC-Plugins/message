package com.oskarsmc.message.locale;

import cloud.commandframework.velocity.VelocityCommandManager;
import com.google.inject.Inject;
import com.oskarsmc.message.configuration.MessageSettings;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Map;

/**
 * The command exception handler for handling custom exceptions
 */
public class CommandExceptionHandler {
    /**
     * Construct the command exception handler
     *
     * @param settings       message settings
     * @param commandManager velocity command manager
     * @param logger         logger
     */
    @Inject
    public CommandExceptionHandler(@NotNull MessageSettings settings, @NotNull VelocityCommandManager<CommandSource> commandManager, @NotNull Logger logger) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        for (Map.Entry<Class<? extends Exception>, String> entry : settings.getCustomErrorHandlers().entrySet()) {
            commandManager.registerExceptionHandler(entry.getKey(), (commandSource, e) -> commandSource.sendMessage(miniMessage.deserialize(entry.getValue())));
        }

        int exceptionHandlerAmount = settings.getCustomErrorHandlers().size();
        if (exceptionHandlerAmount > 0) {
            logger.info("Loaded {} custom exception handlers", settings.getCustomErrorHandlers().size());
        }
    }
}
