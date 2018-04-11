package com.tree.slamJamBotV2;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.io.File;

/**
 * Created by Keith on 11/18/2017.
 */
public class RealCommands {


    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
    	IChannel channel = event.getChannel();
		String message = event.getMessage().getContent().toLowerCase();
        if(message.equals("get commands")){
        	File f = new File("Commands.json");
            SlamUtils.sendFile(event.getChannel(),f);
        }



    }








}
