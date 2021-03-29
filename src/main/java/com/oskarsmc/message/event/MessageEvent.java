package com.oskarsmc.message.event;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

public class MessageEvent {
    private final CommandSource sender;
    private final Player recipient;
    private final String message;

    public MessageEvent(CommandSource sender, Player recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public CommandSource getSender() {
        return sender;
    }

    public Player getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }
}
