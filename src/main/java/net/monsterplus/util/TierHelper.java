package net.monsterplus.util;

import net.monsterplus.main.Main;
import org.bukkit.Location;
import org.bukkit.World;

public class TierHelper
{
    public static int getTierFromLocation(World world, Location location)
    {
        int blockDistance = Main.getInstance().getConfig().getInt("monsterPlusTierBlockDistance");
        double distance = world.getSpawnLocation().distance(location);

        double tier = distance / blockDistance;

        return (int)Math.ceil(tier);
    }
}
