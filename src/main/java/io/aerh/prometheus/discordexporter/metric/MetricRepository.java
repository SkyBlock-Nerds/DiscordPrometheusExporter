package io.aerh.prometheus.discordexporter.metric;

import java.util.ArrayList;
import java.util.List;

public class MetricRepository {
    private static final MetricRepository instance = new MetricRepository();
    private final List<Metric> metrics;

    private MetricRepository() {
        metrics = new ArrayList<>();
    }

    public static MetricRepository getInstance() {
        return instance;
    }

    public void addMetric(Metric metric) {
        metrics.add(metric);
    }

    public Metric getMetric(String name) {
        return metrics.stream()
                .filter(metric -> metric.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No metric with name \"" + name + "\" found"));
    }
}
