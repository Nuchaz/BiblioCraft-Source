package jds.bibliowoods.bop;

import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.helpers.BiblioWoodHelperTab;
import jds.bibliocraft.helpers.RecipeBiblioFramedWood;
import jds.bibliocraft.helpers.RegisterCustomFramedBlocks;
import net.minecraft.block.Block;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid=BiblioWoodsBoP.MODID, name="BiblioWoodBoP", version=BiblioWoodsBoP.VERSION, dependencies="after:bibliocraft; after:biomesoplenty")

public class BiblioWoodsBoP 
{
    public static final String MODID = "bibliowoodsbop";
    public static final String VERSION = "2.0";
    public static CreativeTabs creativeTab;
    
	boolean modloaded = Loader.isModLoaded("biomesoplenty");
	boolean biblioLoaded = Loader.isModLoaded("bibliocraft");
    
	@Mod.Instance(MODID)
	public static BiblioWoodsBoP instance;
	
	@Mod.EventHandler 
	public void load(FMLInitializationEvent event) 
	{
		// TODO the recipe for hellbark gave me eucalyptus
		modloaded = Loader.isModLoaded("biomesoplenty");
		biblioLoaded = Loader.isModLoaded("bibliocraft");
		//modloaded = true;
		if (biblioLoaded && modloaded)
		{
			Block planks = Block.REGISTRY.getObject(new ResourceLocation("biomesoplenty:planks_0")); 
			Block slabs1 = Block.REGISTRY.getObject(new ResourceLocation("biomesoplenty:wood_slab_0"));
			Block slabs2 = Block.REGISTRY.getObject(new ResourceLocation("biomesoplenty:wood_slab_1"));
			Block[] slabs = {slabs1, slabs1, slabs1, slabs1, slabs1, slabs1, slabs1, slabs1, slabs2, slabs2, slabs2, slabs2, slabs2, slabs2, slabs2, slabs2};
			int[] slabMetas = {0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7};
			String[] textures = {
					"biomesoplenty:blocks/sacred_oak_planks",
					"biomesoplenty:blocks/cherry_planks",
					"biomesoplenty:blocks/umbran_planks",
					"biomesoplenty:blocks/fir_planks",
					"biomesoplenty:blocks/ethereal_planks",
					"biomesoplenty:blocks/magic_planks",
					"biomesoplenty:blocks/mangrove_planks",
					"biomesoplenty:blocks/palm_planks",
					"biomesoplenty:blocks/redwood_planks",
					"biomesoplenty:blocks/willow_planks",
					"biomesoplenty:blocks/pine_planks",
					"biomesoplenty:blocks/hellbark_planks",
					"biomesoplenty:blocks/jacaranda_planks",
					"biomesoplenty:blocks/mahogany_planks",
					"biomesoplenty:blocks/ebony_planks",
					"biomesoplenty:blocks/eucalyptus_planks"
				};
			ItemStack icon = new ItemStack(BlockBookcase.instance, 1, 6);
			NBTTagCompound tags = new NBTTagCompound();
			tags.setString("renderTexture", "biomesoplenty:blocks/sacred_oak_planks");
			icon.setTagCompound(tags);
			creativeTab = new BiblioWoodHelperTab("bibliowoodboptab", textures, icon);
			for (int i = 0; i < textures.length; i++)
			{
				RegisterCustomFramedBlocks reg = new RegisterCustomFramedBlocks(textures[i]);
				reg.registerRecipies(new ItemStack(planks, 1, i), new ItemStack(slabs[i], 1, slabMetas[i]));
			}
		}
		else
		{
			FMLLog.warning("BiblioWoods Biomes O Plenty edition failed to load");
			FMLLog.warning("Is BiblioCraft loaded?   "+biblioLoaded);
			FMLLog.warning("Is BiomesOPlenty loaded?   "+modloaded);
		}
	}
	
}
