package io.github.bennyboy1695.betterdiscordbridge.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import io.github.bennyboy1695.betterdiscordbridge.BetterDiscordBridge;
import net.dv8tion.jda.api.entities.Activity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandGameStatus implements SimpleCommand {
    /*
     This used to be "BetterDiscordBridge instance"
     But we want things to be as clean and understanding as possible.
     So "instance" has been changed to "bridge" for the whole plugin.
     */
    private final BetterDiscordBridge bridge;

    public CommandGameStatus(BetterDiscordBridge bridge) {
        this.bridge = bridge;
    }

    /*
    This is very deprecated and will need to be rebuilt,
    However this currently works and that is all that matter.
    Refer to Kyori.Adventure and also brig commands when re working this.

    This is the GameStatus command.
    This allows you to change the bots status seen in discord.

    The deprecations are "execute" and "sendMessage" method.
     */
    @Override
    public void execute(final SimpleCommand.Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length <= 1) {
            source.sendMessage(Component.text("Usage: /gamestatus <playing|listening|watching> a Velocity server!", NamedTextColor.RED));
            return;
        }

        StringBuilder message = new StringBuilder();

        //Creates message from remaining args.
        for(String arg : args) {
            message.append(arg).append(" ");
        }
        //Deletes space at the end of the message.
        message.deleteCharAt(args.length-1);

        if (args[0].equalsIgnoreCase("playing")) {
            bridge.getJDA().getPresence().setActivity(Activity.playing(message.toString()));
        } else if (args[0].equalsIgnoreCase("watching")) {
            bridge.getJDA().getPresence().setActivity(Activity.watching(message.toString()));
        } else if (args[0].equalsIgnoreCase("listening")) {
            bridge.getJDA().getPresence().setActivity(Activity.listening(message.toString()));
        } else {
            source.sendMessage(Component.text("Correct Usage: /gamestatus <playing|listening|watching> a Velocity server!", NamedTextColor.RED));
            return;
        }

        String fullMessage = args[0] + " " + message;
        source.sendMessage(Component.text("Set bots status to: " + fullMessage, NamedTextColor.GREEN));
        bridge.getConfig().getConfigNode().getNode("discord", "info", "status").setValue(fullMessage);
        bridge.getConfig().saveConfig();
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("betterdiscordbridge.command.gamestatus");
    }
}




