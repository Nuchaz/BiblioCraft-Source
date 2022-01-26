package jds.bibliocraft.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class RecipeBiblioFramedWood extends ShapedRecipes
{
	//private static String textureString = "none"; // TODO this fails, it only registered this class 1 time and thus the textureString becomes the last recipe registered
	// and also returns the last textureString reguardless of what wood goes into recipie
	// TODO, maybe I should make a string registry
	private static ArrayList<WoodRegistryEntry> registry;

	public RecipeBiblioFramedWood(int width, int height, NonNullList<Ingredient> ingredientsIn, ItemStack output) 
	{
		super("", width, height, ingredientsIn, output);
		if (registry == null)
			registry = new ArrayList<WoodRegistryEntry>();
		
	}
	
	public static IRecipe addShapedWoodRecipe(ItemStack stack, WoodRegistryEntry entry, Object ... stuff)
	{
		if (registry == null)
			registry = new ArrayList<WoodRegistryEntry>();
		
		registry.add(entry);
		
		//textureString = texture;
		String recipe = "";
		int i = 0;
		int width = 0;
		int height = 0;

        if (stuff[i] instanceof String[])
        {
            String[] astring = (String[])((String[])stuff[i++]);

            for (int l = 0; l < astring.length; ++l)
            {
                String s1 = astring[l];
                ++height;
                width = s1.length();
                recipe = recipe + s1;
            }
        }
        else
        {
            while (stuff[i] instanceof String)
            {
                String s2 = (String)stuff[i++];
                ++height;
                width = s2.length();
                recipe = recipe + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = new HashMap(); i < stuff.length; i += 2)
        {
            Character character = (Character)stuff[i];
            ItemStack itemstack1 = ItemStack.EMPTY;

            if (stuff[i + 1] instanceof Item)
            {
                itemstack1 = new ItemStack((Item)stuff[i + 1]);
            }
            else if (stuff[i + 1] instanceof Block)
            {
                itemstack1 = new ItemStack((Block)stuff[i + 1], 1, 32767);
            }
            else if (stuff[i + 1] instanceof ItemStack)
            {
                itemstack1 = (ItemStack)stuff[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        NonNullList<Ingredient> stackarray = NonNullList.<Ingredient>create();
        //ItemStack[] stackarray = new ItemStack[width * height];

        for (int j = 0; j < width * height; ++j)
        {
            char c0 = recipe.charAt(j);

            if (hashmap.containsKey(Character.valueOf(c0)))
            {
            	// TODO this is totally broken
            	//ItemStack thestack = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
            	//stackarray.add(((Ingredient)thestack);
            }
            //else
           // {
           //     stackarray[j] = ItemStack.EMPTY;
           // }
        }

        NBTTagCompound tags = new NBTTagCompound();
        tags.setString("renderTexture", entry.getTextureString());
        stack.setTagCompound(tags);
		IRecipe shapedrecipe = new RecipeBiblioFramedWood(width, height, stackarray, stack);
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
        // TODO here is where it happens I think, I need to call from the registry.
        
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
