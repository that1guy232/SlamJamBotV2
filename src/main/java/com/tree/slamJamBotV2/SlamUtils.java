package com.tree.slamJamBotV2;

import com.vdurmont.emoji.EmojiManager;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.obj.Reaction;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by Keith on 11/18/2017.
 */
public class SlamUtils {




    static void sendMessage(IChannel channel, String message){
        //
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
        RequestBuffer.request(() -> {
            try{
                channel.sendFile(message,file);

            } catch (DiscordException | FileNotFoundException e){
                System.err.println("File could not be sent with error: ");
                e.printStackTrace();
            }
        });
    }


    static void sendEmbed(IChannel channel, EmbedObject embed) {
        RequestBuffer.request(() -> {
            try{
                channel.sendMessage(embed);

            } catch (DiscordException e){
                System.err.println("File could not be sent with error: ");
                e.printStackTrace();
            }
        });
    }
/*
[5:33 AM] dic: it's probably the issue but the id of the message on discords side likely won't be present every time immediately after, you should only have one RLE throwing method inside a request buffer at one time, the better way to do it is to send the message in one buffer, call .get on that request() to block until the message has been sent, get the id, then for each emoji do your thing, each should be in seperate requestbuffers
[5:34 AM] dic: what's probably happening is that it does the sendmessage, works fine, then one of the addreaction methods throws a RLE because you're doing them too fast, then it re-executes the entire request block, sending the message again
 */
    static long sendEmbedWithReactions(IChannel channel, EmbedObject embed) {
        final long[] messageID = new long[1];
        RequestBuffer.request(() -> {
            try{
                messageID[0] = channel.sendMessage(embed).getLongID();
            } catch (DiscordException e){
                System.err.println("File could not be sent with error: ");
                e.printStackTrace();
            }
        }).get();
        System.err.println(messageID[0]);
        return messageID[0];

    }
}
