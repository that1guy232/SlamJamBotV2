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
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.tree.slamJamBotV2.SlamUtils.*;

public class MemeCommands {
	private Map<Long, ArrayList<MemeCommand>> memeCommands = new HashMap<>();
	private Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();



	@EventSubscriber
	public void onGuildJoinEven(GuildCreateEvent event){
		IGuild guild = event.getGuild();
		try {
			memeCommands.putIfAbsent(guild.getLongID(),loadCommands(guild));

		}catch (NullPointerException e){
			memeCommands.putIfAbsent(guild.getLongID(),loadCommands(guild));
		}


	}

	private ArrayList<MemeCommand> loadCommands(IGuild guild) {
		ArrayList<MemeCommand> tmp = null;
		FileReader fr;
		try {
			fr = new FileReader("commands/"+guild.getName()+"/Commands.json");
			tmp = gson.fromJson(fr,new TypeToken<ArrayList<MemeCommand>>(){}.getType());




		} catch (FileNotFoundException e) {
			System.err.println("Commands Failed to load creating Commands file.");


			try {
				new File("commands/"+guild.getName()).mkdirs();
				File file = new File("commands/"+guild.getName()+"/Commands.json");
				file.createNewFile();


			} catch (IOException e1) {

				e1.printStackTrace();
			}

		}




		return tmp;


	}


	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event){
		IGuild guild = event.getGuild();
		String message = event.getMessage().getContent().toLowerCase();
		IMessage iMessage = event.getMessage();
		String[] command = spiltMessage(message);
		Long key = event.getGuild().getLongID();
		//todo make a user need a certin permission
		if (message.startsWith("!addcommand")) {
			addCommand(guild,command,event);
		}
		if(message.startsWith("!removecommand")){
			removeCommand(guild,command);
		}
		if(message.startsWith("!reloadcommands")){
			memeCommands.remove(key);
			memeCommands.put(key,loadCommands(guild));
		}

		if(message.equals("get commands")) {
			File f = new File("commands/"+guild.getName()+"/Commands.json");
			SlamUtils.sendFile(event.getChannel(), f);
		}
		else {
			sendCommand(key,message,iMessage,event.getChannel());
		}


	}

	private void removeCommand(IGuild guild,String[] command) {

		ArrayList<String> tmp = new ArrayList<>(Arrays.asList(command).subList(1, command.length));

		String name = makeSentence(tmp);
		for (MemeCommand memeCommand:memeCommands.get(guild.getLongID())) {
			for (String commandName:memeCommand.names) {
				if(commandName.equalsIgnoreCase(name)){
					memeCommands.get(guild.getLongID()).remove(memeCommand);
					saveCommands(guild);
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
							RequestBuffer.request(()-> iMessage.addReaction(EmojiManager.getForAlias(emote)));
						}
					}
					if(memeCommand.message != null && !sent){
						String tmp = memeCommand.message;
						tmp = tmp.replace("$mention",iMessage.getAuthor().mention());
						sendMessage(channel,tmp);
					}

					if(memeCommand.filePaths != null && !sent){
						for (String filePath: memeCommand.filePaths) {
							sendFile(channel,new File(filePath));
						}
					}
					sent = true;
				}
			}


		}

	}

	private void addCommand(IGuild guild, String[] command, MessageReceivedEvent event) {
		ArrayList<String> names = new ArrayList<>();
		ArrayList<String> commandMessageWords = new ArrayList<>();
		String commandmessage = null;
		ArrayList<String> tmpemotes = new ArrayList<>();
		String[] filePath = null;
		boolean exact = true;


		State state = null;

		for (String sCommand : command) {
			switch (sCommand.toLowerCase()) {
				case "names:":
					state = State.names;
					break;
				case "message:":
					state = State.message;
					break;
				case "emotes:":
					state = State.emotes;
					break;
				case "exact:":
					state = State.exact;
					break;

					default:switch (state){
						case names:
							names.add(sCommand);
							break;
						case message:
							commandMessageWords.add(sCommand);
							break;
						case emotes:
							tmpemotes.add(sCommand);
							break;
						case exact:
							exact = Boolean.parseBoolean(sCommand);
							break;
					}
			}
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
			memeCommands.get(guild.getLongID()).add(m);



			saveCommands(guild);

		}


	}

	private void saveCommands(IGuild guild) {
		File file = new File("commands/"+guild.getName()+"/Commands.json");
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
			bufferedWriter.write(gson.toJson(memeCommands.get(guild.getLongID())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}




enum State{
	names,
	message,
	emotes,
	exact

}

