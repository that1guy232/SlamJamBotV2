package com.tree.slamJamBotV2.memeCommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tree.slamJamBotV2.SlamUtils;
import com.vdurmont.emoji.EmojiManager;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import static com.tree.slamJamBotV2.SlamUtils.*;

public class MemeCommandsV2 {
	Map<Long, ArrayList<MemeCommand>> memeCommands;

	private Gson gson;

	public MemeCommandsV2() {
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		memeCommands = new HashMap<>();
	}


	@EventSubscriber
	public void onGuildJoinEven(GuildCreateEvent event){

		Long key = event.getGuild().getLongID();
		try {
			memeCommands.putIfAbsent(key,loadCommands(key));

		}catch (NullPointerException e){
			memeCommands.putIfAbsent(key,loadCommands(key));
		}


	}

	private ArrayList<MemeCommand> loadCommands(long key) {
		ArrayList<MemeCommand> tmp = null;
		gson = new GsonBuilder().setPrettyPrinting().create();
		FileReader fr = null;
		try {
			fr = new FileReader("commands/"+key+"Commands.json");
			tmp = gson.fromJson(fr,new TypeToken<ArrayList<MemeCommand>>(){}.getType());




		} catch (FileNotFoundException e) {
			System.err.println("Commands Failed to load creating Commands file.");
			File file = new File("commands/"+key+"Commands.json");
			try {

				file.createNewFile();


			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

		


		return tmp;


	}


	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event){

		String message = event.getMessage().getContent();
		IMessage iMessage = event.getMessage();
		String[] command = spiltMessage(message);
		Long key = event.getGuild().getLongID();
		//todo make a user need a certin permission
		if (message.startsWith("!addcommand")) {
			addCommand(event.getGuild().getLongID(),command,event);
		}
		if(message.startsWith("!removecommand")){
			removeCommand(key,command);
		}
		if(message.startsWith("!reloadcommands")){
			memeCommands.remove(key);
			memeCommands.put(key,loadCommands(key));
		}
		else {
			sendCommand(key,message,iMessage,event.getChannel());
		}


	}

	private void removeCommand(Long key,String[] command) {
		ArrayList<String> tmp = new ArrayList<>();
		for (int i = 1; i < command.length; i++) {
			tmp.add(command[i]);
		}
		String name = makeSentence(tmp);
		for (MemeCommand memeCommand:memeCommands.get(key)) {
			for (String commandName:memeCommand.names) {
				if(commandName.equalsIgnoreCase(name)){
					memeCommands.get(key).remove(memeCommand);
					saveCommands(key);
				}
			}
		}
	}

	private void sendCommand(Long key, String message, IMessage iMessage, IChannel channel) {
		boolean sent = false;
		for (MemeCommand memeCommand: memeCommands.get(key)) {

			if(memeCommand.exact == null){
				memeCommand.exact = true;
			}


			for (String name: memeCommand.names) {
				if(memeCommand.exact && message.startsWith(name) || message.contains(name)){
					if(memeCommand.emotes != null){
						for (String emote:memeCommand.emotes) {
							sent = true;
							RequestBuffer.request(()-> {
								iMessage.addReaction(EmojiManager.getForAlias(emote));
							});
						}
					}
					if(memeCommand.message != null && !sent){
						String tmp = memeCommand.message;
						tmp = tmp.replace("$mention",iMessage.getAuthor().mention());
						sendMessage(channel,tmp);
					}
					if(memeCommand.filePaths != null && !sent){ //todo rewrite so it's in a folder for that specific guild
						for (String filePath: memeCommand.filePaths) {
							sendFile(channel,new File(filePath));
						}
					}
					sent = true;
				}
			}


		}

	}

	private void addCommand(long key, String[] command, MessageReceivedEvent event) {
		ArrayList<String> names = new ArrayList<>();
		ArrayList<String> commandMessageWords = new ArrayList<>();
		String commandmessage = null;
		ArrayList<String> tmpemotes = new ArrayList<>();
		String[] filePath = null;
		boolean exact = true;


		int state = 0;
		boolean justset = false;
		for (int i = 0; i < command.length; i++) {
			if (command[i].equalsIgnoreCase("names:")) {
				justset = true;
				state = 1;
			}
			if (command[i].equalsIgnoreCase("message:")) {
				justset = true;
				state = 2;
			}
			if (command[i].equalsIgnoreCase("emotes:")) {
				justset = true;
				state = 3;
			}
			if (command[i].equalsIgnoreCase("exact:")) {
				justset = true;
				state = 4;
			}


			if (!justset) {
				switch (state) {
					case 1:
						names.add(command[i]);
						break;
					case 2:
						commandMessageWords.add(command[i]);
						break;
					case 3:
						tmpemotes.add(command[i]);
						break;
					case 4:
						exact = Boolean.parseBoolean(command[i]);
						break;
				}
			}
			justset = false;

		}
		if (names.size() <= 0) {
			sendMessage(event.getChannel(), "You must have at least one name for the command to trigger.");
		} else {

			if (event.getMessage().getAttachments().size() > 0) {
				filePath = new String[]{event.getMessage().getAttachments().get(0).getFilename()};
				try {

					URL url = new URL(event.getMessage().getAttachments().get(0).getUrl());

					URLConnection hc = url.openConnection();

					hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

					ImageIO.write(ImageIO.read(hc.getInputStream()), "png", new File(event.getMessage().getAttachments().get(0).getFilename()));


				} catch (IOException e) {
					e.printStackTrace();
					SlamUtils.sendMessage(event.getChannel(),"File must be a image.");
				}

			}



			ArrayList<String> tmpnms = new ArrayList<>();

			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < names.size(); i++) {
				stringBuilder.append(names.get(i));
				if (names.get(i).contains(",") || i == names.size() - 1) {
					tmpnms.add(stringBuilder.toString().replace(",", ""));
					stringBuilder = new StringBuilder();
				} else {
					stringBuilder.append(" ");
				}

			}
			if (commandMessageWords.size() > 0) {
				commandmessage = makeSentence(commandMessageWords);
			}
			String[] emotes;
			if (tmpemotes.size() > 0) {
				emotes = tmpemotes.toArray(new String[0]);
			} else {
				emotes = null;
			}
			MemeCommand m = new MemeCommand(tmpnms.toArray(new String[0]), commandmessage, emotes, exact, filePath);
			System.err.println(m.message);
			memeCommands.get(key).add(m);



			saveCommands(key);

		}


	}

	private void saveCommands(long key) {
		try {
			FileWriter fileWriter = new FileWriter("commands/"+key+"Commands.json");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(gson.toJson(memeCommands.get(key)));
			bufferedWriter.flush();
			bufferedWriter.close();
			fileWriter.close();


		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}


