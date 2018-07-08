package com.tree.slamJamBotV2;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
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
	public static void sendMessage(IChannel channel, String message, String error){
		error = "Message could not be sent with error: ";
		String finalError = error;
		RequestBuffer.request(() -> {
			try{

				channel.sendMessage(message);
			} catch (DiscordException e){
				System.err.println(finalError);
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
                System.err.println(file.getAbsoluteFile());
                e.printStackTrace();
            }
        });
    }


    public static void sendFileWithMessage(IChannel channel, String message,File file){
        RequestBuffer.request(() -> {
            try{
                channel.sendFile(message,file);

            } catch (DiscordException | FileNotFoundException e){
                System.err.println("File could not be sent with error: ");
                e.printStackTrace();
            }
        });
    }

	private static int rollDie(int amount, int die){


    	return 0;
	}
    public static long sendEmbed(IChannel channel, EmbedObject embed) {
        final long[] messageID = new long[1];

        RequestBuffer.request(() -> {
            try{

				long t = channel.sendMessage(embed).getLongID();


				messageID[0] = t;

            } catch (DiscordException e){
                System.err.println("File could not be sent with error: ");
                e.printStackTrace();
            }
        }).get();

        return messageID[0];												//returns 0
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

	public static String readUrl(String urlString) {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
