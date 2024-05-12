package io.aerh.prometheus.discordexporter.metric.impl.jda;

import io.aerh.prometheus.discordexporter.metric.Metric;
import io.prometheus.metrics.core.metrics.Gauge;

public class JDAGatewayPingMetric extends Metric {

    private static final String NAME = "discord_gateway_ping";
    private static final String DESCRIPTION = "The value in milliseconds of the last received Discord Gateway ping";
    private static final String HELP = "Value, in milliseconds, of the last received Discord Gateway ping";

    private Gauge gauge;

    public JDAGatewayPingMetric() {
        super(NAME, DESCRIPTION, HELP);
    }

    @Override
    protected void initialize() {
        gauge = Gauge.builder()
                .name(name)
                .help(help)
                .register();
    }

    @Override
    public void set(double value, String... labelValues) {
        gauge.set(value);
    }

    @Override
    public void increment(double value, String... labelValues) {
        gauge.inc(value);
    }

    @Override
    public void decrement(double value, String... labelValues) {
        gauge.dec(value);
    }
}
