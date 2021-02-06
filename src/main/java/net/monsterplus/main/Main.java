package net.monsterplus.main;

import net.monsterplus.module.ModuleMonster;
import net.monsterplus.module.ModulePlayer;
import net.monsterplus.revamp.item.MPItem;
import net.monsterplus.revamp.item.MPItemQuality;
import net.monsterplus.revamp.item.MPItemType;
import net.monsterplus.revamp.util.MPItemHandler;
import net.monsterplus.util.TierHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;

public class Main extends JavaPlugin
{
    private static Main instance;
    private MPItemHandler itemHandler;

    @Override
    public void onEnable()
    {
        if(instance == null)
        {
            instance = this;
        }

        itemHandler = new MPItemHandler();

        var mpSlowSword = new MPItem("mp_slowsword", "Slooow Sword", MPItemType.TOOL, Material.STONE_SWORD, EquipmentSlot.HAND);
        mpSlowSword.setQuality(MPItemQuality.TRASH);
        mpSlowSword.addAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 16);
        mpSlowSword.addAttribute(Attribute.GENERIC_ATTACK_SPEED, 0.1);

        itemHandler.registerItem(mpSlowSword.getId(), mpSlowSword);

        var mpOpSword = new MPItem("mp_opsword", "Overpowered Sword", MPItemType.TOOL, Material.DIAMOND_SWORD, EquipmentSlot.HAND);
        mpOpSword.setQuality(MPItemQuality.LEGENDARY);
        mpOpSword.addAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 100);
        mpOpSword.addAttribute(Attribute.GENERIC_ATTACK_SPEED, 10.0);

        itemHandler.registerItem(mpOpSword.getId(), mpOpSword);

        var myStarterSword = new MPItem("mp_startersword", "Starter Sword", MPItemType.TOOL, Material.IRON_SWORD, EquipmentSlot.HAND);
        myStarterSword.setQuality(MPItemQuality.UNCOMMON);
        myStarterSword.setDescription("Basic starting sword with bonus attributes.");
        myStarterSword.addAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 5);
        myStarterSword.addAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.0);
        myStarterSword.addBonusAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 3);
        myStarterSword.addBonusAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.0);

        itemHandler.registerItem(myStarterSword.getId(), myStarterSword);

        var myStarterSword2 = new MPItem("mp_startersword2", "Starter Sword", MPItemType.TOOL, Material.IRON_SWORD, EquipmentSlot.HAND);
        myStarterSword2.setQuality(MPItemQuality.UNCOMMON);
        myStarterSword2.setDescription("Basic starting sword with base attributes.");
        myStarterSword2.addAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 5);
        myStarterSword2.addAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.0);

        itemHandler.registerItem(myStarterSword2.getId(), myStarterSword2);

        var myTestSword = new MPItem("mp_testsword", "Test Sword", MPItemType.TOOL, Material.NETHERITE_SWORD, EquipmentSlot.HAND);
        myTestSword.setQuality(MPItemQuality.UNKNOWN);
        myTestSword.addAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 5);
        myTestSword.addAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.6);
        myTestSword.addAttribute(Attribute.GENERIC_ARMOR, 10.0);
        myTestSword.addAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS, 4.0);
        myTestSword.addBonusAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 5);
        myTestSword.addBonusAttribute(Attribute.GENERIC_ATTACK_SPEED, 1.6);
        myTestSword.addBonusAttribute(Attribute.GENERIC_ARMOR, 10.0);
        myTestSword.addBonusAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS, 4.0);

        itemHandler.registerItem(myTestSword.getId(), myTestSword);

        var myArmor = new MPItem("mp_testarmor", "Test Armor", MPItemType.ARMOR, Material.LEATHER_CHESTPLATE, EquipmentSlot.CHEST);
        myArmor.setQuality(MPItemQuality.RARE);
        myArmor.addAttribute(Attribute.GENERIC_ARMOR, 10.0);
        myArmor.addAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS, 4.0);
        myArmor.addBonusAttribute(Attribute.GENERIC_MAX_HEALTH, 10.0);

        itemHandler.registerItem(myArmor.getId(), myArmor);

        var zHelmet = new MPItem("mp_zhelmet", "Zombie Helmet", MPItemType.ARMOR, Material.IRON_HELMET, EquipmentSlot.HEAD);
        zHelmet.setQuality(MPItemQuality.RARE);
        zHelmet.addAttribute(Attribute.GENERIC_ARMOR, 3.0);

        itemHandler.registerItem(zHelmet.getId(), zHelmet);

        var zChestplate = new MPItem("mp_zchestplate", "Zombie Chestplate", MPItemType.ARMOR, Material.IRON_CHESTPLATE, EquipmentSlot.CHEST);
        zChestplate.setQuality(MPItemQuality.RARE);
        zChestplate.addAttribute(Attribute.GENERIC_ARMOR, 6.0);

        itemHandler.registerItem(zChestplate.getId(), zChestplate);

        var zLeggings = new MPItem("mp_zleggings", "Zombie Leggings", MPItemType.ARMOR, Material.IRON_LEGGINGS, EquipmentSlot.LEGS);
        zLeggings.setQuality(MPItemQuality.RARE);
        zLeggings.addAttribute(Attribute.GENERIC_ARMOR, 5.0);

        itemHandler.registerItem(zLeggings.getId(), zLeggings);

        var zBoots = new MPItem("mp_zboots", "Zombie Boots", MPItemType.ARMOR, Material.IRON_BOOTS, EquipmentSlot.FEET);
        zBoots.setQuality(MPItemQuality.RARE);
        zBoots.addAttribute(Attribute.GENERIC_ARMOR, 2.0);

        itemHandler.registerItem(zBoots.getId(), zBoots);

        new BukkitRunnable()
        {

            @Override
            public void run()
            {
                Bukkit.getServer().getOnlinePlayers().forEach((player) ->
                {
                    var cPlayer = (CraftPlayer)player;
                    var cHandle = cPlayer.getHandle();
                    int ping = cHandle.ping;
                    ChatColor pingColor = ChatColor.GREEN;

                    if(ping > 400)
                    {
                        pingColor = ChatColor.RED;
                    }
                    else if (ping >= 200 && ping <= 400)
                    {
                        pingColor = ChatColor.YELLOW;
                    }
                    player.setPlayerListName(player.getDisplayName() + ChatColor.WHITE + " (" + pingColor + ping + ChatColor.WHITE + ") ");
                    player.setPlayerListHeaderFooter(ChatColor.AQUA + "╔════════════════════╗\n\n" + ChatColor.WHITE + Bukkit.getServer().getMotd() + "\n", "\nPing: " + pingColor + ping + ChatColor.AQUA + "\n\n╚════════════════════╝");
                });
            }
        }.runTaskTimer(this, 1, 50);

        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new ModuleMonster(getLogger(), getConfig()), this);
        this.getServer().getPluginManager().registerEvents(new ModulePlayer(getLogger(), getConfig()), this);
    }

    public MPItemHandler getItemHandler()
    {
        return itemHandler;
    }

    public static Main getInstance()
    {
        return instance;
    }
}
