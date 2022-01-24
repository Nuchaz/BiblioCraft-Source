package jds.bibliocraft.storygen;

import java.util.Random;

public class BookGenUtil
{
/**
 * 
Stasis
Trigger
The quest
Surprise
Critical choice
Climax
Reversal
Resolution
 *
 */
	Random rando;
	BookGenWordLists words;
	public String character1 = "";
	public int character1Gender = 0; // 0 for male, 1 for female
	public String character2 = "";
	public int character2Gender = 0; // 0 for male, 1 for female
	public String character1job = "";
	public String antagonist = "";
	public String locationHome = "";
	public String locationHomeType = "";
	public String locationTraveled = "";
	public String locationTraveledType = "";
	BookStructure1 struct1;
	WordsStructure1Standard wordsS1;
	
	public static String[] nounProfession = {"blacksmith", "carpenter", "chef", "geologist", "miner", "reverend", "protector"};
	public static String[] nounLocation = {"village", "camp", "castle", "house", "cottage", "city"};
	
	private String[] titleWord1 = {"Dragon", "Witch", "Prized", "Dream", "Emperor", "Flower", "Dark", "The", "Secret", "Ravaged", "Silent", "Shadow", "Wet", "Angel", "Life", "Night", "World", "Servent", "Gate of", "Dying", 
								   "Burning", "Moon", "Petal", "Rough", "Ship of", "Last", "Deep", "Shadow of", "Healing", "Princess", "Star", "Truth of", "Seventh", "Sharp", "Vanilla", "Tower of", "Bad", "Bold", "Vacant", "Snow", 
									"Jungle", "Ancient", "Frozen", "Whispering", "Wizards", "Living", "Wool", "Cactus", "Self", "Butterfly", "Delicious", "Family", "Short", "Long", "Rooftop", "Window", "Day", "Simple", "Red", "Autumn",
									"Last", "Hot", "War", "Micro"};
	
	private String[] titleWord2 = {"Slayer", "Mage", "Touch", "Wind", "Wizard", "Memory", "Voyage", "Rose", "Heat", "Illusion", "Obsession", "Sword", "Pickaxe", "Miner", "Danger", "Truth", "Prophecy", "Lords", "Souls", "Secrets", 
								   "Words", "Hunter", "Abyss", "Bridge", "Names", "Flames", "Dreams", "Storm", "Serpents", "Wings", "Consort", "Stones", "Silk", "Shards", "Sky", "Wolf", "Academy", "Rainbows", "Planet", "Healing", 
								   "Past", "Memory", "School", "Ice", "Flame", "Thoughts", "Years", "Waves", "Destruction", "Vision", "Treats", "Wheat", "Guy", "Distance", "Concern", "Adventure", "Journey", "Dawn", "Dynamite", "Darkness",
								   "Giant", "Titans", "Fuzz", "Games"};
	
	// and whatever else I need for consistancy throughout a story.
	public BookGenUtil(int type, String name)
	{
		this.character1 = "somone"; // I should maybe generate the consistant bits of information in the constructor.
		this.rando = new Random();
		this.words = new BookGenWordLists();
		//this.wordsS1 = new WordsStructure1Standard();
		character1 = words.getRandomName();
		character1Gender = rando.nextInt(2);
		character2 = words.getRandomName();
		character2Gender = rando.nextInt(2);
		locationHome = words.getRandomName();
		locationHomeType = nounLocation[rando.nextInt(nounLocation.length)];
		locationTraveled = words.getRandomName();
		locationTraveledType = nounLocation[rando.nextInt(nounLocation.length)];
		
		antagonist = words.getRandomName(); // this could be a list of bad creatures?, dragsons creepers and whatnot
		character1job = nounProfession[rando.nextInt(nounProfession.length)];
		
		switch (type) 
		{
			case 2:  {this.wordsS1 = new WordsStructure1Pig();break;}
			case 3:  {this.wordsS1 = new WordsStructure1Chicken();break;}
			case 4:  {this.wordsS1 = new WordsStructure1Cow();break;}
			case 5:  {this.wordsS1 = new WordsStructure1Sheep();break;}
			case 6:  {this.wordsS1 = new WordsStructure1Wolf();break;}
			case 7:  {this.wordsS1 = new WordsStructure1Ocelot();break;}
			case 8:  {this.wordsS1 = new WordsStructure1Creeper();break;}
			case 9:  {this.wordsS1 = new WordsStructure1Zombie();break;}
			case 10: {this.wordsS1 = new WordsStructure1Enderman();break;}
			default: {this.wordsS1 = new WordsStructure1Standard(); break;}
		}
		
		
		struct1 = new BookStructure1(this.character1, this.character2, this.character1Gender, this.character2Gender, this.character1job, this.antagonist, this.locationHome, this.locationTraveled, this.locationHomeType, this.locationTraveledType, this.wordsS1, this.rando);
		//System.out.println("so whats up");

	}
	
	public String getBookTitle()
	{
		return titleWord1[rando.nextInt(titleWord1.length)]+" "+titleWord2[rando.nextInt(titleWord2.length)];
	}
	/////// main 8 part story functions ///////////
	
	public String getStasis(int type, String name)  // type = 0, human, 1:villager, 2:pig, 3:cree[er, 4:chicken
	{
		String sentance = "";
		//int structure = rando.nextInt(2); // add more story structures and randomly choose between them.
		sentance = struct1.getStasis(type, name);
		return sentance;
	}
	
	public String getTrigger(int type, String name)
	{
		return struct1.getTrigger(type, name);
	}
	
	public String getQuest(int type, String name)
	{
		String sentance = "";
		return struct1.getQuest(type, name);
	}
	
	public String getSurprise(int type, String name)
	{
		String sentance = "";
		return struct1.getSurprise(type, name);
	}
	
	public String getChoice(int type, String name)
	{
		String sentance = "";
		return struct1.getChoice(type, name);
	}
	
	public String getClimax(int type, String name)
	{
		String sentance = "";
		return struct1.getClimax(type, name);
	}
	
	public String getReversal(int type, String name)
	{
		String sentance = "";
		return struct1.getReversal(type, name);
	}
	
	public String getResolution(int type, String name)
	{
		String sentance = "";
		return struct1.getResolution(type, name);
	}
	

	///// Other Functions ////////////
	
	public String randomNames(String sentance)
	{
		for (int i = 0; sentance.length()<245; i++)
		{
			sentance += "    "+words.getRandomName();
		}
		return sentance;
	}
}
