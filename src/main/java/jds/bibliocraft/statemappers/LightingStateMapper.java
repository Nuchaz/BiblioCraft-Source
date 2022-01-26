package jds.bibliocraft.statemappers;

import java.util.Map;

import com.google.common.collect.Maps;

import jds.bibliocraft.blocks.BlockLampGold;
import jds.bibliocraft.blocks.BlockLampIron;
import jds.bibliocraft.blocks.BlockLanternGold;
import jds.bibliocraft.blocks.BlockLanternIron;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.models.ModelLamp;
import jds.bibliocraft.models.ModelLantern;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;

public class LightingStateMapper extends DefaultStateMapper
{
	public final static LightingStateMapper instance = new LightingStateMapper();
	
	public LightingStateMapper() {}
	
	@Override
	public Map putStateModelLocations(Block block)
	{
		Map modelLocations = Maps.newLinkedHashMap();
		if (block instanceof BlockLampGold)
		{
			BlockLampGold light = (BlockLampGold)block;
			for (int i = 0; i < EnumColor.values().length; i++)
			{
				IBlockState state = light.getStateFromMeta(i);
				modelLocations.put(state, ModelLamp.modelResourceLocationGold);
			}
		}
		
		if (block instanceof BlockLampIron)
		{
			BlockLampIron light = (BlockLampIron)block;
			for (int i = 0; i < EnumColor.values().length; i++)
			{
				IBlockState state = light.getStateFromMeta(i);
				modelLocations.put(state, ModelLamp.modelResourceLocationIron);
			}
		}
		
		if (block instanceof BlockLanternGold)
		{
			BlockLanternGold light = (BlockLanternGold)block;
			for (int i = 0; i < EnumColor.values().length; i++)
			{
				IBlockState state = light.getStateFromMeta(i);
				modelLocations.put(state, ModelLantern.modelResourceLocationGold);
			}
		}
		
		if (block instanceof BlockLanternIron)
		{
			BlockLanternIron light = (BlockLanternIron)block;
			for (int i = 0; i < EnumColor.values().length; i++)
			{
				IBlockState state = light.getStateFromMeta(i);
				modelLocations.put(state, ModelLantern.modelResourceLocationIron);
			}
		}
		return modelLocations;
	}
}
