package jds.bibliocraft.events;

import java.util.ArrayList;
import java.util.Random;

import jds.bibliocraft.BiblioCraft;
import jds.bibliocraft.Config;
import jds.bibliocraft.items.ItemAtlas;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventDeathDrop 
{
	public Random rando;
	public ArrayList lplayerNames = new ArrayList();
	public ArrayList ldeathX = new ArrayList();
	public ArrayList ldeathZ = new ArrayList();

	
	public EventDeathDrop()
	{
		rando = new Random();
	}
	@SubscribeEvent
	public void onDeath(LivingDeathEvent event)
	{
		
		if (event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			ItemStack mainhand = player.getHeldItem(EnumHand.MAIN_HAND);
			ItemStack offhand = player.getHeldItem(EnumHand.OFF_HAND);
			if (mainhand != ItemStack.EMPTY && EventItemToss.checkIfValidPacketItem(mainhand.getUnlocalizedName()))
			{
				Container testContainer = player.openContainer;
				System.out.println("not today sucker!");
				if (testContainer != null)
				{
					if (EventItemToss.hasProperContainer(mainhand.getUnlocalizedName(), testContainer))
					{
						player.closeScreen();
					}
				}
				else
				{
					player.closeScreen();
				}
			}
			if (offhand != ItemStack.EMPTY && EventItemToss.checkIfValidPacketItem(offhand.getUnlocalizedName()))
			{
				Container testContainer = player.openContainer;
				if (testContainer != null)
				{
					if (EventItemToss.hasProperContainer(offhand.getUnlocalizedName(), testContainer))
					{
						player.closeScreen();
					}
				}
				else
				{
					player.closeScreen();
				}
			}
		}
		
		String name = event.getEntityLiving().getCustomNameTag();
		if (event.getEntityLiving() instanceof EntityPlayer && Config.enableDeathCompass)
		{
			InventoryPlayer inv = ((EntityPlayer)event.getEntityLiving()).inventory;
			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack testItem = inv.getStackInSlot(i);
				if (testItem != ItemStack.EMPTY)
				{
					if (testItem.getItem() instanceof ItemAtlas)
					{
						NBTTagCompound tags = testItem.getTagCompound();
						if (tags != null && tags.hasKey("ench"))
						{
							boolean foundEnch = false;
							NBTTagList enchList = tags.getTagList("ench", Constants.NBT.TAG_COMPOUND);
							for (int j = 0; j < enchList.tagCount(); j++)
							{
								NBTTagCompound enchantment = enchList.getCompoundTagAt(j);
								if (enchantment != null)
								{
									if (enchantment.getInteger("id") == Enchantment.getEnchantmentID(BiblioCraft.deathCompassEnch))
									{
										lplayerNames.add(name);
										ldeathX.add((int) event.getEntityLiving().posX);
										ldeathZ.add((int) event.getEntityLiving().posZ);
										foundEnch = true;
										break;
									}
								}
							}
							if (foundEnch)
							{
								break;
							}
						}
					}
				}
			}
		}
		
		/*
		if (name.contains("Nuchaz"))
		{
			//ItemStack book = new ItemStack(Items.writable_book, 1);
			// maybe this should only be a small chance?
			if ((1+rando.nextInt(10)) <= 2)
			{
				ItemStack screwgun = new ItemStack(ItemLoader.screwgun, 1);
				ItemStack tape = new ItemStack(ItemLoader.tapeMeasure, 1);
				dropItem(event, screwgun);
				dropItem(event, tape);
			}
			if (1+rando.nextInt(2) > 1)
			{
				ItemStack glasses = new ItemStack(ItemLoader.readingglasses, 1, 0);
				dropItem(event, glasses);
			}
		}
		
		if (name.contains("ShabbyQ") || name.contains("Ekacfeeb12"))
		{
			//Random rando = new Random();
			ItemStack feathers = new ItemStack(Items.feather, (rando.nextInt(4)+1));
			dropItem(event, feathers);
			if (1+rando.nextInt(2) > 1)
			{
				ItemStack glasses = new ItemStack(ItemLoader.readingglasses, 1, 1);
				dropItem(event, glasses);
			}
		}
		
		if (name.contains("murphyobrian"))
		{
			if ((1+rando.nextInt(5)) <= 2)
			{
				ItemStack wbook = new ItemStack(Items.writable_book, 1);
				ItemStack books = new ItemStack(Items.book, (rando.nextInt(4)+1));
				dropItem(event, wbook);
				dropItem(event, books);
			}
			if (1+rando.nextInt(2) > 1)
			{
				ItemStack glasses = new ItemStack(ItemLoader.readingglasses, 1, 2);
				dropItem(event, glasses);
			}
			ItemStack book = new ItemStack(Items.book, 1);
			dropItem(event, book);
		}
		*/
	}
	/*
	private void dropItem(LivingDeathEvent event, ItemStack stuff)
	{
		int i = (int)event.entityLiving.posX;
		int j = (int)event.entityLiving.posY;
		int k = (int)event.entityLiving.posZ;
		World world = event.entity.worldObj;//Minecraft.getMinecraft().theWorld;
		//System.out.println(i+"    "+j+"   "+k);
		if (stuff != null && stuff.getCount() > 0)
		{
			float iAdjust = 0;
			float kAdjust;
			//System.out.println(caseTile.getAngle());

			EntityItem entityItem = new EntityItem(world, i+0.5F, j + 1.4F, k + 0.5F, new ItemStack(stuff.getItem(), stuff.getCount(), stuff.getItemDamage()));
			
			if (stuff.hasTagCompound())
			{
				entityItem.getEntityItem().setTagCompound((NBTTagCompound) stuff.getTagCompound().copy());
			}
			float factor = 0.05F;
			entityItem.motionX = rando.nextGaussian() * factor;
			entityItem.motionY = rando.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rando.nextGaussian() * factor;
			//System.out.println("Spawning thing");
			world.spawnEntityInWorld(entityItem);
			stuff.getCount() = 0;
		}
	}
	*/
}
