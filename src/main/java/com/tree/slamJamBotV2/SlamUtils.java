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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Keith on 11/18/2017.
 */
public class SlamUtils {




    public static void sendMessage(IChannel channel, String message){
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



    public static void sendFile(IChannel channel, File file){

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


    static long sendEmbed(IChannel channel, EmbedObject embed) {
        final long[] messageID = new long[1];
        RequestBuffer.request(() -> {
            try{
				messageID[0] = channel.sendMessage(embed).getLongID();

            } catch (DiscordException e){
                System.err.println("File could not be sent with error: ");
                e.printStackTrace();
            }
        });
        return messageID[0];
    }
	public static boolean containsWord(String sentence, String word){
		String regex = ".*\\b" + Pattern.quote(word) + "\\b.*";
		return sentence.matches(regex);
	}

	public static String[] spiltMessage(String message){
		String regex = "\\s";
		return message.split(regex);
	}

	public static String makeSentence(ArrayList<String> strings){
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < strings.size(); i++) {
			stringBuilder.append(strings.get(i));
			if(strings.size()-1 != i){

				stringBuilder.append(" ");
			}
		}
		return stringBuilder.toString();
	}
}
