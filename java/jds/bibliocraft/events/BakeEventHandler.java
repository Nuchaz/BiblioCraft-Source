package jds.bibliocraft.events;

import jds.bibliocraft.models.ModelBookcase;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class BakeEventHandler 
{
	public static final BakeEventHandler instance = new BakeEventHandler();
	
	private BakeEventHandler() {}
	
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event)
    {    
    	System.out.println("Biblio Model Bake Event Sep Class");
    	//event.getModelRegistry().put(ModelBookcase.modelResourceLocation, new ModelBookcase(event));
    }
}
