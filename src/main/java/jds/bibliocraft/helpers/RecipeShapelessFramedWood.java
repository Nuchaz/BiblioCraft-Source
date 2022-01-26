package jds.bibliocraft.helpers;

import java.util.ArrayList;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class RecipeShapelessFramedWood extends ShapelessRecipes
{
	//private static String textureString = "none";
	private static ArrayList<WoodRegistryEntry> registry;

	public RecipeShapelessFramedWood(ItemStack output, NonNullList<Ingredient> inputList) 
	{
		super("", output, inputList);
		if (registry == null)
			registry = new ArrayList<WoodRegistryEntry>();
	}

	public static IRecipe addShapedWoodRecipe(ItemStack stack, WoodRegistryEntry entry, Object ... stuff)
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
		
        NBTTagCompound tags = new NBTTagCompound();
        tags.setString("renderTexture", entry.getTextureString());
        stack.setTagCompound(tags);
        
		IRecipe shapedrecipe = new RecipeShapelessFramedWood(stack, inputstacks);
		return shapedrecipe;
	}
	
	@Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
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
        NBTTagCompound tags = new NBTTagCompound();
        tags.setString("renderTexture", texture);
        itemstack.setTagCompound(tags);
        return itemstack;
    }
	
	private WoodRegistryEntry foundMatch(ItemStack stack)
	{
		//boolean result = false;
		WoodRegistryEntry result = new WoodRegistryEntry("none", "none", "none", false);
		
		for (int i = 0; i < registry.size(); i++)
		{
			WoodRegistryEntry entry = registry.get(i);
			if (entry.hasMatch(stack.getUnlocalizedName()))
			{
				result = entry;
				break;
			}
		}
		
		return result;
	}
}
