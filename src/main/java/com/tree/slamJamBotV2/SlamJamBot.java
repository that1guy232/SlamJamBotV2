package com.tree.slamJamBotV2;

import com.tree.slamJamBotV2.DnDCommands.DnD5eCommands;
import com.tree.slamJamBotV2.DnDCommands.RandomCharacterGen.RandomCharacter;
import com.tree.slamJamBotV2.memeCommands.MemeCommands;
import com.vdurmont.emoji.EmojiManager;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.StatusType;

/**
 * Created by Keith on 11/18/2017.
 */
public class SlamJamBot {

    public static void main(String[] args) {

        boolean test = true;
		if (false){
			RandomCharacter randomCharacter = new RandomCharacter();
			System.err.println("Race: " + randomCharacter.getRace());
			System.err.println("Class: " + randomCharacter.getPClass());
			System.err.println(randomCharacter.getWhyclass());
			//int Str,Dex,Con,Int,Wis,Cha;
			System.err.println("Str:"+ randomCharacter.getStr());
			System.err.println("Dex:"+ randomCharacter.getDex());
			System.err.println("Con:"+ randomCharacter.getCon());
			System.err.println("Int:"+ randomCharacter.getInt());
			System.err.println("Wis:"+ randomCharacter.getWis());
			System.err.println("Cha:"+ randomCharacter.getCha());
			System.err.println();
			System.err.println();
			System.err.println("Age: " + randomCharacter.getAge());
			for (int i = 0; i < randomCharacter.getLifeEvents().length-1; i++) {
				System.err.println(randomCharacter.getLifeEvents()[i]);
			}
			System.err.println();
			System.err.println();
			for (int i = 0; i < randomCharacter.getBackground().length; i++) {
				System.err.println(randomCharacter.getBackground()[i]);
			}
			System.exit(1);
		}
		if(test){
			EmojiManager.getAll().iterator().forEachRemaining(System.err::println);
		}


		if (args.length < 1) {
			System.out.println("Please enter the bots token as the first argument e.g java -jar thisjar.jar tokenhere");
			return;
		}





		IDiscordClient client = new ClientBuilder().withToken(args[0]).setMaxReconnectAttempts(Integer.MAX_VALUE).setStreamingPresence(StatusType.ONLINE, "With meme's","google.com").build();


		client.getDispatcher().registerListener(new SillyCommands());
		client.getDispatcher().registerListener(new StarFinderCommands());
		client.getDispatcher().registerListener(new DnD5eCommands());
		client.getDispatcher().registerListener(new MemeCommands());
		client.getDispatcher().registerListener(new RealCommands());






		client.login();




	}
}
