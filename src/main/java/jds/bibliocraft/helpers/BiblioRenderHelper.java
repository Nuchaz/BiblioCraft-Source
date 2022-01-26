package jds.bibliocraft.helpers;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BiblioRenderHelper
{

    public static String getBlockTextureString(ItemStack stack) 
    {
    	String returnValue = "none";
    	if (stack != ItemStack.EMPTY)
    	{
    		Block stackBlock = Block.getBlockFromItem(stack.getItem());
    		IBlockState state = stackBlock.getDefaultState();
    		ResourceLocation reloc = new ResourceLocation(stackBlock.getUnlocalizedName());
    		IBakedModel test = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack); 
    		returnValue = test.getParticleTexture().getIconName();
    	}
        return returnValue;
    }
}
