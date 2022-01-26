package jds.bibliocraft.blocks;

//import jds.bibliocraft.items.ItemDrill;
//import jds.bibliocraft.items.ItemLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.blockitems.BlockItemBookcase;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityBookcase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BlockBookcase extends BiblioWoodBlock 
{
	public static final String name = "Bookcase";
	public static final BlockBookcase instance = new BlockBookcase(name);
	
	public BlockBookcase(String blockName) 
	{
		super(blockName, true);
		setTickRandomly(true);
		//setRegistryName(blockName);
	}

	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote)
		{
			BiblioTileEntity biblioTile = (BiblioTileEntity)world.getTileEntity(pos);
			ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
			if (playerhand != ItemStack.EMPTY && playerhand.getItem() instanceof BlockItemBookcase)
			{
				return false;
			}
			
			if (biblioTile instanceof TileEntityBookcase)
			{
				TileEntityBookcase tileBookCase = (TileEntityBookcase)biblioTile;
				int yCheck = (int) (hitY * 2);
				EnumFacing angle = tileBookCase.getAngle();
				if  (yCheck == 1)  // so this is the top shelf
				{
					int booktest = isWhatBook(angle, hitX, hitZ);
					if (player.isSneaking())
					{
						if (booktest >= 0 && booktest < 16)
						{
							dropStackInSlot(world, pos, booktest, getDropPositionOffset(pos, player));
							tileBookCase.setBook(booktest, ItemStack.EMPTY);
						}
						return true;
					}
					else
					{
						if (playerhand != ItemStack.EMPTY)
						{
							if (!Config.isBlock(playerhand) && Config.testBookValidity(playerhand))
							{
								// here is where I try to add the book.
								if (booktest >= 0 && booktest < 16)
								{
									boolean addedBook = tileBookCase.setBook(booktest, playerhand); 
									if (addedBook)
									{
										player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
										return true;
									}
								}
							}
							else
							{
								player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
								return true;
							}
							
						}
					}
					
				}
				else if (yCheck == 0)  // and this is the bottom shelf
				{
					int booktest = isWhatBook(angle, hitX, hitZ) + 8;
					if (player.isSneaking())
					{
						if (booktest >= 0 && booktest < 16)
						{
							dropStackInSlot(world, pos, booktest, getDropPositionOffset(pos, player));
							tileBookCase.setBook(booktest, ItemStack.EMPTY);
						}
						return true;
					}
					else
					{
						if (playerhand != ItemStack.EMPTY)
						{
							if (!Config.isBlock(playerhand) && Config.testBookValidity(playerhand))
							{
								if (booktest >= 0 && booktest < 16)
								{
									boolean addedBook = tileBookCase.setBook(booktest, playerhand);  
									if (addedBook)
									{
										player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
										return true;
									}
								}
							}
							else
							{
								player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
								return true;
							}
							
						}
					}
					
				}
				// the gui should open if the player does not have a book in hand. Will use shift-click to remove books.
				player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}

	@Override
    public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player)
    {
    	//System.out.println("Bookcase placment?");
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityBookcase(); 
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		if (tile instanceof TileEntityBookcase)
		{
			TileEntityBookcase bookcase = (TileEntityBookcase)tile;
	    	int[] books = bookcase.getCheckedBooks();
	    	
	    	modelParts.add("bookcase");
	    	for (int i = 0; i < books.length; i++)
	    	{
	    		if (books[i] == 1)
	    		{
	    			modelParts.add("book" + (i+1));
	    		}
	    	}
		}
		else
		{
			modelParts = Lists.newArrayList(OBJModel.Group.ALL);
		}
    	
		return modelParts;
	}

	@Override 
    public float getEnchantPowerBonus(World world, BlockPos pos)
    {
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityBookcase)
		{
			TileEntityBookcase bookcase = (TileEntityBookcase) tile;
			return (1.0F / 8.0F)*bookcase.getFilledSlots();
		}
		else
		{
			return 0.0F;
		}
    }
	

	@Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
	
	@Override
    public int getWeakPower(IBlockState state, IBlockAccess blocka, BlockPos pos, EnumFacing side)
    {
		// IMPORTANT Number To Angle Conversion System
		// 0 = West
		// 1 = North
		// 2 = East
		// 3 = South
		
		// Faces
		// 0 = bottom
		// 1 = top
		// 2 = front of angle 1 = North
		// 3 = front of angle 3 = South
		// 4 = front of angle 0 = West
		// 5 = front of angle 2 = East
		
		TileEntity tile = blocka.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityBookcase)
		{
			TileEntityBookcase bookcase = (TileEntityBookcase)blocka.getTileEntity(pos);
			EnumFacing angle = bookcase.getAngle();
			if (bookcase.getredstone())
			{
				switch (angle)
				{
				case SOUTH:
				{
					if (side == EnumFacing.EAST) 
					{
						return 0;
					}
					else
					{
						return bookcase.getRedstoneBookSlot();
					}
				}
				case WEST:
				{
					if (side == EnumFacing.SOUTH)
					{
						return 0;
					}
					else
					{
						return bookcase.getRedstoneBookSlot();
					}
				}
				case NORTH:
				{
					if (side == EnumFacing.WEST)
					{
						return 0;
					}
					else
					{
						return bookcase.getRedstoneBookSlot();
					}
				}
				case EAST:
				{
					if (side == EnumFacing.NORTH)
					{
						return 0;
					}
					else
					{
						return bookcase.getRedstoneBookSlot();
					}
				}
				default: return 0;
				}
			}
		}
		return 0;
    }
	
	@Override
	public int getStrongPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
		
		return getWeakPower(state, worldIn, pos, side);
    }
	
	public static boolean isTopShelf(float hitY)
	{
		if (hitY > 0.5)
		{
			//Top shelf
			return true;
		}
		else
		{
			//Bottom shelf
			return false;
		}
	}
	
	public static int isWhatBook(EnumFacing angle, float hitX, float hitZ)
	{
		int xt = (int) (hitX * 8);
		int zt = (int)( hitZ * 8);
		switch (angle)
		{
			case SOUTH:
			{ 
				return zt;
			}
			case WEST:
			{ 
				switch (xt)
				{
				case 0: return 7;
				case 1: return 6;
				case 2: return 5;
				case 3: return 4;
				case 4: return 3;
				case 5: return 2;
				case 6: return 1;
				case 7: return 0;
				default: break;
				}
			}
			case NORTH:
			{ 
				switch (zt)
				{
				case 0: return 7;
				case 1: return 6;
				case 2: return 5;
				case 3: return 4;
				case 4: return 3;
				case 5: return 2;
				case 6: return 1;
				case 7: return 0;
				default: break;
				}
			}
			case EAST:
			{ 
				return xt;
			}
			default:break;
		}
		return -1;
	}
	

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rando)
    {
        super.randomDisplayTick(state, world, pos, rando);
        
    	TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof TileEntityBookcase)
        {
	        TileEntityBookcase bookcase = (TileEntityBookcase)tile;
	        if (bookcase != null && bookcase.getFilledSlots() > 0)
	        {
	        	
		        for (int x = pos.getX() - 2; x <= pos.getX() + 2; ++x)
		        {
		            for (int z = pos.getZ() - 2; z <= pos.getZ() + 2; ++z)
		            {
		                if (x > pos.getX() - 2 && x < pos.getX() + 2 && z == pos.getZ() - 1)
		                {
		                    z = pos.getZ() + 2;
		                }
		                if (rando.nextInt(8) == 0)
		                {
		                	
		                    for (int y = pos.getY() - 1; y <= pos.getY() + 1; ++y)
		                    {
		                    	
		                        if (world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.ENCHANTING_TABLE)
		                        {
		                            if (!world.isAirBlock(new BlockPos((x - pos.getX()) / 2 + pos.getX(), y, (z - pos.getZ()) / 2 + pos.getZ())))
		                            {
		                                break;
		                            }
		                            double px = (double)x + 0.5D;
		                            double py = (double)y + 2.0D;
                            		double pz = (double)z + 0.5D;
		                            double velx = (double)((float)(pos.getX() - x) + rando.nextFloat()) - 0.5D;
		                            double vely = (double)((float)(pos.getY() - y) - rando.nextFloat() - 1.0F);
		                            double velz = (double)((float)(pos.getZ() - z) +  rando.nextFloat()) - 0.5D;
		                            world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, px, py, pz, velx, vely, velz, new int[0]);
		                        }
		                    }
		                }
		            }
		        }
   			}
   			
        }
    }

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		return transform;
	}
    
}
