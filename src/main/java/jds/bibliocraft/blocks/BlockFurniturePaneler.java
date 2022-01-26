package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.Config;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.client.BiblioPanelerClient;
import jds.bibliocraft.states.PanelProperty;
import jds.bibliocraft.states.PanelState;
import jds.bibliocraft.states.TextureProperty;
import jds.bibliocraft.states.TextureState;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityFurniturePaneler;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockFurniturePaneler extends BiblioWoodBlock
{
	public static final String name = "FurniturePaneler";
	public static final BlockFurniturePaneler instance = new BlockFurniturePaneler();
	
	public BlockFurniturePaneler()
	{
		super(name, false);
	}

	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing face, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote)
		{
			ItemStack playerHand = player.getHeldItem(EnumHand.MAIN_HAND);
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile instanceof TileEntityFurniturePaneler)
			{
				TileEntityFurniturePaneler paneler = (TileEntityFurniturePaneler)tile;
				int slot = checkTopClickedPlace(paneler.getAngle(), hitX, hitY, hitZ);
				if (player.isSneaking())
				{
					if (face == EnumFacing.UP)
					{
						if (slot >= 0 && slot <= 2)
						{
							ItemStack test = paneler.getStackInSlot(slot);
							if (test != ItemStack.EMPTY)
							{
								dropStackInSlot(world, pos, slot, new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
								//dropItem(world, i, j, k, slot);
								paneler.setInventorySlotContents(slot, ItemStack.EMPTY);
								if (slot == 2)
								{
									paneler.executeRecipe();
								}
								paneler.updateRecipeManager();
								return true;
							}
						}				
					}
				}
				else
				{
					if (playerHand != ItemStack.EMPTY && face == EnumFacing.UP)
					{
						if (slot == 0)
						{
							if (Config.isBlock(playerHand))
							{
								Block thing = Block.getBlockFromItem(playerHand.getItem());
								boolean thaumcraftException = playerHand.getUnlocalizedName().contains("tile.blockWoodenDevice");
								if ((thing.isOpaqueCube(thing.getDefaultState())) || thaumcraftException) 
								{
									if (paneler.addItemsToBlock(playerHand, 0, player))
									{
										
										player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);

										// ByteBuf buffer = Unpooled.buffer();
								    	// ByteBufUtils.writeItemStack(buffer, playerHand);
								    	// buffer.writeInt(paneler.getPos().getX());
								    	// buffer.writeInt(paneler.getPos().getY());
								    	// buffer.writeInt(paneler.getPos().getZ());
										BiblioNetworking.INSTANCE.sendTo(new BiblioPanelerClient(playerHand, paneler.getPos()), (EntityPlayerMP) player);
										//BiblioCraft.ch_BiblioPaneler.sendTo(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioPaneler"), (EntityPlayerMP)player);
										return true;
									}
								}
							}
						}
						
						if (slot == 1)
						{
							if (paneler.checkIfFramedBiblioCraftBlock(playerHand))
							{
								if (paneler.addItemsToBlock(playerHand, 1, player))
								{
									player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
									return true;
								}
							}
						}
					}
				}
				
				paneler.playerFromBlock = player;
				player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
			}
			
			
		}
		return true;
	}
	
	public static int checkTopClickedPlace(EnumFacing angle, float x, float y, float z)
	{
		//System.out.println("angle = "+angle+"x = "+x+"  y = "+y+" z = "+z);
		switch (angle)
		{
			case SOUTH:
			{
				
				if (x > 0.5f)
				{
					//paneler slot
					return 0;
				}
				else
				{
					if (z < 0.5f)
					{
						//input slot
						return 1;
					}
					else
					{
						//output slot
						return 2;
					}
				}
			}
			case WEST:
			{
				if (z > 0.5f)
				{
					//paneler slot
					return 0;
				}
				else
				{
					if (x > 0.5f)
					{
						//input slot
						return 1;
					}
					else
					{
						//output slot
						return 2;
					}
				}
			}
			case NORTH:
			{
				if (x < 0.5f)
				{
					//paneler slot
					return 0;
				}
				else
				{
					if (z > 0.5f)
					{
						//input slot
						return 1;
					}
					else
					{
						//output slot
						return 2;
					}
				}
			}
			case EAST:
			{
				if (z < 0.5f)
				{
					//paneler slot
					return 0;
				}
				else
				{
					if (x < 0.5f)
					{
						//input slot
						return 1;
					}
					else
					{
						//output slot
						return 2;
					}
				}
			}
			default: break;
		}
		return -1;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityFurniturePaneler();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("wood");
		modelParts.add("paneler");
		if (tile instanceof TileEntityFurniturePaneler)
		{
			ItemStack stack = tile.getStackInSlot(0);
			if (stack != ItemStack.EMPTY)
			{
				int stackSize = stack.getCount();
				if (stackSize >= 1)
				{
					modelParts.add("panel1");
				}
				if (stackSize > 8)
				{
					modelParts.add("panel2");
				}
				if (stackSize > 16)
				{
					modelParts.add("panel3");
				}
				if (stackSize > 24)
				{
					modelParts.add("panel4");
				}
				if (stackSize > 32)
				{
					modelParts.add("panel5");
				}
				if (stackSize > 40)
				{
					modelParts.add("panel6");
				}
				if (stackSize > 48)
				{
					modelParts.add("panel7");
				}
				if (stackSize > 54)
				{
					modelParts.add("panel8");
				}
			}
		}
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		
	}

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		//System.out.println("Transform!");
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
				   new Vector3f(1.0f, 1.0f, 1.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
	
    @Override
    public ExtendedBlockState getExtendedBlockStateAlternate(ExtendedBlockState state)
    {
    	return new ExtendedBlockState(this, new IProperty[]{WOOD_TYPE}, new IUnlistedProperty[]{OBJModel.OBJProperty.INSTANCE, TextureProperty.instance, PanelProperty.instance});
    }
    
    @Override
    public IExtendedBlockState getIExtendedBlockStateAlternate(BiblioTileEntity biblioTile, IExtendedBlockState state)
    {
    	TextureState textureString = new TextureState(biblioTile.getCustomTextureString()); // My own custom property just for the texture string.
    	state = state.withProperty(TextureProperty.instance, textureString);
    	if (biblioTile instanceof TileEntityFurniturePaneler)
    	{
    		TileEntityFurniturePaneler paneler = (TileEntityFurniturePaneler)biblioTile;
    		PanelState panalTextureString = new PanelState(paneler.getCustomCraftingTex());
    		state = state.withProperty(PanelProperty.instance, panalTextureString);
    	}
    	
    	return state;
    }
   
    
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		return this.getBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.63F, 1.0F);
	}
}
