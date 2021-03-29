package com.oskarsmc.message.configuration;

import com.moandjiezana.toml.Toml;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class MessageSettings {
    private final File dataFolder;
    private final File file;

    private final String messageSentMiniMessage;
    private final String messageReceivedMiniMessage;
    private final String messageSocialSpyMiniMessage;
    private final Double configVersion;
    private boolean enabled;

    public MessageSettings(File dataFolder) {
        this.dataFolder = dataFolder;
        this.file = new File(this.dataFolder, "config.toml");

        saveDefaultConfig();
        Toml toml = loadConfig();

        this.enabled = toml.getBoolean("plugin.enabled");

        this.messageSentMiniMessage = toml.getString("messages.message-sent");
        this.messageReceivedMiniMessage = toml.getString("messages.message-received");
        this.messageSocialSpyMiniMessage = toml.getString("messages.message-socialspy");

        this.configVersion = toml.getDouble("developer-info.config-version");
    }

    private void saveDefaultConfig() {
        if (!dataFolder.exists()) dataFolder.mkdir();
        if (file.exists()) return;

        try (InputStream in = MessageSettings.class.getResourceAsStream("/config.toml")) {
            Files.copy(in, file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getConfigFile() {
        return new File(dataFolder, "config.toml");
    }

    private Toml loadConfig() {
        return new Toml().read(getConfigFile());
    }

    public String getMessageSentMiniMessage() {
        return messageSentMiniMessage;
    }

    public String getMessageReceivedMiniMessage() {
        return messageReceivedMiniMessage;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
 this.enabled = enabled;
    }

    public Double getConfigVersion() {
        return configVersion;
    }

    public String getMessageSocialSpyMiniMessage() {
        return messageSocialSpyMiniMessage;
    }
}
