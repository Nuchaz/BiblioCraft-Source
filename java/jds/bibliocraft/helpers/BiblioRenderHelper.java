package jds.bibliocraft.helpers;


import jds.bibliocraft.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
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
    		BlockState state = stackBlock.getDefaultState();
    		ResourceLocation reloc = stackBlock.getRegistryName();
    		//IBakedModel test = Minecraft.getInstance().getRenderItem().getItemModelMesher().getItemModel(stack); TODO borked
    		//Minecraft.getInstance().getModelManager().getModel(stack)old
    		//returnValue = test.getParticleTexture().getName().toString(); TODO borked
    	}
        return returnValue;
    }
}
