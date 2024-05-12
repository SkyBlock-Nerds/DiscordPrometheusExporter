package io.aerh.prometheus.discordexporter.metric.impl.jda;

import io.aerh.prometheus.discordexporter.metric.Metric;
import io.prometheus.metrics.core.metrics.Counter;

public class JDAEventCountMetric extends Metric {

    private static final String NAME = "discord_jda_event_count";
    private static final String DESCRIPTION = "The number of JDA events that have been received";
    private static final String[] LABELS = {"event_name"};

    private Counter counter;

    public JDAEventCountMetric() {
        super(NAME, DESCRIPTION, "", LABELS);
    }

    @Override
    protected void initialize() {
        counter = Counter.builder()
                .name(name)
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
