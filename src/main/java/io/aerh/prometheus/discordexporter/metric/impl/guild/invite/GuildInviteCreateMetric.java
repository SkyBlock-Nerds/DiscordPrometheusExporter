package io.aerh.prometheus.discordexporter.metric.impl.guild.invite;

import io.aerh.prometheus.discordexporter.metric.Metric;
import io.prometheus.metrics.core.metrics.Counter;

public class GuildInviteCreateMetric extends Metric {
    private static final String NAME = "discord_guild_invites_created";
    private static final String DESCRIPTION = "The number of invites created in a Discord guild";
    private static final String HELP = "The number of invites created in a Discord guild";
    private static final String[] LABELS = {
            "guild_id",
            "guild_name",
            "channel_id",
            "channel_name",
            "invite_code",
            "inviter_id",
            "inviter_name",
            "max_age",
            "max_uses",
            "is_temporary"
    };

    private Counter counter;

    public GuildInviteCreateMetric() {
        super(NAME, DESCRIPTION, HELP, LABELS);
    }

    @Override
    protected void initialize() {
        counter = Counter.builder()
                .name(name)
                .help(help)
                .labelNames(labels)
                .register();
    }

    @Override
    public void set(double value, String... labelValues) {
        throw new UnsupportedOperationException("Setting is not supported for Counter metrics");
    }

    @Override
    public void increment(double value, String... labelValues) {
        counter.labelValues(labelValues).inc(value);
    }

    @Override
    public void decrement(double value, String... labelValues) {
        throw new UnsupportedOperationException("Decrementing is not supported for Counter metrics");
    }
}
