package com.oskarsmc.message;

import com.google.inject.Inject;
import com.oskarsmc.message.command.MessageBrigadier;
import com.oskarsmc.message.command.ReplyBrigadier;
import com.oskarsmc.message.command.SocialSpyBrigadier;
import com.oskarsmc.message.configuration.MessageSettings;
import com.oskarsmc.message.event.MessageEvent;
import com.oskarsmc.message.util.StatsUtils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.bstats.charts.SingleLineChart;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Inject
    private Metrics.Factory metricsFactory;

    private AtomicInteger messagesSent = new AtomicInteger(0);

    private MessageSettings messageSettings;
    private MessageBrigadier messageBrigadier;
    private SocialSpyBrigadier socialSpyBrigadier;
    private ReplyBrigadier replyBrigadier;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.messageSettings = new MessageSettings(dataDirectory.toFile(), logger);

        Metrics metrics = metricsFactory.make(this, StatsUtils.PLUGIN_ID);
        metrics.addCustomChart(new SingleLineChart("messages_sent", new Callable<Integer>() {
            @Override
            public Integer call() {
                int ret = messagesSent.get();
                messagesSent.set(0);
                return ret;
            }
        }));

        if (messageSettings.isEnabled()) {
            this.messageBrigadier = new MessageBrigadier(this.proxyServer, this.messageSettings);
            this.socialSpyBrigadier = new SocialSpyBrigadier(this.proxyServer, this.messageSettings);
            this.replyBrigadier = new ReplyBrigadier(this.proxyServer, this.messageSettings);

            this.proxyServer.getEventManager().register(this, this.messageBrigadier);
            this.proxyServer.getEventManager().register(this, this.socialSpyBrigadier);
            this.proxyServer.getEventManager().register(this, this.replyBrigadier);
        }
    }

    @Subscribe
    public void messageEvent(MessageEvent event) {
        messagesSent.incrementAndGet();
    }
}
