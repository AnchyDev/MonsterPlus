package net.monsterplus.revamp.util;

import net.monsterplus.main.Main;
import net.monsterplus.revamp.item.MPItem;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Level;

public class MPItemHandler
{
    private HashMap<String, MPItem> itemMap;
    private FileConfiguration itemConfig;

    public MPItemHandler()
    {
        itemMap = new HashMap<String, MPItem>();
        itemConfig = new YamlConfiguration();

        /*if(loadConfig())
        {
            loadItems();
        }*/
    }

    private boolean loadConfig()
    {
        try
        {
            String configPath = "./plugins/MonsterPlus/items.yml";

            if(!Files.exists(Path.of(configPath)))
                Files.createFile(Path.of(configPath));

            itemConfig.load(configPath);

            return true;
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (InvalidConfigurationException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private void loadItems()
    {
        if(itemConfig.getKeys(false).size() > 0)
        {
            itemConfig.getValues(false).forEach((k, v) ->
            {
                Main.getInstance().getLogger().log(Level.INFO, "k: " + k);
                var test = (MemorySection)v;
                Main.getInstance().getLogger().log(Level.INFO, test.getString("id"));
            });
        }
        else
        {
            Main.getInstance().getLogger().log(Level.INFO, "No items found in items.yml.");
        }
    }

    public void registerItem(String key, MPItem item)
    {
        if(!itemMap.containsKey(key))
        {
            itemMap.put(key, item);
        }
    }

    public MPItem getItem(String key)
    {
        return itemMap.get(key);
    }

    public FileConfiguration getItemConfig()
    {
        return itemConfig;
    }
}
