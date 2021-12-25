package jds.bibliowoods.natura;

import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.helpers.BiblioWoodHelperTab;
import jds.bibliocraft.helpers.RegisterCustomFramedBlocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid=BiblioWoodsNatura.MODID, name="BiblioWoodNatura", version=BiblioWoodsNatura.VERSION, dependencies="after:bibliocraft; after:natura")

public class BiblioWoodsNatura 
{
    public static final String MODID = "bibliowoodsnatura";
    public static final String VERSION = "2.0";
    public static CreativeTabs creativeTab;
    
	public boolean modloaded = Loader.isModLoaded("natura");
	public boolean biblioLoaded = Loader.isModLoaded("bibliocraft");
	
	@Mod.Instance(MODID)
	public static BiblioWoodsNatura instance;
	
	@Mod.EventHandler 
	public void load(FMLInitializationEvent event) 
	{
		modloaded = Loader.isModLoaded("natura");
		biblioLoaded = Loader.isModLoaded("bibliocraft");
		
		if (biblioLoaded && modloaded)
		{
			Block planks1 = Block.REGISTRY.getObject(new ResourceLocation("natura:overworld_planks")); 
			Block planks2 = Block.REGISTRY.getObject(new ResourceLocation("natura:nether_planks")); 
			Block slabs1 = Block.REGISTRY.getObject(new ResourceLocation("natura:overworld_slab")); 
			Block slabs2 = Block.REGISTRY.getObject(new ResourceLocation("natura:overworld_slab2")); 
			Block slabs3 = Block.REGISTRY.getObject(new ResourceLocation("natura:nether_slab")); 

			Block[] planks = {planks1, planks1, planks1, planks1, planks1, planks1, planks1, planks1, planks1, planks2, planks2, planks2, planks2};
			int[] plankID = {0,1,2,3,4,5,6,7,8,0,1,2,3};
			Block[] slabs = {slabs1, slabs1, slabs1, slabs1, slabs1, slabs2, slabs2, slabs2, slabs2, slabs3, slabs3, slabs3, slabs3};
			int[] slabID = {0,1,2,3,4,0,1,2,3,0,1,2,3};

			String[] textures = {
					"natura:blocks/planks/overworld/maple_planks",
					"natura:blocks/planks/overworld/silverbell_planks",
					"natura:blocks/planks/overworld/amaranth_planks",
					"natura:blocks/planks/overworld/tiger_planks",
					"natura:blocks/planks/overworld/willow_planks",
					"natura:blocks/planks/overworld/eucalyptus_planks",
					"natura:blocks/planks/overworld/hopseed_planks",
					"natura:blocks/planks/overworld/sakura_planks",
					"natura:blocks/planks/overworld/redwood_planks",
					"natura:blocks/planks/nether/ghostwood_planks",
					"natura:blocks/planks/nether/bloodwood_planks",
					"natura:blocks/planks/nether/darkwood_planks",
					"natura:blocks/planks/nether/fusewood_planks"
			};
			
			ItemStack icon = new ItemStack(BlockBookcase.instance, 1, 6);
			NBTTagCompound tags = new NBTTagCompound();
			tags.setString("renderTexture", "natura:blocks/planks/overworld/tiger_planks");
			icon.setTagCompound(tags);
			creativeTab = new BiblioWoodHelperTab("bibliowoodnaturatab", textures, icon);
			for (int i = 0; i < textures.length; i++)
			{
				RegisterCustomFramedBlocks reg = new RegisterCustomFramedBlocks(textures[i]);
				reg.registerRecipies(new ItemStack(planks[i], 1, plankID[i]), new ItemStack(slabs[i], 1, slabID[i]));
			}

		}
		else
		{
			FMLLog.warning("BiblioWoods Natura edition failed to load");
			FMLLog.warning("Is BiblioCraft loaded?   "+biblioLoaded);
			FMLLog.warning("Is Natura loaded?   "+modloaded);
		}
	}
}
