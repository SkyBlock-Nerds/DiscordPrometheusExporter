package io.aerh.prometheus.discordexporter;

import io.aerh.prometheus.discordexporter.listener.guild.GuildListener;
import io.aerh.prometheus.discordexporter.metric.MetricRepository;
import io.aerh.prometheus.discordexporter.metric.impl.guild.GuildMemberCountMetric;
import io.github.cdimascio.dotenv.Dotenv;
import io.prometheus.client.exporter.HTTPServer;
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
        JDA jda = JDABuilder.createDefault(dotenv.get("DISCORD_BOT_TOKEN"))
                .setEventManager(new AnnotatedEventManager())
                .addEventListeners(new GuildListener())
                .build();

        MetricRepository.getInstance().addMetric(new GuildMemberCountMetric());

        try (HTTPServer server = new HTTPServer.Builder().withPort(1234).build()) {
            System.out.println("Started HTTP server on port 1234");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.close();
                jda.shutdown();
            }));

            jda.awaitReady();
            System.out.println("Bot is ready!");
        } catch (IOException exception) {
            System.err.println("Failed to start HTTP server");
            System.exit(-1);
        } catch (InterruptedException exception) {
            System.err.println("Failed to start bot");
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        new DiscordPrometheusExporter();
    }
}