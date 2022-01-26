package jds.bibliocraft.rendering;

import org.lwjgl.opengl.GL11;

import jds.bibliocraft.CommonProxy;
import jds.bibliocraft.entity.AbtractSteve;
import jds.bibliocraft.entity.ModelDummy;
import jds.bibliocraft.tileentities.TileEntityArmorStand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityArmorStandRenderer extends TileEntitySpecialRenderer
{
	private AbstractClientPlayer steve;
	private RenderManager renderManager;
	private ModelDummy modelDummy= new ModelDummy();
	
	@Override
	public void render(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float what)
	{
		if (steve == null)
		{
			renderManager = Minecraft.getMinecraft().getRenderManager();
			steve = new AbtractSteve(getWorld());
			renderManager.setRenderPosition(0,0,0);
			steve.posX = 0;
			steve.posY = 0;
			steve.posZ = 0;
		}
		if (tile != null && tile instanceof TileEntityArmorStand)
		{
			TileEntityArmorStand stand = (TileEntityArmorStand)tile;
			if (stand.getIsBottomStand())
			{
				float degreeAngle = 0.0f;
				switch (stand.getAngle())
				{
					case SOUTH:
					{
						degreeAngle = 90.0f; 
						break;
					}
					case WEST:
					{
						degreeAngle = 180.0f;  
						break;
					}
					case NORTH:
					{
						degreeAngle = 270.0f;  
						break;
					}
					default:break;
				}
				steve.inventory.armorInventory.set(3, stand.getStackInSlot(0));
				steve.inventory.armorInventory.set(2, stand.getStackInSlot(1));
				steve.inventory.armorInventory.set(1, stand.getStackInSlot(2));
				steve.inventory.armorInventory.set(0, stand.getStackInSlot(3));	
				steve.renderYawOffset = degreeAngle;
				steve.rotationYawHead = degreeAngle;
				GlStateManager.pushMatrix();
				GlStateManager.enableLighting();
		        GlStateManager.enableBlend();
		        double xPos = tile.getPos().getX() + 0.5 - this.rendererDispatcher.entityX;
		        double yPos = tile.getPos().getY() + 0.07 - this.rendererDispatcher.entityY;
		        double zPos = tile.getPos().getZ() + 0.5 - this.rendererDispatcher.entityZ;
		        float yaw = degreeAngle;
		        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				renderManager.renderEntity(steve, xPos, yPos, zPos, degreeAngle, 1.0f, false);
		        GlStateManager.disableBlend();
				GlStateManager.popMatrix();
				
				// TODO the glint effect on the armor stand doesn't work yet.
				//GlStateManager.pushMatrix();
				//GlStateManager.translate(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.06, tile.getPos().getZ() + 0.5);
				//GlStateManager.enableLighting();
				//bindTexture(CommonProxy.BLUEWOOL);
				//GlStateManager.scale(20.0, 20.0, 20.0);
				//modelDummy.renderHead();
				//modelDummy.renderChest();
				//enchant(0);
				//enchant(1);
				//enchant(2);
				//GlStateManager.popMatrix();
			}
		}
	}
	
	public void enchant(int armorType)
	{
		float tickModifier = Minecraft.getSystemTime() % 3000L / 3000.0F * 48.0F;
		bindTexture(CommonProxy.GLINT_PNG);
		GlStateManager.enableBlend();
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
        GlStateManager.depthFunc(GL11.GL_EQUAL);
        GlStateManager.depthMask(false);
        for (int i = 0; i < 2; ++i)
        {
            GlStateManager.disableLighting();
            GlStateManager.color(0.5F * 0.76F, 0.25F * 0.76F, 0.8F * 0.76F, 1.0F);
            GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.loadIdentity();
            float var23 = tickModifier * (0.001F + i * 0.003F) * 20.0F;
            float var24 = 0.33333334F;
            GlStateManager.scale(var24, var24, var24);
            GlStateManager.rotate(30.0F - i * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, var23, 0.0F);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            switch (armorType)
            {
            case 0:{modelDummy.renderHead(); break;}
            case 1:{modelDummy.renderChest(); break;}
            case 2:{modelDummy.renderLegs(); break;}
            case 3:{modelDummy.renderFeet(); break;}
            }
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.depthMask(true);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
	}
}
