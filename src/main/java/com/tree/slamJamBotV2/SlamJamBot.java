package com.tree.slamJamBotV2;

import com.tree.slamJamBotV2.DnDCommands.DnD5eCommands;
import com.tree.slamJamBotV2.Vampire.Vampire;
import com.tree.slamJamBotV2.memeCommands.MemeCommands;
import com.tree.slamJamBotV2.miniGames.PointlessPoints;
import com.tree.slamJamBotV2.miniGames.games.Trivia;
import com.tree.slamJamBotV2.miniGames.games.wordScramble;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

import java.io.IOException;




/**
 * Created by Keith on 11/18/2017.
 */
public class SlamJamBot {

    public static void main(String[] args) throws IOException {










		if (args.length < 1) {
			System.out.println("Please enter the bots token as the first argument e.g java -jar thisjar.jar tokenhere");
			return;
		}

		DiscordApi api = new DiscordApiBuilder().setToken(args[0]).login().join();
		//ActivityUpdater activityUpdater = new ActivityUpdater(api);

			api.addListener(new ActivityUpdater(api));

			api.addListener(new SillyCommands());
			api.addListener(new  RealCommands());
			api.addListener(new DnD5eCommands());
			api.addListener(new Vampire());
			api.addListener(new SmiteCommands());

			MemeCommands memeCommands = new MemeCommands();
			memeCommands.initLoadAll(api);
			api.addListener(memeCommands);

			PointlessPoints pointlessPoints = new PointlessPoints();
			api.addListener(pointlessPoints);
			api.addListener(new wordScramble(pointlessPoints));
			api.addListener(new Trivia(pointlessPoints));
		FallbackLoggerConfiguration.setDebug(true);

//		Stonks stonks = new Stonks(pointlessPoints,api);
//		api.addListener(stonks);





	}
}
