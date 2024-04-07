package io.aerh.prometheus.discordexporter;

import io.aerh.prometheus.discordexporter.listener.guild.GuildListener;
import io.aerh.prometheus.discordexporter.listener.jda.JDAListener;
import io.aerh.prometheus.discordexporter.metric.MetricRepository;
import io.aerh.prometheus.discordexporter.metric.impl.guild.GuildMemberCountMetric;
import io.aerh.prometheus.discordexporter.metric.impl.jda.JDAEventCountMetric;
import io.aerh.prometheus.discordexporter.metric.impl.jda.JDAGatewayPingMetric;
import io.github.cdimascio.dotenv.Dotenv;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

import java.io.IOException;


public class DiscordPrometheusExporter {

    public DiscordPrometheusExporter() {
        start();
    }

    private void start() {
        Dotenv dotenv = Dotenv.load();

        JvmMetrics.builder().register();
        MetricRepository.getInstance().addMetric(new JDAGatewayPingMetric());
        MetricRepository.getInstance().addMetric(new JDAEventCountMetric());
        MetricRepository.getInstance().addMetric(new GuildMemberCountMetric());

        try (HTTPServer server = HTTPServer.builder().port(Integer.parseInt(dotenv.get("PROMETHEUS_PORT", "8080"))).buildAndStart()) {
            System.out.println("HTTP Server listening on port " + server.getPort());

            JDA jda = JDABuilder.createDefault(dotenv.get("DISCORD_BOT_TOKEN"))
                    .setEventManager(new AnnotatedEventManager())
                    .addEventListeners(new JDAListener())
                    .addEventListeners(new GuildListener())
                    .build();

            jda.awaitReady();
            Thread.currentThread().join();
        } catch (IOException exception) {
            System.err.println("Failed to start HTTP server");
            System.exit(-1);
        } catch (InterruptedException exception) {
            System.err.println("Thread was interrupted");
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        new DiscordPrometheusExporter();
    }
}