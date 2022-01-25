package jds.bibliocraft.network.packet.server;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.Config;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.network.packet.Utils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BiblioRecipeCraft implements IMessage {
    ItemStack recipeBook;
    int inventorySlot;

    public BiblioRecipeCraft() {

    }

    public BiblioRecipeCraft(ItemStack recipeBook, int inventorySlot) {
        this.recipeBook = recipeBook;
        this.inventorySlot = inventorySlot;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.recipeBook = ByteBufUtils.readItemStack(buf);
        this.inventorySlot = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.recipeBook);
        buf.writeInt(this.inventorySlot);
    }

    public static class Handler implements IMessageHandler<BiblioRecipeCraft, IMessage> {

        @Override
        public IMessage onMessage(BiblioRecipeCraft message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                ItemStack recipeBook = message.recipeBook;
                int inventorySlot = message.inventorySlot;
                if (Config.enableRecipeBookCrafting) {
                    if (recipeBook != ItemStack.EMPTY && recipeBook.getItem() instanceof ItemRecipeBook) {
                        NonNullList<ItemStack> bookGrid = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);// new
                                                                                                              // ItemStack[9];
                        ItemStack resultStack = ItemStack.EMPTY;
                        NBTTagCompound nbt = recipeBook.getTagCompound();
                        if (nbt != null) {
                            NBTTagList tagList = nbt.getTagList("grid", Constants.NBT.TAG_COMPOUND);
                            bookGrid = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
                            for (int i = 0; i < 9; i++) {
                                NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
                                byte slot = tag.getByte("Slot");
                                if (slot >= 0 && slot < 9) {
                                    ItemStack nbtStack = new ItemStack(tag);
                                    if (nbtStack != ItemStack.EMPTY) {
                                        bookGrid.set(slot, nbtStack);
                                    }
                                }
                            }
                            NBTTagCompound resultTag = nbt.getCompoundTag("result");
                            if (resultTag != null) {
                                resultStack = new ItemStack(resultTag);
                            }
                        }

                        if (resultStack != ItemStack.EMPTY) {
                            if (Utils.checkForValidRecipeIngredients(bookGrid, player, false)) {
                                Container contained = new Container() {
                                    @Override
                                    public boolean canInteractWith(EntityPlayer p_75145_1_) {
                                        return false;
                                    }
                                };
                                InventoryCrafting playerCraftMatrix = new InventoryCrafting(contained, 3, 3);
                                for (int i = 0; i < bookGrid.size(); i++) {
                                    playerCraftMatrix.setInventorySlotContents(i, bookGrid.get(i));
                                }

                                ItemStack result = CraftingManager.findMatchingResult(playerCraftMatrix, player.world);
                                if (result != ItemStack.EMPTY) {
                                    if (Utils.checkForValidRecipeIngredients(bookGrid, player, true)) // remove valid
                                                                                                      // ingredients
                                                                                                      // from
                                    // inventory
                                    {
                                        if (!(player.inventory.addItemStackToInventory(result.copy()))) {
                                            EntityItem entityItem = new EntityItem(player.world, player.posX,
                                                    player.posY,
                                                    player.posZ,
                                                    new ItemStack(result.getItem(), result.getCount(),
                                                            result.getItemDamage()));
                                            if (result.hasTagCompound()) {
                                                entityItem.getItem()
                                                        .setTagCompound(
                                                                (NBTTagCompound) result.getTagCompound().copy());
                                            }
                                            entityItem.motionX = 0;
                                            entityItem.motionY = 0;
                                            entityItem.motionZ = 0;
                                            player.world.spawnEntity(entityItem);
                                        }
                                        Utils.sendARecipeBookTextPacket(player,
                                                result.getDisplayName() + " "
                                                        + I18n.translateToLocal("gui.recipe.crafted"),
                                                inventorySlot);
                                    } else {
                                        Utils.sendARecipeBookTextPacket(player,
                                                I18n.translateToLocal("gui.recipe.failed"),
                                                inventorySlot);
                                    }
                                    return;
                                } else {
                                    Utils.sendARecipeBookTextPacket(player, I18n.translateToLocal("gui.recipe.invalid"),
                                            inventorySlot);
                                    return;
                                }
                            } else {
                                Utils.sendARecipeBookTextPacket(player, I18n.translateToLocal("gui.recipe.missing"),
                                        inventorySlot);
                                return;
                            }
                        } else {
                            Utils.sendARecipeBookTextPacket(player, I18n.translateToLocal("gui.recipe.invalid"),
                                    inventorySlot);
                            return;
                        }
                    }
                    Utils.sendARecipeBookTextPacket(player, I18n.translateToLocal("gui.recipe.wrong"), inventorySlot);
                } else {
                    Utils.sendARecipeBookTextPacket(player, I18n.translateToLocal("gui.recipe.disabled"),
                            inventorySlot);
                }
                return;
            });
            return null;
        }
    }
}
