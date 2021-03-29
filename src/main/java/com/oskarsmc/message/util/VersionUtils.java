package com.oskarsmc.message.util;

import com.oskarsmc.message.configuration.MessageSettings;

public class VersionUtils {
    public static final double CONFIG_VERSION = 0.1;

    public static boolean isLatestConfigVersion(MessageSettings messageSettings) {
        if (messageSettings.getConfigVersion() == null) {
            return false;
        }
        return messageSettings.getConfigVersion() == CONFIG_VERSION;
    }
}
