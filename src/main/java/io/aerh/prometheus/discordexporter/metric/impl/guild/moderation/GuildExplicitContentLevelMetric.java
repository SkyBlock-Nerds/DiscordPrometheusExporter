package io.aerh.prometheus.discordexporter.metric.impl.guild.moderation;

import io.aerh.prometheus.discordexporter.metric.Metric;
import io.prometheus.metrics.core.metrics.Gauge;

public class GuildExplicitContentLevelMetric extends Metric {
    private static final String NAME = "discord_guild_explicit_content_level";
    private static final String DESCRIPTION = "The explicit content filter level of a Discord guild";
    private static final String HELP = "The explicit content filter level of a Discord guild";
    private static final String[] LABELS = {"guild_id", "guild_name"};

    private Gauge gauge;

    public GuildExplicitContentLevelMetric() {
        super(NAME, DESCRIPTION, HELP, LABELS);
    }

    @Override
    protected void initialize() {
        gauge = Gauge.builder()
                .name(name)
                .help(help)
                .labelNames(labels)
                .register();
    }

    @Override
    public void set(double value, String... labelValues) {
        gauge.labelValues(labelValues).set(value);
    }

    @Override
    public void increment(double value, String... labelValues) {
        gauge.labelValues(labelValues).inc(value);
    }

    @Override
    public void decrement(double value, String... labelValues) {
        gauge.labelValues(labelValues).dec(value);
    }
}
