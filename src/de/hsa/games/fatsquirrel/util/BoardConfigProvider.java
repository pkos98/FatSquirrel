package de.hsa.games.fatsquirrel.util;

import de.hsa.games.fatsquirrel.core.EntityType;

import java.util.Map;

public interface BoardConfigProvider {
    public Map<EntityType, Integer> getEntityDistribution();

    public String getSetting(String name);

    public int getSettingAsInt(String name);
}
