package net.monsterplus.revamp.item;

import net.monsterplus.revamp.util.MPItemBuilder;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MPItem
{
    private String id;
    private String name;
    private MPItemType itemType;
    private EquipmentSlot slot;
    private Material material;
    private MPItemQuality quality;
    private int itemLevel;
    private ItemStack item;
    private LinkedHashMap<Attribute, Object> attributes;
    private LinkedHashMap<Attribute, Object> bonusAttributes;
    private String description;

    public MPItem(String identifier, String displayName, MPItemType itemType, Material material, EquipmentSlot slot)
    {
        this.id = identifier;
        this.name = displayName;
        this.itemType = itemType;
        this.material = material;
        this.slot = slot;

        this.quality = MPItemQuality.COMMON;
        this.itemLevel = 0;
        this.attributes = new LinkedHashMap<>();
        this.bonusAttributes = new LinkedHashMap<>();
        this.description = "";
    }

    public EquipmentSlot getSlot()
    {
        return slot;
    }

    public void addAttribute(Attribute attribute, Object value)
    {
        attributes.put(attribute, value);
    }

    public void addBonusAttribute(Attribute attribute, Object value)
    {
        bonusAttributes.put(attribute, value);
    }

    public MPItemQuality getQuality()
    {
        return quality;
    }

    public void setQuality(MPItemQuality quality)
    {
        this.quality = quality;
    }

    public int getItemLevel()
    {
        return itemLevel;
    }

    public void setItemLevel(int itemLevel)
    {
        this.itemLevel = itemLevel;
    }

    public Material getMaterial()
    {
        return material;
    }

    public MPItemType getItemType()
    {
        return itemType;
    }

    public String getId()
    {
        return id;
    }

    public String getDisplayName()
    {
        return name;
    }

    public LinkedHashMap<Attribute, Object> getBonusAttributes()
    {
        return bonusAttributes;
    }

    public LinkedHashMap<Attribute, Object> getAttributes()
    {
        return attributes;
    }

    public ItemStack getItem()
    {
        return getItem(true);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public ItemStack getItem(boolean getCacheIfPossible)
    {
        if(item == null || !getCacheIfPossible)
        {
            var itemBuilder = new MPItemBuilder(this);
            item = itemBuilder.build();
        }

        return item;
    }
}
