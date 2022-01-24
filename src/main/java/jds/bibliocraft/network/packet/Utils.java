package jds.bibliocraft.network.packet;

import java.util.ArrayList;

import jds.bibliocraft.helpers.EnumVertPosition;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

public class Utils {
    public static ItemStack getCurrentMapStack(ItemStack stack) {
        InventoryBasic inv = getInventory(stack);
        NBTTagCompound atlasTags = stack.getTagCompound();
        if (atlasTags != null) {
            int mapSlot = atlasTags.getInteger("mapSlot");
            if (mapSlot != -1) {
                ItemStack mapStack = inv.getStackInSlot(mapSlot);
                if (mapStack != ItemStack.EMPTY && mapStack.getItem() == Items.FILLED_MAP) {
                    return mapStack;
                }
            }
        }
        return null;
    }

    public static InventoryBasic getInventory(ItemStack stack) {
        NBTTagCompound tags = stack.getTagCompound();
        if (tags != null) {
            InventoryBasic atlasInventory = new InventoryBasic("AtlasInventory", true, 48);
            NBTTagList tagList = tags.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
                byte slot = tag.getByte("Slot");
                if (slot >= 0 && slot < atlasInventory.getSizeInventory()) {
                    ItemStack invStack = new ItemStack(tag);
                    atlasInventory.setInventorySlotContents(slot, invStack);
                }
            }
            return atlasInventory;
        } else {
            return null;
        }
    }
    public static NBTTagCompound getNewMapDataCompound(TileEntityMapFrame tile, String newMapName) {
		int mapRotation = tile.getRotation();
		NBTTagCompound newMapData = new NBTTagCompound();
		newMapData.setString("mapName", newMapName);
		newMapData.setInteger("xCenter", tile.mapXCenter);
		newMapData.setInteger("zCenter", tile.mapZCenter);
		newMapData.setInteger("mapScale", tile.mapScale);
		EnumFacing angle = tile.getAngle();
		EnumVertPosition vertAngle = tile.getVertPosition();
		int rotations = 0;
		switch (mapRotation) {
			case 1: {
				rotations = 3;
				break;
			}
			case 2: {
				rotations = 2;
				break;
			}
			case 3: {
				rotations = 1;
				break;
			}
		}
		ArrayList<Float> xPins = new ArrayList<>();
		xPins = (ArrayList<Float>) tile.xPin.clone();
		ArrayList<Float> yPins = new ArrayList<>();
		yPins = (ArrayList) tile.yPin.clone();

		for (int i = 0; i < rotations; i++) {
			ArrayList<Float> xCurrent = xPins;
			ArrayList<Float> yCurrent = yPins;
			if (((angle == EnumFacing.SOUTH || angle == EnumFacing.EAST) && vertAngle == EnumVertPosition.WALL)
					|| vertAngle == EnumVertPosition.CEILING) {
				xPins = yCurrent;
				yPins = xCurrent;
				yCurrent = yPins;
				for (int n = 0; n < xCurrent.size(); n++) {
					yPins.set(n, 1 - (Float) yCurrent.get(n));
				}
			} else {
				xPins = yCurrent;
				yPins = xCurrent;
				xCurrent = xPins;
				for (int n = 0; n < xCurrent.size(); n++) {
					xPins.set(n, 1 - (Float) xCurrent.get(n));
				}
			}
		}

		NBTTagList mapXPins = new NBTTagList();
		for (int i = 0; i < xPins.size(); i++) {
			float xpin = (Float) xPins.get(i);
			if (tile.getVertPosition() == EnumVertPosition.WALL
					&& (tile.getAngle() == EnumFacing.WEST || tile.getAngle() == EnumFacing.NORTH)) {
				xpin = 1.0f - xpin;
			}

			mapXPins.appendTag(new NBTTagFloat(xpin));
		}
		newMapData.setTag("xMapWaypoints", mapXPins);

		NBTTagList mapYPins = new NBTTagList();
		for (int i = 0; i < yPins.size(); i++) {
			float ypin = (Float) yPins.get(i);
			if (tile.getVertPosition() == EnumVertPosition.CEILING || tile.getVertPosition() == EnumVertPosition.WALL) {
				// ceiling
				ypin = 1.0f - ypin;
			}
			mapYPins.appendTag(new NBTTagFloat(ypin));
		}
		newMapData.setTag("yMapWaypoints", mapYPins);

		NBTTagList mapPinNames = new NBTTagList();
		for (int i = 0; i < tile.pinStrings.size(); i++) {
			mapPinNames.appendTag(new NBTTagString((String) tile.pinStrings.get(i)));
		}
		newMapData.setTag("MapWaypointNames", mapPinNames);

		NBTTagList mapPinColors = new NBTTagList();
		for (int i = 0; i < tile.pinColors.size(); i++) {
			mapPinColors.appendTag(new NBTTagFloat((Float) tile.pinColors.get(i)));
		}
		newMapData.setTag("MapWaypointColors", mapPinColors);
		return newMapData;
	}
}
