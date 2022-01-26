package jds.bibliocraft.items;

import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.gui.GuiMapWaypoint;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMapTool extends Item
{
	public static final String name = "maptool";
	public static final ItemMapTool instance = new ItemMapTool();
	
	public ItemMapTool()
	{
		super();
		setCreativeTab(BlockLoader.biblioTab);
		setUnlocalizedName(name);
		maxStackSize = 1;
		setRegistryName(name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (world.isRemote)
		{
			
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
	
	@SideOnly(Side.CLIENT)
    public void openWaypointGUI(World world, EntityPlayer player, float xPin, float yPin, TileEntityMapFrame tile, int waypointNum)
    {
		Minecraft.getMinecraft().displayGuiScreen(new GuiMapWaypoint(world, player, xPin, yPin, tile, waypointNum));
    }
    
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile instanceof TileEntityMapFrame)
		{
			TileEntityMapFrame mapFrame = (TileEntityMapFrame)tile;
			if (mapFrame != null)
			{
				int faceCheck = mapFrame.checkFace(mapFrame.getAngle(), side, mapFrame.getVertPosition()); // Change this from the ID to another thing
				if (faceCheck != -1)
				{
					int pinPoint = -1;
					switch (faceCheck)
					{
						case 0:
						{
							//mapFrame.addPinCoords(hitX, hitY);
							pinPoint = mapFrame.findPinCoords(hitX, hitY);
							if (world.isRemote)
							{
								openWaypointGUI(world, player, hitX, hitY, mapFrame, pinPoint);
							}
							break;
						}
						case 1:
						{
							//mapFrame.addPinCoords(hitZ, hitY);
							pinPoint = mapFrame.findPinCoords(hitZ, hitY);
							if (world.isRemote)
							{
								openWaypointGUI(world, player, hitZ, hitY, mapFrame, pinPoint);
							}
							break;
						}
						case 2:
						{
							//mapFrame.addPinCoords(hitX, hitZ);
							pinPoint = mapFrame.findPinCoords(hitX, hitZ);
							if (world.isRemote)
							{
								openWaypointGUI(world, player, hitX, hitZ, mapFrame, pinPoint);
							}
							break;
						}
					}
				}
			}
		}
		
		return EnumActionResult.FAIL;
	}
}
