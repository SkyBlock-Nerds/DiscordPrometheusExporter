package io.aerh.prometheus.discordexporter.listener.jda;

import io.aerh.prometheus.discordexporter.metric.MetricRepository;
import net.dv8tion.jda.api.events.GatewayPingEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class JDAListener extends ListenerAdapter {

    @SubscribeEvent
    public void onGatewayPing(GatewayPingEvent event) {
        MetricRepository.getInstance().getMetric("discord_gateway_ping").set(event.getNewPing());
    }

    @SubscribeEvent
    public void onGenericEvent(GenericEvent event) {
        MetricRepository.getInstance().getMetric("discord_jda_event_count").increment(1, event.getClass().getSimpleName());
    }
}
