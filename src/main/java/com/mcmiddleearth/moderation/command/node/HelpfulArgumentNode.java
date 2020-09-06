package com.mcmiddleearth.moderation.command.node;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import net.md_5.bungee.api.CommandSender;

import java.util.function.Predicate;

public class HelpfulArgumentNode extends ArgumentCommandNode<CommandSender, String> {

    private final String usageText;

    public HelpfulArgumentNode(String name, ArgumentType<String> type, Command<CommandSender> command, Predicate<CommandSender> requirement,
                               CommandNode<CommandSender> redirect, RedirectModifier<CommandSender> modifier, boolean forks,
                               SuggestionProvider<CommandSender> customSuggestions, String usageText) {
        super(name, type, command, requirement, redirect, modifier, forks, customSuggestions);
        this.usageText = usageText;
    }

    public String getUsageText() {
        return usageText;
    }

}
