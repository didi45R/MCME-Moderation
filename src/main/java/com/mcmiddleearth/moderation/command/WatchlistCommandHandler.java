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
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.logging.Logger;

/**
 *
 * @author Eriol_Eandur
 */

public class WatchlistCommandHandler extends AbstractCommandHandler{

    public WatchlistCommandHandler(String name, CommandDispatcher<CommandSender> dispatcher) {
        super(name);
        dispatcher.register(LiteralArgumentBuilder.<CommandSender>literal(name)
            .executes(context -> {
                sendHelpMessage(context.getSource());
                return 0;
                }));
    }

    private void sendHelpMessage(CommandSender commandSender) {
        if(commandSender instanceof ProxiedPlayer) {
            commandSender.sendMessage(new ComponentBuilder("/watchlist")
                    .color(ChatColor.GREEN).create());
        } else {
            Logger.getLogger(WatchlistCommandHandler.class.getSimpleName()).info("/watchlist");
        }
    }

}
