package io.aerh.prometheus.discordexporter;

import io.aerh.prometheus.discordexporter.listener.guild.GuildListener;
import io.aerh.prometheus.discordexporter.listener.jda.JDAListener;
import io.aerh.prometheus.discordexporter.metric.MetricRepository;
import io.aerh.prometheus.discordexporter.metric.impl.guild.invite.GuildInviteCreateMetric;
import io.aerh.prometheus.discordexporter.metric.impl.guild.invite.GuildInviteDeleteMetric;
import io.aerh.prometheus.discordexporter.metric.impl.guild.moderation.GuildBanCountMetric;
import io.aerh.prometheus.discordexporter.metric.impl.guild.GuildBoostStatusMetric;
import io.aerh.prometheus.discordexporter.metric.impl.guild.moderation.GuildExplicitContentLevelMetric;
import io.aerh.prometheus.discordexporter.metric.impl.guild.member.GuildMaxMemberCountMetric;
import io.aerh.prometheus.discordexporter.metric.impl.guild.member.GuildMemberCountMetric;
import io.aerh.prometheus.discordexporter.metric.impl.guild.member.GuildMemberStatusCountMetric;
import io.aerh.prometheus.discordexporter.metric.impl.guild.moderation.GuildVerificationLevelMetric;
import io.aerh.prometheus.discordexporter.metric.impl.jda.JDAEventCountMetric;
import io.aerh.prometheus.discordexporter.metric.impl.jda.JDAExceptionMetric;
import io.aerh.prometheus.discordexporter.metric.impl.jda.JDAGatewayPingMetric;
import io.github.cdimascio.dotenv.Dotenv;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.IOException;
import java.util.Arrays;


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
        MetricRepository.getInstance().addMetric(new GuildMemberStatusCountMetric());
        MetricRepository.getInstance().addMetric(new GuildMaxMemberCountMetric());
        MetricRepository.getInstance().addMetric(new GuildBanCountMetric());
        MetricRepository.getInstance().addMetric(new GuildBoostStatusMetric());
        MetricRepository.getInstance().addMetric(new GuildVerificationLevelMetric());
        MetricRepository.getInstance().addMetric(new GuildExplicitContentLevelMetric());
        MetricRepository.getInstance().addMetric(new GuildInviteCreateMetric());
        MetricRepository.getInstance().addMetric(new GuildInviteDeleteMetric());
        MetricRepository.getInstance().addMetric(new JDAExceptionMetric());

        try (HTTPServer server = HTTPServer.builder().port(Integer.parseInt(dotenv.get("PROMETHEUS_PORT", "8080"))).buildAndStart()) {
            System.out.println("HTTP Server listening on port " + server.getPort());

            JDA jda = JDABuilder.createDefault(dotenv.get("DISCORD_BOT_TOKEN"))
                    .setEnabledIntents(Arrays.stream(GatewayIntent.values()).toList())
                    .enableCache(CacheFlag.ONLINE_STATUS)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
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