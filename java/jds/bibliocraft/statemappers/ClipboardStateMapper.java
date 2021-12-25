package jds.bibliocraft.statemappers;

import java.util.Map;

import com.google.common.collect.Maps;

import jds.bibliocraft.blocks.BlockClipboard;
import jds.bibliocraft.models.ModelClipboard;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;

public class ClipboardStateMapper extends DefaultStateMapper
{
	public final static ClipboardStateMapper instance = new ClipboardStateMapper();
	
	public ClipboardStateMapper() {}
	
	@Override
	public Map putStateModelLocations(Block block)
	{
		Map modelLocations = Maps.newLinkedHashMap();
		if (block instanceof BlockClipboard)
		{
			BlockClipboard clipboard = (BlockClipboard)block;
			IBlockState state = clipboard.getDefaultState();
			modelLocations.put(state, ModelClipboard.modelResourceLocation);
		}
		return modelLocations;
	}
}
