package jds.bibliocraft.helpers;

import java.util.ArrayList;

import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockArmorStand;
import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.blocks.BlockBookcaseCreative;
import jds.bibliocraft.blocks.BlockCase;
import jds.bibliocraft.blocks.BlockClock;
import jds.bibliocraft.blocks.BlockDesk;
import jds.bibliocraft.blocks.BlockFancySign;
import jds.bibliocraft.blocks.BlockFancyWorkbench;
import jds.bibliocraft.blocks.BlockFramedChest;
import jds.bibliocraft.blocks.BlockFurniturePaneler;
import jds.bibliocraft.blocks.BlockLabel;
import jds.bibliocraft.blocks.BlockMapFrame;
import jds.bibliocraft.blocks.BlockPaintingFrameBorderless;
import jds.bibliocraft.blocks.BlockPaintingFrameFancy;
import jds.bibliocraft.blocks.BlockPaintingFrameFlat;
import jds.bibliocraft.blocks.BlockPaintingFrameMiddle;
import jds.bibliocraft.blocks.BlockPaintingFrameSimple;
import jds.bibliocraft.blocks.BlockPotionShelf;
import jds.bibliocraft.blocks.BlockSeat;
import jds.bibliocraft.blocks.BlockShelf;
import jds.bibliocraft.blocks.BlockTable;
import jds.bibliocraft.blocks.BlockToolRack;
import jds.bibliocraft.items.ItemFramingSaw;
import jds.bibliocraft.items.ItemSeatBack;
import jds.bibliocraft.items.ItemSeatBack2;
import jds.bibliocraft.items.ItemSeatBack3;
import jds.bibliocraft.items.ItemSeatBack4;
import jds.bibliocraft.items.ItemSeatBack5;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RegisterCustomFramedBlocks 
{
	private static final int framedBlocks = 27; // total number of framed blocks
	private ArrayList<ItemStack> blockList;
	private String textureString = "none";
	private boolean enabledMap[];
	
	public RegisterCustomFramedBlocks(String texture)
	{
		this.enabledMap = new boolean[framedBlocks];
		this.enabledMap[0] = Config.enableBookcase;
		this.enabledMap[1] = Config.enableBookcase;
		this.enabledMap[2] = Config.enableFramedChest;
		this.enabledMap[3] = Config.enableFancyWorkbench;
		this.enabledMap[4] = Config.enableFurniturePaneler;
		this.enabledMap[5] = Config.enableGenericshelf;
		this.enabledMap[6] = Config.enableToolrack;
		this.enabledMap[7] = Config.enablePotionshelf;
		this.enabledMap[8] = Config.enableClock;
		this.enabledMap[9] = Config.enablePainting;
		this.enabledMap[10] = Config.enablePainting;
		this.enabledMap[11] = Config.enablePainting;
		this.enabledMap[12] = Config.enablePainting;
		this.enabledMap[13] = Config.enablePainting;
		this.enabledMap[14] = Config.enableWeaponcase;
		this.enabledMap[15] = Config.enableSeat;
		this.enabledMap[16] = Config.enableWritingdesk;
		this.enabledMap[17] = Config.enableMapFrame;
		this.enabledMap[18] = Config.enableWoodLabel;
		this.enabledMap[19] = Config.enableArmorstand;
		this.enabledMap[20] = Config.enableTable;
		this.enabledMap[21] = Config.enableFancySign;
		this.enabledMap[22] = Config.enableSeat;
		this.enabledMap[23] = Config.enableSeat;
		this.enabledMap[24] = Config.enableSeat;
		this.enabledMap[25] = Config.enableSeat;
		this.enabledMap[26] = Config.enableSeat;
		this.textureString = texture;
		this.blockList = new ArrayList<ItemStack>();
		ItemStack bookcase = new ItemStack(BlockBookcase.instance, 1, 6);
		this.blockList.add(new ItemStack(BlockBookcase.instance, 1, 6));                 //0 Bookcase
		this.blockList.add(new ItemStack(BlockBookcaseCreative.instance, 1, 6));         //1 Creative Bookcase
		this.blockList.add(new ItemStack(BlockFramedChest.instance, 1, 6));              //2 Framed Chest
		this.blockList.add(new ItemStack(BlockFancyWorkbench.instance, 1, 6));           //3 Fancy Workbench
		this.blockList.add(new ItemStack(BlockFurniturePaneler.instance, 1, 6));         //4 Paneler
		this.blockList.add(new ItemStack(BlockShelf.instance, 1, 6));                    //5 Shelf
		this.blockList.add(new ItemStack(BlockToolRack.instance, 1, 6));                 //6 Tool Rack
		this.blockList.add(new ItemStack(BlockPotionShelf.instance, 1, 6));              //7 Potion Shelf
		this.blockList.add(new ItemStack(BlockClock.instance, 1, 6));                    //8 Clock
		this.blockList.add(new ItemStack(BlockPaintingFrameBorderless.instance, 1, 6));  //9 Painting Frame Borderless // recipes all contin slabs, blocks, or this borderless
		this.blockList.add(new ItemStack(BlockPaintingFrameFlat.instance, 1, 6));        //10 Painting Frame Flat
		this.blockList.add(new ItemStack(BlockPaintingFrameSimple.instance, 1, 6));      //11 Painting Frame Simple
		this.blockList.add(new ItemStack(BlockPaintingFrameMiddle.instance, 1, 6));      //12 Painting Frame Middle
		this.blockList.add(new ItemStack(BlockPaintingFrameFancy.instance, 1, 6));       //13 Painting Frame Fancy
		this.blockList.add(new ItemStack(BlockCase.instance, 1, 6));                     //14 Case
		this.blockList.add(new ItemStack(BlockSeat.instance, 1, 6));                     //15 Seat
		this.blockList.add(new ItemStack(BlockDesk.instance, 1, 6));                     //16 Desk
		this.blockList.add(new ItemStack(BlockMapFrame.instance, 1, 6));                 //17 Map Frame
		this.blockList.add(new ItemStack(BlockLabel.instance, 1, 6));                    //18 Label
		this.blockList.add(new ItemStack(BlockArmorStand.instance, 1, 6));               //19 Armor Stand
		this.blockList.add(new ItemStack(BlockTable.instance, 1, 6));                    //20 Table
		this.blockList.add(new ItemStack(BlockFancySign.instance, 1, 6));                //21 Fancy Sign
		this.blockList.add(new ItemStack(ItemSeatBack.instance, 1, 6));                  //22 Seat Back
		this.blockList.add(new ItemStack(ItemSeatBack2.instance, 1, 6));                 //23 Seat Back 2
		this.blockList.add(new ItemStack(ItemSeatBack3.instance, 1, 6));                 //24 Seat Back 3
		this.blockList.add(new ItemStack(ItemSeatBack4.instance, 1, 6));                 //25 Seat Back 4
		this.blockList.add(new ItemStack(ItemSeatBack5.instance, 1, 6));                 //26 Seat Back 5
		NBTTagCompound tags = new NBTTagCompound();
		tags.setString("renderTexture", this.textureString);
		for (int i = 0; i < blockList.size(); i++)
		{
			this.blockList.get(i).setTagCompound(tags); 
		}
	}
	
	public ArrayList<ItemStack> getFramedBlockList()
	{
		return this.blockList;
	}
	
	public boolean[] getEnableList()
	{
		return this.enabledMap;
	}
	
	public void registerRecipies(ItemStack plank, ItemStack slab)
	{
		WoodRegistryEntry recipeStrings = new WoodRegistryEntry(slab.getUnlocalizedName(), plank.getUnlocalizedName(), this.textureString, true); 
		
		ItemStack stick = new ItemStack(Items.STICK, 1, 0);
		ItemStack whiteWool = new ItemStack(Blocks.WOOL, 1, 0);
		ItemStack saw = new ItemStack(ItemFramingSaw.instance, 1, 0);
		ItemStack ironIngot = new ItemStack(Items.IRON_INGOT,1,0);
		ItemStack goldIngot = new ItemStack(Items.GOLD_INGOT, 1, 0);
		ItemStack vanclock = new ItemStack(Items.CLOCK, 1, 0);
		ItemStack sign = new ItemStack(Items.SIGN, 1, 0);
		ItemStack paper = new ItemStack(Items.PAPER, 1, 0);
		ItemStack craftingBench = new ItemStack(Blocks.CRAFTING_TABLE, 1, 0);
		ItemStack feather = new ItemStack(Items.FEATHER, 1, 0);
		ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE, 1, 0);
		ItemStack glassPane = new ItemStack(Blocks.GLASS, 1, 0);
		ItemStack torch = new ItemStack(Blocks.TORCH, 1, 0);
		ItemStack woodPP = new ItemStack(Blocks.WOODEN_PRESSURE_PLATE, 1, 0);
		
		/* TODO All recipes are bra-oke-en
		//GameRegistry.addShapedRecipe(name, group, output, params);
		if (Config.enableBookcase)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(0), recipeStrings, new Object[]{"XYX", "XYX", "XYX", Character.valueOf('X'), plank, Character.valueOf('Y'), slab}));
			
		if (Config.enableFramedChest && Config.enableWoodLabel)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(2), recipeStrings, new Object[]{ "SSS", "SLS", "SSS", Character.valueOf('S'), plank, Character.valueOf('L'), this.blockList.get(18)}));
		
		if (Config.enableFancyWorkbench && Config.enableBookcase)
		{
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(3), recipeStrings, new Object[]{"ITF", "SBS", "SSS", Character.valueOf('I'), "dyeBlack", Character.valueOf('T'), craftingBench, Character.valueOf('F'), feather, Character.valueOf('S'), slab, Character.valueOf('B'), this.blockList.get(0)}));
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(3), recipeStrings, new Object[]{"FTI", "SBS", "SSS", Character.valueOf('I'), "dyeBlack", Character.valueOf('T'), craftingBench, Character.valueOf('F'), feather, Character.valueOf('S'), slab, Character.valueOf('B'), this.blockList.get(0)}));
		}
	
		if (Config.enableFurniturePaneler)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(4), recipeStrings, new Object[]{"IFI", "SSS", "PPP", Character.valueOf('S'), slab, Character.valueOf('F'), saw, Character.valueOf('P'), plank, Character.valueOf('I'), ironIngot}));
		
		if (Config.enableGenericshelf)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(5), recipeStrings, new Object[]{"YYY", " X ", "YYY", Character.valueOf('X'), plank, Character.valueOf('Y'), slab}));
		
		if (Config.enableToolrack)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(6), recipeStrings, new Object[]{"YYY", "YXY", "YYY", Character.valueOf('X'), ironIngot, Character.valueOf('Y'), slab}));
			
		if (Config.enablePotionshelf)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(7), recipeStrings, new Object[]{"YYY", "XBX", "YYY", Character.valueOf('X'), plank, Character.valueOf('Y'), slab, Character.valueOf('B'), emptyBottle}));

		if (Config.enableClock)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(8), recipeStrings, new Object[]{ "SCS", "STS", "SGS", Character.valueOf('S'), slab, Character.valueOf('C'), vanclock, Character.valueOf('T'), stick, Character.valueOf('G'), goldIngot}));
		
		if (Config.enablePainting)
		{
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(9), recipeStrings, new Object[]{"TST", "SSS", "TST", Character.valueOf('T'), stick, Character.valueOf('S'), slab})); 
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(10), recipeStrings, new Object[]{"SSS", "SBS", "SSS", Character.valueOf('B'), this.blockList.get(9), Character.valueOf('S'), slab}));
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(11), recipeStrings, new Object[]{"TST", "SBS", "TST", Character.valueOf('T'), stick, Character.valueOf('S'), slab, Character.valueOf('B'), this.blockList.get(9)}));
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(12), recipeStrings, new Object[]{"TST", "TBT", "TST", Character.valueOf('T'), stick, Character.valueOf('S'), slab, Character.valueOf('B'), this.blockList.get(9)}));
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(13), recipeStrings, new Object[]{"TTT", "TBT", "TTT", Character.valueOf('T'), stick, Character.valueOf('B'), this.blockList.get(9)}));
		}
		
		if (Config.enableWeaponcase)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(14), recipeStrings, new Object[]{"YZY", "YXY", "YYY", Character.valueOf('X'), whiteWool, Character.valueOf('Y'), slab, Character.valueOf('Z'), glassPane}));
		
		if (Config.enableWritingdesk)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(16), recipeStrings, new Object[]{"T F", "XXX", "Y Y", Character.valueOf('T'), torch, Character.valueOf('F'), feather, Character.valueOf('X'), slab, Character.valueOf('Y'), plank}));
			
		if (Config.enableMapFrame)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(17), recipeStrings, new Object[]{"SSS", "SXS", "SSS", Character.valueOf('S'), stick, Character.valueOf('X'), slab})); // TODO the stick didn't work?
			
		if (Config.enableWoodLabel)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(18), recipeStrings, new Object[]{"YYY", "YYY", Character.valueOf('Y'), slab}));
		
		if (Config.enableArmorstand)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(19), recipeStrings, new Object[]{" Y ", " Y ", "XXX", Character.valueOf('X'), slab, Character.valueOf('Y'), stick}));
		
		if (Config.enableTable)
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(20), recipeStrings, new Object[]{"XXX", " Y ", " Y ", Character.valueOf('X'), slab, Character.valueOf('Y'), plank}));
		
		if (Config.enableFancySign && Config.enableWoodLabel)
			GameRegistry.addRecipe(RecipeShapelessFramedWood.addShapedWoodRecipe(this.blockList.get(21), recipeStrings, new Object[]{this.blockList.get(18), slab, paper}));
		
		if (Config.enableSeat)
		{
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(22), recipeStrings, new Object[]{" W ", " S ", "T T", Character.valueOf('W'), whiteWool, Character.valueOf('S'), slab, Character.valueOf('T'), stick}));
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(23), recipeStrings, new Object[]{"TWT", "TST", "T T", Character.valueOf('W'), whiteWool, Character.valueOf('S'), slab, Character.valueOf('T'), stick}));
			GameRegistry.addRecipe(RecipeBiblioFramedWood.addShapedWoodRecipe(this.blockList.get(25), recipeStrings, new Object[]{"TWT", " S ", Character.valueOf('W'), whiteWool, Character.valueOf('S'), slab}));
			GameRegistry.addRecipe(RecipeShapelessFramedWood.addShapedWoodRecipe(this.blockList.get(24), recipeStrings, new Object[]{slab, this.blockList.get(23)})); 
			GameRegistry.addRecipe(RecipeShapelessFramedWood.addShapedWoodRecipe(this.blockList.get(26), recipeStrings, new Object[]{slab, slab, this.blockList.get(23)})); 
			GameRegistry.addRecipe(new ShapedOreRecipe(this.blockList.get(15), true, new Object[]{" W ", " S ", "TPT", Character.valueOf('W'), whiteWool, Character.valueOf('S'), slab, Character.valueOf('T'), stick, Character.valueOf('P'), woodPP}));
		}
		*/
	}
	
}
