package jds.bibliocraft.events;

import jds.bibliocraft.helpers.PaintingUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureStichHandler 
{
	public static final TextureStichHandler instance = new TextureStichHandler();

	public TextureStichHandler() {}
	
	 @SubscribeEvent
	 public void onTextureStichEvent(TextureStitchEvent event)
	 {
		 // any custom textures I need to use in my models should be registered here
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:blocks/frame"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/bookcase_books"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/waypointcompass"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/markerpole"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/clipboard"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamp"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamp_iron"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lantern"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lantern_iron"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight0")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight1")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight2")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight3")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight4")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight5")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight6")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight7")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight8")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight9")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight10")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight11")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight12")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight13")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight14")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lamplight15")); 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle0"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle1"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle2"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle3"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle4"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle5"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle6"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle7"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle8"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle9"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle10"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle11"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle12"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle13"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle14"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/lanterncandle15"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/paneler"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_blank"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/benchsides"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/sign_front"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/desk_books"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/clock"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/maptool"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/maptool"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter0"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter1"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter2"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter3"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter4"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter5"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter6"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter7"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter8"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter9"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter10"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter11"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter12"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter13"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter14"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter15"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_blank"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_1"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_2"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_3"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_4"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_5"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_6"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_7"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_8"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_9"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_10"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_11"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_12"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_13"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_14"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typewriter_paper_15"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/bell"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/dinnerplate"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/discrack"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/painting_press"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/sword_pedestal"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/armorstand"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/canvas"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/typesettingtable"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/printpress"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/inkplate0"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/inkplate1"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/inkplate2"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/inkplate3"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/inkplate4"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/inkplate5"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/inkplate6"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/inkplate7"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/inkplate8"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:items/atlas"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:gui/atlas_cover"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:items/clipboardsimple"));
		 
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/64painting01z"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/32painting02z"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/32painting03z"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/64painting04z"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/64painting05z"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/32collage"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/32pie"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/boathouse_64"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/jimi_32"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/raven_32"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/vanilla"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:paintings/custom"));
		 event.getMap().registerSprite(new ResourceLocation("bibliocraft:models/deathcompass"));
		 

		 PaintingUtil.updateCustomArtDatas();
		 if (PaintingUtil.customArtResources != null)
		 {
			 for (int i = 0; i < PaintingUtil.customArtResources.length; i++)
			 {
				 if (PaintingUtil.customArtHeights[i] == PaintingUtil.customArtWidths[i])
				 {
					 // only registers textures with a 1:1 aspect ratio for use with the canvas item model.
					 event.getMap().registerSprite(new ResourceLocation(PaintingUtil.customArtResourceStrings[i])); 
				 }
			 }
		 }
	 }
}
