package com.oskarsmc.message.event;

import java.util.Objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.velocitypowered.api.event.ResultedEvent;

/**
 * StringResult class, used for the result of sent messages.
 */
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

    /**
     * String content of the {@link StringResult}
     *
     * @return String content of the {@link StringResult}
     */
    public @Nullable String string() {
        return this.string;
    }

    /**
     * Generic Allowed {@link StringResult}
     *
     * @return Generic Allowed {@link StringResult}
     */
    public static StringResult allowed() {
        return ALLOWED;
    }

    /**
     * Generic Denied {@link StringResult}
     *
     * @return Generic Denied {@link StringResult}
     */
    public static StringResult denied() {
        return DENIED;
    }

    /**
     * Create a new {@link StringResult} object.
     *
     * @param message The message to set
     * @return The newly instantiated {@link StringResult} object.
     */
    @Contract("_ -> new")
    public static @NotNull StringResult message(String message) {
        return new StringResult(true, Objects.requireNonNull(message));
    }

}
