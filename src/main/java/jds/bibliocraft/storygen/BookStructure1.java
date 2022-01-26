package jds.bibliocraft.storygen;

import java.util.Random;

public class BookStructure1
{
	private String character1 = "";
	private int character1Gender = 0; // 0 for male, 1 for female
	private String character2 = "";
	private int character2Gender = 0; // 0 for male, 1 for female
	private String antagonist = "";
	private String locationHome = "";
	private String locationTraveled = "";
	private String character1Job = "";
	public String locationHomeType = "";
	public String locationTraveledType = "";
	private WordsStructure1Standard words;
	private Random rando;
	private String nounBeasty = "";
	private String nounBeastyName = "";
	
	public BookStructure1(String c1, String c2, int c1gender, int c2gender, String c1job, String antag, String locHome, String locTravel, String locHomeType, String locTravelType, WordsStructure1Standard w, Random r)
	{
		character1= c1;
		character2 = c2;
		character1Gender = c1gender;
		character2Gender = c2gender;
		antagonist = antag;
		locationHome = locHome;
		locationTraveled = locTravel;
		locationHomeType = locHomeType;
		locationTraveledType = locTravelType;
		words = w;
		rando = r;
		character1Job = c1job;
		nounBeasty = words.nounThing5[rando.nextInt(words.nounThing5.length)];
		nounBeastyName = words.nounBeast[rando.nextInt(words.nounBeast.length)];
	}
	
	private String word(String[] wordlist)
	{
		return wordlist[rando.nextInt(wordlist.length)];
	}
	
	public String getHisHer(int himher)
	{
		if (himher == 0)
		{
			return "his";
		}
		else
		{
			return "her";
		}
	}
	
	public String getHeShe(int heshe)
	{
		if (heshe == 0)
		{
			return "he";
		}
		else
		{
			return "she";
		}
	}
	
	public String getcapHeShe(int heshe)
	{
		if (heshe == 0)
		{
			return "He";
		}
		else
		{
			return "She";
		}
	}
	
	public String getHimHer(int heshe)
	{
		if (heshe == 0)
		{
			return "him";
		}
		else
		{
			return "her";
		}
	}
	
	public String getStasis(int type, String name)  // type = 0, human, 1:villager, 2:pig, 3:cree[er, 4:chicken
	{
		String sentance = "";	
		sentance = word(words.capitalAdjectives1)+ " into the "+word(words.nounLocation1)+" lies a "+ word(words.adverb1)+" "+this.locationHomeType+" named "+this.locationHome+". ";
		
		sentance += this.locationHome+" is known as the "+word(words.nounLocation2)+" "+word(words.preposition1)+" the "+word(words.verbyNoun1)+" to those "+word(words.verb1)+". ";
		
		sentance += this.locationHome+" has "+word(words.adjective1)+" "+word(words.nounLocation3)+", a "+word(words.adjective2)+" "+this.character1Job+" and a "+word(words.adjective3)+" "+word(words.nounLocation4)+". ";
		
		sentance += "The "+word(words.adjective4)+" "+this.character1Job+" is "+word(words.adverb2)+" "+word(words.verb1)+" to "+word(words.verb2)+" "+word(words.phrase1)+".";
	
		return sentance;
	}

	
	public String getTrigger(int type, String name)
	{
		String sentance = "";
		
		sentance = "Each "+word(words.nounTime1)+", "+this.character1Job+" "+this.character1+" would "+word(words.verb3)+" to the "+word(words.adverb3)+" of the "+word(words.nounThing1)+" and "+word(words.verbPhrase1)+". ";
		
		sentance += word(words.adverb4)+" the "+word(words.nounBigThing1)+" with "+getHisHer(character1Gender)+" "+word(words.verbPhrase2)+",  "+getHeShe(character1Gender)+" "+word(words.adverb5)+" "+getHisHer(character1Gender)+" "+word(words.verb4)+". ";

		sentance += "A "+word(words.adverb6)+" "+word(words.verb5)+" "+word(words.adjective5)+" "+word(words.adverb7)+" "+getHisHer(character1Gender)+" "+word(words.nounThing2)+" and "+getHeShe(character1Gender)+" "+word(words.verbPhrase3)+". ";
				
		sentance += getcapHeShe(character1Gender)+" could see a "+word(words.verb6)+" of "+word(words.nounThing3)+" "+word(words.adverb8)+" "+word(words.preposition1)+" the "+word(words.verbyNoun1)+".";

		return sentance;
	}
	
	
	public String getQuest(int type, String name)
	{
		String sentance = "";
		
		sentance = "The "+character1Job+" "+word(words.verbPhrase4)+" the "+word(words.adjective6)+" "+this.locationTraveledType+". ";

		sentance += getcapHeShe(character1Gender)+" "+word(words.adjective7)+" "+getHeShe(character1Gender)+" would "+word(words.verb7)+" "+getHisHer(character1Gender)+" "+word(words.verb8)+" to the "+word(words.verbPhrase5)+" in "+locationHome+". ";
				
		sentance += getcapHeShe(character1Gender)+" "+word(words.verb9)+" they "+word(words.verb10)+" any "+word(words.nounThing4)+" they could "+word(words.adjective8)+" and "+getHeShe(character1Gender)+" would "+word(words.verb11)+" a "+
					word(words.verb12)+" to the "+word(words.adjective6)+" "+this.locationTraveledType+" and "+word(words.verb13)+" to "+word(words.verb14)+".";
				
		return sentance;
	}
	
	public String getSurprise(int type, String name)
	{
		String sentance = "";
		
		sentance = word(words.conjunction1)+" "+word(words.adverb9)+" a "+word(words.adverb10)+" "+word(words.nounTime1)+" of "+word(words.verb15)+", "+this.character1+" could "+word(words.verb16)+" "+word(words.preposition2)+" the "+word(words.adjective9)+
				" "+word(words.verbyNoun1)+" and "+word(words.adverbPhrase1)+" the "+this.locationTraveledType+". ";

		sentance += "This was "+word(words.phrase2)+" a "+word(words.adjective10)+" "+this.locationTraveledType+", this was "+word(words.verbPhrase6)+". ";
				
		sentance += "There was a "+word(words.adjective11)+" "+nounBeasty+" "+word(words.adverb11)+" on the "+word(words.nounThing6)+" of the "+this.locationTraveledType+" "+word(words.verb17)+" it "+word(words.verb18)+".";

		return sentance;
	}
	
	public String getChoice(int type, String name)
	{
		String sentance = "";
		
		sentance = this.character1+" "+word(words.verb19)+" to "+word(words.verb20)+" if "+word(words.phrase3)+" but it was too "+word(words.adjective12)+" to see. ";

		sentance += this.character1+" "+word(words.verb21)+" to "+word(words.phrase4)+" to "+word(words.verb22)+" "+word(words.phrase5)+". ";
				
		sentance += "If the "+nounBeasty+" were to "+word(words.verb20)+" "+getHimHer(character1Gender)+", "+getHeShe(character1Gender)+" "+word(words.verb23)+" get "+word(words.phrase6)+". ";
		
		sentance += "But "+getHeShe(character1Gender)+" "+word(words.verb23)+" not "+word(words.verb24)+" the "+word(words.nounThing7)+" of "+word(words.phrase7)+".";

		return sentance;
	}
	
	public String getClimax(int type, String name)
	{
		String sentance = "";
		
		sentance = "As "+this.character1+" "+word(words.adverb12)+" the "+word(words.adjective13)+" "+this.locationTraveledType+", "+getHeShe(character1Gender)+" could "+word(words.verb26)+" a person "+word(words.verb27)+". ";

		sentance += getcapHeShe(character1Gender)+" "+word(words.phrase8)+" to "+word(words.verb28)+" to "+word(words.verb29)+" "+getHimHer(character2Gender)+". ";
				
		sentance += getcapHeShe(character2Gender)+" was "+word(words.verb30)+" "+word(words.phrase9)+". ";
		
		sentance += this.character1+" "+word(words.verb31)+" the "+word(words.nounThing8)+" "+word(words.verb32)+" and "+word(words.verb33)+" if there was "+word(words.phrase10)+". ";
		
		sentance += getcapHeShe(character2Gender)+" said "+getHimHer(character2Gender)+" name was "+this.character2+". ";
		
		sentance += "It was "+word(words.phrase12)+".";

		return sentance;
	}
	
	public String getReversal(int type, String name)
	{
		String sentance = "";
		
		sentance = this.character1+" and "+this.character2+" "+word(words.phrase13)+" the "+word(words.adjective13)+" "+word(words.nounThing9)+", "+word(words.adjective14)+" to "+word(words.phrase14)+" to "+word(words.nounThing10)+". ";

		sentance += "The "+nounBeasty+" had "+word(words.verb34)+" and "+word(words.phrase15)+". ";
				
		sentance += "It was a "+nounBeastyName+"! ";
		
		sentance += "The "+nounBeastyName+" "+word(words.verb35)+" to try to "+word(words.phrase16)+". ";
		
		sentance += this.character1+", "+word(words.adjective14)+" to "+word(words.verb36)+" "+this.character2+", "+word(words.phrase17)+" and "+word(words.verb37)+" at the "+this.nounBeastyName+".";

		return sentance;
	}
	
	
	public String getResolution(int type, String name)
	{
		String sentance = "";
		
		if (type == 1 && rando.nextInt(1000) == 666)
		{
			sentance += "I just wanna tell you how I'm feeling, Gotta make you understand. Never gonna give you up, Never gonna let you down, Never gonna run around and desert you. Never gonna make you cry, Never gonna say goodbye, Never gonna tell a lie and hurt you.";
		}
		else
		{
			sentance = "Just as the "+this.nounBeastyName+" "+word(words.verb19)+" to "+word(words.verb38)+" "+this.character1+", "+getHeShe(character1Gender)+" "+word(words.verb39)+" "+getHisHer(character1Gender)+" "+
					   word(words.nounThing11)+" deep into the "+this.nounBeastyName+"s' "+word(words.nounThing12)+", "+word(words.phrase18)+". ";
			
			sentance += this.character2+" was "+word(words.verb40)+" at "+word(words.phrase19)+". ";
					
			sentance += this.character1+" "+word(words.phrase20)+" "+this.character2+" and "+word(words.verb41)+" "+getHimHer(character2Gender)+", reminding "+getHimHer(character2Gender)+" that "+word(words.phrase21)+". ";
			
			sentance += "They could "+word(words.phrase22)+" to "+this.locationHome+" now.";
		}
		return sentance;
	}
}
