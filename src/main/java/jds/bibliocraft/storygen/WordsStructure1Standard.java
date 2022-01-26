package jds.bibliocraft.storygen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WordsStructure1Standard
{
	// paragraph 1 - stasis
	public String[] capitalAdjectives1 = {"Deep", "Far", "Off", "Rooted", "Buried", "Distant"};
	public String[] nounLocation1 = {"mountains", "caves", "forest", "desert", "plains", "jungle"};
	public String[] adverb1 = {"small", "tiny", "medium", "large", "enormous"};
	
	public String[] nounLocation2 = {"village", "camp", "castle", "house", "cottage", "city"};
	public String[] preposition1 = {"on", "over", "into"};
	public String[] verbyNoun1 = {"hill", "mound", "mountain", "plains", "valley", "horizon", "desert"};
	public String[] verb1 = {"around", "near by", "far away"};
	
	public String[] adjective1 = {"beautiful", "colorful", "bountiful", "extravagant"};
	public String[] nounLocation3 = {"shops", "stores", "restaurants"};
	public String[] adjective2 = {"talented", "skilled", "top notch", "kind-hearted"};
	public String[] adjective3 = {"cozy", "small", "warm", "welcoming"};
	public String[] nounLocation4 = {"church", "community center", "town square", "farmers market"};
	
	public String[] adjective4 = {"local", "nearby", "friendly"};
	public String[] adverb2 = {"always", "usually", "often", "rarely"};
	public String[] verb2 = {"help", "assist"};
	public String[] phrase1 = {"those in need", "a good cause"};
	
	////paragraph 2 - trigger
	public String[] nounTime1 = {"day", "morning", "evening", "afternoon", "night", "weekend", "hour", "week", "month"};
	public String[] verb3 = {"climb", "traverse", "walk"};
	public String[] adverb3 = {"top", "edge", "front", "back"};
	public String[] nounThing1 = {"steeple", "rooftop", "balcony", "window", "town center"};
	public String[] verbPhrase1 = {"say a prayer", "eat a potato", "look at the sky"};
	
	public String[] adverb4 = {"Facing", "Looking to"};
	public String[] nounBigThing1 = {"sun", "horizon"};
	public String[] verbPhrase2 = {"eyes closed", "weapon drawn"};
	public String[] adverb5 = {"whispered", "speaks", "visualizes"};
	public String[] verb4 = {"prayer", "mantra", "battlechant"};
	
	public String[] adverb6 = {"cool", "hot", "hard", "fast"};
	public String[] verb5 = {"breeze", "wind", "rain"};
	public String[] adjective5 = {"burst", "blew", "ran"};
	public String[] adverb7 = {"across", "down", "around"};
	public String[] nounThing2 = {"face", "hair", "chest", "body", "clothes"};
	public String[] verbPhrase3 = {"opened his eyes", "looked to the sky", "heard a loud noise"};
	
	public String[] verb6 = {"plume", "cloud", "bunch"};
	public String[] nounThing3 = {"smoke", "mist", "fire", "light"};
	public String[] adverb8 = {"just", "close", "nearly"};
	
	////paragraph 3 quest
	public String[] verbPhrase4 = {"feared for", "wondered about"};
	public String[] adjective6 = {"nearby", "distant"};
	
	public String[] adjective7 = {"decided"};
	public String[] verb7 = {"announce", "explain", "communicate"};
	public String[] verb8 = {"concern", "curiosity", "wonder"};
	public String[] verbPhrase5 = {"other villagers", "residents", "friends", "neighbors"};

	public String[] verb9 = {"suggested", "demanded", "asked that"};
	public String[] verb10 = {"gather", "collect", "get", "find", "make"};
	public String[] nounThing4 = {"supplies", "medicine"};
	public String[] adjective8 = {"spare", "share"};
	public String[] verb11 = {"make", "take"};
	public String[] verb12 = {"journey", "adventure", "trip"};
	public String[] verb13 = {"try", "attempt"};
	public String[] verb14 = {"help", "assist", "investigate"};
	
	/// paragraph 4 - surprise
	public String[] conjunction1 = {"After", "Now", "Since"};
	public String[] adverb9 = {"nearly", "almost", "close to"};
	public String[] adverb10 = {"full", "half", "quarter"};
	public String[] verb15 = {"walking", "running", "jogging", "horseback riding", "pig riding"};
	public String[] verb16 = {"see", "look down", "look out"};
	public String[] preposition2 = {"over", "on to"};
	public String[] adjective9 = {"final", "last"};
	public String[] adverbPhrase1 = {"down onto", "over to"};
	
	public String[] phrase2 = {"more than"};
	public String[] adjective10 = {"burning", "troubled"};
	public String[] verbPhrase6 = {"an attack", "a party"};
	
	public String[] adjective11 =  {"monstrous"};
	public String[] nounThing5 = {"creature", "beasty", "beast", "animal", "bad guy", "monster"};
	public String[] adverb11 = {"just"};
	public String[] nounThing6 = {"outskirts"};
	public String[] verb17 = {"watching", "making", "observing"};
	public String[] verb18 = {"burn", "smolder", "party"};
	
	
	//// paragraph 5 - Choice
	public String[] verb19 = {"tried", "attempted"};
	public String[] verb20 = {"spot", "see"};
	public String[] phrase3 = {"anyone could still be alive", "anyone was in need of help", "everything was safe"};
	public String[] adjective12 = {"difficult", "smokey", "foggy"};
	
	public String[] verb21 = {"woud have", "wanted", "needed"};
	public String[] phrase4 = {"get closer", "venture downward"};
	public String[] verb22 = {"check", "search"};
	public String[] phrase5 = {"for survivors", "the area"};
	
	public String[] verb23 = {"could", "might", "would", "should"};
	public String[] phrase6 = {"injured or killed", "attacked and beaten"};
	
	public String[] verb24 = {"take", "risk"};
	public String[] nounThing7 = {"chance"};
	public String[] phrase7 = {"leaving somone"};
	
	/// paragraph 6 - Climax
	public String[] adverb12 = {"neared", "got close to", "arrived at", "closed in on"};
	public String[] verb26 = {"hear", "sense", "see"};
	public String[] verb27 = {"crying", "weeping", "injured", "cowering", "fighting back", "hiding"};
	
	public String[] phrase8 = {"ran in", "snuck in", "walked in"};
	public String[] verb28 = {"try", "attempt"};
	public String[] verb29 = {"save", "rescue", "liberate"};
	
	public String[] verb30 = {"trapped", "locked", "wedged", "hidden"};
	public String[] phrase9 = {"inside one of the houses", "inside one of the buildings", "inside a shed", "under some rubble"};
	
	public String[] verb31 = {"pried", "kicked"};
	public String[] nounThing8 = {"door", "wall"};
	public String[] verb32 = {"open", "down", "in"};
	public String[] verb33 = {"asked", "inquired"};
	public String[] phrase10 = {"anyone else", "anyone left", "anything left"};
	
	public String[] phrase12 = {"time to go", "time to run", "dangerous outside"};
	
	/// paragraph 7 - reversal
	public String[] phrase13 = {"ran out of", "got away from", "escaped", "carefully left"};
	public String[] adjective13 = {"burning", "smoldering", "damaged", "broken"};
	public String[] nounThing9 = {"house", "cabin", "shed", "cave", "home", "workshop", "storage room"};
	public String[] adjective14 = {"determined"};
	public String[] phrase14 = {"make it", "escape", "get away", "run"};
	public String[] nounThing10 = {"safety", "refuge", "the outside", "shelter", "a safe place"};
	
	public String[] verb34 = {"awoken", "woke up", "looked around", "smelled a new scent", "heard something", "searched around"};
	public String[] phrase15 = {"spotted them", "smelled them", "seen them", "heard them"};
	
	public String[] nounBeast = {"dragon", "creeper", "zombie", "skeleton", "ender", "red dragon", "end dragon", "zombie pigman", "ghast", "giant squid", "giant pig", "evil witch", "flying creeper", "creeper dragon", "evil rabbit", "wild ocelot"};
	
	public String[] verb35 = {"swooped down", "charged in", "ran at them", "snarled at them", "jumped down", "jumped at them"};
	public String[] phrase16 = {"eat them", "stomp them", "kill them", "claw them", "scare them", "hurt them", "take control", "get the survivors", "attack the survivors"};
	
	public String[] verb36 = {"protect", "defend", "guard", "look after"};
	public String[] phrase17 = {"drew a sword", "drew a big axe", "drew a big knife", "pulled out a fish", "grabbed a weapon", "broke a stick in half"};
	public String[] verb37 = {"jumped", "launched", "ran", "swung", "plunged"};
	
	/// paragraph 8 - resolution
	public String[] verb38 = {"eat", "claw", "step on", "run past", "fly away from"};
	public String[] verb39 = {"sunk", "stuck", "penetrated", "cut", "sliced", "slide"};
	public String[] nounThing11 = {"sword", "knife", "axe", "pickaxe", "hoe", "sharpened stick"};
	public String[] nounThing12 = {"neck", "side", "face", "eye", "back", "belly"};
	public String[] phrase18 = {"killing it", "injuring it", "murdering it", "scaring it away", "wounding it", "forcing it to flee"};
	
	public String[] verb40 = {"shocked", "surprised", "perplexed", "flabbergasted", "astonished", "amazed"};
	public String[] phrase19 = {"how fast that just happend", "the speed of the hero", "the size of the beast", "the heros strength", "the horrible smell"};
	
	public String[] phrase20 = {"ran to", "yelled to", "walked to", "headed to"};
	public String[] verb41 = {"hugged", "grabbed", "spoke to", "looked at"};
	public String[] phrase21 = {"they are safe now", "the beast is dead", "the bomb is diffused", "there is plenty of daylight", "there is no more danger", "they should hurry", "it is getting late", "no threat is too big to handle"};
	
	public String[] phrase22 = {"head back", "travel home", "walk", "journey", "adventure", "ride horseback", "ride pigs", "go back"};
	
	
	public String currentLanguage = "";
	public WordsStructure1Standard()
	{
		//this.currentLanguage = Minecraft.getMinecraft().gameSettings.language;
		//System.out.println(this.currentLanguage);
		
		// read directory and get list of .lang files
		// this total works to read the directory within the jar.

		

		// check for match, if no match, just use en_US.
	}
	
	private void readCustomLanguage()
	{
		try
		{
			InputStream input = getClass().getResourceAsStream("/assets/bibliocraft/lang/no_NO.lang");
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			//StringBuilder out = new StringBuilder();
			String line;
			
			while((line = reader.readLine()) != null)
			{
				//out.append();
				//System.out.println(line);
			}
			
			reader.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void getLanguageList()
	{
		
	}
	
	private void getStructureList()
	{
		
	}
}
