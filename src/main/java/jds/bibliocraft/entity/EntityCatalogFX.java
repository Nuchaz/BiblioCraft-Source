package jds.bibliocraft.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityCatalogFX extends Particle
{

	private ResourceLocation texture = new ResourceLocation("bibliocraft", "textures/particle/particlecatalog.png");
	
	public EntityCatalogFX(World world, double x, double y, double z, double motx, double moty, double motz) 
	{
		super(world, x, y, z);
		this.particleMaxAge = 30;
		this.motionX = motx;
		this.motionY = moty;
		this.motionZ = motz;
        //this.motionX *= 0.01000000149011612D;
        //this.motionY *= 0.01000000149011612D;
        //this.motionZ *= 0.01000000149011612D;
        
	}
	
    @Override
	public void renderParticle(BufferBuilder worldRenderer, Entity entity, float partialTick, float yaw, float pitch, float yawz, float moty, float motz)
    {

        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        double x = this.posX - this.interpPosX;
        double y = this.posY - this.interpPosY;
        double z = this.posZ - this.interpPosZ;
 
        int combinedBrightness = this.getBrightnessForRender(partialTick);
        int skyLightTimes16 = combinedBrightness >> 16 & 65535;
        int blockLightTimes16 = combinedBrightness & 65535;
        double scaler = 0.00 + (10.0 / (this.particleAge + 80)) - 0.03;
        this.particleAlpha = (float)((this.particleMaxAge-this.particleAge+0.0)/(this.particleMaxAge+0.0));
        worldRenderer.pos(x - (yaw * scaler) - (moty * scaler), y - (0.5 * scaler) - (pitch * scaler), z - (0.5 * scaler) - (yawz * scaler) - (motz * scaler))
        			 .tex(1, 1)
        			 .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
        			 .lightmap(skyLightTimes16, blockLightTimes16).endVertex();
        worldRenderer.pos(x - (yaw*scaler) + (moty*scaler), y + (0.5*scaler) + (pitch*scaler), z - (0.5*scaler) - (yawz*scaler) + (motz*scaler))
        			 .tex(0, 1)
        			 .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
        			 .lightmap(skyLightTimes16, blockLightTimes16).endVertex();
        worldRenderer.pos(x + (yaw*scaler) + (moty*scaler), y + (0.5*scaler) + (pitch*scaler), z + (0.5*scaler) + (yawz*scaler) + (motz*scaler))
        			 .tex(0, 0)
        			 .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
        			 .lightmap(skyLightTimes16, blockLightTimes16).endVertex();
        worldRenderer.pos(x + (yaw*scaler) - (moty*scaler), y - (0.5*scaler) - (pitch*scaler), z + (0.5*scaler) + (yawz*scaler) - (motz*scaler))
    				 .tex(1, 0)
    				 .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
    				 .lightmap(skyLightTimes16, blockLightTimes16).endVertex();
    }
    
    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }
        this.move(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
    
    @Override
    public int getFXLayer()
    {
        return 2;
    }

}
