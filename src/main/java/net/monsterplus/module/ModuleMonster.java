package net.monsterplus.module;

import net.monsterplus.main.Main;
import net.monsterplus.util.TierHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModuleMonster extends ModuleListener
{
    private Logger logger;
    private FileConfiguration config;
    private Random rand;

    public ModuleMonster(Logger logger, FileConfiguration config)
    {
        this.logger = logger;
        this.config = config;
        this.rand = new Random();
    }

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent event)
    {
        if(!config.getBoolean("monsterPlusEnabled"))
        {
            return;
        }

        SpawnReason spawnReason = event.getSpawnReason();

        if((spawnReason == SpawnReason.NATURAL ||
                spawnReason == SpawnReason.SPAWNER ||
                spawnReason == SpawnReason.SPAWNER_EGG))
        {
            if(event.getEntity() instanceof Monster || event.getEntity() instanceof Hoglin)
            {
                LivingEntity monster = event.getEntity();

                int tier = TierHelper.getTierFromLocation(monster.getWorld(), monster.getLocation());
                double multiplier = (double) tier / 2.0;

                monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * multiplier);
                monster.setHealth(monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

                monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue() * multiplier);

                if (spawnReason == SpawnReason.SPAWNER_EGG)
                {
                    logger.log(Level.INFO, "MAX HP : " + monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    logger.log(Level.INFO, "DAMAGE : " + monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue());
                    logger.log(Level.INFO, "MULTIPLIER : " + multiplier);
                    logger.log(Level.INFO, "TIER : " + tier);
                }
                monster.setMetadata("isCustomMob", new FixedMetadataValue(Main.getInstance(), 1));
                monster.setMetadata("hasNaturalNametag", new FixedMetadataValue(Main.getInstance(), monster.getName()));
                monster.setMetadata("tierLevel", new FixedMetadataValue(Main.getInstance(), tier));
                formatAndSetNametag(monster);
            }
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event)
    {
        if(!config.getBoolean("monsterPlusEnabled"))
        {
            return;
        }

        if(!event.getEntity().hasMetadata("isCustomMob"))
        {
            return;
        }

        if(event.getEntity() instanceof Monster || event.getEntity() instanceof Hoglin)
        {
            LivingEntity monster = (LivingEntity)event.getEntity();

            if(monster.hasMetadata("hasNaturalNametag"))
            {
                formatAndSetNametag(monster, event.getFinalDamage());
            }

            return;
        }
    }

    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent event)
    {
        if(!event.getEntity().hasMetadata("isCustomMob"))
        {
            return;
        }

        if(event.getTarget() == null)
        {
            if(event.getEntity().hasMetadata("hadTarget"))
            {
                LivingEntity entity = (LivingEntity) event.getEntity();
                entity.setCustomNameVisible(false);
            }
            return;
        }

        if(event.getTarget().getType() == EntityType.PLAYER && event.getEntity() instanceof LivingEntity)
        {
            Player player = (Player) event.getTarget();
            LivingEntity entity = (LivingEntity) event.getEntity();
            entity.setCustomNameVisible(true);
            entity.setMetadata("hadTarget", new FixedMetadataValue(Main.getInstance(), 1));
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event)
    {
        if(!event.getEntity().hasMetadata("isCustomMob"))
        {
            return;
        }

        if(event.getEntity().hasMetadata("tierLevel"))
        {
            if(event.getEntity().getMetadata("tierLevel").size() > 0)
            {
                int tier = event.getEntity().getMetadata("tierLevel").get(0).asInt();
                double multiplier = (double) tier / 2.0;
                event.setDroppedExp((int) (event.getDroppedExp() * multiplier));

                /*for (ItemStack item : event.getDrops())
                {
                    if (!canDropItem(item.getType()))
                    {
                        continue;
                    }

                    item.setAmount((int) (item.getAmount() * multiplier));
                }*/

                if(event.getEntity().getType() == EntityType.ZOMBIE)
                {
                    var dropChance = rand.nextInt(100);

                    if (dropChance <= 1)
                    {
                        var starterSword = Main.getInstance().getItemHandler().getItem("mp_startersword2");
                        starterSword.setDescription("Obtained from Tier " + tier);
                        var variance = rand.nextInt(tier);
                        starterSword.addBonusAttribute(Attribute.GENERIC_ATTACK_DAMAGE, (double)((tier / 2) + variance) + 1);
                        var item = starterSword.getItem(false);
                        event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);
                    }

                    dropChance = rand.nextInt(100);

                    if (dropChance <= 2)
                    {
                        var zHelmet = Main.getInstance().getItemHandler().getItem("mp_zhelmet");
                        zHelmet.setDescription("Obtained from Tier " + tier);
                        var variance = rand.nextInt(tier);
                        var variance2 = rand.nextInt(tier);
                        zHelmet.addBonusAttribute(Attribute.GENERIC_ARMOR, (double)((tier / 2) + variance) + 1);
                        zHelmet.addBonusAttribute(Attribute.GENERIC_MAX_HEALTH, (double)((tier / 2) + variance2) + 1);
                        var item = zHelmet.getItem(false);
                        event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);
                    }

                    dropChance = rand.nextInt(100);

                    if (dropChance <= 2)
                    {
                        var zChestplate = Main.getInstance().getItemHandler().getItem("mp_zchestplate");
                        zChestplate.setDescription("Obtained from Tier " + tier);
                        var variance = rand.nextInt(tier);
                        var variance2 = rand.nextInt(tier);
                        zChestplate.addBonusAttribute(Attribute.GENERIC_ARMOR, (double)((tier / 2) + variance) + 1);
                        zChestplate.addBonusAttribute(Attribute.GENERIC_MAX_HEALTH, (double)((tier / 2) + variance2) + 1);
                        var item = zChestplate.getItem(false);
                        event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);
                    }

                    dropChance = rand.nextInt(100);

                    if (dropChance <= 2)
                    {
                        var zLeggings = Main.getInstance().getItemHandler().getItem("mp_zleggings");
                        zLeggings.setDescription("Obtained from Tier " + tier);
                        var variance = rand.nextInt(tier);
                        var variance2 = rand.nextInt(tier);
                        zLeggings.addBonusAttribute(Attribute.GENERIC_ARMOR, (double)((tier / 2) + variance) + 1);
                        zLeggings.addBonusAttribute(Attribute.GENERIC_MAX_HEALTH, (double)((tier / 2) + variance2) + 1);
                        var item = zLeggings.getItem(false);
                        event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);
                    }

                    dropChance = rand.nextInt(100);

                    if (dropChance <= 2)
                    {
                        var zBoots = Main.getInstance().getItemHandler().getItem("mp_zboots");
                        zBoots.setDescription("Obtained from Tier " + tier);
                        var variance = rand.nextInt(tier);
                        var variance2 = rand.nextInt(tier);
                        zBoots.addBonusAttribute(Attribute.GENERIC_ARMOR, (double)((tier / 2) + variance) + 1);
                        zBoots.addBonusAttribute(Attribute.GENERIC_MAX_HEALTH, (double)((tier / 2) + variance2) + 1);
                        var item = zBoots.getItem(false);
                        event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);
                    }
                }
            }
        }
    }

    public boolean canDropItem(Material material)
    {
        List<Material> allowedItems = new ArrayList<>();
        allowedItems.add(Material.STRING);
        allowedItems.add(Material.SPIDER_EYE);
        allowedItems.add(Material.BLAZE_ROD);
        allowedItems.add(Material.ROTTEN_FLESH);
        allowedItems.add(Material.PRISMARINE_SHARD);
        allowedItems.add(Material.PRISMARINE_CRYSTALS);
        allowedItems.add(Material.ENDER_PEARL);
        allowedItems.add(Material.EMERALD);
        allowedItems.add(Material.GUNPOWDER);
        allowedItems.add(Material.GHAST_TEAR);
        allowedItems.add(Material.ARROW);
        allowedItems.add(Material.BONE);
        allowedItems.add(Material.SLIME_BALL);
        allowedItems.add(Material.REDSTONE);
        allowedItems.add(Material.NETHER_STAR);
        allowedItems.add(Material.GOLD_NUGGET);
        allowedItems.add(Material.IRON_INGOT);

        return allowedItems.contains(material);
    }

    public void formatAndSetNametag(LivingEntity monster)
    {
        String nametag = config.getString("monsterPlusNametagFormat");

        nametag = nametag.replace("{name}", monster.getMetadata("hasNaturalNametag").get(0).asString());

        if(nametag.contains("{health}"))
        {
            double health = monster.getHealth();

            DecimalFormat df = new DecimalFormat("#.#");
            nametag = nametag.replace("{health}", df.format(health));
        }

        nametag = nametag.replace("{maxHealth}", ""+(int)monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

        if(monster.hasMetadata("tierLevel"))
        {
            int tier = monster.getMetadata("tierLevel").get(0).asInt();
            nametag = nametag.replace("{tier}", ""+tier);
        }
        else
        {
            nametag = nametag.replace("{tier}", ""+TierHelper.getTierFromLocation(monster.getWorld(), monster.getLocation()));
        }


        nametag = ChatColor.translateAlternateColorCodes('&', nametag);

        monster.setCustomName(nametag);
        //monster.setCustomNameVisible(true);
    }

    public void formatAndSetNametag(LivingEntity monster, double finalDamage)
    {
        String nametag = config.getString("monsterPlusNametagFormat");

        if(monster.getMetadata("hasNaturalNametag").size() > 0)
            nametag = nametag.replace("{name}", monster.getMetadata("hasNaturalNametag").get(0).asString());

        if(nametag.contains("{health}"))
        {
            double health = monster.getHealth() - finalDamage;

            DecimalFormat df = new DecimalFormat("#.#");
            nametag = nametag.replace("{health}", df.format(health));
        }
        nametag = nametag.replace("{maxHealth}", ""+(int)monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

        if(monster.hasMetadata("tierLevel"))
        {
            if(monster.getMetadata("tierLevel").size() > 0)
            {
                int tier = monster.getMetadata("tierLevel").get(0).asInt();
                nametag = nametag.replace("{tier}", "" + tier);
            }
        }
        else
        {
            nametag = nametag.replace("{tier}", ""+TierHelper.getTierFromLocation(monster.getWorld(), monster.getLocation()));
        }

        nametag = ChatColor.translateAlternateColorCodes('&', nametag);

        monster.setCustomName(nametag);
        //monster.setCustomNameVisible(true);
    }
}
