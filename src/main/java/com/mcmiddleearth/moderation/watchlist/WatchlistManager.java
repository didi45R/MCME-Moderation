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
package com.mcmiddleearth.moderation.watchlist;

import com.mcmiddleearth.moderation.ModerationPlugin;
import com.mcmiddleearth.moderation.configuration.YamlBridge;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Eriol_Eandur
 */

public class WatchlistManager {

    private final Map<UUID,WatchlistPlayerData> watchlist = new HashMap<>();

    private final File dataFile = new File(ModerationPlugin.getInstance().getDataFolder(),"watchlist.yml");



    /**
     * Constructor loads data from watchlist.yml
     */
    public WatchlistManager() {
        if(dataFile.exists()) {
            YamlBridge yaml = new YamlBridge();
            yaml.load(dataFile);
            yaml.getMap().forEach((uuid, data) -> watchlist.put(UUID.fromString(uuid), new WatchlistPlayerData((List<Map<String, Object>>) data)));
        }
    }

    //TODO: methods to manage watchlist entries

    /**
     * This method is required for TabList feature in MCME-Connect plugin
     * @param player Player to check
     * @return if player is on watchlist
     */
    public boolean isOnWatchlist(UUID player) {
        return false; //TODO
    }

    /**
     * This method is required for TabList feature in MCME-Connect plugin
     * @return Prefix to display for players on watchlist
     */
    public String watchlistPrefix() {
        return ""; //TODO
    }

    /**
     * Saves the watchlist to watchlist.yml. Should be called each time the watchlist is modified.
     */
    public void saveToFile() {
        YamlBridge yaml = new YamlBridge();
        watchlist.forEach(((uuid, watchlistPlayerData) -> yaml.set(uuid.toString(),watchlistPlayerData.serialize())));
        yaml.save(dataFile);
    }
}
