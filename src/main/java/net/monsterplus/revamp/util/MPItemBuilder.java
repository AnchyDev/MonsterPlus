package net.monsterplus.revamp.util;

import net.monsterplus.main.Main;
import net.monsterplus.revamp.item.MPItem;
import net.monsterplus.revamp.item.MPItemQuality;
import net.monsterplus.revamp.item.MPItemType;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class MPItemBuilder
{
    private MPItem item;
    private int attributeTotal;

    public MPItemBuilder(MPItem item)
    {
        this.item = item;
        this.attributeTotal = 0;
    }

    public ItemStack build()
    {
        var buildItem = new ItemStack(item.getMaterial(), 1);
        var itemMeta = buildItem.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setDisplayName(getColorFromQuality(item.getQuality()) + strip(item.getDisplayName()));
        itemMeta.setLocalizedName(item.getId() + ":" + item.getDisplayName());

        var lore = new ArrayList<String>();

        if(item.getItemLevel() == 0)
        {
            var itemLevelMultiplier = (item.getAttributes().size() + item.getBonusAttributes().size() + item.getQuality().getValue()) / 2;

            item.getAttributes().forEach((key, value) ->
            {
                if(value instanceof Double)
                {
                    attributeTotal += ((Double) value).intValue();
                }
                else if(value instanceof Float)
                {
                    attributeTotal += ((Float) value).intValue();
                }
                else
                {
                    attributeTotal += (int)value;
                }
            });

            item.getBonusAttributes().forEach((key, value) ->
            {
                if(value instanceof Double)
                {
                    attributeTotal += ((Double) value).intValue();
                }
                else if(value instanceof Float)
                {
                    attributeTotal += ((Float) value).intValue();
                }
                else
                {
                    attributeTotal += (int)value;
                }
            });

            lore.add(ChatColor.YELLOW + strip("Item Level " + Math.round(attributeTotal * itemLevelMultiplier)));
        }
        else
        {
            lore.add(ChatColor.YELLOW + strip("Item Level " + item.getItemLevel()));
        }

        if(item.getAttributes().size() > 0)
        {
            lore.add("");
            lore.add(ChatColor.WHITE + strip("Attributes:"));
            item.getAttributes().forEach((key, value) ->
            {
                switch (key)
                {
                    case GENERIC_ATTACK_DAMAGE:
                        var attackDamageModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", (int)value - 1 /* 1 = HAND DAMAGE */ , AttributeModifier.Operation.ADD_NUMBER, item.getSlot());
                        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attackDamageModifier);
                        lore.add(ChatColor.WHITE + strip("◇ " + (int)value + " Attack Damage"));
                        break;
                    case GENERIC_ATTACK_SPEED:
                        var attackSpeedModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed",  (double)value - 4.0 /* 4.0 = HAND ATTACK SPEED */,  AttributeModifier.Operation.ADD_NUMBER, item.getSlot());
                        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedModifier);
                        lore.add(ChatColor.WHITE + strip("◇ " + (double)value + " Attack Speed"));
                        break;
                    case GENERIC_ARMOR:
                        var armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", (double)value,  AttributeModifier.Operation.ADD_NUMBER, item.getSlot());
                        itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
                        lore.add(ChatColor.WHITE + strip("◇ " + (double)value + " Armor"));
                        break;
                    case GENERIC_MAX_HEALTH:
                        var healthModifier = new AttributeModifier(UUID.randomUUID(), "generic.maxHealth", (double)value,  AttributeModifier.Operation.ADD_NUMBER, item.getSlot());
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, healthModifier);
                        lore.add(ChatColor.WHITE + strip("♡ " + (double)value + " Max Health"));
                        break;
                    case GENERIC_ARMOR_TOUGHNESS:
                        var armorToughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", (double)value,  AttributeModifier.Operation.ADD_NUMBER, item.getSlot());
                        itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, armorToughnessModifier);
                        lore.add(ChatColor.WHITE + strip("◇ " + (double)value + " Armor Toughness"));
                        break;

                }
            });
        }

        if(item.getBonusAttributes().size() > 0)
        {
            lore.add("");
            lore.add(ChatColor.DARK_PURPLE + strip("Bonus Attributes:"));
            item.getBonusAttributes().forEach((key, value) ->
            {
                switch (key)
                {
                    case GENERIC_ATTACK_DAMAGE:
                        var attackDamageModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", (int)value, AttributeModifier.Operation.ADD_NUMBER, item.getSlot());
                        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attackDamageModifier);
                        lore.add(ChatColor.LIGHT_PURPLE + strip("◇ " + (int)value + " Attack Damage"));
                        break;
                    case GENERIC_ATTACK_SPEED:
                        var attackSpeedModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", (double)value,  AttributeModifier.Operation.ADD_NUMBER, item.getSlot());
                        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedModifier);
                        lore.add(ChatColor.LIGHT_PURPLE + strip("◇ " + (double)value + " Attack Speed"));
                        break;
                    case GENERIC_ARMOR:
                        var armorModifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", (double)value,  AttributeModifier.Operation.ADD_NUMBER, item.getSlot());
                        itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier);
                        lore.add(ChatColor.LIGHT_PURPLE + strip("◇ " + (double)value + " Armor"));
                        break;
                    case GENERIC_MAX_HEALTH:
                        var healthModifier = new AttributeModifier(UUID.randomUUID(), "generic.maxHealth", (double)value,  AttributeModifier.Operation.ADD_NUMBER, item.getSlot());
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, healthModifier);
                        lore.add(ChatColor.WHITE + strip("♡ " + (double)value + " Max Health"));
                        break;
                    case GENERIC_ARMOR_TOUGHNESS:
                        var armorToughnessModifier = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", (double)value,  AttributeModifier.Operation.ADD_NUMBER, item.getSlot());
                        itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, armorToughnessModifier);
                        lore.add(ChatColor.LIGHT_PURPLE + strip("◇ " + (double)value + " Armor Toughness"));
                        break;

                }
            });
            lore.add("");
        }

        if(!item.getDescription().isEmpty())
        {
            lore.add(ChatColor.GRAY + strip(item.getDescription()));
        }


        itemMeta.setLore(lore);
        buildItem.setItemMeta(itemMeta);
        return buildItem;
    }

    private ChatColor getColorFromQuality(MPItemQuality quality)
    {
        switch(quality)
        {
            case TRASH:
                return ChatColor.GRAY;
            case COMMON:
                return ChatColor.WHITE;
            case UNCOMMON:
                return ChatColor.GREEN;
            case RARE:
                return ChatColor.BLUE;
            case EPIC:
                return ChatColor.DARK_PURPLE;
            case LEGENDARY:
                return ChatColor.GOLD;
            case UNKNOWN:
                return ChatColor.MAGIC;
            default:
                return ChatColor.WHITE;
        }
    }

    private String strip(String input)
    {
        return ChatColor.stripColor(input);
    }
}
