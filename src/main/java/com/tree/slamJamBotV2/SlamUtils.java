package com.tree.slamJamBotV2;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Keith on 11/18/2017.
 */
public class SlamUtils {


    static void sendMessage(IChannel channel, String message){
        // This might look weird but it'll be explained in another page.
        RequestBuffer.request(() -> {
            try{
                channel.sendMessage(message);
            } catch (DiscordException e){
                System.err.println("Message could not be sent with error: ");
                e.printStackTrace();
            }
        });
    }


    static void sendFile(IChannel channel, File file){
        // This might look weird but it'll be explained in another page.
        RequestBuffer.request(() -> {
            try{
                channel.sendFile(file);
            } catch (DiscordException | FileNotFoundException e){
                System.err.println("File could not be sent with error: ");
                e.printStackTrace();
            }
        });
    }


    static void sendFileWithMessage(IChannel channel, String message,File file){
        // This might look weird but it'll be explained in another page.
        RequestBuffer.request(() -> {
            try{
                channel.sendMessage(message);
                channel.sendFile(file);
            } catch (DiscordException | FileNotFoundException e){
                System.err.println("File could not be sent with error: ");
                e.printStackTrace();
            }
        });
    }

}
