package jds.bibliocraft.blocks;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import jds.bibliocraft.tileentities.TileEntityBookcase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;

public class TestBlock extends ContainerBlock 
{

	public TestBlock() 
	{
		super(Block.Properties.create(Material.WOOD));
		setRegistryName("testblock");
		// TODO Auto-generated constructor stub
	}
	
	public void testLoot() 
	{
		ResourceLocation resourcelocation = Registry.BLOCK.getKey(this);
		System.out.println("dingle bingle fingle wooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooh");
		System.out.println(resourcelocation.getNamespace());
		System.out.println(resourcelocation.getPath());
	
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) 
	{
		// TODO Auto-generated method stub
		return new TileEntityBookcase();
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) 
	{
		return BlockRenderType.MODEL;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() 
	{
		return BlockRenderLayer.SOLID;
	}
	
	@Override
	public boolean isSolid(BlockState state) 
	{
		// this determines if the block should hide faces around it. I could use block states and check against the solid sides of the block or just leave this false.
		return false;
	}
	
	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) 
	{
		return getBiblioShape(state, worldIn, pos);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) 
	{
		//System.out.println("get shape");
		// TODO Auto-generated method stub
		// this changes the outline when you look at a thing, context allows me to know what item the player has and if he is sneaking and change the shapre accordingly. very neat.
		
		return getBiblioShape(state, worldIn, pos);
	}
	
	@Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) 
	{
		//System.out.println("get collision shape");
		// this gets called when expected and kind of works, but I don't think its synced on the server, maybe only the client here?
		//context.
		// it seems like this is not getting updated either server side or client side
		//System.out.println("is client: " + worldIn.)
		// this effects the collision, but I'm not sure its updating on bothsides? maybe only server side, not client?
		//System.out.println("is client? : " + Minecraft.getInstance().world.isRemote);
		testLoot();
		return getBiblioShape(state, worldIn, pos);
	}
	
	public VoxelShape getBiblioShape(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		VoxelShape collision = VoxelShapes.create(new AxisAlignedBB(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F));
		return collision;
	}
}
