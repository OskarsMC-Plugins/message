package com.oskarsmc.message.util;

/**
 * Dependency Checker
 */
public final class DependencyChecker {
    /**
     * Check if the luckperms api is present in the classpath.
     * @return If the luckperms api is present in the classpath.
     */
    public static boolean luckperms() {
        try {
            Class.forName("net.luckperms.api.LuckPerms");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    /**
     * Check if the miniplaceholders api is present in the classpath.
     * @return If the miniplaceholders api is present in the classpath.
     */
    public static boolean miniplaceholders() {
        try {
            Class.forName("io.github.miniplaceholders.api.MiniPlaceholders");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }
}
