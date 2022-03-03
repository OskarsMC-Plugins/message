package com.oskarsmc.message.configuration;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.oskarsmc.message.util.VersionUtils;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * The settings container class for the message plugin.
 */
public final class MessageSettings {
    private final Path dataFolder;
    private final Path file;

    private String messageSentMiniMessage;
    private String messageReceivedMiniMessage;
    private String messageSocialSpyMiniMessage;

    private List<String> messageAlias;
    private List<String> replyAlias;
    private List<String> socialSpyAlias;

    private boolean luckpermsIntegration;
    private boolean selfMessageSending;

    private final double configVersion;
    private boolean enabled;

    /**
     * Construct message settings.
     * @param dataFolder Data Folder
     * @param logger Logger
     */
    @Inject
    public MessageSettings(@DataDirectory @NotNull Path dataFolder, Logger logger) {
        this.dataFolder = dataFolder;
        this.file = this.dataFolder.resolve("config.toml");

        saveDefaultConfig();
        Toml toml = loadConfig();

        this.enabled = toml.getBoolean("plugin.enabled");

        // Version
        this.configVersion = toml.getDouble("developer-info.config-version");

        if (!VersionUtils.isLatestConfigVersion(this)) {
            logger.warn("Your Config is out of date (Latest: {}, Config Version: {})!", VersionUtils.CONFIG_VERSION, this.configVersion());
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
        if (!Files.exists(dataFolder)) {
            try {
                Files.createDirectory(file);
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }

        try (InputStream in = MessageSettings.class.getResourceAsStream("/config.toml")) {
            assert in != null;
            Files.copy(in, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Contract(" -> new")
    private @NotNull Path configFile() {
        return this.dataFolder.resolve("config.toml");
    }

    private Toml loadConfig() {
        return new Toml().read(configFile().toFile());
    }

    /**
     * Get the MiniMessage markup of the senders sent message.
     *
     * @return The MiniMessage markup of the senders sent message.
     */
    public String messageSentMiniMessage() {
        return this.messageSentMiniMessage;
    }

    /**
     * Get the MiniMessage markup of the recipients received message.
     *
     * @return The MiniMessage markup of the recipients received message.
     */
    public String messageReceivedMiniMessage() {
        return this.messageReceivedMiniMessage;
    }

    /**
     * Get if the plugin is enabled.
     * @return If the plugin is enabled.
     */
    @Pure
    public boolean enabled() {
        return this.enabled;
    }

    /**
     * Set if the plugin is enabled.
     *
     * @param enabled If the plugin is enabled.
     */
    public void enabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Get luckperms integration.
     *
     * @return Luckperms integration.
     */
    @Pure
    public boolean luckpermsIntegration() {
        return this.luckpermsIntegration;
    }

    /**
     * Set luckperms integration.
     *
     * @param luckpermsIntegration Luckperms integration
     */
    public void luckpermsIntegration(boolean luckpermsIntegration) {
        this.luckpermsIntegration = luckpermsIntegration;
    }

    /**
     * Get if the config allows players sending messages to themselves.
     *
     * @return If the config allows players sending messages to themselves.
     */
    @Pure
    public boolean selfMessageSending() {
        return selfMessageSending;
    }

    /**
     * Get the configuration version.
     *
     * @return The configuration version.
     */
    @Pure
    public Double configVersion() {
        return configVersion;
    }

    /**
     * Get the MiniMessage markup of the socialspy message.
     *
     * @return The MiniMessage markup of the socialspy message.
     */
    @Pure
    public String messageSocialSpyMiniMessage() {
        return messageSocialSpyMiniMessage;
    }

    /**
     * Get the aliases of the message command.
     *
     * @return The aliases of the message command.
     */
    @Pure
    public List<String> messageAliases() {
        return messageAlias;
    }

    /**
     * Get the aliases of the socialspy command.
     *
     * @return The aliases of the socialspy command.
     */
    @Pure
    public List<String> socialSpyAliases() {
        return socialSpyAlias;
    }

    /**
     * Get the aliases of the reply command.
     *
     * @return The aliases of the reply command.
     */
    @Pure
    public List<String> replyAliases() {
        return replyAlias;
    }
}
