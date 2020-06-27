package com.tree.slamJamBotV2.memeCommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpServer;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.server.ServerJoinListener;
import org.javacord.api.util.logging.ExceptionLogger;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.tree.slamJamBotV2.SlamUtils.makeSentence;
import static com.tree.slamJamBotV2.SlamUtils.spiltMessage;

public class MemeCommands implements MessageCreateListener, ServerJoinListener {
	HttpServer httpserver = null;

	public MemeCommands(){

		try {
			httpserver = HttpServer.create(new InetSocketAddress(8000), 0);
		} catch (IOException e) {
			e.printStackTrace();
		}



		httpserver.setExecutor(null); // creates a default executor
		httpserver.start();
	}

	private Map<Long, ArrayList<MemeCommand>> memeCommands = new HashMap<>();
	private Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();




	public void onServerJoin(ServerJoinEvent event){
		Server server = event.getServer();
		try {
			memeCommands.putIfAbsent(server.getId(),loadCommands(server));

		}catch (NullPointerException e){
			memeCommands.putIfAbsent(server.getId(),loadCommands(server));
		}
		saveCommands(server);


	}

	private ArrayList<MemeCommand> loadCommands(Server guild) {
		ArrayList<MemeCommand> tmp;


		System.err.println("Loading commands");
		String[] names = {"addcommand help"};
		String message =  "```To use the !addcommand function you have to have at least one name to do that you do the following: \n!addcommand names: This is one name, anotherName \nThat will create a empty command that will do nothing. \n\nIf you want the bot to send a message when someone types a name of a command you do this:\n!addcommand names: name message: This will be the message    This will send the message \"This will be the message\" when ever someone types \"name\" into chat.  \n\nIf you would also like it to react to the user who sent the message you do the following \n!addcommand names: name emotes: sunglasses heart (any normal emotes will work just remove the : )   \n\nIf you want the bot to send a image with a command you drag the image over to discord and then were it says   \nadd comment you do the !addcommand with what ever you want.   \n\nIf you want a command like the AAAAAAAA command all you have to do is the following  \n!addcommand names: AAAA message: https://www.youtube.com/watch?v=Y4Z7Ds_yv8o exact: false \nWhat that does is let the word be in another word or as in this case let it be a bunch of a's minim of 4. \n\nTo remove a command all you have to do is type : !removecommand name```";
		MemeCommand addCommandHelp = new MemeCommand(names,message,null,true,null);



		FileReader fr;
		try {
			fr = new FileReader("commands/"+guild.getId()+"/Commands.json");
			tmp = gson.fromJson(fr,new TypeToken<ArrayList<MemeCommand>>(){}.getType());
			System.err.println(tmp);




		} catch (FileNotFoundException e) {
			System.err.println("Commands Failed to load creating Commands file for: " + guild.getId());





			try {
				new File("commands/"+guild.getId()).mkdirs();
				File file = new File("commands/"+guild.getId()+"/Commands.json");
				file.createNewFile();

			} catch (IOException e1) {

				e1.printStackTrace();
			}

			tmp = new ArrayList<>();
			tmp.add(addCommandHelp);

		}


		return tmp;

	}



	@Override
	public void onMessageCreate(MessageCreateEvent event){



		Server server = event.getServer().get();
		String messageString = event.getMessage().getContent().toLowerCase();
		Message message = event.getMessage();
		String[] command = spiltMessage(messageString);
		Long key = event.getServer().get().getId();
		//todo make a user need a certin permission
		if (messageString.startsWith("!addcommand")) {


			System.err.println("This is a test");



			httpserver.createContext("/test", new MyHandler());
			System.err.println(Arrays.toString(command));
			addCommand(server,command,event);
		}
		if (messageString.equalsIgnoreCase("list commands") && !event.getMessageAuthor().isBotUser()){
			ArrayList<String> names = new ArrayList<>();
			memeCommands.get(server.getId()).iterator().forEachRemaining(memeCommand -> {
					names.add(memeCommand.names[0]);
			});
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < names.size(); i++) {
				stringBuilder.append(i+1).append(" "+ names.get(i)+"\n");
			}
			event.getChannel().sendMessage(stringBuilder.toString());
		}
		if(messageString.startsWith("!removecommand")){
		}
		if(messageString.startsWith("!reloadcommands")){
            removeCommand(server,command);
			memeCommands.remove(key);
			memeCommands.put(key,loadCommands(server));
		}

		if(messageString.equals("get commands")) {
			File f = new File("commands/"+server.getId()+"/Commands.json");
			event.getChannel().sendMessage(f);
		}

		else {
			sendCommand2(server,messageString,message,event.getChannel());
			//sendCommand(guild.getLongID(),message,iMessage,event.getChannel());
		}


	}

	private void removeCommand(Server guild,String[] command) {

		ArrayList<String> tmp = new ArrayList<>(Arrays.asList(command).subList(1, command.length));

		String name = makeSentence(tmp);
		for (MemeCommand memeCommand:memeCommands.get(guild.getId())) {
			for (String commandName:memeCommand.names) {
				if(commandName.equalsIgnoreCase(name)){
					memeCommands.get(guild.getId()).remove(memeCommand);
					saveCommands(guild);
				}
			}
		}
	}






	private void sendCommand2(Server guild, String messageString, Message message, Channel channel) {



		boolean sent = false;
		for (MemeCommand memeCommand: memeCommands.get(guild.getId())) {


			if(!sent) {
				if (memeCommand.exact) {
					for (String name : memeCommand.names) {
						if (name.equals(messageString) && messageString.startsWith(name)) {
								send(guild, message, channel, memeCommand);
								sent = true;
								break;
						}

					}


				}
				if (!memeCommand.exact) {
					for (String name : memeCommand.names) {
						if (messageString.contains(name)) {

							send(guild, message, channel, memeCommand);
							sent = true;
							break;
						}
					}
				}
			}
			if(memeCommand.exact == null){
				memeCommand.exact = true;
			}
		}

	}
		private void send(Server server, Message message, Channel channel,MemeCommand memeCommand){

			boolean sent = false;
			if(memeCommand.emotes != null){
				for (String emote:memeCommand.emotes) {
					sent = true;
					System.err.println(":\uD83D\uDE03:");
					message.addReactions(emote).exceptionally(ExceptionLogger.get());
				}
			}
			if(memeCommand.message != null && memeCommand.filePaths != null && !sent){
				String tmp = memeCommand.message;
				tmp = tmp.replace("$mention",message.getAuthor().getDisplayName());
				String filePath = memeCommand.filePaths[0];
				channel.asTextChannel().get().sendMessage(tmp,new File("commands/"+server.getId()+"/"+filePath));

			}else if(memeCommand.filePaths != null && !sent){
				for (String filePath: memeCommand.filePaths) {
					channel.asTextChannel().get().sendMessage(new File("commands/"+server.getId()+"/"+filePath));
				}
			}else if(memeCommand.message != null && !sent){
				String tmp = memeCommand.message;
				tmp = tmp.replace("$mention",message.getAuthor().getDisplayName());
				channel.asTextChannel().get().sendMessage(tmp);
			}

		}




	private void addCommand(Server server, String[] command, MessageCreateEvent event) {
		ArrayList<String> names = new ArrayList<>();
		ArrayList<String> commandMessageWords = new ArrayList<>();
		String commandmessage = null;
		String[] filePath = null;
		boolean exact = true;


		State state = null;
		State laststate = null;


		ArrayList<CustomEmoji> tmpemotes = new ArrayList<>(event.getMessage().getCustomEmojis());
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
				default:
					if(state != null)
						switch (state){
							case names:
								if(!sCommand.equals(" "))
									names.add(sCommand);
								System.err.println("Name:" + sCommand);
								break;
							case message:
								commandMessageWords.add(sCommand);
								break;
							case emotes:


								break;
							case exact:
								exact = Boolean.getBoolean(sCommand);
								break;
						}
			}




		}

		if (names.size() <= 0) {
			event.getChannel().sendMessage("You must have at least one name for the command to trigger.");
		} else {

			if (event.getMessage().getAttachments().size() > 0) {
				filePath = new String[]{event.getMessage().getAttachments().get(0).getFileName()};
				try {

					URL url = (event.getMessage().getAttachments().get(0).getUrl());

					URLConnection hc = url.openConnection();

					hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");


					ImageIO.write(ImageIO.read(hc.getInputStream()), "png", new File(event.getMessage().getAttachments().get(0).getFileName()));


				} catch (IOException e) {
					e.printStackTrace();
                    event.getChannel().sendMessage("File must be a image.");
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

			MemeCommand m = new MemeCommand(names.toArray(new String[0]), commandmessage, emotes, exact, filePath);
			memeCommands.get(server.getId()).add(m);



			saveCommands(server);

		}


	}

	private void saveCommands(Server guild) {
		File file = new File("commands/"+guild.getId()+"/Commands.json");
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
			bufferedWriter.write(gson.toJson(memeCommands.get(guild.getId())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void initLoadAll(DiscordApi api){
		api.getServers().iterator().forEachRemaining(server -> {
			memeCommands.put(server.getId(),loadCommands(server));
		});
	}

}




enum State{
	names,
	message,
	emotes,
	exact

}

