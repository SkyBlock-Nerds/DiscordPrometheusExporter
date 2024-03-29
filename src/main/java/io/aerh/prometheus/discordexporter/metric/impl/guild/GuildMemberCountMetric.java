package io.aerh.prometheus.discordexporter.metric.impl.guild;

import io.aerh.prometheus.discordexporter.metric.Metric;
import io.prometheus.client.Gauge;

public class GuildMemberCountMetric extends Metric {
    private static final String NAME = "discord_guild_member_count";
    private static final String DESCRIPTION = "The number of members in a Discord guild";
    private static final String HELP = "Example help text for the Discord guild count metric";
    private static final String[] LABELS = {"guild_id", "guild_name"};

    private Gauge gauge;

    public GuildMemberCountMetric() {
        super(NAME, DESCRIPTION, HELP, LABELS);
    }

    @Override
    protected void initialize() {
        gauge = Gauge.build()
                .name(name)
                .help(help)
                .labelNames(labels)
                .register(getRegistry());
    }

    @Override
    public void set(double value, String... labelValues) {
        gauge.labels(labelValues).set(value);
    }

    @Override
    public void increment(double value, String... labelValues) {
        gauge.labels(labelValues).inc(value);
    }

    @Override
    public void decrement(double value, String... labelValues) {
        gauge.labels(labelValues).dec(value);
    }
}