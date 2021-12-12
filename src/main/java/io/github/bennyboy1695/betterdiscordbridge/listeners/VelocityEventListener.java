package io.github.bennyboy1695.betterdiscordbridge.listeners;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import io.github.bennyboy1695.betterdiscordbridge.BetterDiscordBridge;

import java.util.regex.Matcher;

public class VelocityEventListener {
    //h//
    private final BetterDiscordBridge bridge;

    public VelocityEventListener(BetterDiscordBridge bridge) {
        this.bridge = bridge;
    }

    @Subscribe
    public void onChat(PlayerChatEvent event) {
        String serverName;
        if (!bridge.getConfig().getUseConfigServerNames()) {
            serverName = bridge.getConfig().getServerNames(event.getPlayer().getCurrentServer().get().getServerInfo().getName());
        } else {
            serverName = event.getPlayer().getCurrentServer().get().getServerInfo().getName();
        }

        String message = bridge.getConfig().getFormats("discord_to")
                .replaceAll("\\{Server}", Matcher.quoteReplacement(serverName))
                .replaceAll("\\{User}", event.getPlayer().getUsername())
                .replaceAll("\\{Message}", event.getMessage());

        /*if(bridge.luckPermsHook != null) {
            message = bridge.luckPermsHook.replacePrefixSuffix(event.getPlayer().getUniqueId(), message);
        }*/

        if (!bridge.getConfig().getChatMode().equals("separated")) {
            bridge.discordMethods.sendMessage(bridge.getJDA(), bridge.getConfig().getChannels("global"), message);
        } else {
            bridge.discordMethods.sendMessage(bridge.getJDA(), bridge.getConfig().getChannels(serverName), message);
        }
    }

    @Subscribe
    public void onPlayerJoin(ServerConnectedEvent event) {
        if(event.getPreviousServer().get() == null) {
            return;
        }
        String serverName = event.getServer().getServerInfo().getName();

        String message = bridge.getConfig().getFormats("discord_join")
                .replaceAll("\\{Server}", Matcher.quoteReplacement(serverName))
                .replaceAll("\\{User}", event.getPlayer().getUsername());

        /*if(bridge.luckPermsHook != null) {
            message = bridge.luckPermsHook.replacePrefixSuffix(event.getPlayer().getUniqueId(), message);
        }*/

        if (!bridge.getConfig().getChatMode().equals("separated")) {
            bridge.discordMethods.sendMessage(bridge.getJDA(), bridge.getConfig().getChannels("global"), message);
        } else {
            bridge.discordMethods.sendMessage(bridge.getJDA(), bridge.getConfig().getChannels(serverName), message);
        }
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        String message = bridge.getConfig().getFormats("discord_disconnect")
                .replaceAll("\\{User}", event.getPlayer().getUsername());

        /*if(bridge.luckPermsHook != null) {
            message = bridge.luckPermsHook.replacePrefixSuffix(event.getPlayer().getUniqueId(), message);
        }*/

        if (!bridge.getConfig().getChatMode().equals("separated")) {
            bridge.discordMethods.sendMessage(bridge.getJDA(), bridge.getConfig().getChannels("global"), message);
        } else {
            for(RegisteredServer server : bridge.getProxyServer().getAllServers()) {
                bridge.discordMethods.sendMessage(bridge.getJDA(), bridge.getConfig().getChannels(server.getServerInfo().getName()), message);
            }
        }
    }
}
