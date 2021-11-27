package com.oskarsmc.message.logic;

import com.google.inject.Inject;
import com.oskarsmc.message.Message;
import com.oskarsmc.message.event.MessageEvent;
import com.oskarsmc.message.util.StatsUtils;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import org.bstats.charts.SingleLineChart;
import org.bstats.velocity.Metrics;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Metrics for message.
 */
public final class MessageMetrics {
    private final AtomicInteger messagesSent = new AtomicInteger();

    /**
     * Initialise BStats Metrics
     * @param plugin Message Plugin
     * @param metricsFactory Metrics Factory
     * @param proxyServer Proxy Server
     */
    @Inject
    public MessageMetrics(Message plugin, Metrics.@NotNull Factory metricsFactory, @NotNull ProxyServer proxyServer) {
        proxyServer.getEventManager().register(plugin, this);
        Metrics metrics = metricsFactory.make(plugin, StatsUtils.PLUGIN_ID);

        metrics.addCustomChart(new SingleLineChart("messages_sent", () -> {
            int ret = messagesSent.get();
            messagesSent.set(0);
            return ret;
        }));
    }

    @Subscribe
    private void onMessageEvent(MessageEvent event) {
        messagesSent.incrementAndGet();
    }
}
