package jds.bibliocraft.events;

import jds.bibliocraft.Config;
import jds.bibliocraft.helpers.EnumPaintingFrame;
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
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BakeEventHandler 
{
    public static final BakeEventHandler instance = new BakeEventHandler();
    
    private BakeEventHandler() {}

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event)
    {    	
    	if (Config.enableBookcase)
    	{
    		
    		event.getModelRegistry().putObject(ModelBookcase.modelResourceLocation, new ModelBookcase());
    		event.getModelRegistry().putObject(ModelBookcase.modelResourceLocationFilledBookcase, new ModelBookcase());
    	}
    	if (Config.enableGenericshelf)
    		event.getModelRegistry().putObject(ModelShelf.modelResourceLocation, new ModelShelf());

    	if (Config.enableClipboard)
    		event.getModelRegistry().putObject(ModelClipboard.modelResourceLocation, new ModelClipboard());
    	if (Config.enableFurniturePaneler)
    		event.getModelRegistry().putObject(ModelFurniturePaneler.modelResourceLocation, new ModelFurniturePaneler());
    	if (Config.enableLamp)
    	{
    		event.getModelRegistry().putObject(ModelLamp.modelResourceLocationGold, new ModelLamp());
    		event.getModelRegistry().putObject(ModelLamp.modelResourceLocationIron, new ModelLamp());
    	}
    	if (Config.enableLantern)
    	{
    		event.getModelRegistry().putObject(ModelLantern.modelResourceLocationGold, new ModelLantern());
    		event.getModelRegistry().putObject(ModelLantern.modelResourceLocationIron, new ModelLantern());
    	}
    	if (Config.enableWoodLabel)
    		event.getModelRegistry().putObject(ModelLabel.modelResourceLocation, new ModelLabel());
    	if (Config.enablePotionshelf)
    		event.getModelRegistry().putObject(ModelPotionShelf.modelResourceLocation, new ModelPotionShelf());
    	if (Config.enableFancySign)
    		event.getModelRegistry().putObject(ModelFancySign.modelResourceLocation, new ModelFancySign());
    	if (Config.enableWritingdesk)
    		event.getModelRegistry().putObject(ModelDesk.modelResourceLocation, new ModelDesk());
    	if (Config.enableToolrack)
    		event.getModelRegistry().putObject(ModelToolRack.modelResourceLocation, new ModelToolRack());
    	if (Config.enableSeat)
    	{
    		event.getModelRegistry().putObject(ModelSeat.modelResourceLocation, new ModelSeat());
    		Object object =  event.getModelRegistry().getObject(ModelSeatBack1.modelResourceLocation);
	        if (object instanceof IBakedModel)
	        {
	        	ModelSeatBack1 seatback = new ModelSeatBack1((IBakedModel)object);
	        	event.getModelRegistry().putObject(ModelSeatBack1.modelResourceLocation, seatback);
	        }
	        object =  event.getModelRegistry().getObject(ModelSeatBack2.modelResourceLocation);
	        if (object instanceof IBakedModel)
	        {
	        	ModelSeatBack2 seatback = new ModelSeatBack2((IBakedModel)object);
	        	event.getModelRegistry().putObject(ModelSeatBack2.modelResourceLocation, seatback);
	        }
	        object =  event.getModelRegistry().getObject(ModelSeatBack3.modelResourceLocation);
	        if (object instanceof IBakedModel)
	        {
	        	ModelSeatBack3 seatback = new ModelSeatBack3((IBakedModel)object);
	        	event.getModelRegistry().putObject(ModelSeatBack3.modelResourceLocation, seatback);
	        }
	        object =  event.getModelRegistry().getObject(ModelSeatBack4.modelResourceLocation);
	        if (object instanceof IBakedModel)
	        {
	        	ModelSeatBack4 seatback = new ModelSeatBack4((IBakedModel)object);
	        	event.getModelRegistry().putObject(ModelSeatBack4.modelResourceLocation, seatback);
	        }
	        object =  event.getModelRegistry().getObject(ModelSeatBack5.modelResourceLocation);
	        if (object instanceof IBakedModel)
	        {
	        	ModelSeatBack5 seatback = new ModelSeatBack5((IBakedModel)object);
	        	event.getModelRegistry().putObject(ModelSeatBack5.modelResourceLocation, seatback);
	        }
    	}
    	if (Config.enableFancyWorkbench)
    		event.getModelRegistry().putObject(ModelFancyWorkbench.modelResourceLocation, new ModelFancyWorkbench());
    	if (Config.enableTable)
    		event.getModelRegistry().putObject(ModelTable.modelResourceLocation, new ModelTable());
    	if (Config.enableFramedChest)
    		event.getModelRegistry().putObject(ModelFramedChest.modelResourceLocation, new ModelFramedChest());
    	if (Config.enableClock)
    		event.getModelRegistry().putObject(ModelClock.modelResourceLocation, new ModelClock());
    	if (Config.enableMapFrame)
    	{
    		event.getModelRegistry().putObject(ModelMapFrame.modelResourceLocation, new ModelMapFrame());
    		Object object =  event.getModelRegistry().getObject(ModelMapTool.modelResourceLocation);
            if (object instanceof IBakedModel)
            {
            	ModelMapTool maptool = new ModelMapTool((IBakedModel)object);
            	event.getModelRegistry().putObject(ModelMapTool.modelResourceLocation, maptool);
            }
    	}
    	if (Config.enableWeaponcase)
    		event.getModelRegistry().putObject(ModelCase.modelResourceLocation, new ModelCase());
    	if (Config.enablePainting)
    	{
	    	event.getModelRegistry().putObject(ModelPainting.modelResourceLocationBorderless, new ModelPainting(EnumPaintingFrame.BORDERLESS));
	    	event.getModelRegistry().putObject(ModelPainting.modelResourceLocationFancy, new ModelPainting(EnumPaintingFrame.FANCY));
	    	event.getModelRegistry().putObject(ModelPainting.modelResourceLocationFlat, new ModelPainting(EnumPaintingFrame.FLAT));
	    	event.getModelRegistry().putObject(ModelPainting.modelResourceLocationMiddle, new ModelPainting(EnumPaintingFrame.MIDDLE));
	    	event.getModelRegistry().putObject(ModelPainting.modelResourceLocationSimple, new ModelPainting(EnumPaintingFrame.SIMPLE));
	    	event.getModelRegistry().putObject(ModelPaintingPress.modelResourceLocation, new ModelPaintingPress());
	    	Object object =  event.getModelRegistry().getObject(ModelCanvas.modelResourceLocation);
	        //if (object instanceof IBakedModel)
	        //{
	        	ModelCanvas canvas = new ModelCanvas((IBakedModel)object);
	        	event.getModelRegistry().putObject(ModelCanvas.modelResourceLocation,  canvas);
	        //}
    	}
    	if (Config.enableTypewriter)
    		event.getModelRegistry().putObject(ModelTypewriter.modelResourceLocation, new ModelTypewriter());
    	if (Config.enableSwordPedestal)
    		event.getModelRegistry().putObject(ModelSwordPedestal.modelResourceLocation, new ModelSwordPedestal());
    	if (Config.enableArmorstand)
    		event.getModelRegistry().putObject(ModelArmorStand.modelResourceLocation, new ModelArmorStand());
    	if (Config.enableDeskBell)
    		event.getModelRegistry().putObject(ModelBell.modelResourceLocation, new ModelBell());
    	if (Config.enableCookieJar)
    		event.getModelRegistry().putObject(ModelCookieJar.modelResourceLocation, new ModelCookieJar());
    	if (Config.enableDinnerPlate)
    		event.getModelRegistry().putObject(ModelDinnerPlate.modelResourceLocation, new ModelDinnerPlate());
    	if (Config.enableDiscRack)
    		event.getModelRegistry().putObject(ModelDiscRack.modelResourceLocation, new ModelDiscRack());
    	if (Config.enablePrintpressTypeMachine)
    	{
    		event.getModelRegistry().putObject(ModelTypesettingTable.modelResourceLocation, new ModelTypesettingTable());
    		event.getModelRegistry().putObject(ModelPrintingPress.modelResourceLocation, new ModelPrintingPress());
    	}
    	if (Config.enableTapemeasure)
    	{
			event.getModelRegistry().putObject(ModelMarkerPole.modelResourceLocation, new ModelMarkerPole());
	    	Object object = event.getModelRegistry().getObject(ModelCompass.modelResourceLocation);
	        if (object instanceof IBakedModel)
	        {
	        	ModelCompass waypointCompass = new ModelCompass((IBakedModel)object);
	        	event.getModelRegistry().putObject(ModelCompass.modelResourceLocation, waypointCompass);
	        }
    	}
    	if (Config.enableDeathCompass)
    	{
	    	Object object = event.getModelRegistry().getObject(ModelDeathCompass.modelResourceLocation);
	        if (object instanceof IBakedModel)
	        {
	        	ModelDeathCompass deathCompass = new ModelDeathCompass((IBakedModel)object);
	        	event.getModelRegistry().putObject(ModelDeathCompass.modelResourceLocation, deathCompass);
	        }
    	}
        if (Config.enableAtlas)
        {
	        Object object = event.getModelRegistry().getObject(ModelAtlas.modelResourceLocation);
	        //if (object instanceof IBakedModel)
	        //{
	        	// The map in first person veiw is uneffected
	        	ModelAtlas atlas = new ModelAtlas((IBakedModel)object);
	        	event.getModelRegistry().putObject(ModelAtlas.modelResourceLocation, atlas);
	        //}
        }
    }
}
