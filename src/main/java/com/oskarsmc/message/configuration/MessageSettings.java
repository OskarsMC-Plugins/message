package com.oskarsmc.message.configuration;

import com.moandjiezana.toml.Toml;
import com.oskarsmc.message.util.VersionUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class MessageSettings {
    private final File dataFolder;
    private final File file;

    private final String messageSentMiniMessage;
    private final String messageReceivedMiniMessage;
    private final String messageSocialSpyMiniMessage;

    private final List<String> messageAlias;
    private final List<String> replyAlias;
    private final List<String> socialSpyAlias;

    private final Component noPermissionComponent;
    private final Component messagePlayerNotFoundComponent;
    private final Component messageUsageComponent;

    private final Component replyNoPlayerFoundComponent;
    private final Component replyUsageComponent;

    private final Double configVersion;
    private boolean enabled;

    public MessageSettings(File dataFolder, Logger logger) {
        this.dataFolder = dataFolder;
        this.file = new File(this.dataFolder, "config.toml");

        saveDefaultConfig();
        Toml toml = loadConfig();

        this.enabled = toml.getBoolean("plugin.enabled");
        
        // Version
        this.configVersion = toml.getDouble("developer-info.config-version");

        if (!VersionUtils.isLatestConfigVersion(this)) {
            logger.warn("Your Config is out of date (Latest: " + VersionUtils.CONFIG_VERSION + ", Config Version: " + this.getConfigVersion() + ")!");
            logger.warn("Please backup your current config.toml, and delete the current one. A new config will then be created on the next proxy launch.");
            logger.warn("The plugin's functionality will not be enabled until the config is updated.");
            this.setEnabled(false);
        }

        // Messages
        this.messageSentMiniMessage = toml.getString("messages.message-sent");
        this.messageReceivedMiniMessage = toml.getString("messages.message-received");
        this.messageSocialSpyMiniMessage = toml.getString("messages.message-socialspy");

        // Aliases
        this.messageAlias = toml.getList("aliases.message");
        this.replyAlias = toml.getList("aliases.reply");
        this.socialSpyAlias = toml.getList("aliases.socialspy");

        // Errors - General
        this.noPermissionComponent = MiniMessage.get().parse(toml.getString("error-messages.no-permission"));

        // Errors - Message
        this.messagePlayerNotFoundComponent = MiniMessage.get().parse(toml.getString("error-messages.player-not-found"));
        this.messageUsageComponent = MiniMessage.get().parse(toml.getString("error-messages.message-usage"));

        // Errors - Reply
        this.replyNoPlayerFoundComponent = MiniMessage.get().parse(toml.getString("error-messages.reply-no-player-found"));
        this.replyUsageComponent = MiniMessage.get().parse(toml.getString("error-messages.reply-usage"));
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

    public List<String> getMessageAlias() {
        return messageAlias;
    }

    public List<String> getSocialSpyAlias() {
        return socialSpyAlias;
    }

    public List<String> getReplyAlias() {
        return replyAlias;
    }

    public Component getNoPermissionComponent() {
        return noPermissionComponent;
    }

    public Component getMessagePlayerNotFoundComponent() {
        return messagePlayerNotFoundComponent;
    }

    public Component getReplyNoPlayerFoundComponent() {
        return replyNoPlayerFoundComponent;
    }

    public Component getMessageUsageComponent() {
        return messageUsageComponent;
    }

    public Component getReplyUsageComponent() {
        return replyUsageComponent;
    }
}
