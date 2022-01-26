package jds.bibliocraft.blocks;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityDinnerPlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

public class BlockDinnerPlate extends BiblioSimpleBlock
{
	public static final BlockDinnerPlate instance = new BlockDinnerPlate();
	public static final String name = "DinnerPlate";
	private int clicks = 0;
	
	public BlockDinnerPlate()
	{
		super(Material.GLASS, SoundType.GLASS, name);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		return this.getBlockBounds(0.15F, 0.0F, 0.15F, 0.85F, 0.1F, 0.85F);
	}
	
	@Override
	public boolean onBlockActivatedCustomCommands(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			ItemStack playerStack = player.getHeldItem(EnumHand.MAIN_HAND);
			TileEntityDinnerPlate plateTile = (TileEntityDinnerPlate)world.getTileEntity(pos);
			if (plateTile != null)
			{
				if (playerStack != ItemStack.EMPTY)
				{
					if (playerStack.getItem() instanceof ItemFood)
					{
						int addedFood = plateTile.addFood(playerStack);
						if (addedFood != -1)
						{
							if (addedFood == 0)
							{
								player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
							}
							else
							{
								ItemStack newStack = playerStack.copy();
								newStack.setCount(addedFood);
								//System.out.println(addedFood);
								player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
								player.inventory.setInventorySlotContents(player.inventory.currentItem, newStack);
							}
							return true;
						}
					}
				}
				if (plateTile.isPlateEmpty() || player.isSneaking())
				{
					player.openGui(BiblioCraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
					return true;
				}
				
				FoodStats playerFoodStats = player.getFoodStats();
				if (playerFoodStats.needFood())
				{
					clicks++;
					if (clicks >= 1 && clicks <= 3)
					{
						world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
					}
					else
					{
						ItemStack slot0 = plateTile.getStackInSlot(0);
						ItemStack slot1 = plateTile.getStackInSlot(1);
						ItemStack slot2 = plateTile.getStackInSlot(2);
						if (slot0 != ItemStack.EMPTY && playerFoodStats.needFood() && slot0.getItem() instanceof ItemFood)
						{
							ItemStack food0 = plateTile.getFood(0);
							if (plateTile.isFoodHaveBowl(food0, world, player))
							{
								dropEmptyBowl(world, pos);
							}
						}
						if (slot1 != ItemStack.EMPTY && playerFoodStats.needFood() && slot1.getItem() instanceof ItemFood)
						{
							ItemStack food1 = plateTile.getFood(1);
							if (plateTile.isFoodHaveBowl(food1, world, player))
							{
								dropEmptyBowl(world, pos);
							}
						}
						if (slot2 != ItemStack.EMPTY && playerFoodStats.needFood() && slot2.getItem() instanceof ItemFood)
						{
							ItemStack food2 = plateTile.getFood(2);
							if (plateTile.isFoodHaveBowl(food2, world, player))
							{
								dropEmptyBowl(world, pos);
							}
						}
						world.playSound(player, pos, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
						clicks = 0;
					}
				}
			}
		}
		return true;
	}
	
	 private void dropEmptyBowl(World world, BlockPos pos)
	{
		ItemStack bowl = new ItemStack(Items.BOWL, 0);
		bowl.setCount(1);
		if (bowl != ItemStack.EMPTY && bowl.getCount() > 0)
		{
			float iAdjust = 0;
			float kAdjust;
			//System.out.println(caseTile.getAngle());

			EntityItem entityItem = new EntityItem(world, pos.getX()+0.5F, pos.getY()+0.5F, pos.getZ()+0.5F, new ItemStack(bowl.getItem(), bowl.getCount(), bowl.getItemDamage()));
			entityItem.motionX = 0;
			entityItem.motionY = 0;
			entityItem.motionZ = 0;
			world.spawnEntity(entityItem);
			bowl.setCount(0);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityDinnerPlate();
	}
	
	@Override
	public TRSRTransformation getAdditionalTransforms(TRSRTransformation transform, BiblioTileEntity tile)
	{
		transform = transform.compose(new TRSRTransformation(new Vector3f(0.0f, 0.0f, 0.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f), 
				   new Vector3f(1.0f, 1.0f, 1.0f), 
				   new Quat4f(0.0f, 1.0f, 0.0f, 1.0f)));
		return transform;
	}
}
