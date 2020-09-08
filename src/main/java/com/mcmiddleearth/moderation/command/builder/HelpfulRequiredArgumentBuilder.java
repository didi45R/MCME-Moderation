package com.mcmiddleearth.moderation.command.builder;

import com.mcmiddleearth.moderation.command.node.HelpfulArgumentNode;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import net.md_5.bungee.api.CommandSender;

public class HelpfulRequiredArgumentBuilder extends ArgumentBuilder<CommandSender, HelpfulRequiredArgumentBuilder> {
    private String helpText;
    private String tooltip;
    private final String name;
    private final ArgumentType<String> type;
    private SuggestionProvider<CommandSender> suggestionsProvider = null;

    private HelpfulRequiredArgumentBuilder(final String name, final ArgumentType<String> type) {
        this.name = name;
        this.type = type;
        this.helpText = "";
        this.tooltip = "";
    }

    public static HelpfulRequiredArgumentBuilder argument(final String name, final ArgumentType<String> type) {
        return new HelpfulRequiredArgumentBuilder(name, type);
    }

    public HelpfulRequiredArgumentBuilder suggests(final SuggestionProvider<CommandSender> provider) {
        this.suggestionsProvider = provider;
        return getThis();
    }

    public HelpfulRequiredArgumentBuilder withHelpText(String helpText) {
        this.helpText = helpText;
        return getThis();
    }

    public HelpfulRequiredArgumentBuilder withTooltip(String tooltip) {
        this.tooltip = tooltip;
        return getThis();
    }

    public SuggestionProvider<CommandSender> getSuggestionsProvider() {
        return suggestionsProvider;
    }

    @Override
    protected HelpfulRequiredArgumentBuilder getThis() {
        return this;
    }

    public ArgumentType<String> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ArgumentCommandNode<CommandSender, String> build() {
        final ArgumentCommandNode<CommandSender, String> result = new HelpfulArgumentNode(getName(), getType(), getCommand(),
                                                                    getRequirement(), getRedirect(), getRedirectModifier(),
                                                                    isFork(), getSuggestionsProvider(), helpText, tooltip);

        for (final CommandNode<CommandSender> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
