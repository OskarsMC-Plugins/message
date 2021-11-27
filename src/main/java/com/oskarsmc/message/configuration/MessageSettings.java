package com.oskarsmc.message.configuration;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.oskarsmc.message.util.VersionUtils;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class MessageSettings {
    private final File dataFolder;
    private final File file;

    private String messageSentMiniMessage;
    private String messageReceivedMiniMessage;
    private String messageSocialSpyMiniMessage;

    private List<String> messageAlias;
    private List<String> replyAlias;
    private List<String> socialSpyAlias;

    private boolean luckpermsIntegration;
    private boolean selfMessageSending;

    private final Double configVersion;
    private boolean enabled;

    @Inject
    public MessageSettings(@DataDirectory @NotNull Path dataFolder, Logger logger) {
        this.dataFolder = dataFolder.toFile();
        this.file = new File(this.dataFolder, "config.toml");

        saveDefaultConfig();
        Toml toml = loadConfig();

        this.enabled = toml.getBoolean("plugin.enabled");
        
        // Version
        this.configVersion = toml.getDouble("developer-info.config-version");

        if (!VersionUtils.isLatestConfigVersion(this)) {
            logger.warn("Your Config is out of date (Latest: " + VersionUtils.CONFIG_VERSION + ", Config Version: " + this.configVersion() + ")!");
            logger.warn("Please backup your current config.toml, and delete the current one. A new config will then be created on the next proxy launch.");
            logger.warn("The plugin's functionality will not be enabled until the config is updated.");
            this.enabled(false);
            return;
        }

        // Plugin Features
        this.luckpermsIntegration = toml.getBoolean("plugin.luckperms-integration");
        this.selfMessageSending = toml.getBoolean("plugin.allow-self-message-sending");

        // Messages - Message
        this.messageSentMiniMessage = toml.getString("messages.message-sent");
        this.messageReceivedMiniMessage = toml.getString("messages.message-received");
        this.messageSocialSpyMiniMessage = toml.getString("messages.message-socialspy");

        // Aliases
        this.messageAlias = toml.getList("aliases.message");
        this.replyAlias = toml.getList("aliases.reply");
        this.socialSpyAlias = toml.getList("aliases.socialspy");
    }

    private void saveDefaultConfig() {
        //noinspection ResultOfMethodCallIgnored
        dataFolder.mkdir();
        if (file.exists()) return;

        try (InputStream in = MessageSettings.class.getResourceAsStream("/config.toml")) {
            assert in != null;
            Files.copy(in, file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Contract(" -> new")
    private @NotNull File configFile() {
        return new File(dataFolder, "config.toml");
    }

    private Toml loadConfig() {
        return new Toml().read(configFile());
    }

    public String messageSentMiniMessage() {
        return messageSentMiniMessage;
    }

    public String messageReceivedMiniMessage() {
        return messageReceivedMiniMessage;
    }

    @Pure
    public boolean enabled() {
        return enabled;
    }

    public void enabled(boolean enabled) {
 this.enabled = enabled;
    }

    @Pure
    public boolean luckpermsIntegration() { return luckpermsIntegration; }

    public void luckpermsIntegration(boolean luckpermsIntegration) { this.luckpermsIntegration = luckpermsIntegration; }

    @Pure
    public boolean selfMessageSending() { return selfMessageSending; }

    @Pure
    public Double configVersion() {
        return configVersion;
    }

    @Pure
    public String messageSocialSpyMiniMessage() {
        return messageSocialSpyMiniMessage;
    }

    @Pure
    public List<String> messageAliases() {
        return messageAlias;
    }

    @Pure
    public List<String> socialSpyAliases() {
        return socialSpyAlias;
    }

    @Pure
    public List<String> replyAliases() {
        return replyAlias;
    }
}
