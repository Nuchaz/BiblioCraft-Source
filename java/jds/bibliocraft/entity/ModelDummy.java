package jds.bibliocraft.entity;

import net.minecraft.client.model.ModelBiped;

public class ModelDummy extends ModelBiped
{
		
		private float scale = (1.0F / 16.0F);
		// need to break these up into the 4 pieces of armor so there is a seperate render method for each piece

		
		public void renderHead()
		{
			bipedHead.render(scale);
			bipedHeadwear.render(scale);
		}

		public void renderChest()
		{
			bipedBody.render(scale);
	        bipedRightArm.render(scale);
	        bipedLeftArm.render(scale);	
		}
		
		public void renderLegs()
		{
	        bipedRightLeg.render(scale);
	        bipedLeftLeg.render(scale);
		}
		
		public void renderFeet()
		{
	        bipedRightLeg.render(scale);
	        bipedLeftLeg.render(scale);
		}
}
