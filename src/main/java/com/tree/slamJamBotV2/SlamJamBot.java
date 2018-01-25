package com.tree.slamJamBotV2;

import com.tree.slamJamBotV2.memeCommands.MemeCommands;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.Discord4JHandler;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

import java.io.FileNotFoundException;

/**
 * Created by Keith on 11/18/2017.
 */
public class SlamJamBot {

    public static void main(String[] args) throws FileNotFoundException {





        if(args.length < 1){
            System.out.println("Please enter the bots token as the first argument e.g java -jar thisjar.jar tokenhere");
            return;
        }


        IDiscordClient client = new ClientBuilder().withToken(args[0]).setMaxReconnectAttempts(Integer.MAX_VALUE).streaming("with meme's","https://www.google.ca/").build();

        CommandHandler commandHandler = new Discord4JHandler(client);

        client.getDispatcher().registerListener(new SillyCommands());
        client.getDispatcher().registerListener(new StarFinderCommands());
        client.getDispatcher().registerListener(new DnD5eCommands());
        client.getDispatcher().registerListener(new MemeCommands());
      //  commandHandler.registerCommand(new RainbowCommands());
        commandHandler.registerCommand(new RealCommands());

        client.login();



    }
}
