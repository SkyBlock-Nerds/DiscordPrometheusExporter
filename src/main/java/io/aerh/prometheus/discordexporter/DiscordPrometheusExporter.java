package io.aerh.prometheus.discordexporter;

import io.aerh.prometheus.discordexporter.listener.guild.GuildListener;
import io.aerh.prometheus.discordexporter.metric.MetricRepository;
import io.aerh.prometheus.discordexporter.metric.impl.guild.GuildMemberCountMetric;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;


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

        try {
            jda.awaitReady();
            System.out.println("Bot is ready!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DiscordPrometheusExporter();
    }
}