package io.github.bennyboy1695.betterdiscordbridge.utils;

import io.github.bennyboy1695.betterdiscordbridge.BetterDiscordBridge;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordMethods {

    private final BetterDiscordBridge bridge;
    private final long guildID;
    private final Pattern mentionsRegex = Pattern.compile("@([^\\W]+)");
    private final Pattern mentionsSpace = Pattern.compile("@'([\\s^\\w]+)'");

    public DiscordMethods(BetterDiscordBridge bridge, long guildID) {
        this.bridge = bridge;
        this.guildID = guildID;
    }

    public void sendMessage(JDA jda , long channelID, String message) {
        TextChannel textChannel = jda.getTextChannelById(channelID);
        if (textChannel == null) {
            return;
        }

        Guild guild = jda.getGuildById(guildID);

        Matcher mentions = mentionsSpace.matcher(message);
        while(mentions.find()) {
            String name = mentions.group().replaceAll("'", "");
            List<Member> members = guild.getMembersByEffectiveName(name.replace("@", ""), true);
            if (!members.isEmpty()) {
                message = message.replace(mentions.group(), members.get(0).getAsMention());
            }
        }

        mentions = mentionsRegex.matcher(message);
        while(mentions.find()) {
            List<Member> members = guild.getMembersByEffectiveName(mentions.group().replace("@", ""), true);
            if (!members.isEmpty()) {
                message = message.replace(mentions.group(), members.get(0).getAsMention());
            }
        }

        textChannel.sendMessage(message).queue();
    }

    public void doShutdown() {
        try {
            bridge.getJDA().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAutoDeleteMessage(JDA jda, long channelID, Object message, long seconds) {
        TextChannel textChannel = jda.getTextChannelById(channelID);
        if (textChannel == null) {
            return;
        }
        textChannel.sendMessage((String) message).queue(sentMsg -> sentMsg.delete().queueAfter(seconds, TimeUnit.SECONDS));
    }
}
