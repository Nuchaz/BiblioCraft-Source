package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.items.ItemAtlasPlate;
import jds.bibliocraft.items.ItemEnchantedPlate;
import jds.bibliocraft.items.ItemPlate;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityPrintPress;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockPrintingPress extends BiblioSimpleBlock
{
	public static final String name = "PrintingPress";
	public static final BlockPrintingPress instance = new BlockPrintingPress();
	public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumColor.class);
	
	public BlockPrintingPress()
	{
		super(Material.IRON, SoundType.ANVIL, name);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity t = world.getTileEntity(pos);
		if(!world.isRemote && t != null && t instanceof TileEntityPrintPress)
		{
			TileEntityPrintPress tile = (TileEntityPrintPress)t;
			int slot = getSlot(tile, side, hitX, hitZ);
			ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
			switch (slot)
			{
				case 0:
				{
					//ink
					if (playerhand != ItemStack.EMPTY && tile.isInk(playerhand))
					{
						tile.addStackToInventoryFromWorld(playerhand, slot, player);
					}
					else
					{
						tile.removeStackFromInventoryFromWorld(slot, player, this);
					}
					break;
				}
				case 1:
				{
					//plate
					if (playerhand != ItemStack.EMPTY && (playerhand.getItem() instanceof ItemPlate || playerhand.getItem() instanceof ItemEnchantedPlate || playerhand.getItem() instanceof ItemAtlasPlate))
					{
						tile.addStackToInventoryFromWorld(playerhand, slot, player);
					}
					else
					{
						tile.removeStackFromInventoryFromWorld(slot, player, this);
					}
					break;
				}
				case 2:
				{
					//input
					if (playerhand != ItemStack.EMPTY && (playerhand.getItem() instanceof ItemBook || playerhand.getItem() instanceof ItemAtlas))
					{
						tile.addStackToInventoryFromWorld(playerhand, slot, player);
					}
					else
					{
						tile.removeStackFromInventoryFromWorld(slot, player, this);
					}
					break;
				}
				case 3:
				{
					//output
					tile.removeStackFromInventoryFromWorld(slot, player, this);
					break;
				}
			}
		}
		return true;
	}
	
	public static int getSlot(TileEntityPrintPress tile, EnumFacing face, float hitX, float hitZ)
	{
		int output = -1;
		if (face != EnumFacing.UP)
			return output;
			
		float adjustedX = hitX;
		float adjustedZ = hitZ;
		
		switch (tile.getAngle())
		{
			case WEST:
			{
				adjustedX = hitZ;
				adjustedZ = 1.0f - hitX;
				break;
			}
			case NORTH:
			{
				adjustedX = 1.0f - hitX;
				adjustedZ = 1.0f - hitZ;
				break;
			}
			case EAST:
			{
				adjustedX = 1.0f - hitZ;
				adjustedZ = hitX;
				break;
			}
			default: break;
		}
		
		if (adjustedX <= 0.33)
		{
			// front of the press
			if (adjustedZ > 0.5)
			{
				//output
				output = 3;
			}
			else
			{
				//blank books
				output = 2;
			}
		}
		else if (adjustedX > 0.7)
		{
			//ink
			output = 0;
		}
		else
		{
			//plate
			output = 1;
		}
		return output;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityPrintPress();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile)
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("press");
		ItemStack plate = tile.getStackInSlot(1);
		ItemStack input = tile.getStackInSlot(2); 
		ItemStack output = tile.getStackInSlot(3);
		if (plate != ItemStack.EMPTY)
		{
			modelParts.add("plate");
		}
		if (input != ItemStack.EMPTY)
		{
			int stacksize = input.getCount();
			if (stacksize > 0)
				modelParts.add("book1");
			if (stacksize > 16)
				modelParts.add("book2");
			if (stacksize > 32)
				modelParts.add("book3");
			if (stacksize > 48)
				modelParts.add("book4");
		}
		if (output != ItemStack.EMPTY)
		{
			if (output.getItem() instanceof ItemEnchantedBook)
			{
				modelParts.add("bookEnchant");
			}
			else
			{
				modelParts.add("bookBlue");
			}
		}
		return modelParts;
	}
	
	@Override
	public ExtendedBlockState getExtendedBlockStateAlternate(ExtendedBlockState state) 
	{
		ExtendedBlockState exstate = new ExtendedBlockState(this, new IProperty[] {COLOR}, new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE});
		return exstate;
	}

	@Override
	public IExtendedBlockState getIExtendedBlockStateAlternate(BiblioTileEntity biblioTile, IExtendedBlockState state) 
	{
		EnumColor color = EnumColor.getColorEnumFromID(getInkquantity(biblioTile));
		state = (IExtendedBlockState)state.withProperty(COLOR, color);
		return state;
	}
	
	private int getInkquantity(BiblioTileEntity biblioTile)
	{
		int output = 0;
		ItemStack ink = biblioTile.getStackInSlot(0);
    	if (ink != ItemStack.EMPTY)
    	{
    		int size = ink.getCount();
    		if (size > 0 && size < 8) output = 1;
    		if (size > 7 && size < 16) output = 2;
    		if (size > 15 && size < 24) output = 3;
    		if (size > 23 && size < 32) output = 4;
    		if (size > 31 && size < 40) output = 5;
    		if (size > 39 && size < 48) output = 6;
    		if (size > 47 && size < 56) output = 7;
    		if (size > 55) return 8;
		}
		return output;
	}
}
