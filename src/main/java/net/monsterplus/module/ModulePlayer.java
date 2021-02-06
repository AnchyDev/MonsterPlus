package net.monsterplus.module;

import net.monsterplus.main.Main;
import net.monsterplus.util.TierHelper;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class ModulePlayer extends ModuleListener
{
    private Logger logger;
    private FileConfiguration config;

    public ModulePlayer(Logger logger, FileConfiguration config)
    {
        this.logger = logger;
        this.config = config;
    }

    /*public boolean checkRecipe(Recipe recipe, ItemStack[] itemMatrix)
    {
        String name = recipe.getResult().getItemMeta().getLocalizedName();

        String[] idName = name.split(":");

        if(idName.length > 1)
        {
            logger.log(Level.INFO, "Checking recipe : "+idName[0]);
            return Main.getInstance().getItemHandler().getItem(idName[0]).assertCraftable(itemMatrix);
        }

        return true;
    }*/

    /*@EventHandler
    public void onPrepareItemCraftEvent(PrepareItemCraftEvent event)
    {
        if(event.getRecipe() != null)
        {
            var inventory = event.getInventory();
            var itemMatrix = inventory.getMatrix();


            if(!checkRecipe(event.getRecipe(), itemMatrix))
            {
                inventory.setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }*/

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event)
    {
        if(event.getPlayer().isOp())
        {
            String message = event.getMessage();

            if(message.startsWith("!"))
            {
                try
                {
                    String command = message.split("!")[1];
                    String[] args = command.split(" ");
                    switch (args[0])
                    {
                        case "item":
                            var itemHandler = Main.getInstance().getItemHandler();
                            var item = itemHandler.getItem(args[1]).getItem(false);

                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    if(item == null)
                                    {
                                        event.getPlayer().sendMessage("No item found with that key.");
                                    }
                                    else
                                    {
                                        event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), item);
                                    }
                                }
                            }.runTask(Main.getInstance());

                            event.setCancelled(true);
                            break;
                    }
                }
                catch(Exception e)
                {
                    event.getPlayer().sendMessage("INVALID ARGS" + e.getMessage());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event)
    {
        if(!config.getBoolean("monsterPlusEnabled"))
        {
            return;
        }

        if(!config.getBoolean("monsterPlusTierChangeMessage"))
        {
            return;
        }

        Player player = event.getPlayer();
        World world = player.getWorld();
        Location from = event.getFrom();
        Location to = event.getTo();

        int fromTier = TierHelper.getTierFromLocation(world, from);
        int toTier = TierHelper.getTierFromLocation(world, to);

        if(fromTier == toTier)
        {
            return;
        }

        if(fromTier > toTier)
        {
            String title = config.getString("monsterPlusTierTitleWeaker");
            String subTitle = config.getString("monsterPlusTierSubTitleWeaker");
            int titleFadeIn = config.getInt("monsterPlusTierTitleFadeInTicksWeaker");
            int titleStay = config.getInt("monsterPlusTierTitleStayTicksWeaker");
            int titleFadeOut = config.getInt("monsterPlusTierTitleFadeOutTicksWeaker");

            formatAndSendTitle(player, to, title, subTitle, titleFadeIn, titleStay, titleFadeOut);
        }
        else if(toTier > fromTier)
        {
            String title = config.getString("monsterPlusTierTitleStronger");
            String subTitle = config.getString("monsterPlusTierSubTitleStronger");
            int titleFadeIn = config.getInt("monsterPlusTierTitleFadeInTicksStronger");
            int titleStay = config.getInt("monsterPlusTierTitleStayTicksStronger");
            int titleFadeOut = config.getInt("monsterPlusTierTitleFadeOutTicksStronger");

            formatAndSendTitle(player, to, title, subTitle, titleFadeIn, titleStay, titleFadeOut);
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
    {
        //Damage Value ArmorStand
        if(event.getDamager() instanceof Player)
        {
            Player player = (Player) event.getDamager();
            double damage = event.getFinalDamage();

            Location location = event.getEntity().getLocation();
            location.setY(location.getY() + (event.getEntity().getHeight() / 2));

            DecimalFormat dFormat = new DecimalFormat("#.#");

            ArmorStand aStand = event.getEntity().getWorld().spawn(location, ArmorStand.class, (armorStand) -> {
                armorStand.setGravity(false);
                armorStand.setInvulnerable(true);
                armorStand.setBasePlate(false);
                armorStand.setInvisible(true);
                armorStand.setMarker(true);
                armorStand.setCustomName(dFormat.format(damage));
                armorStand.setCustomNameVisible(true);
            });

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    aStand.remove();
                }
            }.runTaskLater(Main.getInstance(), 30);
        }
    }

    public void formatAndSendTitle(Player player, Location toLocation, String title, String subTitle, int fadeIn, int stay, int fadeOut)
    {
        int tier = TierHelper.getTierFromLocation(player.getWorld(), toLocation);

        title = title.replace("{tier}", ""+tier);
        title = ChatColor.translateAlternateColorCodes('&', title);

        subTitle = subTitle.replace("{tier}", ""+tier);
        subTitle = ChatColor.translateAlternateColorCodes('&', subTitle);

        player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
    }
}
