package jds.bibliocraft.items;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.BlockLoader;
import jds.bibliocraft.gui.GuiAtlasMap;
import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.network.BiblioNetworking;
import jds.bibliocraft.network.packet.client.BiblioAtlasTGUI;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAtlas extends ItemMap
{
	
	public static final String name = "AtlasBook";
	public static final ItemAtlas instance = new ItemAtlas();
	
	public ItemAtlas()
	{
		super();
		setUnlocalizedName(name);
		setMaxStackSize(1);
		setCreativeTab(BlockLoader.biblioTab);
		setRegistryName(name);
	}
	
	private int createUniqueID()
	{
		Random rando = new Random();
		
		return Math.abs((int)(rando.nextLong() * MinecraftServer.getCurrentTimeMillis())); 
	}
	
	@Override
	public int getItemEnchantability()
    {
        return 15;
    }
	
	@Override
    public boolean isMap()
    {
        return true;
    }
	
	@Override
    public int getEntityLifespan(ItemStack itemStack, World world)
    {
        return 36000;
    }
	

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (player.isSneaking() && hand != EnumHand.OFF_HAND)//player.getHeldItem(EnumHand.OFF_HAND) == ItemStack.EMPTY)
		{

			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile instanceof TileEntityMapFrame)
			{

				if (!world.isRemote)
				{
					ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
					TileEntityMapFrame mapFrame = (TileEntityMapFrame)tile;
					if (mapFrame.getHasMap())
					{
						int matchingMapSlot = findMatchingMapSlot(stack, mapFrame);
						if (matchingMapSlot == -1)
						{
							addnewMapToAtlasFromFrame(stack, mapFrame, player);
						}
						else
						{
							boolean alreadySetToMap = false;
							NBTTagCompound tags = stack.getTagCompound();
							if (tags != null && tags.hasKey("mapSlot"))
							{
								if (tags.getInteger("mapSlot") == matchingMapSlot)
								{
									alreadySetToMap = true;
								}
							}
							if (!alreadySetToMap)
							{
								saveMapSlotToNBT(stack, player, matchingMapSlot);
								
							}
							else
							{
								BiblioNetworking.INSTANCE.sendTo(new BiblioAtlasTGUI(stack, pos), (EntityPlayerMP) player);
								// ByteBuf buffer = Unpooled.buffer();
						    	// ByteBufUtils.writeItemStack(buffer, stack);
						    	// buffer.writeInt(pos.getX());
						    	// buffer.writeInt(pos.getY());
						    	// buffer.writeInt(pos.getZ());
						    	// BiblioCraft.ch_BiblioAtlas.sendTo(new FMLProxyPacket(new PacketBuffer(buffer), "BiblioAtlasTGUI"), (EntityPlayerMP) player);
							}
						}
					}
					else
					{
						if (checkForAndRemoveStackedEmptyMap(stack, player))
						{
							ItemStack atlasMapStack = getCurrentMapStack(stack);//.copy();
							NBTTagCompound mapData = getAtlasMapNBTdata(stack);
							if (mapData != null && atlasMapStack != ItemStack.EMPTY)
							{
								mapFrame.addMap(atlasMapStack.copy());
								mapFrame.addMapPinDataFromAtlas(mapData);
							}
						}
					}
				}
				
				return EnumActionResult.SUCCESS;
			}
		}
		
		return EnumActionResult.PASS;
	}
	
	private boolean isAutoCenterActive(ItemStack stack)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null)
		{
			if (tags.hasKey("autoCenter"))
			{
				return tags.getBoolean("autoCenter");
			}
		}
		return false;
	}
	
	private boolean addnewMapToAtlasFromFrame(ItemStack stack, TileEntityMapFrame tile, EntityPlayer player)
	{
		InventoryBasic inv = getInventory(stack);
		int newMapSlot = findMapSlotForNewMap(stack);
		ItemStack newMap = tile.getStackInSlot(0);
		NBTTagCompound tags = stack.getTagCompound();
		if (inv != null && newMapSlot != -1 && newMap != ItemStack.EMPTY && tags != null)
		{
			String newMapName = "Map_"+newMap.getItemDamage();
			if (inv.getStackInSlot(newMapSlot) == ItemStack.EMPTY)
			{
				int emptyMapsSlot = getEmptyStackedMapSlot(stack);
				if (emptyMapsSlot != -1)
				{
					ItemStack currentSlot = inv.getStackInSlot(emptyMapsSlot);
					boolean vanillaTest = currentSlot != ItemStack.EMPTY && currentSlot.getItem() == Items.MAP;
					boolean tfcTest = currentSlot != ItemStack.EMPTY && currentSlot.getItem() == Items.PAPER && Loader.isModLoaded("terrafirmacraft") && Loader.isModLoaded("BiblioWoodsTFC");
					if (vanillaTest || tfcTest)
					{
						currentSlot.setCount(currentSlot.getCount() - 1);
						if (currentSlot.getCount() == 0)
						{
							currentSlot = ItemStack.EMPTY;
						}
						inv.setInventorySlotContents(emptyMapsSlot, currentSlot);
					}
				}
				else
				{
					return false;
				}
			}
			boolean foundMapData = false;
			NBTTagList atlasMapsDatas = new NBTTagList();
			int hasCurrentMapDatas = -1;
			if (tags.hasKey("maps"))
			{
				atlasMapsDatas = tags.getTagList("maps", Constants.NBT.TAG_COMPOUND);
				
				for (int i = 0; i < atlasMapsDatas.tagCount(); i++)
				{
					NBTTagCompound testTag = atlasMapsDatas.getCompoundTagAt(i);
					if (testTag != null && testTag.hasKey("mapName"))
					{
						String oldMap = testTag.getString("mapName");
						if (oldMap.contentEquals(newMapName))
						{
							atlasMapsDatas.removeTag(i);
							foundMapData = true;
						}
					}
				}
			}
			newMap = newMap.copy();
			inv.setInventorySlotContents(newMapSlot, newMap); 
			NBTTagList itemList = new NBTTagList();
	    	for (int n = 0; n < inv.getSizeInventory(); n++)
	    	{
	    		ItemStack invSlot = inv.getStackInSlot(n);
	    		if (invSlot != ItemStack.EMPTY)
	    		{
	    			NBTTagCompound tag = new NBTTagCompound();
	    			tag.setByte("Slot", (byte) n);
	    			invSlot.writeToNBT(tag);
	    			itemList.appendTag(tag);
	    		}
	    	}
	    	tags.setTag("Inventory", itemList);
    		atlasMapsDatas.appendTag(getNewMapDataCompound(tile, newMapName));
    		tags.setTag("maps", atlasMapsDatas);
	    	if (!isAutoCenterActive(stack))
	    	{
	    		tags.setInteger("mapSlot", newMapSlot);
	    	}
	    	
	    	stack.setTagCompound(tags);
	    	EnumHand hand = getHandWithAtlas(stack, player);
	    	if (hand != null)
	    	{
	    		if (hand == EnumHand.MAIN_HAND)
	    		{
	    			player.inventory.setInventorySlotContents(player.inventory.currentItem, stack.copy());
	    		}
	    		else
	    		{
	    			player.inventory.offHandInventory.set(0, stack.copy());
	    			//player.inventory.getSlotFor(stack);
	    		}
	    	}
	    	return true;
		}
		return false;
	}
	
	private NBTTagCompound getNewMapDataCompound(TileEntityMapFrame tile, String newMapName)
	{
		NBTTagCompound newMapData = new NBTTagCompound();
		newMapData.setString("mapName", newMapName);
    	newMapData.setInteger("xCenter", tile.mapXCenter);
    	newMapData.setInteger("zCenter", tile.mapZCenter);
    	newMapData.setInteger("mapScale", tile.mapScale);
    	
    	NBTTagList mapXPins = new NBTTagList();
    	for (int i = 0; i < tile.xPin.size(); i++)
    	{
    		float xpin = (Float)tile.xPin.get(i);
			if (tile.getVertPosition() == EnumVertPosition.WALL && (tile.getAngle() == EnumFacing.WEST || tile.getAngle() == EnumFacing.NORTH))
			{
				xpin = 1.0f - xpin;
			}
			
    		mapXPins.appendTag(new NBTTagFloat(xpin));
    	}
    	newMapData.setTag("xMapWaypoints", mapXPins);
    	
    	NBTTagList mapYPins = new NBTTagList();
    	for (int i = 0; i < tile.yPin.size(); i++)
    	{
    		float ypin = (Float)tile.yPin.get(i);
			if (tile.getVertPosition() == EnumVertPosition.CEILING || tile.getVertPosition() == EnumVertPosition.WALL)
			{
				// ceiling
				ypin = 1.0f - ypin;
			}
    		mapYPins.appendTag(new NBTTagFloat(ypin));
    	}
    	newMapData.setTag("yMapWaypoints", mapYPins);
    	
    	NBTTagList mapPinNames = new NBTTagList();
    	for (int i = 0; i < tile.pinStrings.size(); i++)
    	{
    		mapPinNames.appendTag(new NBTTagString((String)tile.pinStrings.get(i)));
    	}
    	newMapData.setTag("MapWaypointNames", mapPinNames);
    	
    	NBTTagList mapPinColors = new NBTTagList();
    	for (int i = 0; i < tile.pinColors.size(); i++)
    	{
    		mapPinColors.appendTag(new NBTTagFloat((Float)tile.pinColors.get(i)));
    	}
    	newMapData.setTag("MapWaypointColors", mapPinColors);
		return newMapData;
	}
	
	
	private int findMapSlotForNewMap(ItemStack stack)
	{
		InventoryBasic inv = getInventory(stack);
		if (inv != null)
		{
			for (int i = 6; i < inv.getSizeInventory(); i++)
			{
				ItemStack mapStack = inv.getStackInSlot(i);
				boolean vanillaTest = mapStack != ItemStack.EMPTY && mapStack.getItem() == Items.MAP;
				boolean tfcTest = mapStack != ItemStack.EMPTY && mapStack.getItem() == Items.PAPER && Loader.isModLoaded("terrafirmacraft") && Loader.isModLoaded("BiblioWoodsTFC");
				if (vanillaTest || tfcTest)
				{
					return i;
				}
			}
			
			for (int i = 6; i < inv.getSizeInventory(); i++)
			{
				ItemStack emptyStack = inv.getStackInSlot(i);
				if (emptyStack == ItemStack.EMPTY)
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	private int findMatchingMapSlot(ItemStack stack, TileEntityMapFrame tile)
	{
		ItemStack frameMap = tile.getStackInSlot(0);
		InventoryBasic inv = getInventory(stack);
		NBTTagCompound atlasTags = stack.getTagCompound();
		if (frameMap != ItemStack.EMPTY && frameMap.getItem() ==Items.FILLED_MAP && inv != null && atlasTags != null)
		{
			NBTTagList maps = atlasTags.getTagList("maps", Constants.NBT.TAG_COMPOUND);
			NBTTagCompound mapTag = null;
			String mapName = "Map_"+frameMap.getItemDamage();
			for (int n = 6; n < inv.getSizeInventory(); n++)
			{
				ItemStack atlasStack = inv.getStackInSlot(n);
				if (atlasStack != ItemStack.EMPTY && atlasStack.getItem() ==Items.FILLED_MAP)
				{
					if (atlasStack.getItemDamage() == frameMap.getItemDamage())
					{
						return n;
					}
				}
			}
		}
		return -1;
	}
	
	private NBTTagCompound getAtlasMapNBTdata(ItemStack stack)
	{
		InventoryBasic inv = getInventory(stack);
		NBTTagCompound atlasTags = stack.getTagCompound();
		ItemStack mapStack = getCurrentMapStack(stack);
		if (atlasTags != null && inv != null && mapStack != ItemStack.EMPTY && atlasTags.hasKey("maps"))
		{
			NBTTagList maps = atlasTags.getTagList("maps", Constants.NBT.TAG_COMPOUND);
			NBTTagCompound mapTag = null;
			String mapName = "Map_"+mapStack.getItemDamage();
	    	for (int n = 0; n < maps.tagCount(); n++)
	    	{
	    		mapTag = maps.getCompoundTagAt(n);
	    		if (mapTag != null && mapTag.hasKey("mapName"))
	    		{
	    			if (mapTag.getString("mapName").contentEquals(mapName))
	    			{
	    				return mapTag;
	    			}
	    		}
	    	}
		}
		return null;
	}
	
	private ItemStack getCurrentMapStack(ItemStack stack)
	{
		InventoryBasic inv = getInventory(stack);
		NBTTagCompound atlasTags = stack.getTagCompound();
		if (atlasTags != null)
		{
			int mapSlot = atlasTags.getInteger("mapSlot");
			if (mapSlot != -1)
			{
				ItemStack mapStack = inv.getStackInSlot(mapSlot);
				if (mapStack != ItemStack.EMPTY && mapStack.getItem() ==Items.FILLED_MAP)
				{
					return mapStack;
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	private boolean checkForAndRemoveStackedEmptyMap(ItemStack stack, EntityPlayer player)
	{
		InventoryBasic inv = getInventory(stack);
		int emptyMapsSlot = getEmptyStackedMapSlot(stack);
		NBTTagCompound tags = stack.getTagCompound();
		if (inv != null && emptyMapsSlot != -1 && tags != null)
		{
			ItemStack currentSlot = inv.getStackInSlot(emptyMapsSlot);
			boolean vanillaTest = currentSlot != ItemStack.EMPTY && currentSlot.getItem() == Items.MAP;
			boolean tfcTest = currentSlot != ItemStack.EMPTY && currentSlot.getItem() == Items.PAPER && Loader.isModLoaded("terrafirmacraft") && Loader.isModLoaded("BiblioWoodsTFC");
			if (vanillaTest || tfcTest)
			{
				currentSlot.setCount(currentSlot.getCount() - 1);
				if (currentSlot.getCount() == 0)
				{
					currentSlot = ItemStack.EMPTY;
				}
				inv.setInventorySlotContents(emptyMapsSlot, currentSlot);
				
				NBTTagList itemList = new NBTTagList();
		    	for (int n = 0; n < inv.getSizeInventory(); n++)
		    	{
		    		ItemStack invSlot = inv.getStackInSlot(n);
		    		if (invSlot != ItemStack.EMPTY)
		    		{
		    			NBTTagCompound tag = new NBTTagCompound();
		    			tag.setByte("Slot", (byte) n);
		    			invSlot.writeToNBT(tag);
		    			itemList.appendTag(tag);
		    		}
		    	}
		    	tags.setTag("Inventory", itemList);
		    	stack.setTagCompound(tags);
		    	EnumHand hand = getHandWithAtlas(stack, player);
		    	if (hand != null) 
		    	{
		    		if (hand == EnumHand.MAIN_HAND)
		    		{
		    			player.inventory.setInventorySlotContents(player.inventory.currentItem, stack.copy());
		    		}
		    		else
		    		{
		    			player.inventory.offHandInventory.set(0, stack.copy());
		    		}
		    	}
				return true;
			}
		}
		return false;
	}
	
	private int getEmptyStackedMapSlot(ItemStack stack)
	{
		InventoryBasic inv = getInventory(stack);
		if (inv != null)
		{
			for (int i = 0; i < 6; i++)
			{
				ItemStack currentSlot = inv.getStackInSlot(i);
				boolean vanillaTest = currentSlot != ItemStack.EMPTY && currentSlot.getItem() == Items.MAP;
				boolean tfcTest = currentSlot != ItemStack.EMPTY && currentSlot.getItem() == Items.PAPER && Loader.isModLoaded("terrafirmacraft") && Loader.isModLoaded("BiblioWoodsTFC");
				if (vanillaTest || tfcTest)
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	private InventoryBasic getInventory(ItemStack stack)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null)
		{
			InventoryBasic atlasInventory = new InventoryBasic("AtlasInventory", true, 48);
			NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < tagList.tagCount(); i++)
			{
				NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
				byte slot = tag.getByte("Slot");
				if (slot >= 0 && slot < atlasInventory.getSizeInventory())
				{
					ItemStack invStack = new ItemStack(tag);
					atlasInventory.setInventorySlotContents(slot, invStack);
				}
			}
			return atlasInventory;
		}
		else
		{
			return null;
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		//System.out.println(world.isRemote + "   " + player.getHeldItem(EnumHand.OFF_HAND));
		if (player.getHeldItem(EnumHand.OFF_HAND) == ItemStack.EMPTY || player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.AIR)
		{
			NBTTagCompound tags = player.getHeldItem(hand).getTagCompound();
			int gui = 0;
			if (tags != null)
			{
				if (tags.hasKey("lastGUImode"))
				{
					gui = tags.getInteger("lastGUImode");
				}
			}
			if (gui == 0)
			{
				if (!world.isRemote)
				{
					player.openGui(BiblioCraft.instance, 100, player.world, (int)player.posX, (int)player.posY, (int)player.posZ);
				}
			}
			else
			{
				player.rotationPitch = 50.0f;
				if (world.isRemote)
				{
					openMapGUI(world, player, player.getHeldItem(hand));
				}
			}
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	@SideOnly(Side.CLIENT)
    public void openMapGUI(World world, EntityPlayer player, ItemStack stack)
    {
		Minecraft.getMinecraft().displayGuiScreen(new GuiAtlasMap(world, player, stack));
    }
	
	public boolean getAtlasInventory(ItemStack stack, World world, EntityPlayer player, boolean autoCreate, boolean autoCenter, int zoomLevel, int selectedSlot)//, byte mapScale)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null)
		{
			InventoryBasic atlasInventory = new InventoryBasic("AtlasInventory", true, 48);
			NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < tagList.tagCount(); i++)
			{
				NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
				byte slot = tag.getByte("Slot");
				if (slot >= 0 && slot < atlasInventory.getSizeInventory())
				{
					ItemStack invStack = new ItemStack(tag);
					atlasInventory.setInventorySlotContents(slot, invStack);
				}
			}
			
			ItemStack currentSlot = ItemStack.EMPTY;
			if (autoCenter)
			{
				for (int i = 6; i < atlasInventory.getSizeInventory(); i++)
				{
					currentSlot = atlasInventory.getStackInSlot(i);
					if (currentSlot != ItemStack.EMPTY)
					{
						if (currentSlot.getItem() instanceof ItemMap)
						{
							MapData mapdata =Items.FILLED_MAP.getMapData(currentSlot, world);//ItemMap.getMapData(invStack, world);
							if (mapdata != null)
							{
								if (isPlayerWithinMapData(mapdata, player, zoomLevel))
								{
									stack.setItemDamage(currentSlot.getItemDamage());
									saveMapSlotToNBT(stack, player, i); 
									return true;
								}
							}
						}
					}
				}
			}
			else
			{
				currentSlot = atlasInventory.getStackInSlot(selectedSlot);
				if (currentSlot != ItemStack.EMPTY)
				{
					stack.setItemDamage(currentSlot.getItemDamage());
					saveMapSlotToNBT(stack, player, selectedSlot); 
				}
				return true;
			}
			
			
			if (autoCreate)
			{
				boolean foundBlankMap = false;
				int newMapSlot = -1;
				
				for (int i = 6; i < atlasInventory.getSizeInventory(); i++)
				{
					currentSlot = atlasInventory.getStackInSlot(i);
					boolean vanillaTest = currentSlot != ItemStack.EMPTY && currentSlot.getItem() == Items.MAP;
					boolean tfcTest = currentSlot != ItemStack.EMPTY && currentSlot.getItem() == Items.PAPER && Loader.isModLoaded("terrafirmacraft") && Loader.isModLoaded("BiblioWoodsTFC");
					if (vanillaTest || tfcTest)
					{
						if (currentSlot.getCount() == 1)
						{
							foundBlankMap = true;
							newMapSlot = i;
						}
					}
				}
				if (!foundBlankMap)
				{
					for (int i = 0; i<6; i++)
					{
						currentSlot = atlasInventory.getStackInSlot(i);
						boolean vanillaTest = currentSlot != ItemStack.EMPTY && currentSlot.getItem() == Items.MAP;
						boolean tfcTest = currentSlot != ItemStack.EMPTY && currentSlot.getItem() == Items.PAPER && Loader.isModLoaded("terrafirmacraft") && Loader.isModLoaded("BiblioWoodsTFC");
						if (vanillaTest || tfcTest)
						{
							ItemStack testBlankSlot = ItemStack.EMPTY;
							for (int n = 6; n<atlasInventory.getSizeInventory(); n++)
							{
								testBlankSlot = atlasInventory.getStackInSlot(n);
								if (testBlankSlot == ItemStack.EMPTY && !foundBlankMap)
								{
									foundBlankMap = true;
									newMapSlot = n;
									//break;
								}
							}
							
							if (foundBlankMap)
							{
								currentSlot.setCount(currentSlot.getCount() - 1);
								if (currentSlot.getCount() == 0)
								{
									currentSlot = ItemStack.EMPTY;
								}
								atlasInventory.setInventorySlotContents(i, currentSlot);
								break;
							}
						}
					}
				}
				
				if (foundBlankMap && newMapSlot != -1)
				{
					ItemStack newmap = makeNewMap(world, player, zoomLevel, stack);
					atlasInventory.setInventorySlotContents(newMapSlot, newmap);
			    	NBTTagCompound ntags = stack.getTagCompound();
			    	if (ntags == null)
			    	{
			    		ntags = new NBTTagCompound();
			    	}
			    	ntags.setInteger("mapSlot", -1);
			    	NBTTagList itemList = new NBTTagList();
			    	for (int n = 0; n < atlasInventory.getSizeInventory(); n++)
			    	{
			    		ItemStack invSlot = atlasInventory.getStackInSlot(n);
			    		if (invSlot != ItemStack.EMPTY)
			    		{
			    			NBTTagCompound tag = new NBTTagCompound();
			    			tag.setByte("Slot", (byte) n);
			    			invSlot.writeToNBT(tag);
			    			itemList.appendTag(tag);
			    		}
			    	}
			    	ntags.setTag("Inventory", itemList);
			    	
			    	NBTTagList maps = getUpdatedMapTagList(atlasInventory, ntags, world);
			    	ntags.setTag("maps", maps);
			    	stack.setTagCompound(ntags);
			    	stack.setItemDamage(newmap.getItemDamage());
			    	if (checkIfPlayerIsHoldingAtlas(player))
			    	{
			    		EnumHand hand = getHandWithAtlas(stack, player);
			    		if (hand != null)
			    		{
				    		if (hand == EnumHand.MAIN_HAND)
				    		{
				    			player.inventory.setInventorySlotContents(player.inventory.currentItem, stack.copy()); 
				    		}
				    		else
				    		{
				    			player.inventory.offHandInventory.set(0, stack.copy());
				    		}
			    		}
			    	}
			    	return true;
				}
				
				
			}
			saveMapSlotToNBT(stack, player, -1);
		}
		return false;
	}
	
	private boolean checkIfPlayerIsHoldingAtlas(EntityPlayer player)
	{
		ItemStack hand = player.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack offHand = player.getHeldItem(EnumHand.OFF_HAND);
		if (hand.getItem() instanceof ItemAtlas || offHand.getItem() instanceof ItemAtlas)// ||   the problem is I put the atlas back in the main hand.
		{
			return true;
		}
		return false;
	}
	
	private EnumHand getHandWithAtlas(ItemStack stack, EntityPlayer player)
	{
		EnumHand hand = null;
		NBTTagCompound currTags = stack.getTagCompound();
		if (currTags.hasKey("atlasID"))
		{
			int id = currTags.getInteger("atlasID");
			ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);
			NBTTagCompound mainTags = mainHand.getTagCompound();
			ItemStack offHand = player.getHeldItem(EnumHand.OFF_HAND);
			NBTTagCompound offTags = offHand.getTagCompound();
			if (mainHand.getItem() instanceof ItemAtlas && mainTags.hasKey("atlasID"))
			{
				if (id == mainTags.getInteger("atlasID"))
				{
					return EnumHand.MAIN_HAND;
				}
			}
			if (offHand.getItem() instanceof ItemAtlas && offTags.hasKey("atlasID"))
			{
				if (id == offTags.getInteger("atlasID"))
				{
					return EnumHand.OFF_HAND;
				}
			}
		}
		return hand;
	}
	
	private void saveMapSlotToNBT(ItemStack stack, EntityPlayer player, int slot)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null)
		{
			tags.setInteger("mapSlot", slot);
			stack.setTagCompound(tags);
			stack.setItemDamage(setAtlasDamage(stack, slot));
	    	if (checkIfPlayerIsHoldingAtlas(player))
	    	{
	    		EnumHand hand = getHandWithAtlas(stack, player);
	    		if (hand != null) 
	    		{
		    		if (hand == EnumHand.MAIN_HAND)
		    		{
		    			player.inventory.setInventorySlotContents(player.inventory.currentItem, stack.copy()); 
		    		}
		    		else
		    		{
		    			player.inventory.offHandInventory.set(0, stack.copy());
		    		}
	    		}
	    	}
		}
	}
	
	public static int setAtlasDamage(ItemStack atlas, int newslot)
	{
		NBTTagCompound tags = atlas.getTagCompound();
		InventoryBasic atlasInventory = new InventoryBasic("AtlasInventory", false, 48);
		NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < atlasInventory.getSizeInventory())
			{
				ItemStack invStack = new ItemStack(tag);
				atlasInventory.setInventorySlotContents(slot, invStack);
			}
		}
		
		ItemStack newmap = atlasInventory.getStackInSlot(newslot);
		if (newmap != ItemStack.EMPTY && newmap.getItem() instanceof ItemMap)
		{
			return newmap.getItemDamage();
		}
		
		return 0;
	}
	
	public ItemStack makeNewMap(World world, EntityPlayer player, int scale, ItemStack oldMap)
	{
		MapData oldmapdata =Items.FILLED_MAP.getMapData(oldMap, world);
		double mx = 0;
		double mz = 0;
		int newXcenter = 0;
		int newZcenter = 0;
		ItemStack itemstack = new ItemStack(Items.FILLED_MAP, 1, world.getUniqueDataId("map"));
        String s = "map_" + itemstack.getItemDamage();
        MapData mapdata = new MapData(s);
        world.setData(s, mapdata);
        mapdata.scale = (byte)scale;
        int i = 128 * (1 << mapdata.scale);      
        
		if (oldmapdata != null)
		{
			 mx = oldmapdata.xCenter;
			 mz = oldmapdata.zCenter;
			switch (playerDirection(world, player, oldmapdata))
			{
				case 0: // EAST +X
				{
					newXcenter = (int)(mx+128*Math.pow(2, scale));
					newZcenter = (int)mz;
					break;
				}
				case 1: // WEST -X
				{
					newXcenter = (int)(mx-128*Math.pow(2, scale));
					newZcenter = (int)mz;
					break;
				}
				case 2: // NORTH -Z
				{
					newXcenter = (int)mx;
					newZcenter = (int)(mz-128*Math.pow(2, scale));
					break;
				}
				case 3: // SOUTH +Z
				{
					newXcenter = (int)mx;
					newZcenter = (int)(mz+128*Math.pow(2, scale));
					break;
				}
				default:
				{
					newXcenter = (int)(Math.round(player.posX / (double)i) * (long)i);
					newZcenter = (int)(Math.round(player.posZ / (double)i) * (long)i);
					break;
				}
			}
		}
		else
		{
			newXcenter = (int)(Math.round(player.posX / (double)i) * (long)i);
			newZcenter = (int)(Math.round(player.posZ / (double)i) * (long)i);
		}                                                                                                                   
        mapdata.xCenter = newXcenter;//(int)(Math.round(player.posX / (double)i) * (long)i);
        mapdata.zCenter = newZcenter;//(int)(Math.round(player.posZ / (double)i) * (long)i);
        mapdata.dimension = world.provider.getDimension();
        mapdata.markDirty();
        return itemstack;
	}
	
	 private NBTTagList getUpdatedMapTagList(InventoryBasic atlasInventory, NBTTagCompound tags, World world)
    {
    	NBTTagList maps = null;
    	NBTTagList newMaps = new NBTTagList();
    	if (tags.hasKey("maps"))
    	{
    		maps = tags.getTagList("maps", Constants.NBT.TAG_COMPOUND);
    	}
    	for (int i = 6; i < atlasInventory.getSizeInventory(); i++)
    	{
    		ItemStack currentMap = atlasInventory.getStackInSlot(i);
    		if (currentMap != ItemStack.EMPTY)
    		{
    			String mapName = "Map_"+currentMap.getItemDamage();
	    		boolean foundMap = false;
	    		NBTTagCompound mapTag = new NBTTagCompound();
	    		
	    		if (maps != null)
	    		{
			    	for (int n = 0; n < maps.tagCount(); n++)
			    	{
			    		mapTag = maps.getCompoundTagAt(n);
			    		if (mapTag != null && mapTag.hasKey("mapName"))
			    		{
			    			if (mapTag.getString("mapName").contentEquals(mapName))
			    			{
			    				foundMap = true;
			    				break;
			    			}
			    		}
			    	}
	    		}
		    	if (!foundMap)
		    	{
		    		MapData mapdata = getMyMapData(currentMap, world);
		    		if (mapdata != null)
		    		{
		    			foundMap = true;
			    		mapTag = new NBTTagCompound();
			    		mapTag.setString("mapName", mapName);
			    		mapTag.setInteger("xCenter", mapdata.xCenter);
			    		mapTag.setInteger("zCenter", mapdata.zCenter);
			    		mapTag.setInteger("mapScale", mapdata.scale);
		    		}
		    	}
		    	if (foundMap)
		    	{
		    		newMaps.appendTag(mapTag);
		    	}
    		}
    	}
    	
    	return newMaps;
    }
    
    private MapData getMyMapData(ItemStack stack, World world)
	{
    	if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemMap)
		{
			MapData mapdata =Items.FILLED_MAP.getMapData(stack, world);//((ItemMap)(stack.getItem())).getMapData(stack, Minecraft.getMinecraft().theWorld);//ItemMap.getMapData(invStack, world);
			if (mapdata != null)
			{
				return mapdata;
			}
		}
		return null;
	}
	
	private int playerDirection(World world, EntityPlayer player, MapData mapdata)
	{
		double px = player.posX;
		double pz = player.posZ;
		double mx = mapdata.xCenter;
		double mz = mapdata.zCenter;
		int scale = mapdata.scale;
		double factor = 64*Math.pow(2, scale);
		//System.out.println("Player x = "+px+"    Player z = "+pz+"    Mapx = "+mx+"    Map z = "+mz+"   Factor = "+factor);
		if (px > (mx+factor) && px < (mx+factor)+(2*factor))
		{
			return 0; // East   +X
		}
		if (px < (mx-factor) && px > (mx-factor)-(2*factor))
		{
			return 1;  // West  -X
		}
		if (pz < (mz+factor) && pz > (mz+factor)+(2*factor))
		{
			return 2; // North  -Z
		} 
		if (pz > (mz-factor) && pz < (mz-factor)-(2*factor))
		{
			return 3; // South  +Z
		}
		return -1;
	}
	
	public boolean isPlayerWithinMapData(MapData map, EntityPlayer player, int zoomLevel)
	{
		if (map == null || player == null)
		{
			return false;
		}
		double px = player.posX;
		double pz = player.posZ;
		double mx = map.xCenter;
		double mz = map.zCenter;
		int scale = map.scale;
		if (scale != zoomLevel)
		{
			return false;
		}
		//System.out.println("player pos = "+px+"    "+mz+"    scale = "+scale);
		double factor = 64*Math.pow(2, scale);
		//System.out.println(factor);
		if (px < (mx+factor) && px > (mx-factor) && pz < (mz+factor) && pz > (mz-factor))
		{
			return true;
		}
		return false;
	}
	
	@Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean inhand)
	{
       	NBTTagCompound tags = stack.getTagCompound();
    	if (tags == null)
    	{
    		tags = new NBTTagCompound();
    		stack.setTagCompound(tags);
    	}
        if (!world.isRemote)
        {
        	if (!tags.hasKey("atlasID"))
        	{
        		int id = this.createUniqueID();
        		tags.setInteger("atlasID", this.createUniqueID());
        		stack.setTagCompound(tags);
        	}
        	
        	if (inhand || slot == 0)
        	{
	            MapData mapdata =Items.FILLED_MAP.getMapData(stack, world);
	            if (entity instanceof EntityPlayer)
	            {
	                EntityPlayer entityplayer = (EntityPlayer)entity;
	                mapdata.trackingPosition = true;
	                mapdata.updateVisiblePlayers(entityplayer, stack);
	                if (checkIfPlayerIsHoldingAtlas(entityplayer))
	                {
	                	boolean autoCenter = tags.getBoolean("autoCenter");
	                	boolean autoCreate = tags.getBoolean("autoCreate");
	                	int zoomLevel = tags.getInteger("zoomLevel");
	                	int selectedSlot = tags.getInteger("mapSlot");
	
	                	if (getMapSlot(stack) == -1)
	                	{
	                		getAtlasInventory(stack, world, entityplayer, autoCreate, autoCenter, zoomLevel, selectedSlot);//, mapdata.scale);
	                	}
	
	                	if (autoCenter)
	                	{  	
		                    if (!isPlayerWithinMapData(mapdata, entityplayer, zoomLevel))
		                    {
		                    	getAtlasInventory(stack, world, entityplayer, autoCreate, autoCenter, zoomLevel, selectedSlot);
		                    }
	                	}	
	                    this.updateMapData(world, entity, mapdata); 
	                }
	            }
        	}
        }
        
        if (world.isRemote)
        {
        	//client calculations for compass rendering
        	if (entity != null && entity instanceof EntityPlayer && tags.hasKey("savedCompass") && tags.getInteger("savedCompass") != -1)
    		{
    			int sX = 0;
    			int sZ = 0;
    			double time = 0.0d;
    			double prevAngle = 0.0d;
    			if (tags != null)
    			{
    				 sX = tags.getInteger("compassX");
    				 sZ = tags.getInteger("compassZ");
    				 time = tags.getDouble("time");
    				 prevAngle = tags.getDouble("prevAngle");
    			}
    			EntityPlayer player = (EntityPlayer)entity;
    			double yaw = MathHelper.wrapDegrees(player.rotationYaw) + 90.0d;
    			double dx = sX - player.posX;
    			double dz = sZ - player.posZ;
    			double newAngle = yaw - (Math.atan2(dz, dx)*(180.0d/Math.PI));
    			double angleDelta = Math.abs(prevAngle - newAngle);
    			prevAngle = newAngle;
    			double delta = 0.0d;
    			boolean runDelta = false;
    			if (angleDelta > 8.0d)
    			{
    				time -= 0.25;
    				if (angleDelta > 40.0d)
    				{
    					time -= 0.2;
    				}
    			}
    			else
    			{
    				runDelta = true;
    				if (time <= 5.25)
    				{
    					time += 0.06;
    				}
    				else
    				{
    					time = 5.25d;
    				}
    			}
    			if (time > 5.25)
    			{
    				time = 5.25d;
    			}
    			if (time <= 1.309D)
    			{
    				time = 1.309D;
    			}
    			if (runDelta)
    			{
    				delta = Math.exp(-time)*(64*Math.sin(3*time)-64*Math.cos(3*time));
    			}
    			float theta = (float) (newAngle+delta);
    			tags.setFloat("needleAngle", theta);
    			tags.setDouble("prevAngle", prevAngle);
    			tags.setDouble("time", time);
    		}
        }
        
    }
	
	private int getMapSlot(ItemStack stack)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null)
		{
			return tags.getInteger("mapSlot");
		}
		
		return -1;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        NBTTagList nbttaglist = ItemEnchantedBook.getEnchantments(stack);
        //System.out.println("test " + stack.getTagCompound());
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getShort("id");
            Enchantment enchantment = Enchantment.getEnchantmentByID(j);
           // System.out.println("id = " + j + "     " + enchantment);
            if (enchantment != null)
            {
                tooltip.add(enchantment.getTranslatedName(nbttagcompound.getShort("lvl")));
            }
        }
    }

}
