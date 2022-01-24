package jds.bibliowoods.botania;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid=BiblioWoodsBotania.MODID, name="BiblioWoodBotania", version=BiblioWoodsBotania.VERSION, dependencies="after:bibliocraft; after:botania")

public class BiblioWoodsBotania 
{
    public static final String MODID = "bibliowoodsbotania";
    public static final String VERSION = "2.0";
    public static CreativeTabs creativeTab;
    
	public boolean modloaded = Loader.isModLoaded("botania");
	public boolean biblioLoaded = Loader.isModLoaded("bibliocraft");
	
	@Mod.Instance(MODID)
	public static BiblioWoodsBotania instance;
	
	@Mod.EventHandler 
	public void load(FMLInitializationEvent event) 
	{
		modloaded = Loader.isModLoaded("botania");
		biblioLoaded = Loader.isModLoaded("bibliocraft");
		
		if (biblioLoaded && modloaded)
		{
			// TODO these work, but I am not sure how excited I am about botania?, maybe I could ask twitter what blocks i hsould do?
			Block planks = Block.REGISTRY.getObject(new ResourceLocation("botania:shimmerwoodPlanks"));
			Block planks2 = Block.REGISTRY.getObject(new ResourceLocation("botania:livingwood"));
			Block planks3 = Block.REGISTRY.getObject(new ResourceLocation("botania:dreamwood"));
			Block planks5 = Block.REGISTRY.getObject(new ResourceLocation("botania:livingrock"));
			testBlock(planks);
			testBlock(planks2);
			testBlock(planks3);
			testBlock(planks5);
		}
		else
		{
			FMLLog.warning("BiblioWoods Botania edition failed to load");
			FMLLog.warning("Is BiblioCraft loaded?   "+biblioLoaded);
			FMLLog.warning("Is BiomesOPlenty loaded?   "+modloaded);
		}
	}
	
	public void testBlock(Block block)
	{
		if (block != null)
		{
			for (int i = 0; i < 16; i++)
			{
				ItemStack stack = new ItemStack(block, 1, i);
				if (stack != ItemStack.EMPTY)
					System.out.println("stack " + i + "   = " + stack.getDisplayName() + "   unlocalized = " + stack.getUnlocalizedName());
			}
		}
	}
}
