package com.oskarsmc.message.util;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.oskarsmc.message.configuration.MessageSettings;
import com.oskarsmc.message.logic.MessageHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MessageModule extends AbstractModule {
    private final MessageSettings messageSettings;

    public MessageModule(MessageSettings messageSettings) {
        this.messageSettings = messageSettings;
    }

    @Override
    protected void configure() {

    }

    @Contract(" -> new")
    @Singleton
    @Provides
    private @NotNull MessageHandler provideHandler() {
        return new MessageHandler(messageSettings);
    }
}
