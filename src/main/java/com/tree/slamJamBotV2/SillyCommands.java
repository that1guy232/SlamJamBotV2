package com.tree.slamJamBotV2;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

import java.io.File;

/**
 * Created by Keith on 11/18/2017.
 */
public class SillyCommands {


    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        String message = event.getMessage().getContent();
        IChannel channel = event.getChannel();
        IGuild guild = event.getGuild();

        if(message.contains("AAAA")){
            SlamUtils.sendMessage(event.getChannel(),"https://www.youtube.com/watch?v=Y4Z7Ds_yv8o");
          //  joinVoice();
       //     playSound(guild,event);
           // leaveVoice();
        }
        if(message.contains("REEE")){
            SlamUtils.sendMessage(event.getChannel(),"https://youtu.be/LGgWWL9lTZs");
        }
        if(message.equalsIgnoreCase("easy breezy beautiful crocodile") || message.equalsIgnoreCase("ebbc")){
            SlamUtils.sendFile(channel,new File("corc.jpg"));
        }


    }



}
