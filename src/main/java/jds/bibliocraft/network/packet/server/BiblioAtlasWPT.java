package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.network.packet.Utils;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// Doesn't crash but I have no idea how to work the atlas so I am not sure if it's fully working!
public class BiblioAtlasWPT implements IMessage {
    boolean toMapFrame;
    BlockPos pos;
    ItemStack atlasStack;

    // dummy constructor for FML
    public BiblioAtlasWPT() {

    }

    public BiblioAtlasWPT(boolean toMapFrame, BlockPos pos, ItemStack atlasStack) {
        this.toMapFrame = toMapFrame;
        this.pos = pos;
        this.atlasStack = atlasStack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.toMapFrame = buf.readBoolean();
        this.pos = BlockPos.fromLong(buf.readLong());
        this.atlasStack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.toMapFrame);
        buf.writeLong(this.pos.toLong());
        ByteBufUtils.writeItemStack(buf, this.atlasStack);
    }

    public static class Handler implements IMessageHandler<BiblioAtlasWPT, IMessage> {

        @Override
        public IMessage onMessage(BiblioAtlasWPT message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (Utils.hasPointLoaded(player, message.pos)) {
                    TileEntity tile = player.world.getTileEntity(message.pos);
                    if (tile != null && tile instanceof TileEntityMapFrame && message.atlasStack != ItemStack.EMPTY
                            && message.atlasStack.getItem() instanceof ItemAtlas) {
                        TileEntityMapFrame frameTile = (TileEntityMapFrame) tile;
                        if (message.toMapFrame) {
                            transferWaypointsToMapFrame(frameTile, message.atlasStack);
                            // player.worldObj.markBlockForUpdate(frameTile.getPos());
                            frameTile.getWorld().notifyBlockUpdate(frameTile.getPos(),
                                    frameTile.getWorld().getBlockState(frameTile.getPos()),
                                    frameTile.getWorld().getBlockState(frameTile.getPos()), 3);
                        } else {
                            transferWaypointsToAtlas(frameTile, message.atlasStack, player);
                        }
                    }
                }
            });
            return null;
        }

        private void transferWaypointsToMapFrame(TileEntityMapFrame frameTile, ItemStack atlasStack) {
            InventoryBasic inv = Utils.getInventory(atlasStack);
            NBTTagCompound atlasTags = atlasStack.getTagCompound();
            ItemStack mapStack = Utils.getCurrentMapStack(atlasStack);
            if (atlasTags != null && inv != null && mapStack != ItemStack.EMPTY && atlasTags.hasKey("maps")) {
                NBTTagList maps = atlasTags.getTagList("maps", Constants.NBT.TAG_COMPOUND);
                NBTTagCompound mapTag = null;
                String mapName = "Map_" + mapStack.getItemDamage();
                for (int n = 0; n < maps.tagCount(); n++) {
                    mapTag = maps.getCompoundTagAt(n);
                    if (mapTag != null && mapTag.hasKey("mapName")) {
                        if (mapTag.getString("mapName").contentEquals(mapName)) {
                            frameTile.addMapPinDataFromAtlas(mapTag);

                            for (int i = 0; i < frameTile.getRotation(); i++) {
                                frameTile.rotateWaypoints();
                            }

                        }
                    }
                }
            }

        }

        private void transferWaypointsToAtlas(TileEntityMapFrame frameTile, ItemStack atlasStack,
                EntityPlayerMP player) {
            InventoryBasic inv = Utils.getInventory(atlasStack);
            ItemStack newMap = frameTile.getStackInSlot(0);
            NBTTagCompound tags = atlasStack.getTagCompound();
            if (inv != null && tags != null) {
                String newMapName = "Map_" + newMap.getItemDamage();
                NBTTagList atlasMapsDatas = new NBTTagList();
                if (tags.hasKey("maps")) {
                    atlasMapsDatas = tags.getTagList("maps", Constants.NBT.TAG_COMPOUND);

                    for (int i = 0; i < atlasMapsDatas.tagCount(); i++) {
                        NBTTagCompound testTag = atlasMapsDatas.getCompoundTagAt(i);
                        if (testTag != null && testTag.hasKey("mapName")) {
                            String oldMap = testTag.getString("mapName");
                            if (oldMap.contentEquals(newMapName)) {
                                atlasMapsDatas.removeTag(i);
                            }
                        }
                    }
                }
                atlasMapsDatas.appendTag(Utils.getNewMapDataCompound(frameTile, newMapName));
                tags.setTag("maps", atlasMapsDatas);
                atlasStack.setTagCompound(tags);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, atlasStack);
            }
        }

    }
}
