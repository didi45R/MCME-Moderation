package com.mcmiddleearth.moderation.command.builder;

import com.mcmiddleearth.moderation.command.node.HelpfulLiteralNode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.md_5.bungee.api.CommandSender;

public class HelpfulLiteralBuilder extends LiteralArgumentBuilder<CommandSender> {

    private String helpText;
    private String tooltip;

    protected HelpfulLiteralBuilder(String literal) {
        super(literal);
        helpText = "";
        tooltip = "";
    }

    public static HelpfulLiteralBuilder literal(final String name) {
        return new HelpfulLiteralBuilder(name);
    }

    public HelpfulLiteralBuilder withHelpText(String helpText) {
        this.helpText = helpText;
        return getThis();
    }

    public HelpfulLiteralBuilder withTooltip(String tooltip) {
        this.tooltip = tooltip;
        return getThis();
    }

    @Override
    public HelpfulLiteralBuilder getThis() {
        return this;
    }

    @Override
    public HelpfulLiteralNode build() {
        final HelpfulLiteralNode result = new HelpfulLiteralNode(getLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(),
                                                   isFork(), helpText, tooltip);

        for (final CommandNode<CommandSender> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
