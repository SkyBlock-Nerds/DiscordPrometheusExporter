package io.aerh.prometheus.discordexporter.metric;

public abstract class Metric {
    protected final String name;
    protected final String description;
    protected final String help;
    protected final String[] labels;

    public Metric(String name, String description, String help, String... labels) {
        this.name = name;
        this.description = description;
        this.help = help;
        this.labels = labels;
        initialize();
    }

    protected abstract void initialize();

    public abstract void set(double value, String... labelValues);

    public abstract void increment(double value, String... labelValues);

    public abstract void decrement(double value, String... labelValues);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHelp() {
        return help;
    }

    public String[] getLabels() {
        return labels;
    }
}
