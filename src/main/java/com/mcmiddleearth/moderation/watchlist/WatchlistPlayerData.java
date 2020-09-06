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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Eriol_Eandur
 */

public class WatchlistPlayerData {

    public List<WatchlistReason> reasons = new ArrayList<>();

    public WatchlistPlayerData(WatchlistReason reason) {
        reasons.add(reason);
    }

    /**
     * Constructor to read data from watchlist.yml
     * @param data
     */
    public WatchlistPlayerData(List<Map<String,Object>> data) {
        data.forEach(reason -> {
            try {
                reasons.add(new WatchlistReason(reason));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Required to save data to watchlist.yml
     * @return
     */
    public List<Map<String,Object>> serialize() {
        List<Map<String,Object>> result = new ArrayList<>();
        reasons.forEach(reason -> result.add(reason.serialize()));
        return result;
    }
}
