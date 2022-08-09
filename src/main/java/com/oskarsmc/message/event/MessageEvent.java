package com.oskarsmc.message.event;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Objects;

/**
 * The event that is fired when a message is sent.
 */
public class MessageEvent implements ResultedEvent<StringResult> {
    private final CommandSource sender;
    private final Player recipient;
    private final String message;
    private final TagResolver.Builder extraPlaceholders;
    private StringResult result = StringResult.allowed();

    /**
     * Construct the message event.
     * @param sender The message sender. Can be console.
     * @param recipient The message recipient.
     * @param message The message.
     */
    public MessageEvent(CommandSource sender, Player recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.extraPlaceholders = TagResolver.builder();
    }

    /**
     * The sender of the message.
     * @return The sender of the message.
     */
    public CommandSource sender() {
        return sender;
    }

    /**
     * The recipient (receiver) of the message.
     * @return The recipient (receiver) of the message.
     */
    public Player recipient() {
        return recipient;
    }

    /**
     * Get the original contents of the message.
     * @return The contents of the message.
     */
    public String message() {
        return message;
    }

    /**
     * Add an extra placeholder.
     * @param key The MiniMessage key.
     * @param value The Component to replace the placeholder with.
     */
    public void extraPlaceholder(String key, Component value) {
        this.extraPlaceholder(Placeholder.component(key, value));
    }

    /**
     * Add an extra placeholder.
     * @param resolver The resolver to add.
     */
    public void extraPlaceholder(TagResolver resolver) {
        extraPlaceholders.resolver(resolver);
    }

    /**
     * Get the extra placeholders.
     * @return The extra placeholders.
     */
    public TagResolver extraPlaceholders() {
        return extraPlaceholders.build();
    }

    @Override
    public StringResult getResult() {
        return this.result;
    }

    @Override
    public void setResult(StringResult result) {
        this.result = Objects.requireNonNull(result);
    }
}
