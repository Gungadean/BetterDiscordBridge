package io.github.bennyboy1695.betterdiscordbridge.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.UUID;

public class LuckPermsHook {

    public LuckPerms luckPerms;

    public LuckPermsHook() {
        this.luckPerms = LuckPermsProvider.get();
    }

    public String replacePrefixSuffix(UUID player, String message) {
        if(true) { //bridge.getProxyServer().getPluginManager().isLoaded("luckperms")
            User user = luckPerms.getUserManager().getUser(player);
            if(user != null) {
                if(user.getCachedData().getMetaData().getPrefix() != null) {
                    message = message.replaceAll("\\{Prefix}", user.getCachedData().getMetaData().getPrefix());
                }

                if(user.getCachedData().getMetaData().getSuffix() != null) {
                    message = message.replaceAll("\\{Suffix}", user.getCachedData().getMetaData().getSuffix());
                }
            }
        }
        return message;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
