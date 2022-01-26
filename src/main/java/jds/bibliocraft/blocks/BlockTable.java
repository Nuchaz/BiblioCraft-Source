package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.helpers.EnumColor;
import jds.bibliocraft.items.ItemDrill;
import jds.bibliocraft.states.TextureState;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityTable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockTable extends BiblioWoodBlock
{
	public static final String name = "Table";
	public static final BlockTable instance = new BlockTable();
	
	public BlockTable()
	{
		super(name, false);
	}

	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if (!world.isRemote)
		{
			ItemStack playerhand = player.getHeldItem(EnumHand.MAIN_HAND);
			TileEntityTable tabletile = (TileEntityTable)world.getTileEntity(pos);
			if (side == EnumFacing.UP)
			{
				if (player.isSneaking())
				{
					player.openGui(BiblioCraft.instance, 9, world, pos.getX(), pos.getY(), pos.getZ()); 
					return true;
				}
				else
				{
					 if (tabletile != null)
					 {
						 if (playerhand != ItemStack.EMPTY)
						 {
							 if(playerhand.getItem() == Item.getItemFromBlock(Blocks.CARPET))
							 {
								 int additem = tabletile.setTableCloth(playerhand);
								 if (additem != -1)
								 {
									 if (additem == 0)
									 {
										player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
									 }
									 else
									 {
										playerhand.setCount(additem);
									 	player.inventory.setInventorySlotContents(player.inventory.currentItem, playerhand);
									 }
								 }
								 return true;
							 }
							 
							 Item drilltest = playerhand.getItem();
							 if(drilltest != ItemStack.EMPTY.getItem())
							 {
								 if (drilltest instanceof ItemDrill)
								 {
									 dropStackInSlot(world, pos, 1, new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
									 tabletile.setTableCloth(ItemStack.EMPTY);
									 return false;
								 }
							 }
	
						 }
						 if (playerhand != ItemStack.EMPTY && !tabletile.isSlotFull())
						 {
							 //place the item in the inventory
							 tabletile.addStackToInventoryFromWorld(playerhand, 0, player);
							 return true;
						 }
						 if (tabletile.isSlotFull())
						 {
							 // drop the item
							 dropStackInSlot(world, pos, 0, new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
							 tabletile.setTableSlot(ItemStack.EMPTY);
							 return true;
						 }
					 }
				 }
				 return true;
			 }
			 else
			 {
				 //not top of table
				 if (playerhand != ItemStack.EMPTY)
				 {
					 if(playerhand.getItem() == Item.getItemFromBlock(Blocks.CARPET))
					 {
						 int additem = tabletile.setCarpet(playerhand);
						 if (additem == -1)
						 {
							 
						 }
						 else
						 {
							 if (additem == 0)
							 {
								player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
							 }
							 else
							 {
								playerhand.setCount(additem);
								player.inventory.setInventorySlotContents(player.inventory.currentItem, playerhand);
							 }
						 }
						 return true;
					 }
				 }
				 
			 }
			 if (playerhand != ItemStack.EMPTY && tabletile != null)
			 {
				 String itemname = playerhand.toString().toLowerCase();
				 if (itemname.contains("measure") || itemname.contains("wrench") || itemname.contains("screwdriver") || itemname.contains("crowbar") || itemname.contains("bibliodrill") || itemname.contains("handdrill"))
				 {
					 if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH)
					 {
						 int xangle = tabletile.getSlotX();
						 switch (xangle)
						 {
							 case 0:{tabletile.setSlotX(1); break;}
							 case 1:{tabletile.setSlotX(2); break;}
							 case 2:{tabletile.setSlotX(3); break;}
							 default:{tabletile.setSlotX(0); break;}
						 }
					 }
					 if (side == EnumFacing.EAST || side == EnumFacing.WEST)
					 {
						 int yangle = tabletile.getSlotY();
						 switch (yangle)
						 {
							 case 0:{tabletile.setSlotY(1); break;}
							 case 1:{tabletile.setSlotY(2); break;}
							 case 2:{tabletile.setSlotY(3); break;}
							 default:{tabletile.setSlotY(0); break;}
						 }
					 }
				 }
			 }
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityTable();
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("monoleg");
		modelParts.add("tableBevel01");
		modelParts.add("tableBevel02");
		modelParts.add("tableBevel03");
		modelParts.add("tableBevel04");
		
		if (tile instanceof TileEntityTable)
		{
			TileEntityTable table = (TileEntityTable)tile;
			modelParts = new ArrayList<String>();
			if (table.getMonoleg())
				modelParts.add("monoleg");
			if (table.getLeg1())
				modelParts.add("leg01");
			if (table.getLeg2())
				modelParts.add("leg02");
			if (table.getLeg3())
				modelParts.add("leg03");
			if (table.getLeg4())
				modelParts.add("leg04");
			if (table.getTop1())
			{ 
				modelParts.add("tableBevel01"); 
				if (table.isClothSlotFull())
					modelParts.add("bevCloth001");
			} 
			else 
			{ 
				modelParts.add("tableSquare01"); 
				if (table.isClothSlotFull())
				{
					modelParts.add("squareCloth01");
					if (table.getExpSide1())
						modelParts.add("squareClothSide001a");
					if (table.getExpSide4())
						modelParts.add("squareClothSide001b");
				}
			}
			if (table.getTop2())
			{ 
				modelParts.add("tableBevel02"); 
				if (table.isClothSlotFull())
					modelParts.add("bevCloth004");
			} 
			else 
			{ 
				modelParts.add("tableSquare02"); 
				if (table.isClothSlotFull())
				{
					modelParts.add("squareCloth004");
					if (table.getExpSide1())
						modelParts.add("squareClothSide004b");
					if (table.getExpSide3())
						modelParts.add("squareClothSide004a");
				}
			}
			if (table.getTop3())
			{ 
				modelParts.add("tableBevel03"); 
				if (table.isClothSlotFull())
					modelParts.add("bevCloth003");
			} 
			else 
			{ 
				modelParts.add("tableSquare03"); 
				if (table.isClothSlotFull())
				{
					modelParts.add("squareCloth003");
					if (table.getExpSide2())
						modelParts.add("squareClothSide003a");
					if (table.getExpSide3())
						modelParts.add("squareClothSide003b");
				}
			}
			if (table.getTop4())
			{ 
				modelParts.add("tableBevel04"); 
				if (table.isClothSlotFull())
				{
					modelParts.add("bevCloth002");
				}
			} 
			else 
			{ 
				modelParts.add("tableSquare04"); 
				if (table.isClothSlotFull())
				{
					modelParts.add("squareCloth002");
					if (table.getExpSide2())
						modelParts.add("squareClothSide002b");
					if (table.getExpSide4())
						modelParts.add("squareClothSide002a");
				}
			}
			if (table.isCarpetFull())
			{
				modelParts.add("carpet");
			}
		}
		
		return modelParts;
	}

	@Override
	public void additionalPlacementCommands(BiblioTileEntity biblioTile, EntityLivingBase player) 
	{
		biblioTile.setAngle(EnumFacing.SOUTH);
		if (biblioTile instanceof TileEntityTable)
		{
			checkNeighborTables(biblioTile.getWorld(), biblioTile.getPos().getX(), biblioTile.getPos().getY(), biblioTile.getPos().getZ(), (TileEntityTable)biblioTile);
		}
	}

	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile) 
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(-0.0f, 0.0f, -0.0f), 
			     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
			     new Vector3f(1.0f, 1.0f, 1.0f), 
			     new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}

	@Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
		 if (side == EnumFacing.UP)
		 {
			 return true;
		 }
		 else
		 {
			 return false;
		 }
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		AxisAlignedBB output = this.getBlockBounds(0.00F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		TileEntity tilee = blockAccess.getTileEntity(pos);
		if (tilee != null && tilee instanceof TileEntityTable)
		{
			TileEntityTable tile = (TileEntityTable)tilee;
			boolean leg1 = tile.getLeg1();
			boolean leg2 = tile.getLeg2();
			boolean leg3 = tile.getLeg3();
			boolean leg4 = tile.getLeg4();
			boolean legMono = tile.getMonoleg();
			if (leg1 == false && leg2 == false && leg3 == false && leg4 == false && legMono == false)
			{
				output = this.getBlockBounds(0.00F, 0.88F, 0.0F, 1.0F, 1.0F, 1.0F);
			}
		}
		return output;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityTable)
		{
			TileEntityTable tabletile = (TileEntityTable)world.getTileEntity(pos);
			checkNeighborTables(tile.getWorld(), pos.getX(), pos.getY(), pos.getZ(), tabletile);
			//world.markBlockForUpdate(pos);
			tabletile.getWorld().notifyBlockUpdate(tabletile.getPos(), tabletile.getWorld().getBlockState(tabletile.getPos()), tabletile.getWorld().getBlockState(tabletile.getPos()), 3);
		}
	}
	
	 public void checkNeighborTables(World world, int x, int y, int z, TileEntityTable table)
	 {
		 Block blockid1 = world.getBlockState(new BlockPos(x+1, y, z)).getBlock();
		 Block blockid2 = world.getBlockState(new BlockPos(x-1, y, z)).getBlock();
		 Block blockid3 = world.getBlockState(new BlockPos(x, y, z+1)).getBlock();
		 Block blockid4 = world.getBlockState(new BlockPos(x, y, z-1)).getBlock();
		 if (blockid1 instanceof BlockTable || blockid2 instanceof BlockTable || blockid3 instanceof BlockTable || blockid4 instanceof BlockTable)
		 {
			// set quad legs and tops acorrding
			 boolean l1 = true;
			 boolean l2 = true;
			 boolean l3 = true;
			 boolean l4 = true;
			 boolean t1 = true;
			 boolean t2 = true;
			 boolean t3 = true;
			 boolean t4 = true;
			 boolean ml = false;
			 
			 boolean expside1 = false;
			 boolean expside2 = false;
			 boolean expside3 = false;
			 boolean expside4 = false;
			 
			 if (blockid1 instanceof BlockTable )
			 {
				// System.out.println("Block1");
				 l1 = false;
				 l2 = false;
				 t1 = false;
				 t2 = false;
			 }
			 else
			 {
				 expside1 = true;
			 }
			 if (blockid2 instanceof BlockTable)
			 {
				// System.out.println("Block2");
				 l3 = false;
				 l4 = false;
				 t3 = false;
				 t4 = false;
			 }
			 else
			 {
				 expside2 = true;
			 }
			 if (blockid3 instanceof BlockTable)
			 {
				// System.out.println("Block3");
				 l2 = false;
				 l3 = false;
				 t2 = false;
				 t3 = false;
			 }
			 else
			 {
				 expside3 = true;
			 }
			 if (blockid4 instanceof BlockTable)
			 {
				 //System.out.println("Block4");
				 l1 = false;
				 l4 = false;
				 t1 = false;
				 t4 = false;
			 }
			 else
			 {
				 expside4 = true;
			 }
			 
			 table.setLegs(l1, l2, l3, l4, ml);
			 table.setTops(t1, t2, t3, t4);
			 table.setExposeSides(expside1, expside2, expside3, expside4);
		 }
		 else
		 {
				 // this sets the table to be a single table with 4 beveled edges and 1 center post
			 table.setLegs(false, false, false, false, true);
			 table.setTops(true, true, true, true);
			
		 }
	 }
	 
		@Override
	    public TextureState addAdditionTextureStateInformation(BiblioTileEntity tile, TextureState state)
	    {
			ItemStack cloth = tile.getStackInSlot(1);
			ItemStack carpet = tile.getStackInSlot(2);
			if (cloth != ItemStack.EMPTY)
			{
				state.setColorOne(EnumColor.getColorFromCarpetOrWool(cloth));
			}
			if (carpet != ItemStack.EMPTY)
			{
				state.setColorTwo(EnumColor.getColorFromCarpetOrWool(carpet));
			}
			return state;
	    }
}
