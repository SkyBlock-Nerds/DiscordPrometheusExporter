package io.aerh.prometheus.discordexporter.listener.guild;

import io.aerh.prometheus.discordexporter.metric.MetricRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostTierEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateExplicitContentLevelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMaxMembersEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateVerificationLevelEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class GuildListener extends ListenerAdapter {

    @SubscribeEvent
    public void onGuildReady(GuildReadyEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_member_count").set(event.getGuild().getMemberCount(), event.getGuild().getId(), event.getGuild().getName());
        System.out.println("Set guild count for guild " + event.getGuild().getName() + " to " + event.getGuild().getMemberCount() + " members");

        MetricRepository.getInstance().getMetric("discord_guild_max_member_count").set(event.getGuild().getMaxMembers(), event.getGuild().getId(), event.getGuild().getName());
        System.out.println("Set max member count for guild " + event.getGuild().getName() + " to " + event.getGuild().getMaxMembers() + " members");

        event.getGuild().loadMembers().onSuccess(members -> {
            members.stream().collect(Collectors.groupingBy(Member::getOnlineStatus)).forEach((status, memberList) -> {
                MetricRepository.getInstance().getMetric("discord_guild_member_status_count").set(
                        memberList.size(),
                        event.getGuild().getId(),
                        event.getGuild().getName(),
                        status.getKey()
                );
                System.out.println("Set status count for " + status.getKey() + " to " + memberList.size() + " members");
            });
        });

        event.getGuild().retrieveBanList().queue(banList -> {
            MetricRepository.getInstance().getMetric("discord_guild_ban_count").set(banList.size(), event.getGuild().getId(), event.getGuild().getName());
            System.out.println("Set ban count for guild " + event.getGuild().getName() + " to " + banList.size() + " bans");
        });

        MetricRepository.getInstance().getMetric("discord_guild_boost_status").set(event.getGuild().getBoostCount(), event.getGuild().getId(), event.getGuild().getName(), String.valueOf(event.getGuild().getBoostTier().getKey()));
        System.out.println("Set boost status for guild " + event.getGuild().getName() + " to " + event.getGuild().getBoostCount() + " boosts (Tier: " + event.getGuild().getBoostTier() + ")");

        MetricRepository.getInstance().getMetric("discord_guild_verification_level").set(event.getGuild().getVerificationLevel().getKey(), event.getGuild().getId(), event.getGuild().getName());
        System.out.println("Set verification level for guild " + event.getGuild().getName() + " to " + event.getGuild().getVerificationLevel().getKey());

        MetricRepository.getInstance().getMetric("discord_guild_explicit_content_level").set(event.getGuild().getExplicitContentLevel().getKey(), event.getGuild().getId(), event.getGuild().getName());
        System.out.println("Set explicit content level for guild " + event.getGuild().getName() + " to " + event.getGuild().getExplicitContentLevel().getKey());
    }

    @SubscribeEvent
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_member_count").set(event.getGuild().getMemberCount(), event.getGuild().getId(), event.getGuild().getName());
        System.out.println("Incremented guild count for guild " + event.getGuild().getName());
    }

    @SubscribeEvent
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_member_count").set(event.getGuild().getMemberCount(), event.getGuild().getId(), event.getGuild().getName());
        System.out.println("Decremented guild count for guild " + event.getGuild().getName());
    }

    @SubscribeEvent
    public void onGuildUpdateMaxMembers(GuildUpdateMaxMembersEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_max_member_count").set(event.getNewValue(), event.getGuild().getId(), event.getGuild().getName());
        System.out.println("Set max member count for guild " + event.getGuild().getName() + " to " + event.getNewValue() + " members");
    }

    @SubscribeEvent
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_member_status_count").set(
                event.getGuild().getMembers().stream().filter(member -> member.getOnlineStatus() == event.getNewOnlineStatus()).count(),
                event.getGuild().getId(),
                event.getGuild().getName(),
                event.getNewOnlineStatus().getKey()
        );

        System.out.println("Set status count for " + event.getNewOnlineStatus().getKey() + " to " + event.getGuild().getMembers().stream().filter(member -> member.getOnlineStatus() == event.getNewOnlineStatus()).count() + " members");
    }

    @SubscribeEvent
    public void onGuildBan(GuildBanEvent event) {
        event.getGuild().retrieveBanList().queue(banList -> {
            MetricRepository.getInstance().getMetric("discord_guild_ban_count").set(banList.size(), event.getGuild().getId(), event.getGuild().getName());
        });
    }

    @SubscribeEvent
    public void onGuildUnban(GuildUnbanEvent event) {
        event.getGuild().retrieveBanList().queue(banList -> {
            MetricRepository.getInstance().getMetric("discord_guild_ban_count").set(banList.size(), event.getGuild().getId(), event.getGuild().getName());
        });
    }

    @SubscribeEvent
    public void onGuildUpdateBoostCount(GuildUpdateBoostCountEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_boost_status").set(event.getGuild().getBoostCount(), event.getGuild().getId(), event.getGuild().getName(), String.valueOf(event.getGuild().getBoostTier().getKey()));
    }

    @SubscribeEvent
    public void onGuildUpdateBoostTier(GuildUpdateBoostTierEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_boost_status").set(event.getGuild().getBoostCount(), event.getGuild().getId(), event.getGuild().getName(), String.valueOf(event.getNewValue().getKey()));
    }

    @SubscribeEvent
    public void onGuildUpdateVerificationLevel(GuildUpdateVerificationLevelEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_verification_level").set(event.getNewValue().getKey(), event.getGuild().getId(), event.getGuild().getName());
    }

    @SubscribeEvent
    public void onGuildUpdateExplicitContentLevel(GuildUpdateExplicitContentLevelEvent event) {
        MetricRepository.getInstance().getMetric("discord_guild_explicit_content_level").set(event.getNewValue().getKey(), event.getGuild().getId(), event.getGuild().getName());
    }

    @SubscribeEvent
    public void onGuildInviteCreate(GuildInviteCreateEvent event) {
        String guildId = event.getGuild().getId();
        String guildName = event.getGuild().getName();
        String channelId = event.getInvite().getChannel() == null ? "null" : event.getInvite().getChannel().getId();
        String channelName = event.getInvite().getChannel() == null ? "null" : event.getInvite().getChannel().getName();
        String inviteCode = event.getInvite().getCode();
        String inviterId = event.getInvite().getInviter() == null ? "null" : event.getInvite().getInviter().getId();
        String inviterName = event.getInvite().getInviter() == null ? "null" : event.getInvite().getInviter().getName();
        String maxAge = String.valueOf(event.getInvite().getMaxAge());
        String maxUses = String.valueOf(event.getInvite().getMaxUses());
        String isTemporary = String.valueOf(event.getInvite().isTemporary());

        MetricRepository.getInstance().getMetric("discord_guild_invites_created").increment(1, guildId, guildName, channelId, channelName, inviteCode, inviterId, inviterName, maxAge, maxUses, isTemporary);
        System.out.println("Incremented invite creation count for guild " + guildName + " by 1");
    }

    @SubscribeEvent
    public void onGuildInviteDelete(GuildInviteDeleteEvent event) {
        String guildId = event.getGuild().getId();
        String guildName = event.getGuild().getName();
        String channelId = event.getChannel().getId();
        String channelName = event.getChannel().getName();
        String inviteCode = event.getCode();

        MetricRepository.getInstance().getMetric("discord_guild_invites_deleted").increment(1, guildId, guildName, channelId, channelName, inviteCode);
        System.out.println("Incremented invite deletion count for guild " + guildName + " by 1");
    }
}
