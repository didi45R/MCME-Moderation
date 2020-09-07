package com.mcmiddleearth.moderation.command.node;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.md_5.bungee.api.CommandSender;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class HelpfulLiteralNode extends LiteralCommandNode<CommandSender> {

    private final String usageText;

    public HelpfulLiteralNode(String literal, Command<CommandSender> command, Predicate<CommandSender> requirement,
                              CommandNode<CommandSender> redirect, RedirectModifier<CommandSender> modifier,
                              boolean forks, String usageText) {
        super(literal, command, requirement, redirect, modifier, forks);
        this.usageText = usageText;
    }

    @Override
    public String getUsageText() {
        return usageText;
    }

    @Override
    public CompletableFuture<Suggestions> listSuggestions(CommandContext<CommandSender> context, SuggestionsBuilder builder) {
        if (canUse(context.getSource()) && getLiteral().toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
            return builder.suggest(getLiteral(),new LiteralMessage(getUsageText())).buildFuture();
        } else {
            return Suggestions.empty();
        }
    }
}
