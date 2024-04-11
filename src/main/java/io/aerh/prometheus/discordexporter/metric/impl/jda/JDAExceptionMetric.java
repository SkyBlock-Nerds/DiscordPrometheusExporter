package io.aerh.prometheus.discordexporter.metric.impl.jda;

import io.aerh.prometheus.discordexporter.metric.Metric;
import io.prometheus.metrics.core.metrics.Counter;

public class JDAExceptionMetric extends Metric {
    private static final String NAME = "discord_jda_exceptions";
    private static final String DESCRIPTION = "The number of exceptions thrown by JDA";
    private static final String HELP = "The number of exceptions thrown by JDA";
    private static final String[] LABELS = {"exception"};

    private Counter counter;

    public JDAExceptionMetric() {
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
