package jds.bibliocraft.network.packet.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.server.FMLServerHandler;

public class BiblioSoundPlayer implements IMessage
{
	BlockPos position;
	float volume;
	float pitch;
	String theSound; // it might be better to just pass along an id to grab a reference to a preloaded sound, but well see if this works first, it's simpler.

	public BiblioSoundPlayer()
	{
		
	}
	
	public BiblioSoundPlayer(String sound, BlockPos pos, float vol, float pit)
	{
		this.theSound = sound;
		this.position = pos;
		this.volume = vol;
		this.pitch = pit;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.theSound = ByteBufUtils.readUTF8String(buf);
		this.position = new BlockPos(buf.readFloat(), buf.readFloat(), buf.readFloat());
		this.volume = buf.readFloat();
		this.pitch = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeUTF8String(buf, theSound);
		buf.writeFloat(position.getX());
		buf.writeFloat(position.getY());
		buf.writeFloat(position.getZ());
		buf.writeFloat(this.volume);
		buf.writeFloat(this.pitch);
	}
	
    public static class Handler implements IMessageHandler<BiblioSoundPlayer, IMessage> 
    {
        @Override
        public IMessage onMessage(BiblioSoundPlayer message, MessageContext ctx) 
        {
        	
            Minecraft.getMinecraft().addScheduledTask(() -> 
            {
            	PlaySound(message.theSound, message.position, message.volume, message.pitch);
            });
			return null;
        }
    }
    
    public static void PlaySound(String soundString, BlockPos thePosition, float vol, float pit)
    {
    	SoundEvent sound = new SoundEvent(new ResourceLocation(soundString));
    	Minecraft.getMinecraft().world.playSound(thePosition, sound, SoundCategory.BLOCKS, vol, pit, false);
    }

}
