package jds.bibliocraft.storygen;

import java.util.Random;

import net.minecraftforge.common.config.Configuration;

public class BookGenWordLists
{

	private static String[] wVowels = {"a","e","i","o","u"};
	private static String[] wConsonants = {"b","c","d","f","g","h","j","k","l","m","n","p","r","s","t","v","w","rd","rk","rv","rt","nk","wn","th","ch","lb","tr","mw"};
	private static String[] wEndConsonants = {"c","d","g","k","l","m","n","p","r","s","t","rd","rk","rv","rt","ny","nk","wn","ch", "gon", "wood", "ix", "th", "ck"};
	private static String[] wCapitalLetters = {"A","B","C","D","F","G","H","J","K","L","M","N","P","R","S","T","V","W","Z"};
	Random rando;
	public static Configuration config;
	
	////paragraph 1
	
	
	public BookGenWordLists()
	{
		rando = new Random();
		/*
		File configFolder = new File(Minecraft.getMinecraft().mcDataDir, "config");
		config = new Configuration(new File(configFolder, "BiblioCraftWordLists.cfg"));
		config.load();
		config.addCustomCategoryComment("Words", "These are the lists of words that the typewriter randomly chooses from as it constructs a story from a predetermined story structure");
		config.save();
		*/
	}
	
	private String getRandomVowel()
	{
		return wVowels[rando.nextInt(wVowels.length)];
	}
	
	private String getRandomConsonant()
	{
		return wConsonants[rando.nextInt(wConsonants.length)];
	}
	
	private String getRandomCapitalLetter()
	{
		return wCapitalLetters[rando.nextInt(wCapitalLetters.length)];
	}
	
	private String getRandomEndConsonant()
	{
		return wEndConsonants[rando.nextInt(wEndConsonants.length)];
	}
	
	public String getRandomName()
	{
		String name = "";
		int nameType = rando.nextInt(3);
		switch (nameType)
		{
			case 0:
			{
				name += getRandomCapitalLetter();
				name += getRandomVowel();
				name += getRandomConsonant();
				name += getRandomVowel();
				name += getRandomVowel();
				break;
			}
			case 1:
			{
				name += getRandomCapitalLetter();
				name += getRandomVowel();
				name += getRandomVowel();
				name += getRandomEndConsonant();
				break;
			}
			case 2:
			{
				name += getRandomCapitalLetter();
				name += getRandomVowel();
				name += getRandomConsonant();
				name += getRandomVowel();
				name += getRandomEndConsonant();
				break;
			}
		}
		
		return name;
	}
}
