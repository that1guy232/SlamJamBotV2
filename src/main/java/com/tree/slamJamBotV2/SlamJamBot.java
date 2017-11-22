package com.tree.slamJamBotV2;

import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.Discord4JHandler;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

/**
 * Created by Keith on 11/18/2017.
 */
public class SlamJamBot {

    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("Please enter the bots token as the first argument e.g java -jar thisjar.jar tokenhere");
            return;
        }



        IDiscordClient client = new ClientBuilder().withToken(args[0]).setMaxReconnectAttempts(Integer.MAX_VALUE).build();

        client.changePlayingText("with meme's");
        //client.getDispatcher().reg

        CommandHandler commandHandler = new Discord4JHandler(client);

        client.getDispatcher().registerListener(new SillyCommands());
      //  client.getDispatcher().registerListener(new RealCommands());
        commandHandler.registerCommand(new RealCommands());



        client.login();



    }
}
