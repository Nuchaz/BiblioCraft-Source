package jds.bibliocraft.network.packets.client;

import com.google.common.base.Supplier;

import jds.bibliocraft.tileentities.BiblioTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketModelUpdate 
{
	public BiblioTileEntity tileEntity;
	
	public PacketModelUpdate(BiblioTileEntity tile)
	{
		this.tileEntity = tile;
		// TODO I dont actually need this I guess
	}
	
	public static void encode(PacketModelUpdate pack, PacketBuffer buffer) 
	{
		buffer.writeBlockPos(pack.tileEntity.getPos());
	}
	
    public static PacketModelUpdate decode(PacketBuffer buffer) 
    { 
    	//buffer.readBlockPos()
    	//Minecraft.getInstance().world.getBlockState(pos)
    	return null;//new PacketModelUpdate(buffer.readBoolean()); 
    }
    
    public static class Handler 
    {
	    public static void handle(final PacketModelUpdate pack, Supplier<NetworkEvent.Context> context)
	    {
	    	context.get().enqueueWork(() -> 
	    	{
	    		//ServerPlayerEntity player = context.get().getSender();
	    		//if (player == null)
	    		//	return;

	    		
	    		
	    	});
	    	context.get().setPacketHandled(true);
	    }
    }
}
