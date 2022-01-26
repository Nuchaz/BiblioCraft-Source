package jds.bibliocraft.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jds.bibliocraft.entity.EntityCandleFX;
import jds.bibliocraft.helpers.EnumMetalType;
import jds.bibliocraft.tileentities.BiblioLightTileEntity;
import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLanternGold extends BiblioLightBlock
{
	public static String name = "LanternGold";
	public static BlockLanternGold instance = new BlockLanternGold();
	
	public BlockLanternGold()
	{
		super(name);
	}

	@Override
	public List<String> getModelParts(BiblioTileEntity tile) 
	{
		List<String> modelParts = new ArrayList<String>();
		modelParts.add("lamp");
		modelParts.add("candle");
		modelParts.add("glass");
		switch (tile.getVertPosition())
		{
			case CEILING: 
			{
				modelParts.add("lampHanger");
				modelParts.add("topPlate");
				break;
			}
			case WALL:
			{
				modelParts.add("lampHanger");
				modelParts.add("wallPlate");
				break;
			}
			case FLOOR:
			{
				break;
			}
		}
		return modelParts;
	}

	@Override
	public void additionalLightPlacmentCommands(BiblioTileEntity biblioTile) 
	{
		if (biblioTile instanceof BiblioLightTileEntity)
		{
			BiblioLightTileEntity light = (BiblioLightTileEntity)biblioTile;
			light.setLightType(EnumMetalType.GOLD);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		return this.getBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.7F, 0.7F);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand)
    {
		Minecraft mc = Minecraft.getMinecraft();
		Particle candleFlame = new EntityCandleFX(worldIn, pos.getX()+0.5, pos.getY()+0.3, pos.getZ()+0.5, 0.0D, 0.001D, 0.0D);
		mc.effectRenderer.addEffect(candleFlame);
    }
}