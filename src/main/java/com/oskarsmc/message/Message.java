package com.oskarsmc.message;

import com.google.inject.Inject;
import com.oskarsmc.message.command.MessageBrigadier;
import com.oskarsmc.message.command.SocialSpyBrigadier;
import com.oskarsmc.message.configuration.MessageSettings;
import com.oskarsmc.message.util.VersionUtils;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "message",
        name = "Message",
        version = "0.1.0",
        description = "Cross Server Messaging Platform for Velocity",
        url = "https://software.oskarsmc.com/",
        authors = {"OskarsMC", "OskarZyg"}
)
public class Message {

    @Inject
    private Logger logger;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private @DataDirectory
    Path dataDirectory;

    private MessageSettings messageSettings;
    private MessageBrigadier messageBrigadier;
    private SocialSpyBrigadier socialSpyBrigadier;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.messageSettings = new MessageSettings(dataDirectory.toFile());

        if (!VersionUtils.isLatestConfigVersion(messageSettings)) {
            logger.warn("Your Config is out of date (Latest: " + VersionUtils.CONFIG_VERSION + ", Config Version: " + messageSettings.getConfigVersion() + ")!");
            logger.warn("Please backup your current config.toml, and delete the current one. A new config will then be created on the next proxy launch.");
            logger.warn("The plugin's functionality will not be enabled until the config is updated.");
            messageSettings.setEnabled(false);
        }

        if (messageSettings.isEnabled()) {
            messageBrigadier = new MessageBrigadier(this.proxyServer, this.messageSettings);
            socialSpyBrigadier = new SocialSpyBrigadier(this.proxyServer, this.messageSettings);
            proxyServer.getEventManager().register(this, socialSpyBrigadier);
        }
    }
}
