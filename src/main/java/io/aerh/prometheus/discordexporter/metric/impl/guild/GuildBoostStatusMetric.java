package io.aerh.prometheus.discordexporter.metric.impl.guild;

import io.aerh.prometheus.discordexporter.metric.Metric;
import io.prometheus.metrics.core.metrics.Gauge;

public class GuildBoostStatusMetric extends Metric {
    private static final String NAME = "discord_guild_boost_status";
    private static final String DESCRIPTION = "The status of boosts in a Discord guild";
    private static final String HELP = "The status of boosts in a Discord guild";
    private static final String[] LABELS = {"guild_id", "guild_name", "boost_tier"};

    private Gauge gauge;

    public GuildBoostStatusMetric() {
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
