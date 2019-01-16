package com.tree.slamJamBotV2.SmiteCommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tree.slamJamBotV2.SlamUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Embed;
import sx.blah.discord.util.EmbedBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class SmiteCommands {
	Random random = new Random();

	//http://prntscr.com/lpv73j
	private Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private ArrayList<God> gods;
	private ArrayList<Item> items;
	private ArrayList<Relic> relics;
	private ArrayList<Starter> starters;
	public SmiteCommands (){
		ArrayList<God> tmpG;
		ArrayList<Item> tempI;
		ArrayList<Relic> tempR;
		ArrayList<Starter> tempS;

		try {
			FileReader fr = new FileReader("smite/Smite_Gods.JSON");
			FileReader fr2 = new FileReader("smite/Smite_Items.JSON");
			FileReader fr3 = new FileReader("smite/Smite_Relics.JSON");
			FileReader fr4 = new FileReader("smite/Smite_Starter.JSON");
			tmpG = gson.fromJson(fr,new TypeToken<ArrayList<God>>(){}.getType());
			tempI = gson.fromJson(fr2,new TypeToken<ArrayList<Item>>(){}.getType());
			tempR = gson.fromJson(fr3,new TypeToken<ArrayList<Relic>>(){}.getType());
			tempS = gson.fromJson(fr4,new TypeToken<ArrayList<Starter>>(){}.getType());
			items = tempI;
			gods = tmpG;
			relics = tempR;
			starters = tempS;


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	String[] command;
	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.withTitle("Random God!");
		command = SlamUtils.spiltMessage(event.getMessage().getContent().toLowerCase());

		Build build = null;

		God god;
		Item[] currentItems     = new Item[6];
		Relic[] currentRelics   = new Relic[2];
		Starter currentStarter;


		if(event.getMessage().getContent().toLowerCase().equals("random smite")){

		}

		if (command[0].equals("smite")) {




			if (command[1].equals("random")) {
				GodClass godClass = null;
				Type type = null;
				buildType buildtype = null;

				for (String s: command) {
					if(EnumUtils.isValidEnum(GodClass.class,s)){
						godClass = GodClass.valueOf(s);
					}
					if(EnumUtils.isValidEnum(Type.class,s)){
						type = Type.valueOf(s);
					}
					if(EnumUtils.isValidEnum(buildType.class,s)){
						buildtype = buildType.valueOf(s);
					}
				}







				if (command.length > 2) {


					if (EnumUtils.isValidEnum(GodClass.class, command[2])) {        //IF THE COMMAND ARG IS A CLASS
						god = getGodByClass();

					} else if (EnumUtils.isValidEnum(Type.class, command[2])) {     //IF THE COMMAND ARG IS A TYPE

						god = getGodByType();

					} else if (EnumUtils.isValidEnum(buildType.class, command[2])){ //IF THE COMMAND HAS A BUILDTYPE

					}else {
						build = randomAll();


					}


				}else {
					build = randomAll();

				}


				StringBuilder stringBuilder = new StringBuilder();

				for (int i = 0; i < build.items.length; i++) {
					stringBuilder.append("\n" + build.items[i].name);
				}
				String itemNames = stringBuilder.toString();

				Embed.EmbedField godField = new Embed.EmbedField(build.god.name,build.god.type + "\n" + build.god.godclass,false);
				embedBuilder.appendField(godField);
				embedBuilder.withImage(String.valueOf(build.god.image));

				embedBuilder.appendField("Relics", build.relics[0].name + "\n" + build.relics[1].name,true);

				embedBuilder.appendField("Starter Item", build.starter.name,true);


				embedBuilder.appendField("Items", String.valueOf(itemNames),false);



				SlamUtils.sendEmbed(event.getChannel(), embedBuilder.build());


				//SlamUtils.sendMessage(event.getChannel(), build.god.Info());
			}

		}
	}


	private Build randomAll() {



		God god = gods.get(random.nextInt(gods.size() - 1));
		Item[] itemsC = new Item[6];
		Relic[] relicsC = new Relic[2];
		Starter starter;


		for (int i = 0; i < 6; i++) {
			Item tmpItem= items.get(random.nextInt(items.size()-1));
			if(!tmpItem.type.equals(Type.both) && !tmpItem.type.equals(god.type)){
				i--;
			}else if(ArrayUtils.contains(itemsC,tmpItem)){
				i--;

			}else {
				itemsC[i] = tmpItem;
			}

		}






		for (int i = 0; i < relicsC.length; i++) {
			Relic r = relics.get(random.nextInt(relics.size()-1));
			if(ArrayUtils.contains(relicsC,r)){
				i--;
			}else {
				relicsC[i] = r;
			}

		}

		starter = starters.get(random.nextInt(starters.size()-1));




		return new Build(god,itemsC,relicsC,starter);

	}






	private God getGodByType() {
		Type type = Type.valueOf(command[2]);
		God god;
		switch (type){
			case magical:
				god = gods.get(random.nextInt(gods.size()-1));
				while (god.type != Type.magical){
					god = gods.get(random.nextInt(gods.size()-1));
				}
				return god;
			case physical:
				god = gods.get(random.nextInt(gods.size()-1));
				while (god.type != Type.physical){
					god = gods.get(random.nextInt(gods.size()-1));
				}
				return god;
			default: god = null;
		}
		return null;
	}

	private God getGodByClass(){
		GodClass godClass = GodClass.valueOf(command[2]);
		God god;
		switch (godClass){

			case assassin:
				 god = gods.get(random.nextInt(gods.size()-1));
				 while (god.godclass != GodClass.assassin){
					 god = gods.get(random.nextInt(gods.size()-1));
				 }
				return god;

			case guardian:
				god = gods.get(random.nextInt(gods.size()-1));
				while (god.godclass != GodClass.guardian){
					god = gods.get(random.nextInt(gods.size()-1));
				}
				return god;
			case hunter:
				god = gods.get(random.nextInt(gods.size()-1));
				while (god.godclass != GodClass.hunter){
					god = gods.get(random.nextInt(gods.size()-1));
				}
				return god;
			case mage:
				god = gods.get(random.nextInt(gods.size()-1));
				while (god.godclass != GodClass.mage){
					god = gods.get(random.nextInt(gods.size()-1));
				}
				return god;
			case warrior:
				god = gods.get(random.nextInt(gods.size()-1));
				while (god.godclass != GodClass.warrior){
					god = gods.get(random.nextInt(gods.size()-1));
				}
				return god;
				default: god = null;
		}
		return null;
	}


}


class God{
	String name;
	URL image;
	Type type;
	GodClass godclass;
	boolean heals;

	String Info(){
		return  "\n```Name: " + name  + "\n" + "ImageURL: " + image.toString() + "\nType: " + type.name() + "\nClass: " + godclass.name() + "\nHeals: " + heals + "```";
	}

}
enum Type{
	magical,physical,both
}
enum GodClass{
	assassin,guardian,hunter,mage,warrior
}


enum buildType{
	 offensive,
	 power,
	 attack_speed,
	 lifesteal,
	 penetration,
	 crit,
	 defensive,
	 physical_def,
	 magical_def,
	 health,
	 hp5,
	 ccr,
	 utility,
	 aura,
	 movement,
	 cooldown,
	 mana,
	 mp5,
	 boots,
	 melee_only
}
class Item {


	 String name;
	 String image;
	 Type type;
 	 boolean offensive;
	 boolean power;
	 boolean attack_speed;
	 boolean lifesteal;
	 boolean penetration;
	 boolean crit;
	 boolean defensive;
	 boolean physical_def;
	 boolean magical_def;
	 boolean health;
	 boolean hp5;
	 boolean ccr;
	 boolean utility;
	 boolean aura;
	 boolean movement;
	 boolean cooldown;
	 boolean mana;
	 boolean mp5;
	 boolean boots;
	 boolean melee_only;
}
class Relic {
	String name;
	String image;
}
class Starter{
	String name;
	String image;
	boolean offensive;
	boolean power;
	boolean attack_speed;
	boolean penetration;
	boolean defensive;
	boolean physical_def;
	boolean magical_def;
	boolean health;
	boolean utility;
	boolean aura;
	boolean movement;
	boolean cooldown;
	boolean mana;
	boolean mp5;

}
class Build{
	God god;
	Item[] items;
	Relic[] relics;
	Starter starter;

	public Build(God god, Item[] items, Relic[] relics, Starter starter) {
		this.god = god;
		this.items = items;
		this.relics = relics;
		this.starter = starter;
	}
}