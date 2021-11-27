package com.oskarsmc.message.util;

import com.oskarsmc.message.configuration.MessageSettings;
import org.jetbrains.annotations.NotNull;

public final class VersionUtils {

    public static final double CONFIG_VERSION = 1.0;

    public static boolean isLatestConfigVersion(@NotNull MessageSettings messageSettings) {
        if (messageSettings.configVersion() == null) {
            return false;
        }
        return messageSettings.configVersion() == CONFIG_VERSION;
    }
}
