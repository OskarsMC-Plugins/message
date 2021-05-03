package com.oskarsmc.message.event;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;

import java.util.Objects;

public class MessageEvent implements ResultedEvent<ResultedEvent.GenericResult> {
    private final CommandSource sender;
    private final Player recipient;
    private String message;
    private GenericResult result = GenericResult.allowed();

    public MessageEvent(CommandSource sender, Player recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
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

    @Override
    public GenericResult getResult() {
        return this.result;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = Objects.requireNonNull(result);
    }
}
