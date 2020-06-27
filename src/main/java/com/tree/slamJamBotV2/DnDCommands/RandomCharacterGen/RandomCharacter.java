package com.tree.slamJamBotV2.DnDCommands.RandomCharacterGen;



import static java.util.concurrent.ThreadLocalRandom.current;

public class RandomCharacter {




	String Race;
	String PClass;
	int Str;
	int Dex;
	int Con;
	int Int;
	int Wis;
	int Cha;
	String Whyclass;
	String Age;
	String[] LifeEvents;



	String[] Background;
	private static final String[] Races = {"Mountain Dwarf","Hill Dwarf","High Elf","Dark Elf","Wood Elf","Lightfoot Halfling","Stout Halfling","Human","Dragonborn","Forest Gnome","Rock Gnome","Half-Elf","Half-Orc","Tiefling"};
	private static final String[] Classes = {"Barbarian","Bard","Cleric","Druid","Fighter","Monk","Paladin","Ranger","Rogue", "Sorcerer","Warlock","Wizard"};
	public RandomCharacter() {
		CharacterBackgrounds characterBackgrounds = new CharacterBackgrounds();
		Background = characterBackgrounds.getRandomBackGround();
		Race = Races[current().nextInt(0,Races.length-1)];
		PClass = Classes[current().nextInt(0,Classes.length-1)];
		genStats();
		incressAbiltysBasedOnRace();
		Whyclass = genWhyclass();
		Age = genAge();
		if(LifeEvents.length == 1){
			LifeEvents[0] = genLifeEvent();
		}else {
			for (int i = 0; i < LifeEvents.length - 1; i++) {
				LifeEvents[i] = genLifeEvent();
			}
		}


	}

	private String genLifeEvent() {
		//int i = LifeEvents.length;

		int roll = current().nextInt(1,100);
		if (roll <= 10){
			return getTragedie();
		}else if (roll <= 20){
			return  getBoon();
		}else if(roll <= 30) {
			return  "You fell in love or got married. lfyou get this result more than once, you can choose to have a child instead. Work with your DM to determine the identity of your love interest.";
		}else if (roll <= 40){
			return  "You made an enemy of an adventurer. Roll a d6.("+current().nextInt(1,6)+") An odd number indicates you are to blame for the rift, and an even number indicates you are blameless. Work with your DM to determine this hostile character's identity and the danger this enemy poses to you.";
		}else if (roll <= 50) {
			return  "You made a friend of an adventurer. Work with your DM to add more detail to this friendly character and establish how your friendship began";
		}else if (roll <= 70){
			return "You Spent time working in a job related to your background. Start the game with an extra "+current().nextInt(2,12)+" gp.";
		}else if (roll <= 75){
			return "You met someone important. Work out additional details with your DM as needed to fit this character into your backstory.";
		}else if (roll <= 80){
			return  getAdventure();
		}else if (roll <= 85){
			return  getSupernaturalexp();
		}else if (roll <= 90){
			return 	getBattle();
		}else if (roll <= 95){
			return getCrime();
		}else if (roll <= 99){
			return  getSomethingMagical();
		}else{
			return getSomethingWeird();
		}
	}

	private String getSomethingWeird() {
		int roll = current().nextInt(1,12);
		switch (roll){
			case 1:
				return "You were turned into a toad and remained in that form for "+current().nextInt(1,4)+" weeks.";
			case 2:
				return "You were petrified and remained a stone statue for a time until someone freed you.";
			case 3:
				return "You were enslaved by a hag, a satyr, or some other being and lived in that creature’s thrall for "+current().nextInt(1,6)+" years";
			case 4:
				return "A dragon held you as a prisoner for "+current().nextInt(1,4)+" months until adventurers killed it.";
			case 5:
				return "You were taken captive by a race ofevil humanoids such as drow, kuo-toa, or quaggoths. You lived as a slave in the Underdark until you escaped.";
			case 6:
				return "You served a powerful adventurer as a hireling. You have only recently left that service. Work with your DM to determine the basic details about your former employer.";
			case 7:
				return "You went insane for "+current().nextInt(1,6)+" years and recently regained your sanity. A tic or some other bit of odd behavior might linger.";
			case 8:
				return "A lover of yours was secretly a silver dragon.";
			case 9:
				return "You were captured by a cult and nearly sacrificed on an altar to the foul being the cultists served. You escaped, but you fear they will find you.";
			case 10:
				return "You met a demigod, an archdevil, an archfey, a demon lord, or a titan, and you lived to tell the tale.";
			case 11:
				return "You were swallowed by a giant fish and spent a month in its gullet before you escaped.";
			default:
				return "A powerful being granted you a wish, but you squandered it on something frivolous";

		}
	}

	private String getSomethingMagical() {
		int roll = current().nextInt(1,10);
		StringBuilder sb = new StringBuilder();
		switch (roll){
			case 1:
				return "You were charmed or frightened by a spell.";
			case 2:
				return "You were injured by the effect ofa spell.";
			case 3:
				return "You witnessed a powerful spell being cast by a cleric, a druid, a sorcerer, a warlock, or a wizard.";
			case 4:
				return "You drank a potion (of the DM's choice).";
			case 5:
				return "You found a spell scroll (of the DM's choice) and succeeded in casting the Spell it contained.";
			case 6:
				return "You were affected by teleportation magic.";
			case 7:
				return "You turned invisible for a time.";
			case 8:
				return "You identified an illusion for what it was.";
			case 9:
				return "You saw a creature being conjured by magic.";
			default:
				sb.append("Your fortune was read by a diviner. ");
				sb.append("Let the DM pick on of the following events as a portent of your future (which might or might not comte ture). \n");
				sb.append("1." + genLifeEvent() + "\n");
				sb.append("2." + genLifeEvent());
				return sb.toString();
		}

	}

	private String getCrime() {
		int roll = current().nextInt(1,8);
		switch (roll){
			case 1:
				return "You committed murder or were wrongly accused of doing so";
			case 2:
				return "You committed theft or were wrongly accused of doing so";
			case 3:
				return "You committed burglary or were wrongly accused of doing so";
			case 4:
				return "You assaulted someone or were wrongly accused of doing so";
			case 5:
				return "You smuggled or were wrongly accused of doing so";
			case 6:
				return "You kidnapped or were wrongly accused of doing so";
			case 7:
				return "You committed extortion or were wrongly accused of doing so";
			default:
				return "You counterfeited or were wrongly accused of doing so";
		}
	}

	private String getBattle() {
		StringBuilder sb = new StringBuilder();
		sb.append("You fought in a battle. ");

		int roll = current().nextInt(1,12);
		if(roll == 1){
			sb.append("You were knocked out and left for dead. You woke up hours later with no recollection of the battle.");
		}else if (roll <= 3 ){
			sb.append("You were badly injured in the fight, and you still bear the awful scars of those wounds.");
		}else if(roll == 4){
			sb.append("You ran away from the battle to save your life, but you still feel shame for your cowardice.");
		}else if(roll <= 7){
			sb.append("You suffered only minor injuries, and the wounds all healed without leaving scars.");
		}else if(roll <= 9){
			sb.append("You survived the battle, but you suffer from terrible nightmares in which you relive the experience.");
		}else if(roll <= 11){
			sb.append("You escaped the battle unscathed, though many of your Friends were injured or lost.");
		}else {
			sb.append("You acquitted your self well in battle and are remembered as a hero. You might have received a medal for your bravery.");
		}


		return sb.toString();
	}

	private String getSupernaturalexp() {
		int roll = current().nextInt(1,100);
		if(roll <= 5){
			int years = current().nextInt(1,6);
			return "You were ensorcelled by a fey and enslaved for "+years+"years before you escaped.";
		}else if(roll <= 10){
			return "You saw a demon and ran away before it could do   " +
					"anything to you";
		}else if(roll <= 15){
			return "A devil tempted you. Make a DC 10 Wisdom saving   " +
					"throw. On a failed save, your alignment shifts one   " +
					"step toward evil (if it‘s not evil already), and you   " +
					"start the game with an additional 1d20 + 50 gp.";
		}else if(roll <= 20){
			return "You woke up one morning miles from your home,   " +
					"with no idea how you got there.";
		}else if(roll <= 30){
			return "You visited a holy site and felt the presence ofthe   " +
					"divine there.";
		}else if(roll <= 40){
			return "You witnessed a falling red star, a face appearing in   " +
					"the frost, or some other bizarre happening. You are   " +
					"certain that it was an omen of some sort.";
		}else if(roll <= 50){
			return "You escaped certain death and believe it was the   " +
					"intervention ofa god that saved you.";
		}else if(roll <= 60){
			return "You witnessed a minor miracle.";
		}else if(roll <= 70){
			return "You explored an empty house and found it to be   " +
					"haunted.";
		}else if(roll <= 75){
			int possedBy = current().nextInt(1,6);
			switch (possedBy){
				case 1:
					return "You were briefly possessed by a celestial";
				case 2:
					return "You were briefly possessed by a devil";
				case 3:
					return "You were briefly possessed by a demon";
				case 4:
					return "You were briefly possessed by a fey";
				case 5:
					return "You were briefly possessed by a elemental";
				default:
					return "You were briefly possessed by a undead";
			}

		}else if(roll <= 80){
			return "You saw a ghost.";
		}else if(roll <= 85){
			return "You saw a ghoul feeding on a corpse.";
		}else if(roll <= 90){
			return "A celestial or a fiend visited you in your dreams to   " +
					"give a warning of dangers to come.";
		}else if(roll <= 95){
			return "You briefly visited the Feywild or the Shadowfell.";
		}else{
			return "You saw a portal that you believe leads to another   " +
					"plane of existence.";
		}
	}

	private String getAdventure() {
		int roll = current().nextInt(1,100);
		if (roll <= 10){
			return "You nearly died. You have nasty scars on your " +
					"body, and you are missing an ear, "+current().nextInt(1,3)+" fingers, or "+current().nextInt(1,4)+" toes.";
		}else if(roll <= 20){
			return "You suffered a grievous injury. Although the wound " +
					"healed, it still pains you from time to time.";
		}else if(roll <= 30){
			return "You were wounded, but in time you fully recovered.";
		}else if(roll <= 40){
			return "You contracted a disease while exploring a filthy   " +
					"warren. You recovered from the disease, but you   " +
					"have a persistent cough, pockmarks on your skin,   " +
					"or prematurely gray hair.";
		}else if(roll <= 50){
			return "You were poisoned by a trap or a monster. You recovered,   " +
					"but the next time you must make a saving   " +
					"throw against poison, you make the saving throw   " +
					"with disadvantage.";
		}else if(roll <= 60){
			return "You lost something of sentimental value to you   " +
					"during your adventure. Remove one trinket from   " +
					"your possessions.";
		}else if(roll <= 70){
			return "You were terribly frightened by something you encountered and ran away, abandoning your companions   " +
					"to their fate.";
		}else if(roll <= 80){
			return "You learned a great deal during your adventure.   " +
					"The next time you make an ability check or a saving   " +
					"throw, you have advantage on the roll.";
		}else if(roll <= 90){
			return "You found some treasure on your adventure. You   " +
					"have "+current().nextInt(2,12)+" gp left from your share of it.";
		}else if(roll <= 99){
			int gp = current().nextInt(1,20)+50;
			return "You found a considerable amount of treasure on   " +
					"your adventure. You have "+gp+" gp left from   " +
					"your share of it";
		}else {
			return "You came across a common magic item (of the   " +
					"DM’s choice).";
		}
	}

	private String getBoon() {
		int roll = current().nextInt(1,10);
		switch (roll) {
			default: return "ERROR";
			case 1: return "A friendly wizard gave you a spell scroll containing " +
					"one cantrip (of the DM's choice).";
			case 2: return "You saved the life of a commoner, who now owes " +
					"you a life debt. This individual accompanies you on " +
					"your travels and performs mundane tasks for you, " +
					"but will leave if neglected, abused, or imperiled. " +
					"Determine details about this character by  working with your DM.";
			case 3: return "You found a riding horse.";
			case 4: return "You found some money. You have "+current().nextInt(1,20)+"gp in addi" +
					"tion to your regular starting funds.";
			case 5: return "A relative bequeathed you a simple weapon of your " +
					"choice.";
			case 6: return "You found something interesting. You gain one " +
					"additional trinket.";
			case 7: return "You once performed a service for a local temple. " +
					"The next time you visit the temple, you can receive " +
					"healing up to your hit point maximum.";
			case 8: return "A friendly alchemist gifted you with a potion of " +
					"healing or a flask of acid, as you choose.";
			case 9: return "You found a treasure map.";
			case 10: return "A distant relative left you a stipend that enables " +
					"you to live at the comfortable lifestyle for "+ current().nextInt(1,20)+
					" years. lf you choose to live at a higher lifestyle, you " +
					"reduce the price of the lifestyle by 2 gp during that " +
					"time period.";
		}
	}

	private String getTragedie() {
		int roll = current().nextInt(1,12);
		switch (roll){
			case 3:
				return "A friendship ended bitterly, and the other person " +
						"is now hostile to you. The cause might have been a " +
						"misunderstanding or something you or the former " +
						"friend did.";
			case 4:
				return "You lost all your possessions in a disaster, and you " +
						"had to rebuild your life.";

			case 5:
				return "You were imprisoned for a crime you didn’t commit " +
						"and spent "+current().nextInt(1,6)+" years at hard labor, in jail, or " +
						"shackled to an oar in a slave galley.";

			case 6:
				return "War ravaged your home community, reducing everything " +
						"to rubble and ruin. in the aftermath, you " +
						"either helped your town rebuild or moved somewhere " +
						"else.";

			case 7:
				return "A lover disappeared without a trace. You have been " +
						"looking for that person ever since.";

			case 8:
				return "A terrible blight in your home community caused " +
						"crops to fail, and many starved. You lost a sibling " +
						"or some other family member.";

			case 9:
				return "You did something that brought terrible shame to " +
						"you in the eyes of your family. You might have been " +
						"involved in a scandal, dabbled in dark magic, or " +
						"offended someone important. The attitude of your " +
						"family members toward you becomes indifferent at " +
						"best, though they might eventually forgive you.";

			case 10:
				return "For a reason you were never told, you were exiled " +
						"from your community. You then either wandered in " +
						"the wilderness for a time or promptly found a new " +
						"place to live.";

			case 11:
				return "A romantic relationship ended. Roll 3d6 ("+ current().nextInt(3,18)+") An odd " +
						"number means it ended with bad feelings, while an " +
						"even number means it ended amicably.";

			case 12:
				String cause = getCauseOfDeath();
				String tragedy = "A current or prospective romantic partner of yours " +
						"died. because of "+ cause;
				if(cause.equals("Murder")){
					int x = current().nextInt(1,12);
					if(x == 1){
						tragedy = "A current or prospective romantic partner of yours " +
								"died. because of "+ cause + " You were either directly or indirectly responsible. ";
					}
				}
				return tragedy;


			default:
				return "A family member or a close friend died because of " + getCauseOfDeath();
		}
	}

	private String getCauseOfDeath() {
		int roll = current().nextInt(1,12);
		switch (roll){
			case 1:
				return "Unknown";
			case 2:
				return "Murdered";

			case 3:
				return "Killed in battle";

			case 4:
				return "Accident related to class or occupation";

			case 5:
				return "Accident unrelated to class or occupation";

			case 8:
				return "Apparent suicide";

			case 9:
				return "Torn apart by an animal or a natural disaster";

			case 10:
				return "Consumed by a monster";

			case 11:
				return "Executed for a crime or tortured to death";

			case 12:
				return "Bizarre event, such as being hit by a meteorite, " +
						"struck down by an angry god, or killed by a hatching " +
						"slaad egg";

			default:
				return "natural causes, such as disease or old age";
		}
	}


	private String genAge() {
		int roll = current().nextInt(1,100);
		if (roll <= 20){
			LifeEvents = new String[1];
			return "20 years or younger";
		}else if(roll <= 59){
			LifeEvents = new String[current().nextInt(1,4)];
			return "21-30 years";
		}else if(roll <= 69){
			LifeEvents = new String[current().nextInt(1,6)];
			return "31-40 years";
		}else if(roll <= 89){
			LifeEvents = new String[current().nextInt(1,8)];
			return "41-50 years";
		}else if(roll <=99){
			LifeEvents = new String[current().nextInt(1,10)];
			return "51-60 years";
		}else {
			LifeEvents = new String[current().nextInt(1,12)];
			return "61 years or older";
		}
	}

	private String genWhyclass() {
		StringBuilder sb = new StringBuilder();
		if(PClass.equals("Barbarian")){
			sb.append("I became a barbarian because ");
			switch (current().nextInt(0,5)){
				case 0:
					sb.append("my devotion to my people lifted me in battle, making me powerful and dangerous.");
					break;
				case 1:
					sb.append("the spirits of my ancestors called on me to carry out a great task");
					break;
				case 2:
					sb.append("I lost control in battle one day, and it was as if something else was manipulating my body, forcing it to kill every foe I could reach.");
					break;
				case 3:
					sb.append("I went on a spiritual journey to find myself and instead found a spirit animal to guide, protect and inspire me.");
					break;
				case 4:
					sb.append("I was struck by lightning and lived. Afterward, I found new strength within me that let me push beyond my limitation.");
					break;
				case 5:
					sb.append("my anger needed to be channeled into battle, or I risked becoming an indiscriminate killer.");
					break;
			}
		}
		if(PClass.equals("Bard")){
			sb.append("I became a bard because ");
			switch (current().nextInt(0,5)){
				case 0:
					sb.append("I awakened my latent bardic abilities through trial and error.");
					break;
				case 1:
					sb.append("I was gifted performer and attracted the attention of a master bard who schooled me in the old techniques.");
					break;
				case 2:
					sb.append("I joined a loose society of scholars and orators to learn new techniques of performance and magic.");
					break;
				case 3:
					sb.append("I felt a calling to recount the deeds of champions and heroes, to bring them alive in song and story.");
					break;
				case 4:
					sb.append("I joined one of the great colleges to learn old lore, the secrets of magic and the art of performance.");
					break;
				case 5:
					sb.append("I picked up a musical instrument one day instantly discovered that i could play it.");
					break;
			}
		}
		if(PClass.equals("Cleric")){
			sb.append("I became a cleric because ");
			switch (current().nextInt(0,5)){
				case 0:
					sb.append("A supernatural being in service to the gods called me to become a divine agent in the world.");
					break;
				case 1:
					sb.append("I saw the injustice and horror in the world and felt moved to take a stand against them.");
					break;
				case 2:
					sb.append("my god gave me an unmistakable sigh. I dropped everything to serve the divine.");
					break;
				case 3:
					sb.append("although i was always devout, it wasn't until i completed a pilgrimage that i knew my true calling.");
					break;
				case 4:
					sb.append("I used to serve in my religion's bureaucracy but found I needed to work in the world, to bring the message of my faith to the darkest corners of the land.");
					break;
				case 5:
					sb.append("I relize that my god works through me, and I do as commanded, even though I don't know why i was chosen to serve.");
					break;
			}
		}
		if(PClass.equals("Druid")){
			sb.append("I became a druid because ");
			switch (current().nextInt(0,5)){
				case 0:
					sb.append("I saw to much devastation in the wild places, too much of nature's splendor ruined by the despoilers. I joined a circle of druids to fight back against the enemies of nature.");
					break;
				case 1:
					sb.append("I found a place among a group of druids after I fled a catastrophe.");
					break;
				case 2:
					sb.append("I have always had an affinity for animals, so I explored my talent to see how I could best use it.");
					break;
				case 3:
					sb.append("I befriended a druid and was moved by druidic teachings. I decided to follow my friend's guidance and something back to the world.");
					break;
				case 4:
					sb.append("while I was growing up, I saw spirits all around me entites no one else could perceive. I sought out the druids to help me understand the visions and communicate with these beings.");
					break;
				case 5:
					sb.append("I have always felt disgust for creatures of unnatural origin. For this reason, I immersed myself in the study of druidic mysteries and became a champion of the natural order.");
					break;
			}
		}
		if(PClass.equals("Fighter")){
			sb.append("I became a fighter because ");
			switch (current().nextInt(0,5)){

				case 0:
					sb.append("I wanted to hone my combat skills, and so I joined a war college.");
					break;
				case 1:
					sb.append("I squired for a knight who taught me how to fight, care for a steed and conduct myself with honor. I decided to take up that path for myself.");
					break;
				case 2:
					sb.append("Horrible monsters descended on my community, killing someone I loved. I took up arms to destroy those creatures and others of a similar nature.");
					break;
				case 3:
					sb.append("I joined the army and learned how to fight as part of a group.");
					break;
				case 4:
					sb.append("I grew up fighting, and I refined my talents by defending myself against people who crossed me. I could always pick up just about any weapon and make know how to use it effectively.");
					break;
				case 5:
					sb.append("I could always pick up just about any weapon and know how to use it effectively.");
					break;


			}
		}
		if(PClass.equals("Monk")){
			sb.append("I became a monk because ");
			switch (current().nextInt(0,5)){
				case 0:
					sb.append("I was chosen to study at a secluded monastery. There, I was taught the fundamental techniques required to eventfully master a tradition.");
					break;
				case 1:
					sb.append("I sought instruction to gain a deeper understanding of existence and my place in the world.");
					break;
				case 2:
					sb.append("I stumbled into a portal to the Shadowfell and took refuge at a strange monastery, were i learned how to defend myself against the forces of darkness.");
					break;
				case 3:
					sb.append("I was overwhelmed with grief after losing someone close to me, and i sought the advice of philosophers to help me cope with my loss.");
					break;
				case 4:
					sb.append("I could feel that special sort of pwer lay within me, so i sought out those who could help me call it forth and master it.");
					break;
				case 5:
					sb.append("I was wild and undisciplined as a youngster, but then I realized the error of my ways. I applied to a monastery and became a monk as a way to live a live of discipline.");
					break;
			}
		}
		if(PClass.equals("Paladin")){
			sb.append("I became a paladin because ");
			switch (current().nextInt(0,5)){
				case 0:
					sb.append("A fantastical being appeared before me and called on me to undertake a holy quest.");
					break;
				case 1:
					sb.append("one of my ancestors left a holy quest unfulfilled, so I intend to finish that work.");
					break;
				case 2:
					sb.append("The world is a dark and terrible place. I decided to serve as a beacon of light shining out against the gathering shadows.");
					break;
				case 3:
					sb.append("I served as a paladin's squire, learning all I needed to swear my own sacred oath.");
					break;
				case 4:
					sb.append("Evil must be opposed on all fronts. I feel compelled to seek out wickedness and purge it from the world.");
					break;
				case 5:
					sb.append("becoming a paladin was natural consequence of my unwavering faith. In taking my vows, I became the holy sword of my religion.");
					break;
			}
		}
		if(PClass.equals("Ranger")){
			sb.append("I became a ranger because ");
			switch (current().nextInt(0,5)){
				case 0:
					sb.append("I found purpose while I honed my hunting skills by bringing down dangerous animals at the edge of civilization.");
					break;
				case 1:
					sb.append("I always had a way with animals, able to calm them with a soothing word and a touch.");
					break;
				case 2:
					sb.append("I suffer from terrible wanderlust, so being a ranger game me reason not to remain in one place for to long.");
					break;
				case 3:
					sb.append("I have seen what happens when the monsters come out from the dark. I took it upon myself to become the first line of defense against the evils that lie beyond civilization's borders.");
					break;
				case 4:
					sb.append("I met a grizzled ranger who taught me woodcraft and the secrets of the wild lands.");
					break;
				case 5:
					sb.append("I served in an army, learning the precepts of my profession while blazing trails and scouting enemy encampments.");
					break;
			}
		}
		if(PClass.equals("Rogue")){
			sb.append("I became a rouge because ");
			switch (current().nextInt(0,5)){
				case 0:
					sb.append("I've always been nimble and quick of wit, so I decided to use those talents to help me make my way in the world.");
					break;
				case 1:
					sb.append("an assassin or thief wronged me, so I focused my training on mastering the skills of my enemy to better combat foes of that sort.");
					break;
				case 2:
					sb.append("I decided to turn my natural lucky streak into the basis of a career, though I still realize that improving my skills is essential.");
					break;
				case 3:
					sb.append("An experienced rouge saw something in me and taught me several useful tricks.");
					break;
				case 4:
					sb.append("I took up with a group of ruffians who showed me how to get what I want through sneakiness rather then direct confrontation.");
					break;
				case 5:
					sb.append("I'm a sucker for a shiny bauble or a sack of coins, as long as I can get my hands on it without risking life and limb.");
					break;
			}
		}
		if(PClass.equals("Sorcerer")){
			sb.append("I became a sorcerer because ");
			switch (current().nextInt(0,5)){
				case 0:
					sb.append("when I was born, all the water in the house froze solid, the mild spoiled, or all the iron turned to copper. My family is convinced that this event was a harbinger of stranger things to come for me.");
					break;
				case 1:
					sb.append("I suffered a terrible emotional or physical strain, which brought forth my latent magical power. I have fought to control it ever since.");
					break;
				case 2:
					sb.append("my immediate family never spoke of my ancestors, and when I asked, they would change the subject. It wasn't until I started displaying strange talents that the full truth of my heritage came out.");
					break;
				case 3:
					sb.append("when a monster threatened one of my friends, I became filled with anxiety. I lashed out instinctively and blasted the wretched thing with a force that cam from within me.");
					break;
				case 4:
					sb.append("sensing something special in me, a stranger taught me how to control my gift.");
					break;
				case 5:
					sb.append("after I escaped from a magical conflagration, I realized that though I was unharmed, I was not unchanged. I began to exhibit unusual abilities that I am just beginning to understand.");
					break;
			}
		}
		if(PClass.equals("Warlock")){
			sb.append("I became a warlock because ");
			switch (current().nextInt(0,5)){
				case 0:
					sb.append("while wandering around in a forbidden place, I encountered an otherworldly being that offered to enter into a pact with me.");
					break;
				case 1:
					sb.append("I was examining a stranger tome I found in an abandoned library when the entity that would become my patron suddenly appeared before me.");
					break;
				case 2:
					sb.append("I stumbled into the clutches of my patron after I accidentally stepped through a magical doorway.");
					break;
				case 3:
					sb.append("when faced with a terrible crisis, I prayed to any being who would listen, and the creature that answered me became my patron.");
					break;
				case 4:
					sb.append("my future patron visited me in my dreams and offered great power in exchange for my service. ");
					break;
				case 5:
					sb.append("one of my ancestors had a pack with my patron so that entity was determined to bind me to the same agreement.");
					break;
			}
		}
		if(PClass.equals("Wizard")){
			sb.append("I became a wizard because ");
			switch (current().nextInt(0,5)){
				case 0:
					sb.append("an old wizard chose me from among several candidates to serve an apprenticeship.");
					break;
				case 1:
					sb.append("when I became lost in a forest, a hedge wizard found me, took me in, and taught me the rudiments of magic.");
					break;
				case 2:
					sb.append("I grew up listening to tales of great wizards and knew I wanted to follow their path. I strove to be accepted at an academy of magic and succeeded.");
					break;
				case 3:
					sb.append("One of my relatives was an accomplished wizard who decided i was smart enough to learn the craft.");
					break;
				case 4:
					sb.append("while exploring an old tomb, library, or temple, I found a spellbook. I was immediately driven to learn all I could about becoming a wizard.");
					break;
				case 5:
					sb.append("was a prodigy who demonstrated mastery of the arcane arts at an early age. When I became old enough to set out on my own, I did so to learn more magic and expand my power.");
					break;
			}
		}
		return sb.toString();
	}

	private void genStats() {
		Str = current().nextInt(3,18);//3d6
		Dex = current().nextInt(3,18);//3d6
		Con = current().nextInt(3,18);//3d6
		Int = current().nextInt(3,18);//3d6
		Wis = current().nextInt(3,18);//3d6
		Cha = current().nextInt(3,18);//3d6
	}

	private void incressAbiltysBasedOnRace() {
		if(Race.equals("Mountain Dwarf")){
			Str += 2;
			Con+=2;
		}
		if(Race.equals("Hill Dwarf")){
			Wis +=1;
			Con+=2;
		}
		if(Race.equals("High Elf")){
			Int+=1;
			Dex+=2;
		}
		if(Race.equals("Dark Elf")){
			Cha+=1;
			Dex+=2;
		}
		if(Race.equals("Wood Elf")){
			Wis+=1;
			Dex+=2;
		}
		if(Race.equals("Lightfoot Halfling")){
			Cha+=1;
			Dex+=2;
		}
		if(Race.equals("Stout Halfling")){
			Con+=1;
			Dex+=2;
		}
		if(Race.equals("Human")){
			Str+=1;
			Dex+=1;
			Con+=1;
			Int+=1;
			Wis+=1;
			Cha+=1;
		}
		if(Race.equals("Dragonborn")){
			Str+=2;
			Con+=1;
		}
		if(Race.equals("Forest Gnome")){
			Int+=2;
			Dex+=1;
		}
		if(Race.equals("Rock Gnome")){
			Int+=2;
			Con+=1;
		}
		if(Race.equals("Half-Elf")){
			Cha+=2;
			int rSkill1 = current().nextInt(0,4);
			int rSkill2 = current().nextInt(0,4);
			while (rSkill2 == rSkill1){
				 rSkill2 = current().nextInt(0,4);
			}
			randomSKill(rSkill1);
			randomSKill(rSkill2);
		}
		if(Race.equals("Half-Orc")){
			Str+=2;
			Con+=1;
		}
		if(Race.equals("Tiefling")){
			Int+=1;
			Cha+=2;
		}
	}

	private void randomSKill(int rSkill2) {
		switch (rSkill2){
			case 0:
				Str+=1;
				break;
			case 1:
				Dex+=1;
				break;
			case 2:
				Con+=1;
				break;
			case 3:
				Int+=1;
				break;
			case 4:
				Wis+=1;
				break;
		}
	}


	public String getPClass() {
		return PClass;
	}

	public int getStr() {
		return Str;
	}

	public int getDex() {
		return Dex;
	}

	public int getCon() {
		return Con;
	}

	public int getInt() {
		return Int;
	}

	public int getWis() {
		return Wis;
	}

	public int getCha() {
		return Cha;
	}
	public String getRace() {
		return Race;
	}

	public String getWhyclass() {
		return Whyclass;
	}

	public String getAge() {
		return Age;
	}

	public String[] getLifeEvents() {
		return LifeEvents;
	}
	public String[] getBackground() {
		return Background;
	}
}
