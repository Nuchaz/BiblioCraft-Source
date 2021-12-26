package jds.bibliocraft.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class RecipeShapelessFramedWood extends ShapelessRecipe
{
	//private static String textureString = "none";
	private static ArrayList<WoodRegistryEntry> registry;

	public RecipeShapelessFramedWood(String name, ItemStack output, NonNullList<Ingredient> inputList) 
	{
		super(new ResourceLocation(name), "", output, inputList);
		if (registry == null)
			registry = new ArrayList<WoodRegistryEntry>();
	}

	// TODO I might have screwed this up. Dunno.
	public static IRecipe addShapedWoodRecipe(String name, ItemStack stack, WoodRegistryEntry entry, Object ... stuff)
	{
		if (registry == null)
			registry = new ArrayList<WoodRegistryEntry>();
		
		registry.add(entry);
		//textureString = texture.getTextureString();
		NonNullList<Ingredient> inputstacks = NonNullList.<Ingredient>create();
		for (int i = 0; i < stuff.length; i++)
		{
			if (stuff[i] instanceof ItemStack)
			{
				inputstacks.add((Ingredient)stuff[i]);
			}
		}
		
        CompoundNBT tags = new CompoundNBT();
        tags.putString("renderTexture", entry.getTextureString());
        stack.setTag(tags);
        
		IRecipe shapedrecipe = new RecipeShapelessFramedWood(name, stack, inputstacks);
		return shapedrecipe;
	}
	
	@Override
    public ItemStack getCraftingResult(CraftingInventory inv)
    {
        ItemStack itemstack = this.getRecipeOutput().copy();
        WoodRegistryEntry match;
        String texture = "none";
        for (int i = 0; i < 9; i++)
        {
        	match = foundMatch(inv.getStackInSlot(i));
        	if (match.getIfReal())
        	{
        		texture = match.getTextureString();
        		break;
        	}
        }
        CompoundNBT tags = new CompoundNBT();
        tags.putString("renderTexture", texture);
        itemstack.setTag(tags);
        return itemstack;
    }
	
	private WoodRegistryEntry foundMatch(ItemStack stack)
	{
		//boolean result = false;
		WoodRegistryEntry result = new WoodRegistryEntry("none", "none", "none", false);
		
		for (int i = 0; i < registry.size(); i++)
		{
			WoodRegistryEntry entry = registry.get(i);
			if (entry.hasMatch(stack.getDisplayName().getUnformattedComponentText()))
			{
				result = entry;
				break;
			}
		}
		
		return result;
	}
}
