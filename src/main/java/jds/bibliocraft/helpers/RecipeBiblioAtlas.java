package jds.bibliocraft.helpers;

import java.util.HashMap;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.enchantments.EnchantmentDeathCompass;
import jds.bibliocraft.items.ItemAtlas;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeBiblioAtlas extends ShapedRecipes
{

	public RecipeBiblioAtlas(int a, int b, NonNullList<Ingredient> stacks, ItemStack stack)
	{
		super("bibliocraft:enchantedatlas", a, b, stacks, stack);
	}
	
	@Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack itemstack = this.getRecipeOutput().copy();
        
        ItemStack oldAtlas = inv.getStackInSlot(4);
        if (oldAtlas != ItemStack.EMPTY)
        {
        	NBTTagCompound tags = oldAtlas.getTagCompound();
        	NBTTagCompound newTags;
        	if (tags == null)
        	{
        		newTags = new NBTTagCompound();
        	}
        	else
        	{
        		newTags = (NBTTagCompound) tags.copy();
        	}
    		NBTTagList enchTags = new NBTTagList();
			NBTTagCompound enchantments = new NBTTagCompound();
			enchantments.setInteger("id", EnchantmentDeathCompass.getEnchantmentID(BiblioCraft.deathCompassEnch));
			enchantments.setInteger("lvl", 1);
			enchTags.appendTag(enchantments);
			//newTags.setTag("StoredEnchantments", enchTags);
			newTags.setTag("ench", enchTags);
			itemstack.setTagCompound(newTags);
			itemstack.setItemDamage(oldAtlas.getItemDamage());
        	
        }
        return itemstack;
    }
	
	@Override
	public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_)
    {
		//System.out.println("checks for matches");
        for (int i = 0; i <= 3 - this.recipeWidth; ++i)
        {
            for (int j = 0; j <= 3 - this.recipeHeight; ++j)
            {
                if (this.checkMatch(p_77569_1_, i, j, true))
                {
                    return true;
                }

                if (this.checkMatch(p_77569_1_, i, j, false))
                {
                    return true;
                }
            }
        }
        return false;
    }
	
	private boolean checkMatch(InventoryCrafting p_77573_1_, int p_77573_2_, int p_77573_3_, boolean p_77573_4_)
	{
        for (int k = 0; k < 3; ++k)
        {
            for (int l = 0; l < 3; ++l)
            {
                int i1 = k - p_77573_2_;
                int j1 = l - p_77573_3_;
                Ingredient itemstack = Ingredient.EMPTY;

                if (i1 >= 0 && j1 >= 0 && i1 < this.recipeWidth && j1 < this.recipeHeight)
                {
                    if (p_77573_4_)
                    {
                        itemstack = this.recipeItems.get(this.recipeWidth - i1 - 1 + j1 * this.recipeWidth);
                    }
                    else
                    {
                        itemstack = this.recipeItems.get(i1 + j1 * this.recipeWidth);
                    }
                }

                ItemStack itemstack1 = p_77573_1_.getStackInRowAndColumn(k, l);

                if (itemstack1 != ItemStack.EMPTY || itemstack != Ingredient.EMPTY)
                {
                    if (itemstack1 == ItemStack.EMPTY && itemstack != Ingredient.EMPTY || itemstack1 != ItemStack.EMPTY && itemstack == Ingredient.EMPTY)
                    {
                    	
                        return false;
                    }
                    ItemStack stack = (itemstack.getMatchingStacks())[0];
                    if (stack.getItem() != itemstack1.getItem())
                    {
                    	
                        return false;
                    }

                    if (stack.getItemDamage() >= 32767)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
	
    public static IRecipe addAtlasEnchantRecipe(ItemStack stack, Object ... stuff)
    {
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;
        if (stuff[i] instanceof String[])
        {
            String[] astring = (String[])((String[])stuff[i++]);

            for (int l = 0; l < astring.length; ++l)
            {
                String s1 = astring[l];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        }
        else
        {
            while (stuff[i] instanceof String)
            {
                String s2 = (String)stuff[i++];
                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = new HashMap(); i < stuff.length; i += 2)
        {
            Character character = (Character)stuff[i];
            Ingredient itemstack1 = Ingredient.EMPTY;

            if (stuff[i + 1] instanceof Item)
            {
                itemstack1 = Ingredient.fromItem((Item)stuff[i + 1]);//new ItemStack((Item)stuff[i + 1]);
            }
            else if (stuff[i + 1] instanceof Block)
            {
                itemstack1 = Ingredient.fromStacks(new ItemStack((Block)stuff[i + 1], 1, 32767));
            }
            else if (stuff[i + 1] instanceof ItemStack)
            {
                itemstack1 = Ingredient.fromStacks((ItemStack)stuff[i + 1]);
            }

            hashmap.put(character, itemstack1);
        }
        //ItemStack[] aitemstack = new ItemStack[j * k];
        NonNullList<Ingredient> aitemstack = NonNullList.<Ingredient>withSize(j * k, Ingredient.EMPTY);//NonNullList.<Ingredient>withsize;
        for (int i1 = 0; i1 < j * k; ++i1)
        {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(Character.valueOf(c0)))
            {
                aitemstack.set(i1, ((Ingredient)hashmap.get(Character.valueOf(c0))));
            }
            else
            {
                aitemstack.set(i1,  Ingredient.EMPTY);//[i1] = ItemStack.EMPTY;
            }
        }
        if (aitemstack.size() >=5)
        {
	        //ItemStack oldAtlas = aitemstack.get(4); 
	        Ingredient t = aitemstack.get(4); 
	        ItemStack[] m = t.getMatchingStacks();
	        ItemStack oldAtlas = m[0];
	        if (oldAtlas != ItemStack.EMPTY)
	        {
	        	if (oldAtlas.getItem() instanceof ItemAtlas)
	        	{
	        		NBTTagCompound tags = oldAtlas.getTagCompound();
	        		if (tags == null)
	        		{
	        			tags = new NBTTagCompound();
	        		}
	        		if (tags != null)
	        		{
	        			NBTTagList enchTags = new NBTTagList();
	        			NBTTagCompound enchantments = new NBTTagCompound();
	        			enchantments.setInteger("id", EnchantmentDeathCompass.getEnchantmentID(BiblioCraft.deathCompassEnch));
	        			enchantments.setInteger("lvl", 1);
	        			enchTags.appendTag(enchantments);
	        			//tags.setTag("StoredEnchantments", enchTags);
	        			tags.setTag("ench", enchTags);
	        			stack.setTagCompound(tags);
	        		}
	        	}
	        }
        }
        IRecipe shapedrecipes = new RecipeBiblioAtlas(j, k, aitemstack, stack); // TODO this is broken, so broken
        return shapedrecipes;
    }
}
