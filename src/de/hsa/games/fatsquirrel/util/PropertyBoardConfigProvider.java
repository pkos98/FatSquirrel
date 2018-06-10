package de.hsa.games.fatsquirrel.util;

import de.hsa.games.fatsquirrel.core.EntityType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyBoardConfigProvider implements BoardConfigProvider {

    private Properties properties = new Properties();

    public PropertyBoardConfigProvider(String fileName) {
        try {
            properties.load(new FileInputStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<EntityType, Integer> getEntityDistribution() {
        Map<EntityType, Integer> occurrenceByType = new HashMap<>();
        for (EntityType type : EntityType.values()) {
            switch (type) {
                case MINI_SQUIRREL:
                case MINI_SQUIRREL_BOT:
                case EMPTY_FIELD:
                case MASTER_SQUIRREL_BOT:
                case HAND_OPERATED_MASTER_SQUIRREL:
                    continue;
            }
            occurrenceByType.put(type, Integer.parseInt(properties.getProperty(type.name())));
        }
        return occurrenceByType;
    }

    @Override
    public String getSetting(String name) {
        return properties.getProperty(name);
    }

    @Override
    public int getSettingAsInt(String name) {
        return Integer.parseInt(properties.getProperty(name));
    }

}
