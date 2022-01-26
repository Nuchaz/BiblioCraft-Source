package jds.bibliocraft.entity;

import jds.bibliocraft.CommonProxy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class EntitySeatRenderer extends Render<EntitySeat>
{

	public EntitySeatRenderer(RenderManager renderManager) 
	{
		super(renderManager);
	}

	@Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks)
    {
		
    }
	
	@Override
	protected ResourceLocation getEntityTexture(EntitySeat entity)
	{
		return CommonProxy.PLANKSOAK;
	}
	

}
