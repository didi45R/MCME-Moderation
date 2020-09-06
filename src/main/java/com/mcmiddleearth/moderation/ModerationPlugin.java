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
package com.mcmiddleearth.moderation;

import com.mcmiddleearth.moderation.command.AbstractCommandHandler;
import com.mcmiddleearth.moderation.command.ModerationPluginCommand;
import com.mcmiddleearth.moderation.command.ReportCommandHandler;
import com.mcmiddleearth.moderation.command.WatchlistCommandHandler;
import com.mcmiddleearth.moderation.configuration.ModerationConfig;
import com.mcmiddleearth.moderation.watchlist.WatchlistManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Eriol_Eandur
 */


public class ModerationPlugin extends Plugin implements Listener {
    
    private static ModerationPlugin instance;

    private static ModerationConfig config;
    private static File configFile;

    private CommandDispatcher<CommandSender> commandDispatcher;
    private Set<ModerationPluginCommand> commands;

    private WatchlistManager watchlistManager;

    @Override
    public void onEnable() {
        instance = this;
        configFile = new File(getDataFolder(),"config.yml");
        saveDefaultConfig();
        config = new ModerationConfig(configFile);
        commandDispatcher =  new CommandDispatcher<>();

        commands.add(new ModerationPluginCommand(commandDispatcher,
                new WatchlistCommandHandler("watchlist", commandDispatcher)));
        commands.add(new ModerationPluginCommand(commandDispatcher,
                new ReportCommandHandler("report", commandDispatcher)));

        commands.forEach(command -> ProxyServer.getInstance().getPluginManager()
                .registerCommand(this, command));

                        //Listener for tab complete
        ProxyServer.getInstance().getPluginManager().registerListener(this,this);
        watchlistManager = new WatchlistManager();
    }

    @Override
    public void onDisable() {
        //maybe TODO: e.g. cancel scheduled tasks.
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        for (ModerationPluginCommand command : commands) {
            if (event.getCursor().startsWith(command.getName())) {
                command.onTabComplete(event);
                return;
            }
        }
    }

    private void saveDefaultConfig() {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if(!configFile.exists()) {
            try {
                if(configFile.createNewFile()) {
                    try (InputStreamReader in = new InputStreamReader(getResourceAsStream("config.yml"));
                         FileWriter fw = new FileWriter(configFile)) {
                        char[] buf = new char[1024];
                        int read = 1;
                        while (read > 0) {
                            read = in.read(buf);
                            if (read > 0)
                                fw.write(buf, 0, read);
                        }
                        fw.flush();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ModerationPlugin.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }

    public static ModerationPlugin getInstance() {
        return instance;
    }

    public static ModerationConfig getConfig() {
        return config;
    }

    public WatchlistManager getWatchlistManager() {
        return watchlistManager;
    }
}