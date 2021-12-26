package jds.bibliocraft.blocks;

//import jds.bibliocraft.items.ItemDrill;
//import jds.bibliocraft.items.ItemLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.Config;
import jds.bibliocraft.GuiLoader;
import jds.bibliocraft.blocks.BiblioWoodBlock.EnumWoodType;
import jds.bibliocraft.blocks.blockitems.BlockItemBookcase;
import jds.bibliocraft.config.ServerConfig;
import jds.bibliocraft.containers.ContainerBookcase;
import jds.bibliocraft.containers.provider.ContainerBookcaseProvider;
import jds.bibliocraft.helpers.EnumWoodsType;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityBookcase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;;


public class BlockBookcase extends BiblioWoodBlock 
{
	public static final String name = "bookcase";
	
	private INamedContainerProvider containerProvider = new INamedContainerProvider() 
	{
		
		@Override
		public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) 
		{
			//                         Inventory inv = new Inventory(9);
            //for (int i = 0; i < inv.getSizeInventory(); i++)
            //{
           //     inv.setInventorySlotContents(i, new ItemStack(Items.DIAMOND));
           // }
           // return new TestContainer(p_createMenu_1_, inv, text);
			return new ContainerBookcase(id, inv, new Inventory(16));
		}
		
		@Override
		public ITextComponent getDisplayName() 
		{
			return new StringTextComponent("bookcasecontainer");
		}
	};
	//public static final BlockBookcase instance = new BlockBookcase(name);
	
	public BlockBookcase(EnumWoodsType wood) 
	{
		super(name, true, wood);
		//setTickRandomly(true); // TODO this doesnt work
		
		//setRegistryName(blockName);
	}
	
	public BlockBookcase(String nameo, EnumWoodsType wood) 
	{
		super(nameo, true, wood);
		//setTickRandomly(true); // TODO this doesnt work
		
		//setRegistryName(blockName);
	}
	


	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, BlockState state, PlayerEntity player, Direction side, double hitX, double hitY, double hitZ) 
	{
		//ServerConfig.loadConfig(); // TODO temp testing
		if (!world.isRemote)
		{
			
			BiblioTileEntity biblioTile = (BiblioTileEntity)world.getTileEntity(pos);
			ItemStack playerhand = player.getHeldItem(Hand.MAIN_HAND);
			if (playerhand != ItemStack.EMPTY && playerhand.getItem() instanceof BlockItemBookcase)
			{
				return false;
			}
			
			if (biblioTile instanceof TileEntityBookcase)
			{
				
				TileEntityBookcase tileBookCase = (TileEntityBookcase)biblioTile;
				double hitreduction = hitY - pos.getY();
				int yCheck = (int) (hitreduction * 2);
				Direction angle = tileBookCase.getAngle();
				//System.out.println("on block activated ycheck: " + yCheck);
				if  (yCheck == 1)  // so this is the top shelf
				{
					
					int booktest = isWhatBook(angle, hitX - pos.getX(), hitZ - pos.getZ());
					if (player.isSneaking())
					{
						if (booktest >= 0 && booktest < 16)
						{
							//dropStackInSlot(world, pos, booktest, getDropPositionOffset(pos, player));
							//spawnDrops(state, world, getDropPositionOffset(pos, player), biblioTile, player, biblioTile.getStackInSlot(booktest));
							spawnAsEntity(world, getDropPositionOffset(pos, player), biblioTile.getStackInSlot(booktest));
							tileBookCase.setBook(booktest, ItemStack.EMPTY);
						}
						return true;
					}
					else
					{
						//System.out.println("on block activated what book:" + booktest);
						if (playerhand != ItemStack.EMPTY)
						{
							System.out.println(!Config.isBlock(playerhand));
							boolean validBook = true; // Config.testBookValidity(playerhand) && !Config.isBlock(playerhand) // TODO this isnt working right
							if (validBook)
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
								//NetworkHooks.openGui()
								//player.openContainer
								System.out.println("gui haa ha");
								//player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ()); TODO temp
								return true;
							}
							
						}
					}
					
				}
				else if (yCheck == 0)  // and this is the bottom shelf
				{
					int booktest = isWhatBook(angle, hitX - pos.getX(), hitZ - pos.getZ()) + 8;
					if (player.isSneaking())
					{
						if (booktest >= 0 && booktest < 16)
						{
							//dropStackInSlot(world, pos, booktest, getDropPositionOffset(pos, player));
							//spawnDrops(state, world, getDropPositionOffset(pos, player), biblioTile, player, biblioTile.getStackInSlot(booktest));
							spawnAsEntity(world, getDropPositionOffset(pos, player), biblioTile.getStackInSlot(booktest));
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
								System.out.println("gui hoo ho");
								//player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ()); TODO temp
								return true;
							}
							
						}
					}
					
				}
				System.out.println("default guyi");
				//NetworkHooks.openGui(player, new ContainerBookcase(0, player.inventory, new PacketBuffer(null)));
				// the gui should open if the player does not have a book in hand. Will use shift-click to remove books.
				//player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ()); TODO temp
				//ContainerBookcaseProvider provider = new ContainerBookcaseProvider(tileBookCase);
				player.openContainer(new ContainerBookcaseProvider(tileBookCase));
			}
		}
		return true;
	}

	@Override
    public void additionalPlacementCommands(BiblioTileEntity biblioTile, LivingEntity player)
    {
    	//System.out.println("Bookcase placment?");
    }
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) 
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

	/* TODO this is borked
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
	*/

	@Override
    public boolean canProvidePower(BlockState state)
    {
        return true;
    }
	
	@Override
    public int getWeakPower(BlockState state, IBlockReader blocka, BlockPos pos, Direction side)
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
			Direction angle = bookcase.getAngle();
			if (bookcase.getredstone())
			{
				switch (angle)
				{
				case SOUTH:
				{
					if (side == Direction.EAST) 
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
					if (side == Direction.SOUTH)
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
					if (side == Direction.WEST)
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
					if (side == Direction.NORTH)
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
	public int getStrongPower(BlockState state, IBlockReader worldIn, BlockPos pos, Direction side)
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
	
	public static int isWhatBook(Direction angle, double hitX, double hitZ)
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
	
/* TODO temp disable
    @OnlyIn(Dist.CLIENT)
    @Override
    public void tick(BlockState state, World world, BlockPos pos, Random rando)
    {
        super.tick(state, world, pos, rando);
        
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
		                            //world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, px, py, pz, velx, vely, velz, new int[0]); TODO borked
		                        }
		                    }
		                }
		            }
		        }
   			}
   			
        }
    }
*/
	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		return transform;
	}
    
}
