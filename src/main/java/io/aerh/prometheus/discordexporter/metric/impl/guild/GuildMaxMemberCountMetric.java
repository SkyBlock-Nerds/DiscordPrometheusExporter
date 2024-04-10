package io.aerh.prometheus.discordexporter.metric.impl.guild;

import io.aerh.prometheus.discordexporter.metric.Metric;
import io.prometheus.metrics.core.metrics.Gauge;

public class GuildMaxMemberCountMetric extends Metric {
    private static final String NAME = "discord_guild_max_member_count";
    private static final String DESCRIPTION = "The maximum number of members in a Discord guild";
    private static final String HELP = "The maximum number of members in a Discord guild";
    private static final String[] LABELS = {"guild_id", "guild_name"};
    
    private Gauge gauge;

    public GuildMaxMemberCountMetric() {
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
