package jds.bibliocraft;

import jds.bibliocraft.blocks.BlockArmorStand;
import jds.bibliocraft.blocks.BlockBell;
import jds.bibliocraft.blocks.BlockBookcase;
import jds.bibliocraft.blocks.BlockBookcaseCreative;
import jds.bibliocraft.blocks.BlockCase;
import jds.bibliocraft.blocks.BlockClipboard;
import jds.bibliocraft.blocks.BlockClock;
import jds.bibliocraft.blocks.BlockCookieJar;
import jds.bibliocraft.blocks.BlockDesk;
import jds.bibliocraft.blocks.BlockDinnerPlate;
import jds.bibliocraft.blocks.BlockDiscRack;
import jds.bibliocraft.blocks.BlockFancySign;
import jds.bibliocraft.blocks.BlockFancyWorkbench;
import jds.bibliocraft.blocks.BlockFramedChest;
import jds.bibliocraft.blocks.BlockFurniturePaneler;
import jds.bibliocraft.blocks.BlockLabel;
import jds.bibliocraft.blocks.BlockLampGold;
import jds.bibliocraft.blocks.BlockLampIron;
import jds.bibliocraft.blocks.BlockLanternGold;
import jds.bibliocraft.blocks.BlockLanternIron;
import jds.bibliocraft.blocks.BlockMapFrame;
import jds.bibliocraft.blocks.BlockMarkerPole;
import jds.bibliocraft.blocks.BlockPaintingFrameBorderless;
import jds.bibliocraft.blocks.BlockPaintingFrameFancy;
import jds.bibliocraft.blocks.BlockPaintingFrameFlat;
import jds.bibliocraft.blocks.BlockPaintingFrameMiddle;
import jds.bibliocraft.blocks.BlockPaintingFrameSimple;
import jds.bibliocraft.blocks.BlockPaintingPress;
import jds.bibliocraft.blocks.BlockPotionShelf;
import jds.bibliocraft.blocks.BlockPrintingPress;
import jds.bibliocraft.blocks.BlockSeat;
import jds.bibliocraft.blocks.BlockShelf;
import jds.bibliocraft.blocks.BlockSwordPedestal;
import jds.bibliocraft.blocks.BlockTable;
import jds.bibliocraft.blocks.BlockToolRack;
import jds.bibliocraft.blocks.BlockTypeWriter;
import jds.bibliocraft.blocks.BlockTypesettingTable;
import jds.bibliocraft.entity.EntitySeat;
import jds.bibliocraft.entity.EntitySeatRenderer;
import jds.bibliocraft.events.BakeEventHandler;
import jds.bibliocraft.events.RenderAtlasFace;
import jds.bibliocraft.events.RenderClipboardText;
import jds.bibliocraft.events.TextureStichHandler;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.items.ItemAtlasPlate;
import jds.bibliocraft.items.ItemBigBook;
import jds.bibliocraft.items.ItemChase;
import jds.bibliocraft.items.ItemClipboard;
import jds.bibliocraft.items.ItemDeathCompass;
import jds.bibliocraft.items.ItemDrill;
import jds.bibliocraft.items.ItemEnchantedPlate;
import jds.bibliocraft.items.ItemFramingBoard;
import jds.bibliocraft.items.ItemFramingSaw;
import jds.bibliocraft.items.ItemFramingSheet;
import jds.bibliocraft.items.ItemHandDrill;
import jds.bibliocraft.items.ItemLock;
import jds.bibliocraft.items.ItemMapTool;
import jds.bibliocraft.items.ItemNameTester;
import jds.bibliocraft.items.ItemPaintingCanvas;
import jds.bibliocraft.items.ItemPlate;
import jds.bibliocraft.items.ItemPlumbLine;
import jds.bibliocraft.items.ItemReadingGlasses;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.items.ItemRedstoneBook;
import jds.bibliocraft.items.ItemSeatBack;
import jds.bibliocraft.items.ItemSeatBack2;
import jds.bibliocraft.items.ItemSeatBack3;
import jds.bibliocraft.items.ItemSeatBack4;
import jds.bibliocraft.items.ItemSeatBack5;
import jds.bibliocraft.items.ItemSlottedBook;
import jds.bibliocraft.items.ItemStockroomCatalog;
import jds.bibliocraft.items.ItemTape;
import jds.bibliocraft.items.ItemTapeMeasure;
import jds.bibliocraft.items.ItemWaypointCompass;
import jds.bibliocraft.models.ModelArmorStand;
import jds.bibliocraft.models.ModelAtlas;
import jds.bibliocraft.models.ModelBell;
import jds.bibliocraft.models.ModelBookcase;
import jds.bibliocraft.models.ModelCanvas;
import jds.bibliocraft.models.ModelCase;
import jds.bibliocraft.models.ModelClipboard;
import jds.bibliocraft.models.ModelClock;
import jds.bibliocraft.models.ModelCompass;
import jds.bibliocraft.models.ModelCookieJar;
import jds.bibliocraft.models.ModelDeathCompass;
import jds.bibliocraft.models.ModelDesk;
import jds.bibliocraft.models.ModelDinnerPlate;
import jds.bibliocraft.models.ModelDiscRack;
import jds.bibliocraft.models.ModelFancySign;
import jds.bibliocraft.models.ModelFancyWorkbench;
import jds.bibliocraft.models.ModelFramedChest;
import jds.bibliocraft.models.ModelFurniturePaneler;
import jds.bibliocraft.models.ModelLabel;
import jds.bibliocraft.models.ModelLamp;
import jds.bibliocraft.models.ModelLantern;
import jds.bibliocraft.models.ModelMapFrame;
import jds.bibliocraft.models.ModelMapTool;
import jds.bibliocraft.models.ModelMarkerPole;
import jds.bibliocraft.models.ModelPainting;
import jds.bibliocraft.models.ModelPaintingPress;
import jds.bibliocraft.models.ModelPotionShelf;
import jds.bibliocraft.models.ModelPrintingPress;
import jds.bibliocraft.models.ModelSeat;
import jds.bibliocraft.models.ModelSeatBack1;
import jds.bibliocraft.models.ModelSeatBack2;
import jds.bibliocraft.models.ModelSeatBack3;
import jds.bibliocraft.models.ModelSeatBack4;
import jds.bibliocraft.models.ModelSeatBack5;
import jds.bibliocraft.models.ModelShelf;
import jds.bibliocraft.models.ModelSwordPedestal;
import jds.bibliocraft.models.ModelTable;
import jds.bibliocraft.models.ModelToolRack;
import jds.bibliocraft.models.ModelTypesettingTable;
import jds.bibliocraft.models.ModelTypewriter;
import jds.bibliocraft.rendering.TileEntityArmorStandRenderer;
import jds.bibliocraft.rendering.TileEntityCaseRenderer;
import jds.bibliocraft.rendering.TileEntityClipboardRenderer;
import jds.bibliocraft.rendering.TileEntityClockRenderer;
import jds.bibliocraft.rendering.TileEntityDeskRenderer;
import jds.bibliocraft.rendering.TileEntityDinnerPlateRenderer;
import jds.bibliocraft.rendering.TileEntityDiscRackRenderer;
import jds.bibliocraft.rendering.TileEntityFancySignRenderer;
import jds.bibliocraft.rendering.TileEntityFramedChestRenderer;
import jds.bibliocraft.rendering.TileEntityFurniturePanelerRenderer;
import jds.bibliocraft.rendering.TileEntityLabelRenderer;
import jds.bibliocraft.rendering.TileEntityMapFrameRenderer;
import jds.bibliocraft.rendering.TileEntityPaintPressRenderer;
import jds.bibliocraft.rendering.TileEntityPaintingRenderer;
import jds.bibliocraft.rendering.TileEntityPotionShelfRenderer;
import jds.bibliocraft.rendering.TileEntityPrintPressRenderer;
import jds.bibliocraft.rendering.TileEntityShelfRenderer;
import jds.bibliocraft.rendering.TileEntitySwordPedestalRenderer;
import jds.bibliocraft.rendering.TileEntityTableRenderer;
import jds.bibliocraft.rendering.TileEntityToolRackRenderer;
import jds.bibliocraft.rendering.TileEntityTypeWriterRenderer;
import jds.bibliocraft.statemappers.BiblioBlockStateMapper;
import jds.bibliocraft.statemappers.ClipboardStateMapper;
import jds.bibliocraft.statemappers.LightingStateMapper;
import jds.bibliocraft.statemappers.MarkerPoleStateMapper;
import jds.bibliocraft.tileentities.TileEntityArmorStand;
import jds.bibliocraft.tileentities.TileEntityCase;
import jds.bibliocraft.tileentities.TileEntityClipboard;
import jds.bibliocraft.tileentities.TileEntityClock;
import jds.bibliocraft.tileentities.TileEntityDesk;
import jds.bibliocraft.tileentities.TileEntityDinnerPlate;
import jds.bibliocraft.tileentities.TileEntityDiscRack;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import jds.bibliocraft.tileentities.TileEntityFramedChest;
import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import jds.bibliocraft.tileentities.TileEntityLabel;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import jds.bibliocraft.tileentities.TileEntityPaintPress;
import jds.bibliocraft.tileentities.TileEntityPainting;
import jds.bibliocraft.tileentities.TileEntityPotionShelf;
import jds.bibliocraft.tileentities.TileEntityPrintPress;
import jds.bibliocraft.tileentities.TileEntityShelf;
import jds.bibliocraft.tileentities.TileEntitySwordPedestal;
import jds.bibliocraft.tileentities.TileEntityTable;
import jds.bibliocraft.tileentities.TileEntityToolRack;
import jds.bibliocraft.tileentities.TileEntityTypewriter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy 
{

	@SuppressWarnings("unchecked")
	public void registerRenderers()
	{
		MinecraftForge.EVENT_BUS.register(TextureStichHandler.instance);
		MinecraftForge.EVENT_BUS.register(BakeEventHandler.instance);
		
		if (!Config.disablerenderers)
		{
			if (Config.enableGenericshelf){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShelf.class, new TileEntityShelfRenderer());} 
			if (Config.enableClipboard)
			{
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityClipboard.class, new TileEntityClipboardRenderer());
				MinecraftForge.EVENT_BUS.register(new RenderClipboardText());
			}
			if (Config.enableFurniturePaneler){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFurniturePaneler.class, new TileEntityFurniturePanelerRenderer());}
			if (Config.enableWoodLabel){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLabel.class, new TileEntityLabelRenderer());}
			if (Config.enableToolrack){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityToolRack.class, new TileEntityToolRackRenderer());}
			if (Config.enableWeaponcase){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCase.class, new TileEntityCaseRenderer());}
			if (Config.enablePotionshelf){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPotionShelf.class, new TileEntityPotionShelfRenderer());}
			if (Config.enableClock){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityClock.class, new TileEntityClockRenderer());}
			if (Config.enableFramedChest){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFramedChest.class, new TileEntityFramedChestRenderer());}
			if (Config.enableTable){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTable.class, new TileEntityTableRenderer());}
			if (Config.enableWritingdesk){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDesk.class, new TileEntityDeskRenderer());}
			if (Config.enableMapFrame){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMapFrame.class, new TileEntityMapFrameRenderer());}
			if (Config.enableDinnerPlate){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDinnerPlate.class, new TileEntityDinnerPlateRenderer());}
			if (Config.enableTypewriter){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTypewriter.class, new TileEntityTypeWriterRenderer());}
			if (Config.enableSwordPedestal){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySwordPedestal.class, new TileEntitySwordPedestalRenderer());}
			if (Config.enableDiscRack){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDiscRack.class, new TileEntityDiscRackRenderer());}
			if (Config.enablePainting)
			{
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPainting.class, new TileEntityPaintingRenderer());
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPaintPress.class,  new TileEntityPaintPressRenderer());
			}
			if (Config.enableFancySign){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFancySign.class, new TileEntityFancySignRenderer());}
			if (Config.enableSeat){
				RenderingRegistry.registerEntityRenderingHandler(EntitySeat.class, new EntitySeatRenderer(Minecraft.getMinecraft().getRenderManager()));}
			if (Config.enablePrintpressTypeMachine){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrintPress.class, new TileEntityPrintPressRenderer());}
			
			if (Config.enableArmorstand){
				ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArmorStand.class, new TileEntityArmorStandRenderer());
			}
		}
		
		OBJLoader.INSTANCE.addDomain("bibliocraft");

		if (Config.enableBookcase)
		{
			Item bookcase_item = Item.getItemFromBlock(BlockBookcase.instance);
			Item bookcase_creative_item = Item.getItemFromBlock(BlockBookcaseCreative.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(bookcase_item, i, ModelBookcase.modelResourceLocation);
				ModelLoader.setCustomModelResourceLocation(bookcase_creative_item, i, ModelBookcase.modelResourceLocationFilledBookcase);
			}
			ModelLoader.setCustomStateMapper(BlockBookcase.instance, BiblioBlockStateMapper.instance);
			ModelLoader.setCustomStateMapper(BlockBookcaseCreative.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableGenericshelf)
		{
			Item shelf_item = Item.getItemFromBlock(BlockShelf.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(shelf_item, i, ModelShelf.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockShelf.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableFurniturePaneler)
		{
			Item paneler_item = Item.getItemFromBlock(BlockFurniturePaneler.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(paneler_item, i, ModelFurniturePaneler.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockFurniturePaneler.instance, BiblioBlockStateMapper.instance);
			
			ModelResourceLocation framingBoardModel = new ModelResourceLocation("bibliocraft:framingboard");
			ModelLoader.setCustomModelResourceLocation(ItemFramingBoard.instance, 0, framingBoardModel);
			
			ModelResourceLocation framingSheetModel = new ModelResourceLocation("bibliocraft:framingsheet");
			ModelLoader.setCustomModelResourceLocation(ItemFramingSheet.instance, 0, framingSheetModel);
			
			ModelResourceLocation sawModel = new ModelResourceLocation("bibliocraft:saw");
			ModelLoader.setCustomModelResourceLocation(ItemFramingSaw.instance, 0, sawModel);
		}
		if (Config.enableWoodLabel)
		{
			Item item = Item.getItemFromBlock(BlockLabel.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelLabel.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockLabel.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableToolrack)
		{
			Item item = Item.getItemFromBlock(BlockToolRack.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelToolRack.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockToolRack.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enablePotionshelf)
		{
			Item item = Item.getItemFromBlock(BlockPotionShelf.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelPotionShelf.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockPotionShelf.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableSeat)
		{
			Item item = Item.getItemFromBlock(BlockSeat.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelSeat.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockSeat.instance, BiblioBlockStateMapper.instance);
			
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(ItemSeatBack.instance, i, ModelSeatBack1.modelResourceLocation); 
				ModelLoader.setCustomModelResourceLocation(ItemSeatBack2.instance, i, ModelSeatBack2.modelResourceLocation); 
				ModelLoader.setCustomModelResourceLocation(ItemSeatBack3.instance, i, ModelSeatBack3.modelResourceLocation); 
				ModelLoader.setCustomModelResourceLocation(ItemSeatBack4.instance, i, ModelSeatBack4.modelResourceLocation); 
				ModelLoader.setCustomModelResourceLocation(ItemSeatBack5.instance, i, ModelSeatBack5.modelResourceLocation); 
			}
		}
		if (Config.enableFancySign)
		{
			Item item = Item.getItemFromBlock(BlockFancySign.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelFancySign.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockFancySign.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableFancyWorkbench)
		{
			Item item = Item.getItemFromBlock(BlockFancyWorkbench.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelFancyWorkbench.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockFancyWorkbench.instance, BiblioBlockStateMapper.instance);
			
			ModelResourceLocation recipeBookModel = new ModelResourceLocation("bibliocraft:recipebook");
			ModelLoader.setCustomModelResourceLocation(ItemRecipeBook.instance, 0, recipeBookModel);
		}
		if (Config.enableFramedChest)
		{
			Item item = Item.getItemFromBlock(BlockFramedChest.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelFramedChest.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockFramedChest.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableWritingdesk)
		{
			Item item = Item.getItemFromBlock(BlockDesk.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelDesk.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockDesk.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableTable)
		{
			Item item = Item.getItemFromBlock(BlockTable.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelTable.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockTable.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableClock)
		{
			Item item = Item.getItemFromBlock(BlockClock.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelClock.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockClock.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableMapFrame)
		{
			Item item = Item.getItemFromBlock(BlockMapFrame.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelMapFrame.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockMapFrame.instance, BiblioBlockStateMapper.instance);
			ModelLoader.setCustomModelResourceLocation(ItemMapTool.instance, 0, ModelMapTool.modelResourceLocation); 
		}
		if (Config.enableWeaponcase)
		{
			Item item = Item.getItemFromBlock(BlockCase.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelCase.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockCase.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enablePainting)
		{
			Item item = Item.getItemFromBlock(BlockPaintingFrameBorderless.instance);
			Item item2 = Item.getItemFromBlock(BlockPaintingFrameSimple.instance);
			Item item3 = Item.getItemFromBlock(BlockPaintingFrameFlat.instance);
			Item item4 = Item.getItemFromBlock(BlockPaintingFrameFancy.instance);
			Item item5 = Item.getItemFromBlock(BlockPaintingFrameMiddle.instance);
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, ModelPainting.modelResourceLocationBorderless);
				ModelLoader.setCustomModelResourceLocation(item2, i, ModelPainting.modelResourceLocationSimple);
				ModelLoader.setCustomModelResourceLocation(item3, i, ModelPainting.modelResourceLocationFlat);
				ModelLoader.setCustomModelResourceLocation(item4, i, ModelPainting.modelResourceLocationFancy);
				ModelLoader.setCustomModelResourceLocation(item5, i, ModelPainting.modelResourceLocationMiddle);
			}
			ModelLoader.setCustomStateMapper(BlockPaintingFrameBorderless.instance, BiblioBlockStateMapper.instance);
			ModelLoader.setCustomStateMapper(BlockPaintingFrameSimple.instance, BiblioBlockStateMapper.instance);
			ModelLoader.setCustomStateMapper(BlockPaintingFrameFlat.instance, BiblioBlockStateMapper.instance);
			ModelLoader.setCustomStateMapper(BlockPaintingFrameFancy.instance, BiblioBlockStateMapper.instance);
			ModelLoader.setCustomStateMapper(BlockPaintingFrameMiddle.instance, BiblioBlockStateMapper.instance);
			
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockPaintingPress.instance), 0, ModelPaintingPress.modelResourceLocation);
			ModelLoader.setCustomStateMapper(BlockPaintingPress.instance, BiblioBlockStateMapper.instance);
			ModelLoader.setCustomModelResourceLocation(ItemPaintingCanvas.instance, 0, ModelCanvas.modelResourceLocation);
		}
		

		if (Config.enableLamp)
		{
			for (int i = 0; i < EnumColor.values().length; i++)
			{
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockLampGold.instance), i, ModelLamp.modelResourceLocationGold);
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockLampIron.instance), i, ModelLamp.modelResourceLocationIron);
			}
			ModelLoader.setCustomStateMapper(BlockLampGold.instance, LightingStateMapper.instance);
			ModelLoader.setCustomStateMapper(BlockLampIron.instance, LightingStateMapper.instance);
		}
		if (Config.enableLantern)
		{
			for (int i = 0; i < EnumColor.values().length; i++)
			{
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockLanternGold.instance), i, ModelLantern.modelResourceLocationGold);
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockLanternIron.instance), i, ModelLantern.modelResourceLocationIron);
			}
			ModelLoader.setCustomStateMapper(BlockLanternGold.instance, LightingStateMapper.instance);
			ModelLoader.setCustomStateMapper(BlockLanternIron.instance, LightingStateMapper.instance);
		}

		if (Config.enableTapemeasure)
		{
			Item marker_item = Item.getItemFromBlock(BlockMarkerPole.instance);
			ModelLoader.setCustomModelResourceLocation(marker_item, 0, ModelMarkerPole.modelResourceLocation);
			ModelLoader.setCustomStateMapper(BlockMarkerPole.instance, MarkerPoleStateMapper.instance);
			ModelResourceLocation tapemeasureModel = new ModelResourceLocation("bibliocraft:tapemeasure");
			ModelLoader.setCustomModelResourceLocation(ItemTapeMeasure.instance, 0, tapemeasureModel);
			ModelResourceLocation tapeModel = new ModelResourceLocation("bibliocraft:tape");
			ModelLoader.setCustomModelResourceLocation(ItemTape.instance, 0, tapeModel);
		}
		if (Config.enablePrintpressTypeMachine)
		{
			Item typesetting_item = Item.getItemFromBlock(BlockTypesettingTable.instance);
			ModelLoader.setCustomModelResourceLocation(typesetting_item, 0, ModelTypesettingTable.modelResourceLocation);
			ModelLoader.setCustomStateMapper(BlockTypesettingTable.instance, BiblioBlockStateMapper.instance);
			
			Item printpress_item = Item.getItemFromBlock(BlockPrintingPress.instance);
			ModelLoader.setCustomModelResourceLocation(printpress_item, 0, ModelPrintingPress.modelResourceLocation);
			ModelLoader.setCustomStateMapper(BlockPrintingPress.instance, BiblioBlockStateMapper.instance);
			
			ModelResourceLocation chaseModel = new ModelResourceLocation("bibliocraft:chase");
			ModelLoader.setCustomModelResourceLocation(ItemChase.instance, 0, chaseModel);
			
			ModelResourceLocation plateModel = new ModelResourceLocation("bibliocraft:plate");
			ModelLoader.setCustomModelResourceLocation(ItemPlate.instance, 0, plateModel);
			ModelLoader.setCustomModelResourceLocation(ItemAtlasPlate.instance, 0, plateModel);
			ModelLoader.setCustomModelResourceLocation(ItemEnchantedPlate.instance, 0, plateModel);
		}
		if (Config.enableClipboard)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockClipboard.instance), 0, ModelClipboard.clipboardBasicModel);
			ModelLoader.setCustomStateMapper(BlockClipboard.instance, ClipboardStateMapper.instance);
			ModelResourceLocation clipboardModel = new ModelResourceLocation("bibliocraft:clipboard");
			ModelLoader.setCustomModelResourceLocation(ItemClipboard.instance, 0, clipboardModel);
		}

		if (Config.enableTypewriter)
		{
			for (int i = 0; i < EnumColor.values().length; i++)
			{
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockTypeWriter.instance), i, ModelTypewriter.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockTypeWriter.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableSwordPedestal)
		{
			for (int i = 0; i < EnumColor.values().length; i++)
			{
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockSwordPedestal.instance), i, ModelSwordPedestal.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockSwordPedestal.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableArmorstand)
		{
			for (int i = 0; i <= BlockLoader.NUMBER_OF_WOODS; i++)
			{
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockArmorStand.instance), i, ModelArmorStand.modelResourceLocation);
			}
			ModelLoader.setCustomStateMapper(BlockArmorStand.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableDeskBell)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockBell.instance), 0, ModelBell.modelResourceLocation);
			ModelLoader.setCustomStateMapper(BlockBell.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableCookieJar)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockCookieJar.instance), 0, ModelCookieJar.modelResourceLocation);
			ModelLoader.setCustomStateMapper(BlockCookieJar.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableDinnerPlate)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockDinnerPlate.instance), 0, ModelDinnerPlate.modelResourceLocation);
			ModelLoader.setCustomStateMapper(BlockDinnerPlate.instance, BiblioBlockStateMapper.instance);
		}
		if (Config.enableDiscRack)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BlockDiscRack.instance), 0, ModelDiscRack.modelResourceLocation);
			ModelLoader.setCustomStateMapper(BlockDiscRack.instance, BiblioBlockStateMapper.instance);
		}

		if (Config.enableDeathCompass)
		{
			ModelLoader.setCustomModelResourceLocation(ItemDeathCompass.instance, 0, ModelDeathCompass.modelResourceLocation); 
		}
		if (Config.enableWaypointCompass)
		{
			ModelLoader.setCustomModelResourceLocation(ItemWaypointCompass.instance, 0, ModelCompass.modelResourceLocation); 
		}
		
		// Item Rendering
		if (Config.enableBigBook)
		{
			ModelResourceLocation bigbookModel = new ModelResourceLocation("bibliocraft:bigbook");
			ModelLoader.setCustomModelResourceLocation(ItemBigBook.instance, 0, bigbookModel);
		}

		if (Config.enableAtlas)
		{
			ModelResourceLocation atlasModel = new ModelResourceLocation("bibliocraft:atlas");
			for (int i = 0; i < 4096; i++) 
			{
				ModelLoader.setCustomModelResourceLocation(ItemAtlas.instance, i, ModelAtlas.modelResourceLocation);
			}
			MinecraftForge.EVENT_BUS.register(new RenderAtlasFace());
			// TODO register my atlas custom renderer here
		}

		if (Config.enableDrill)
		{
			ModelResourceLocation screwgunModel = new ModelResourceLocation("bibliocraft:screwgun");
			ModelLoader.setCustomModelResourceLocation(ItemDrill.instance, 0, screwgunModel);
		}
		if (Config.enableHandDrill)
		{
			ModelResourceLocation handdrillModel = new ModelResourceLocation("bibliocraft:handdrill");
			ModelLoader.setCustomModelResourceLocation(ItemHandDrill.instance, 0, handdrillModel);
		}
		if (Config.enableLock)
		{
			ModelResourceLocation lockModel = new ModelResourceLocation("bibliocraft:lock");
			ModelLoader.setCustomModelResourceLocation(ItemLock.instance, 0, lockModel);
		}
		if (Config.enablePlumbLine)
		{
			ModelResourceLocation plumbLineModel = new ModelResourceLocation("bibliocraft:plumbline");
			ModelLoader.setCustomModelResourceLocation(ItemPlumbLine.instance, 0, plumbLineModel);
		}
		

		if (Config.enableStockroomCatalog)
		{
			ModelResourceLocation stockCatalogModel = new ModelResourceLocation("bibliocraft:stockcatalog");
			ModelLoader.setCustomModelResourceLocation(ItemStockroomCatalog.instance, 0, stockCatalogModel);
		}
		if (Config.enableReadingglasses)
		{
			ModelResourceLocation glassesModel = new ModelResourceLocation("bibliocraft:readingglasses");
			ModelLoader.setCustomModelResourceLocation(ItemReadingGlasses.instance, 0, glassesModel);
			
			ModelResourceLocation tintedGlassesModel = new ModelResourceLocation("bibliocraft:tintedglasses");
			ModelLoader.setCustomModelResourceLocation(ItemReadingGlasses.instance, 1, tintedGlassesModel);
			
			ModelResourceLocation monocleModel = new ModelResourceLocation("bibliocraft:monocle");
			ModelLoader.setCustomModelResourceLocation(ItemReadingGlasses.instance, 2, monocleModel);
		}
		
		if (Config.enableSlottedBook)
		{
			ModelResourceLocation vanillaBookModel = new ModelResourceLocation("bibliocraft:book");
			ModelLoader.setCustomModelResourceLocation(ItemSlottedBook.instance, 0, vanillaBookModel);
		}
		if (Config.enableRedstonebook)
		{
			ModelResourceLocation vanillaBookModel = new ModelResourceLocation("bibliocraft:book");
			ModelLoader.setCustomModelResourceLocation(ItemRedstoneBook.instance, 0, vanillaBookModel);
		}
		if (Config.enableTesterItem)
		{
			ModelResourceLocation vanillaBookModel = new ModelResourceLocation("bibliocraft:book");
			ModelLoader.setCustomModelResourceLocation(ItemNameTester.instance, 0, vanillaBookModel);
		}	
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initTileEntities()
	{
		super.initTileEntities();
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void initNetwork()
	{
		super.initNetwork();
		
	}
	
}
