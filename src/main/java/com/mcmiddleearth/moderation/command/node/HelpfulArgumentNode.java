package com.mcmiddleearth.moderation.command.node;

import com.mcmiddleearth.moderation.command.argument.HelpfulArgument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import net.md_5.bungee.api.CommandSender;

import java.util.concurrent.CompletableFuture;
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

    @Override
    public CompletableFuture<Suggestions> listSuggestions(final CommandContext<CommandSender> context, final SuggestionsBuilder builder) throws CommandSyntaxException {
        if(canUse(context.getSource())) {
            if (getCustomSuggestions() == null) {
                if (getType() instanceof HelpfulArgument) {
                    return ((HelpfulArgument) getType()).listSuggestions(context, builder, usageText);
                } else {
                    return getType().listSuggestions(context, builder);
                }
            } else {
                return getCustomSuggestions().getSuggestions(context, builder);
            }
        }
        return Suggestions.empty();
    }


}
