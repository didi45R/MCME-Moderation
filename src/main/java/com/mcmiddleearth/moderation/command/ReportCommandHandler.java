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
package com.mcmiddleearth.moderation.command;

import com.mcmiddleearth.moderation.ModerationPlugin;
import com.mcmiddleearth.moderation.Permission;
import com.mcmiddleearth.moderation.command.argument.PlayerArgument;
import com.mcmiddleearth.moderation.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.moderation.command.builder.HelpfulRequiredArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

/**
 * @author Eriol_Eandur
 */

public class ReportCommandHandler extends AbstractCommandHandler {

    public ReportCommandHandler(String name, CommandDispatcher<CommandSender> dispatcher) {
        super(name);
        dispatcher.register(HelpfulLiteralBuilder
                .literal(name)
                    .withUsageText("Report other players to Moderators.")
                    .requires(commandSender -> commandSender.hasPermission(Permission.SEND_REPORT))
                    .executes(context -> sendHelpMessage(context.getSource()))
                    .then(HelpfulLiteralBuilder.literal("help")
                        .withUsageText("Get help about report command.")
                        .executes(context -> sendHelpMessage(context.getSource()))
                    .then(HelpfulRequiredArgumentBuilder.argument("player", new PlayerArgument())
                        .withUsageText("Player needs to be online.")
                        .suggests((context,suggestionsBuilder) ->
                            suggestionsBuilder.suggest("Enter your reason to report "+context.getArgument("player",String.class)).buildFuture())
                        .then(HelpfulRequiredArgumentBuilder.argument("reason", greedyString())
                            .executes(context -> sendReport(context.getSource(), context.getArgument("player",String.class),
                                                            context.getArgument("reason", String.class)))))));
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
