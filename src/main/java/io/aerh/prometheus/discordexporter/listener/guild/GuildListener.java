package io.aerh.prometheus.discordexporter.listener.guild;

import io.aerh.prometheus.discordexporter.metric.MetricRepository;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class GuildListener extends ListenerAdapter {

    @SubscribeEvent
    public void onGuildReady(GuildReadyEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_count").set(event.getGuild().getMemberCount(), event.getGuild().getId(), event.getGuild().getName());
        System.out.println("Set guild count for guild " + event.getGuild().getName() + " to " + event.getGuild().getMemberCount() + " members");
    }

    @SubscribeEvent
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_count").increment(1, event.getGuild().getId(), event.getGuild().getName());
        System.out.println("Incremented guild count for guild " + event.getGuild().getName());
    }

    @SubscribeEvent
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_count").decrement(1, event.getGuild().getId(), event.getGuild().getName());
        System.out.println("Decremented guild count for guild " + event.getGuild().getName());
    }
}
