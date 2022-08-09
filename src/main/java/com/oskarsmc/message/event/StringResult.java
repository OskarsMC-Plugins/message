package com.oskarsmc.message.event;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import com.velocitypowered.api.event.ResultedEvent;

public class StringResult implements ResultedEvent.Result {
    private static final StringResult ALLOWED = new StringResult(true, null);
    private static final StringResult DENIED = new StringResult(false, null);

    private final boolean status;
    private final String string;

    private StringResult(boolean b, String string) {
      this.status = b;
      this.string = string;
    }

    @Override
    public boolean isAllowed() {
        return this.status;
    }

    public @Nullable String string() {
        return this.string;
    }

    public static StringResult allowed() {
        return ALLOWED;
    }

    public static StringResult denied() {
        return DENIED;
    }

    public static StringResult message(String message) {
        return new StringResult(true, Objects.requireNonNull(message));
    }
    
}
