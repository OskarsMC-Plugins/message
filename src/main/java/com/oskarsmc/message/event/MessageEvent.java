package com.oskarsmc.message.event;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MessageEvent implements ResultedEvent<ResultedEvent.GenericResult> {
    private final CommandSource sender;
    private final Player recipient;
    private String message;
    private final ConcurrentHashMap<String, Component> extraPlaceholders;
    private GenericResult result = GenericResult.allowed();

    public MessageEvent(CommandSource sender, Player recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.extraPlaceholders = new ConcurrentHashMap<>();
    }

    public CommandSource sender() {
        return sender;
    }

    public Player recipient() {
        return recipient;
    }

    public String message() {
        return message;
    }

    public void message(String message) {
        this.message = message;
    }

    public void extraPlaceholder(String key, Component value) {
        extraPlaceholders.put(key, value);
    }

    public Component extraPlaceholder(String key) {
        return extraPlaceholders.get(key);
    }

    public Map<String, Component> extraPlaceholders() {
        return Collections.unmodifiableMap(extraPlaceholders);
    }

    @Override
    public GenericResult getResult() {
        return this.result;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = Objects.requireNonNull(result);
    }
}
