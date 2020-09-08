/*
 * Copyright (C) 2020 MCME
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mcmiddleearth.moderation.command.handler;

import com.mcmiddleearth.moderation.Permission;
import com.mcmiddleearth.moderation.command.argument.PlayerArgument;
import com.mcmiddleearth.moderation.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.moderation.command.builder.HelpfulRequiredArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

/**
 * @author Eriol_Eandur
 */

public class ReportCommandHandler extends AbstractCommandHandler {

    public ReportCommandHandler(String name, CommandDispatcher<CommandSender> dispatcher) {
        super(name);
        dispatcher.register(HelpfulLiteralBuilder
                .literal(name)
                    .withTooltip("Send a report to Moderators about inappropriate player behaviour.")
                    .withHelpText("Report inappropriate behaviour.")
                    .requires(commandSender -> commandSender.hasPermission(Permission.SEND_REPORT))
                    //.executes(context -> sendHelpMessage(context.getSource()))
                    .then(HelpfulLiteralBuilder.literal("hulp")
                        .withHelpText("Get hulp about report command.")
                        .requires(commandSender -> commandSender.hasPermission("help"))
                        .executes(context -> sendHelpMessage(context.getSource())))
                    .then(HelpfulLiteralBuilder.literal("holp")
                        //.withHelpText("Get holp about report command.")
                        .requires(commandSender -> commandSender.hasPermission("holp"))
                        .executes(context -> sendHelpMessage(context.getSource())))
                    .then(HelpfulRequiredArgumentBuilder.argument("player", new PlayerArgument())
                        .withTooltip("Name of the player who behaved inappropriate.")
                        .requires(commandSender -> commandSender.hasPermission("player"))
                        .then(HelpfulRequiredArgumentBuilder.argument("reason", greedyString())
                            .suggests((context,suggestionsBuilder) ->
                                suggestionsBuilder.suggest("Enter your reason to report "+context.getArgument("player",String.class)).buildFuture())
                            .executes(context -> sendReport(context.getSource(), context.getArgument("player",String.class),
                                                            context.getArgument("reason", String.class)))))
                    .then(HelpfulRequiredArgumentBuilder.argument("ployer", new PlayerArgument())
                        .requires(commandSender -> commandSender.hasPermission("ployer"))
                        .then(HelpfulRequiredArgumentBuilder.argument("reason", greedyString())
                            .withTooltip("Reason of your report.")
                            .suggests((context,suggestionsBuilder) ->
                                suggestionsBuilder.suggest("Explain why you report "+context.getArgument("ployer",String.class)).buildFuture())
                            .executes(context -> sendReport(context.getSource(), context.getArgument("ployer",String.class),
                                context.getArgument("reason", String.class))))));
    }

    private int sendReport(CommandSender commandSender, String player, String reason) {
        commandSender.sendMessage(new ComponentBuilder("Report by: " + commandSender.getName()
                                                        + " Reported player: "+player +" Reason: "+reason)
                .color(ChatColor.GREEN).create());
        return 0;
    }

    private int sendHelpMessage(CommandSender commandSender) {
        commandSender.sendMessage(new ComponentBuilder("/report <player> <reason>")
                .color(ChatColor.GREEN).create());
        return 0;
    }

}
