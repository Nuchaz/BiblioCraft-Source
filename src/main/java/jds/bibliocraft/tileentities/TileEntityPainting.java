package jds.bibliocraft.tileentities;

import jds.bibliocraft.Config;
import jds.bibliocraft.blocks.BlockPaintingFrameBorderless;
import jds.bibliocraft.blocks.BlockPaintingFrameFancy;
import jds.bibliocraft.blocks.BlockPaintingFrameFlat;
import jds.bibliocraft.blocks.BlockPaintingFrameMiddle;
import jds.bibliocraft.blocks.BlockPaintingFrameSimple;
import jds.bibliocraft.helpers.EnumPaintingFrame;
import jds.bibliocraft.items.ItemPaintingCanvas;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityPainting extends BiblioTileEntity
{
	// TODO something in this is crashing
	public int paintingType = 0;
	public String paintingTitle = "blank";
	public int paintingRotation = 0;
	public int paintingMasterCorner = 0;
	public int paintingScale = 1;
	public int paintingAspectRatio = 0;
	public int paintingPixelRes = 0;
	public int customPaintingAspectX = 1;
	public int customPaintingAspectY = 1;
	public boolean connectedTop = false;
	public boolean connectedLeft = false;
	public boolean connectedBottom = false;
	public boolean connectedRight = false;
	public boolean showTLCorner = true;
	public boolean showTRCorner = true;
	public boolean showBRCorner = true;
	public boolean showBLCorner = true;
	public boolean containerUpdate = false;
	public boolean hideFrame;
	public EnumPaintingFrame style = EnumPaintingFrame.BORDERLESS;
	
	public TileEntityPainting()
	{
		super(1, true); // single slot for the painting itself
	}
	
	public void setFrameStyle(EnumPaintingFrame frame)
	{
		this.style = frame;
	}
	
	public EnumPaintingFrame getFrameStyle()
	{
		return this.style;
	}
	
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
    	double distance = Config.renderDistancePainting;
        return distance*distance;
    }
    
	public void setHideFrame(boolean hide)
	{
		this.hideFrame = hide;
	}
	
	public boolean getHideFrame()
	{
		return this.hideFrame;
	}
	
	public void setConnectTop(boolean connect)
	{
		this.connectedTop = connect;
		checkDiagnoalsForCorners(true);
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setConnectLeft(boolean connect)
	{
		this.connectedLeft = connect;
		
		//getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		checkDiagnoalsForCorners(true);
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
		//sendUpdateNotify();
	}
	
	public void setConnectBottom(boolean connect)
	{
		this.connectedBottom = connect;
		checkDiagnoalsForCorners(true);
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setConnectRight(boolean connect)
	{
		this.connectedRight = connect;
		checkDiagnoalsForCorners(true);
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setPaintingRotation(int rot)
	{
		this.paintingRotation = rot;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setPacketUpdate(int corner, int scale, int pixels, int aspect, int rotation, int aspectX, int aspectY)
	{
		this.paintingMasterCorner = corner;
		this.paintingScale = scale;
		this.paintingPixelRes = pixels;
		this.paintingAspectRatio = aspect;
		this.paintingRotation = rotation;
		this.customPaintingAspectX = aspectX;
		this.customPaintingAspectY = aspectY;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setPacketAspectsUpdate(int aspectX, int aspectY)
	{
		this.customPaintingAspectX = aspectX;
		this.customPaintingAspectY = aspectY;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void setContainterUpdate(boolean update)
	{
		this.containerUpdate = update;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public boolean getContainerUpdate()
	{
		return this.containerUpdate;
	}
	
	public int getCustomPaintingAspectX()
	{
		return this.customPaintingAspectX;
	}
	
	public int getCustomPaintingAspectY()
	{
		return this.customPaintingAspectY;
	}
	
	public void resetPaintingData()
	{
		paintingRotation = 0;
		paintingMasterCorner = 0;
		paintingScale = 1;
		paintingAspectRatio = 0;
		paintingPixelRes = 0;
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	public void checkDiagnoalsForCorners(boolean checkFurtherPaintings)
	{
		TileEntity topLeftdiagTile = null;//worldObj.getTileEntity(pos);
		TileEntity topRightdiagTile = null;//worldObj.getTileEntity(pos);
		TileEntity bottomRightdiagTile = null;//worldObj.getTileEntity(pos);
		TileEntity bottomLeftdiagTile = null;//worldObj.getTileEntity(pos);
		switch (this.getAngle())
		{
			case SOUTH:
			{
				topLeftdiagTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()-1));
				topRightdiagTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()+1));
				bottomRightdiagTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()+1));
				bottomLeftdiagTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()-1)); // good
				break;
			}
			case WEST:
			{
				topLeftdiagTile = world.getTileEntity(new BlockPos(pos.getX()+1, pos.getY()+1, pos.getZ()));
				topRightdiagTile = world.getTileEntity(new BlockPos(pos.getX()-1, pos.getY()+1, pos.getZ()));
				bottomRightdiagTile = world.getTileEntity(new BlockPos(pos.getX()-1, pos.getY()-1, pos.getZ()));
				bottomLeftdiagTile = world.getTileEntity(new BlockPos(pos.getX()+1, pos.getY()-1, pos.getZ()));
				break;
			}
			case NORTH:
			{
				topLeftdiagTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()+1));
				topRightdiagTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()-1));
				bottomRightdiagTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()-1));
				bottomLeftdiagTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()+1));
				break;
			}
			case EAST:
			{
				topLeftdiagTile = world.getTileEntity(new BlockPos(pos.getX()-1, pos.getY()+1, pos.getZ()));
				topRightdiagTile = world.getTileEntity(new BlockPos(pos.getX()+1, pos.getY()+1, pos.getZ()));
				bottomRightdiagTile = world.getTileEntity(new BlockPos(pos.getX()+1, pos.getY()-1, pos.getZ()));
				bottomLeftdiagTile = world.getTileEntity(new BlockPos(pos.getX()-1, pos.getY()-1, pos.getZ()));
				break;
			}	
			default: break;
		}
		
		this.showTLCorner = true;
		this.showTRCorner = true;
		this.showBRCorner = true;
		this.showBLCorner = true;
		
		if (topLeftdiagTile != null && topLeftdiagTile instanceof TileEntityPainting)
		{
			TileEntityPainting paintTile = (TileEntityPainting)topLeftdiagTile;
			if (paintTile.getConnectBottom() && paintTile.getConnectRight())
			{
				this.showTLCorner = false;
				if (checkFurtherPaintings)
				{
					paintTile.checkDiagnoalsForCorners(false);
				}
				else
				{
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				}
				paintTile.updateLocalPaintings();
			}
		}
		
		if (topRightdiagTile != null && topRightdiagTile instanceof TileEntityPainting)
		{
			TileEntityPainting paintTile = (TileEntityPainting)topRightdiagTile;
			if (paintTile.getConnectBottom() && paintTile.getConnectLeft())
			{
				this.showTRCorner = false;
				if (checkFurtherPaintings)
				{
					paintTile.checkDiagnoalsForCorners(false);
				}
				else
				{
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				}
				paintTile.updateLocalPaintings();
			}
		}
		
		if (bottomRightdiagTile != null && bottomRightdiagTile instanceof TileEntityPainting)
		{
			TileEntityPainting paintTile = (TileEntityPainting)bottomRightdiagTile;
			if (paintTile.getConnectTop() && paintTile.getConnectLeft())
			{
				this.showBRCorner = false;
				if (checkFurtherPaintings)
				{
					paintTile.checkDiagnoalsForCorners(false);
				}
				else
				{
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				}
				paintTile.updateLocalPaintings();
			}
		}
		
		if (bottomLeftdiagTile != null && bottomLeftdiagTile instanceof TileEntityPainting)
		{
			TileEntityPainting paintTile = (TileEntityPainting)bottomLeftdiagTile;
			if (paintTile.getConnectTop() && paintTile.getConnectRight())
			{
				this.showBLCorner = false;
				if (checkFurtherPaintings)
				{
					paintTile.checkDiagnoalsForCorners(false);
					//paintTile.updateLocalPaintings();
				}
				else
				{
					getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
				}
				paintTile.updateLocalPaintings();
			}
		}
	}
	
	public void updateLocalPaintings()
	{
		TileEntity topTile = null;//worldObj.getTileEntity(pos);
		TileEntity bottomTile = null;//worldObj.getTileEntity(pos);
		TileEntity rightTile = null;//worldObj.getTileEntity(pos);
		TileEntity leftTile = null;//worldObj.getTileEntity(pos);
		switch (this.getAngle())
		{
			case SOUTH:
			{
				topTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()));
				bottomTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()));
				rightTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1));
				leftTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)); // good
				break;
			}
			case WEST:
			{
				topTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()));
				bottomTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()));
				rightTile = world.getTileEntity(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()));
				leftTile = world.getTileEntity(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()));
				break;
			}
			case NORTH:
			{
				topTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()));
				bottomTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()));
				rightTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1));
				leftTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1));
				break;
			}
			case EAST:
			{
				topTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()));
				bottomTile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()));
				rightTile = world.getTileEntity(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()));
				leftTile = world.getTileEntity(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()));
				break;
			}
			default: break;
		}
		
		if (topTile != null && topTile instanceof TileEntityPainting)
		{
			TileEntityPainting tile = (TileEntityPainting)topTile;
			tile.updateMe();
		}
		if (bottomTile != null && bottomTile instanceof TileEntityPainting)
		{
			TileEntityPainting tile = (TileEntityPainting)bottomTile;
			tile.updateMe();
		}
		if (rightTile != null && rightTile instanceof TileEntityPainting)
		{
			TileEntityPainting tile = (TileEntityPainting)rightTile;
			tile.updateMe();
		}
		if (leftTile != null && leftTile instanceof TileEntityPainting)
		{
			TileEntityPainting tile = (TileEntityPainting)leftTile;
			tile.updateMe();
		}
	}
	
	public void updateMe()
	{
		getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
	}
	
	private void sendUpdateNotify()
	{
		if (this.getAngle() == EnumFacing.SOUTH || this.getAngle() == EnumFacing.NORTH)
		{
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()-1), BlockPaintingFrameBorderless.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()+1), BlockPaintingFrameBorderless.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()-1), BlockPaintingFrameBorderless.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()+1), BlockPaintingFrameBorderless.instance, true);
			//world.notifyBlockOfStateChange(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()+1), BlockPaintingFrameBorderless.instance);
		}
		else
		{
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX()+1, pos.getY()+1, pos.getZ()), BlockPaintingFrameBorderless.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX()-1, pos.getY()+1, pos.getZ()), BlockPaintingFrameBorderless.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX()+1, pos.getY()-1, pos.getZ()), BlockPaintingFrameBorderless.instance, true);
			world.notifyNeighborsOfStateChange(new BlockPos(pos.getX()-1, pos.getY()-1, pos.getZ()), BlockPaintingFrameBorderless.instance, true);
		}
	}
	public boolean getConnectTop()
	{
		return this.connectedTop;
	}
	
	public boolean getConnectLeft()
	{
		return this.connectedLeft;
	}
	
	public boolean getConnectBottom()
	{
		return this.connectedBottom;
	}
	
	public boolean getConnectRight()
	{
		return this.connectedRight;
	}
	
	public boolean getShowTLCorner()
	{
		return this.showTLCorner;
	}
	public boolean getShowTRCorner()
	{
		return this.showTRCorner;
	}
	public boolean getShowBRCorner()
	{
		return this.showBRCorner;
	}
	public boolean getShowBLCorner()
	{
		return this.showBLCorner;
	}
	
	public int getPaintingRotation()
	{
		return this.paintingRotation;
	}

	public int getPaintingCorner()
	{
		return this.paintingMasterCorner;
	}
	
	public int getPaintingScale()
	{
		return this.paintingScale;
	}
	
	public int getPaintingRes()
	{
		return this.paintingPixelRes;
	}
	
	public int getPaintingAspectRatio()
	{
		return this.paintingAspectRatio;
	}
	
	public boolean hasPainting()
	{
		if (getStackInSlot(0) != ItemStack.EMPTY)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public int getPaintingType()
	{
		return this.paintingType;
	}
	
	public String getPaintingTitle()
	{
		return this.paintingTitle;
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.size();
	}
	
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inventory.get(slot);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        AxisAlignedBB bb = INFINITE_EXTENT_AABB;
        bb =  new AxisAlignedBB(pos.getX()-3-this.paintingScale*(this.paintingAspectRatio+1), 
        							  pos.getY()-3-this.paintingScale*(this.paintingAspectRatio+1), 
        							  pos.getZ()-3-this.paintingScale*(this.paintingAspectRatio+1), 
        							  pos.getX()+4+this.paintingScale*(this.paintingAspectRatio+1), 
        							  pos.getY()+4+this.paintingScale*(this.paintingAspectRatio+1), 
        							  pos.getZ()+4+this.paintingScale*(this.paintingAspectRatio+1));
        return bb;
    }
    
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		Item stackitem = itemstack.getItem();
		if (stackitem != ItemStack.EMPTY.getItem())
		{
			if (stackitem == ItemPaintingCanvas.instance)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() 
	{
		String output = BlockPaintingFrameBorderless.name;
		switch (this.getFrameStyle())
		{
			case BORDERLESS: { output = BlockPaintingFrameBorderless.name; break; }
			case FANCY: { output = BlockPaintingFrameFancy.name; break; }
			case FLAT: { output = BlockPaintingFrameFlat.name; break; }
			case MIDDLE: { output = BlockPaintingFrameMiddle.name; break; }
			case SIMPLE: { output = BlockPaintingFrameSimple.name; break; }
		}
		return output;
	}

	@Override
	public void setInventorySlotContentsAdditionalCommands(int slot, ItemStack stack) 
	{
		if (stack != ItemStack.EMPTY)
		{
			if (stack.getItem() == ItemPaintingCanvas.instance)
			{
				NBTTagCompound tags = stack.getTagCompound();
				if (tags != null)
				{
					this.paintingTitle = tags.getString("paintingTitle");
					this.paintingType = tags.getInteger("paintingType");
				}
			}
		}
	}

	@Override
	public void loadCustomNBTData(NBTTagCompound nbt) 
	{
		this.paintingType = nbt.getInteger("paintingType");
		this.paintingTitle = nbt.getString("paintingTitle");
		this.connectedTop = nbt.getBoolean("connectedTop");
		this.connectedLeft = nbt.getBoolean("connectedLeft");
		this.connectedBottom = nbt.getBoolean("connectedBottom");
		this.connectedRight = nbt.getBoolean("connectedRight");
		this.paintingRotation = nbt.getInteger("paintingRotation");
		this.showTLCorner = nbt.getBoolean("showTLCorner");
		this.showTRCorner = nbt.getBoolean("showTRCorner");
		this.showBRCorner = nbt.getBoolean("showBRCorner");
		this.showBLCorner = nbt.getBoolean("showBLCorner");
		this.paintingMasterCorner = nbt.getInteger("masterCorner");
		this.paintingScale = nbt.getInteger("paintingScale");
		this.paintingPixelRes = nbt.getInteger("paintingPixelRes");
		this.paintingAspectRatio = nbt.getInteger("paintingAspectRatio");
		this.customPaintingAspectX = nbt.getInteger("customPaintingAspectX");
		this.customPaintingAspectY = nbt.getInteger("customPaintingAspectY");
		this.hideFrame = nbt.getBoolean("hideFrame");
		this.style = EnumPaintingFrame.getEnumFromID(nbt.getInteger("frameStyle"));
		this.containerUpdate = nbt.getBoolean("containerUpdate");
	}

	@Override
	public NBTTagCompound writeCustomNBTData(NBTTagCompound nbt) 
	{
    	nbt.setInteger("paintingType", this.paintingType);
    	nbt.setString("paintingTitle", this.paintingTitle);
    	nbt.setBoolean("connectedTop", this.connectedTop);
    	nbt.setBoolean("connectedLeft", this.connectedLeft);
    	nbt.setBoolean("connectedBottom", this.connectedBottom);
    	nbt.setBoolean("connectedRight", this.connectedRight);
    	nbt.setInteger("paintingRotation", this.paintingRotation);
    	nbt.setBoolean("showTLCorner", this.showTLCorner);
    	nbt.setBoolean("showTRCorner", this.showTRCorner);
    	nbt.setBoolean("showBRCorner", this.showBRCorner);
    	nbt.setBoolean("showBLCorner", this.showBLCorner);
    	nbt.setInteger("masterCorner", this.paintingMasterCorner);
    	nbt.setInteger("paintingScale", this.paintingScale);
    	nbt.setInteger("paintingPixelRes", this.paintingPixelRes);
    	nbt.setInteger("paintingAspectRatio", this.paintingAspectRatio);
    	nbt.setInteger("customPaintingAspectX", this.customPaintingAspectX);
    	nbt.setInteger("customPaintingAspectY", this.customPaintingAspectY);
    	nbt.setBoolean("hideFrame", this.hideFrame);
    	nbt.setInteger("frameStyle", this.style.getID());
    	nbt.setBoolean("containerUpdate", this.containerUpdate);
		return nbt;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		ITextComponent chat = new TextComponentString(getName());
		return chat;
	}
}
