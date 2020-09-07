package com.mcmiddleearth.moderation.command;

import com.google.common.base.Joiner;
import com.mcmiddleearth.moderation.ModerationPlugin;
import com.mcmiddleearth.moderation.command.handler.AbstractCommandHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ModerationPluginCommand extends Command {

    private CommandDispatcher<CommandSender> commandDispatcher;

    public ModerationPluginCommand(CommandDispatcher<CommandSender> commandDispatcher, AbstractCommandHandler handler) {
        super(handler.getCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            String message = String.format("%s %s", getName(), Joiner.on(' ').join(args)).trim();
//Logger.getGlobal().info(message);
            ParseResults<CommandSender> result = commandDispatcher.parse(message, sender);
            Logger.getGlobal().info("Parsed");
            result.getExceptions().entrySet().stream()
                    .findFirst().ifPresent(error -> sender.sendMessage(new ComponentBuilder(error.getValue().getMessage())
                    .color(ChatColor.RED).create()));
            if(result.getExceptions().isEmpty()) {
                if(result.getContext().getRange().getEnd() < result.getReader().getString().length()) {
                    //check for possible child nodes to collect suggestions and bake better error message
                    StringBuilder helpMessage;
                    String parsedCommand = "/" + result.getReader().getString()
                            .substring(0, result.getContext().getRange().getEnd());

                    Collection<CommandNode<CommandSender>> children = (result.getContext().getNodes().isEmpty()?new ArrayList<>():
                            result.getContext().getNodes().get(result.getContext().getNodes().size() - 1).getNode().getChildren()
                                    .stream().filter(node -> node.canUse(result.getContext().getSource())).collect(Collectors.toList()));
                    if (children.isEmpty()) {
                        if (result.getContext().getCommand() == null) {
                            helpMessage = new StringBuilder("Invalid command. Maybe you don't have permission.");
                        } else {
                            helpMessage = new StringBuilder("Invalid command. Maybe you want to do:\n" + parsedCommand);
                        }
                    } else {
                        helpMessage = new StringBuilder("Invalid command. Maybe you want to do:");
                        for (CommandNode<CommandSender> child : children) {
                            helpMessage.append("\n").append(parsedCommand).append(" ").append(child.getName())
                                    .append(": ").append(child.getUsageText());
                        }
                    }
                    sender.sendMessage(new ComponentBuilder(helpMessage.toString())
                            .color(ChatColor.RED).create());
                } else if(result.getContext().getCommand() == null) {
                    sender.sendMessage(new ComponentBuilder("Invalid command. Maybe you don't have permission.")
                            .color(ChatColor.RED).create());
                } else {
                    commandDispatcher.execute(result);
                }
            }
        } catch (CommandSyntaxException e) {
            sender.sendMessage(new ComponentBuilder("Internal command parser exception!")
                    .color(ChatColor.RED).create());
        }
    }

    public void onTabComplete(TabCompleteEvent event) {
        if (event.getSender() instanceof CommandSender) {
            try {
                CompletableFuture<Suggestions> completionSuggestions
                        = commandDispatcher.getCompletionSuggestions(commandDispatcher.parse(event.getCursor(), (ProxiedPlayer) event.getSender()));
                event.getSuggestions().addAll(completionSuggestions.get().getList().stream().map(Suggestion::getText).collect(Collectors.toList()));
            } catch (InterruptedException | ExecutionException e) {
                Logger.getLogger(ModerationPlugin.class.getSimpleName()).log(Level.WARNING,"Command tab complete error.",e);
            }
        }
    }

}
