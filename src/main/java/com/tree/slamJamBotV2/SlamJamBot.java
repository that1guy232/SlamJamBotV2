package com.tree.slamJamBotV2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tree.slamJamBotV2.DnDCommands.DnD5eCommands;
import com.tree.slamJamBotV2.SmiteCommands.SmiteCommands;
import com.tree.slamJamBotV2.memeCommands.MemeCommands;
import com.tree.slamJamBotV2.miniGames.PointlessPoints;
import com.tree.slamJamBotV2.miniGames.games.Trivia;
import com.tree.slamJamBotV2.miniGames.games.wordScramble;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.StatusType;

/**
 * Created by Keith on 11/18/2017.
 */
public class SlamJamBot {

    public static void main(String[] args) {
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
        boolean test = false;
		if (test){

			Trivia trivia  = new Trivia(new PointlessPoints());
			String json = SlamUtils.readUrl("https://opentdb.com/api.php?amount=1");
			//System.err.println(json);

			//json = gson.toJson(json);
			//System.err.println(json);



		}else {


			if (args.length < 1) {
				System.out.println("Please enter the bots token as the first argument e.g java -jar thisjar.jar tokenhere");
				return;
			}


			PointlessPoints pointlessPoints = new PointlessPoints();


			IDiscordClient client = new ClientBuilder().withToken(args[0]).setMaxReconnectAttempts(Integer.MAX_VALUE).setStreamingPresence(StatusType.ONLINE, "With meme's", "google.com").build();


			client.getDispatcher().registerListener(pointlessPoints);
			client.getDispatcher().registerListener(new SillyCommands());
			//	client.getDispatcher().registerListener(new StarFinderCommands());
			client.getDispatcher().registerListener(new DnD5eCommands());
			client.getDispatcher().registerListener(new RealCommands());

			client.getDispatcher().registerListener(new Trivia(pointlessPoints));
			client.getDispatcher().registerListener(new wordScramble(pointlessPoints));
			//client.getDispatcher().registerListener(new MiniGames());

			client.getDispatcher().registerListener(new SmiteCommands());
			//client.getDispatcher().registerListener(new UserQuotes());


			client.getDispatcher().registerListener(new MemeCommands());


			client.login();


		}

	}
}
