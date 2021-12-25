package jds.bibliocraft.helpers;

import jds.bibliocraft.CommonProxy;
import net.minecraft.util.ResourceLocation;

public class BiblioEnums
{
	public static int[] pianoXSizes = {1,2};
	public static int[] pianoYSizes = {1,1};
	public static int[] seaofportalsXSizes = {1,4};
	public static int[] seaofportalsYSizes = {1,3};

	public static int[] pieXSizes = {1};
	public static int[] pieYSizes = {1};
	
	public static int[] pianoPixelSizes = {64, 128};
	public static int[] piePixelSizes = {32, 64};
	public static int[] collagePixelSizes = {32, 64, 128};
	
	public static String[] piano1x1 = {"bibliocraft:paintings/64painting01z", "bibliocraft:paintings/128painting01z"};
	public static String[] piano1x2 = {"bibliocraft:paintings/64painting01l", "bibliocraft:paintings/128painting01l"};
	public static String[][] pianox = {piano1x1,piano1x2};
	
	public static String[] bowloffruit1x1 = {"bibliocraft:paintings/32painting02z", "bibliocraft:paintings/64painting02z", "bibliocraft:paintings/128painting02z"};
	public static String[] bowloffruit1x2 = {"bibliocraft:paintings/32painting02l", "bibliocraft:paintings/64painting02l", "bibliocraft:paintings/128painting02l"};
	public static String[][] bowloffruitx = {bowloffruit1x1,bowloffruit1x2};
	
	public static String[] sunoffires1x1 = {"bibliocraft:paintings/32painting03z", "bibliocraft:paintings/64painting03z", "bibliocraft:paintings/128painting03z"};
	public static String[] sunoffires1x2 = {"bibliocraft:paintings/32painting03l", "bibliocraft:paintings/64painting03l", "bibliocraft:paintings/128painting03l"};
	public static String[][] sunoffiresx = {sunoffires1x1,sunoffires1x2};
	
	public static String[] seaofportals1x1 = {"bibliocraft:paintings/64painting04z", "bibliocraft:paintings/128painting04z"};
	public static String[] seaofportals3x4 = {"bibliocraft:paintings/64painting04", "bibliocraft:paintings/128painting04"};
	public static String[][] seaofportalsx = {seaofportals1x1, seaofportals3x4};
	
	public static String[] seaofcloserportals1x1 = {"bibliocraft:paintings/64painting04", "bibliocraft:paintings/128painting04"};
	public static String[][] seaofcloserportalsx = {seaofcloserportals1x1};
	
	public static String[] cornerexplody1x1 = {"bibliocraft:paintings/64painting05z", "bibliocraft:paintings/128painting05z"};
	public static String[] cornerexplody1x2 = {"bibliocraft:paintings/128painting05l", "bibliocraft:paintings/256painting05l"};
	public static String[][] cornerexplodyx = {cornerexplody1x1,cornerexplody1x2};
	
	public static String[] pie1x1 = {"bibliocraft:paintings/32pie", "bibliocraft:paintings/64pie"};
	public static String[][] piex = {pie1x1};
	
	public static String[] loveofbacon1x1 = {"bibliocraft:paintings/32collage", "bibliocraft:paintings/64collage", "bibliocraft:paintings/128collage"};
	public static String[][] loveofbaconx = {loveofbacon1x1};
	
	public static String[] boathouse1x1 = {"bibliocraft:paintings/boathouse_64", "bibliocraft:paintings/boathouse_128"};
	public static String[] boathouse3x4 = {"bibliocraft:paintings/boathouse_64l", "bibliocraft:paintings/boathouse_128l"};
	public static String[][] boathousex = {boathouse1x1, boathouse3x4};
	
	public static String[] jimi1x1 = {"bibliocraft:paintings/jimi_32", "bibliocraft:paintings/jimi_64", "bibliocraft:paintings/jimi_128"};
	public static String[][] jimix = {jimi1x1};
	
	public static String[] raven1x1 = {"bibliocraft:paintings/raven_32", "bibliocraft:paintings/raven_64", "bibliocraft:paintings/raven_128"};
	public static String[] raven3x4 = {"bibliocraft:paintings/raven_32l", "bibliocraft:paintings/raven_64l", "bibliocraft:paintings/raven_128l"};
	public static String[][] ravensx = {raven1x1, raven3x4};
	
	//////////
	
	public static ResourceLocation[] piano1x1r = {CommonProxy.PAINTING01_64ZOOM, CommonProxy.PAINTING01_128ZOOM};
	public static ResourceLocation[] piano1x2r = {CommonProxy.PAINTING01_64LONG, CommonProxy.PAINTING01_128LONG};
	public static ResourceLocation[][] pianoxr = {piano1x1r, piano1x2r};
	
	public static ResourceLocation[] bowloffruit1x1r = {CommonProxy.PAINTING02_32ZOOM, CommonProxy.PAINTING02_64ZOOM, CommonProxy.PAINTING02_128ZOOM};
	public static ResourceLocation[] bowloffruit1x2r = {CommonProxy.PAINTING02_32LONG, CommonProxy.PAINTING02_64LONG, CommonProxy.PAINTING02_128LONG};
	public static ResourceLocation[][] bowloffruitxr = {bowloffruit1x1r, bowloffruit1x2r};
	
	public static ResourceLocation[] sunoffires1x1r = {CommonProxy.PAINTING03_32ZOOM, CommonProxy.PAINTING03_64ZOOM, CommonProxy.PAINTING03_128ZOOM};
	public static ResourceLocation[] sunoffires1x2r = {CommonProxy.PAINTING03_32LONG, CommonProxy.PAINTING03_64LONG, CommonProxy.PAINTING03_128LONG};
	public static ResourceLocation[][] sunoffiresxr = {sunoffires1x1r, sunoffires1x2r};
	
	public static ResourceLocation[] seaofportals1x1r = {CommonProxy.PAINTING04_64ZOOM, CommonProxy.PAINTING04_128ZOOM};
	public static ResourceLocation[] seaofportals3x4r = {CommonProxy.PAINTING04_64FULL, CommonProxy.PAINTING04_128FULL};
	public static ResourceLocation[][] seaofportalsxr = {seaofportals1x1r, seaofportals3x4r};
	
	public static ResourceLocation[] seaofcloserportals1x1r = {CommonProxy.PAINTING04_64FULL, CommonProxy.PAINTING04_128FULL};
	public static ResourceLocation[][] seaofcloserportalsxr = {seaofcloserportals1x1r};
	
	public static ResourceLocation[] cornerexplody1x1r = {CommonProxy.PAINTING05_64ZOOM, CommonProxy.PAINTING05_128ZOOM};
	public static ResourceLocation[] cornerexplody1x2r = {CommonProxy.PAINTING05_128LONG, CommonProxy.PAINTING05_256LONG};
	public static ResourceLocation[][] cornerexplodyxr = {cornerexplody1x1r, cornerexplody1x2r};
	
	public static ResourceLocation[] pie1x1r = {CommonProxy.PAINTINGPIE_32FULL, CommonProxy.PAINTINGPIE_64FULL};
	public static ResourceLocation[][] piexr = {pie1x1r};
	
	public static ResourceLocation[] loveofbacon1x1r = {CommonProxy.PAINTINGCOLLAGE_32FULL, CommonProxy.PAINTINGCOLLAGE_64FULL, CommonProxy.PAINTINGCOLLAGE_128FULL};
	public static ResourceLocation[][] loveofbaconxr = {loveofbacon1x1r};
	
	public static ResourceLocation[] boathouse1x1r = {CommonProxy.BOATHOUSE_64FULL, CommonProxy.BOATHOUSE_128FULL};
	public static ResourceLocation[] boathouse3x4r = {CommonProxy.BOATHOUSE_64LONG, CommonProxy.BOATHOUSE_128LONG};
	public static ResourceLocation[][] boathousexr = {boathouse1x1r, boathouse3x4r};
	
	public static ResourceLocation[] jimi1x1r = {CommonProxy.JIMI_32FULL, CommonProxy.JIMI_64FULL, CommonProxy.JIMI_128FULL };
	public static ResourceLocation[][] jimixr = {jimi1x1r};
	
	public static ResourceLocation[] raven1x1r = {CommonProxy.RAVEN_32FULL, CommonProxy.RAVEN_64FULL, CommonProxy.RAVEN_128FULL};
	public static ResourceLocation[] raven3x4r = {CommonProxy.RAVEN_32LONG, CommonProxy.RAVEN_64LONG, CommonProxy.RAVEN_128LONG};
	public static ResourceLocation[][] ravensxr = {raven1x1r, raven3x4r};
	
	public static enum EnumBiblioPaintings
	{	
		piano("piano", pianoXSizes, pianoYSizes, pianoPixelSizes, pianox, pianoxr),
		
		bowloffruit("bowloffruit", pianoXSizes, pianoYSizes, collagePixelSizes, bowloffruitx, bowloffruitxr),
		
		sunoffires("sunoffires", pianoXSizes, pianoYSizes, collagePixelSizes, sunoffiresx, sunoffiresxr),
		
		seaofportals("seaofportals", seaofportalsXSizes, seaofportalsYSizes, pianoPixelSizes, seaofportalsx, seaofportalsxr),
		
		cornerexplody("cornerexplody", pianoXSizes, pianoYSizes, pianoPixelSizes, cornerexplodyx, cornerexplodyxr),
		
		pie("pie", pieXSizes, pieYSizes, piePixelSizes, piex, piexr),
		
		loveofbacon("loveofbacon", pieXSizes, pieYSizes, collagePixelSizes, loveofbaconx, loveofbaconxr),
		
		boathouse("boathouse", seaofportalsXSizes, seaofportalsYSizes, pianoPixelSizes, boathousex, boathousexr),
		
		jimi("jimi", pieXSizes, pieYSizes, collagePixelSizes, jimix, jimixr),
		
		raven("raven", seaofportalsXSizes, seaofportalsYSizes, collagePixelSizes, ravensx, ravensxr);
		
		public final String title;
		public final int[] sizeX;
		public final int[] sizeY;
		public final int[] resolution;
		public final String[][] paintingTexturesStrings;
		public final ResourceLocation[][] paintingTextures;
		
		private EnumBiblioPaintings(String id, int[] xsize, int[] ysize, int[] pixels, String[][] textureString, ResourceLocation[][] textures)
		{
			this.title = id;
			this.sizeX = xsize;
			this.sizeY = ysize;
			this.resolution = pixels;
			this.paintingTexturesStrings = textureString;
			this.paintingTextures = textures;
		}
	}
}
