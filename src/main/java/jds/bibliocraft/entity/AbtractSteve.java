package jds.bibliocraft.entity;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.world.World;

public class AbtractSteve extends AbstractClientPlayer
{
	private static GameProfile gp = new GameProfile(UUID.randomUUID(), "BiblioSteve");
	
	public AbtractSteve(World world)
	{
		super(world, gp);
		setInvisible(true);
	}
}
