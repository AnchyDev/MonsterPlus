package net.monsterplus.item;

import net.monsterplus.main.Main;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public abstract class CustomItem
{
    //TODO:
    //ADD GET SET RECIPE
    //STORE IT AS AN ARRAY OF ITEMSTACK
    //CHECK THE NON-NULL VALUES AND COMPARE AGAINST MATRIX FOR METADATA
    //NAME : SPLIT LENGTH OVER 1
    //RECIPENAME == MATRIX NAME
    private ItemStack item;

    public ItemStack getItemStack()
    {
        return item;
    }

    public void setItemStack(ItemStack item)
    {
        this.item = item;
    }

    public abstract ItemStack[] getRecipe();

    public boolean assertCraftable(ItemStack[] matrix)
    {
        var recipe = getRecipe();

        //If there is no recipe available then recipe will have a length of 0.
        if(recipe.length < 1)
        {
            return false;
        }

        int i = 0;
        for(var item : recipe)
        {
            //If the recipe item is null then is doesn't matter, it's just an empty slot.
            if(item == null)
            {
                i += 1;
                continue;
            }

            //If for whatever reason this is null then the recipe result shouldn't be showing in the first place.
            if (matrix[i] == null)
            {
                return false;
            }

            //One of the items don't match, abort recipe result.
            if(!matrix[i].getItemMeta().getLocalizedName().equalsIgnoreCase(item.getItemMeta().getLocalizedName()))
            {
                return false;
            }

            i += 1;
        }

        return true;
    }
}
