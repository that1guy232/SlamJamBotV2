package com.tree.slamJamBotV2.miniGames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.tree.slamJamBotV2.Item;
import com.tree.slamJamBotV2.SlamUtils;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javatuples.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class PointlessPoints implements MessageCreateListener {
	public ArrayList<User> users = new ArrayList<>();
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	 public User getUser(long longID) {
		for (User user: users) {
			if(user.getUserID() == longID){
				saveUsers();
				return user;
			}
		}
		return null;
	}

	Item[] items;
	public PointlessPoints(){


		try {


			 items = gson.fromJson(new JsonReader(new FileReader("magicitems.txt")), Item[].class);



			Type userpoints = new TypeToken<ArrayList<User>>(){}.getType();
			users = gson.fromJson(new JsonReader(new FileReader("pointlesspoints.json")), userpoints);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	 public void saveUsers() {
		try {
			Files.write(Paths.get("pointlesspoints.json"),gson.toJson(users).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}



	}









	
	public void onMessageCreate(MessageCreateEvent event) {
		String message = event.getMessage().getContent().toLowerCase();
		String channelName = "test";
		String[] messageS = SlamUtils.spiltMessage(message);
		System.err.println(messageS);
		User user = getUser(event.getMessageAuthor().getId());
	    System.err.println("Messeged created Triggred in ppoints");

	    if(event.getMessageAuthor().isBotOwner()){
	    	if(message.startsWith("!test")){
	    		getUser(event.getMessageAuthor().getId()).addPoints(100);
			}
		}


	    if(messageS[0].equalsIgnoreCase("!items")){
	    	System.err.println("this should work");
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle(event.getMessageAuthor().getDisplayName() + "'s items.");
			StringBuilder sb = new StringBuilder();
			user.getItem().forEach(item -> sb.append(item.getName() + "\n"));
			embedBuilder.addField("Items",sb.toString());
			event.getChannel().sendMessage(embedBuilder);
		}


	    if(messageS[0].equalsIgnoreCase("!buy")){
	    	if(user.getPoints() >= 100){
	    		user.redeemPoints(100);
	    		Item rItem = items[ThreadLocalRandom.current().nextInt(items.length)];
	    		user.addItems(rItem);
	    		event.getChannel().sendMessage("You got: " + rItem.getName() );
	    		saveUsers();
			}else
				event.getChannel().sendMessage("Sorry you have to have 100 ppoints to buy pointless items. You currently have: " + user.getPoints() + " ppoints");
		}

		if(messageS[0].equalsIgnoreCase("!gamble")){
			if(messageS.length < 2){
				event.getChannel().sendMessage("You must gamble a amount of ppoints!");
			}
			if(messageS[1].equals("lost")){
				event.getChannel().sendMessage("You lost " + user.getLostGambles() + " games! Congratz!");

			}else if (messageS[1].equals("won")){
				event.getChannel().sendMessage("You won " + user.getWonGambles() + " games.");
			}else {


				try {

					int Gpoints = Integer.valueOf(messageS[1]);
					long Upoints = getUser(event.getMessageAuthor().getId()).getPoints();
					if(Upoints >= Gpoints){

						if(ThreadLocalRandom.current().nextInt(0,100) > 50){
							event.getChannel().sendMessage("Gratz! you won " + Gpoints + " points." );
							user.addPoints(Gpoints);
							user.addGambleWon();
						}else {
							event.getChannel().sendMessage("Woo! you lost!");
							user.subPoints(Gpoints);
							user.addGambleLoss();
						}
					}else {
						event.getChannel().sendMessage("Sorry " + event.getMessageAuthor().getDisplayName() + " You only have " + Upoints + " points.");
					}


				}catch (NumberFormatException e){

					event.getChannel().sendMessage("You must gamble a amount of ppoints!");
				}
			}


		}

		if(message.startsWith("!ppoints")){
		    System.err.println("Triggred !ppoints");
			if(messageS.length > 1){
				if(messageS[1].equals("redeem")){
					if(messageS.length < 2){
						event.getChannel().sendMessage("You must enter a amount of points to redeem.");
					}else {
						try {

							int toRedeem = Integer.valueOf(messageS[2]);
							getUser(event.getMessageAuthor().getId()).redeemPoints(toRedeem);
							event.getChannel().sendMessage("You redeemed your points!");

						}catch (NumberFormatException e){

						}
					}
				}
				if(messageS[1].equals("daily")){
					daily(new Pair(event.getMessageAuthor().getId(),event.getChannel()));


				}

			}

			else if(message.contains(channelName)){

				if(event.getServer().get().getChannels().stream().filter(channel -> channel.getName().contains(channelName)).findFirst().orElse(null) != null){
				Optional<String> channelNameEvent =    event.getChannel().asServerChannel().map(
				            Nameable::getName
                    );
					if(!channelNameEvent.equals(channelName)){
						event.getChannel().sendMessage("You must be in " + channelName + " to use this command.");
					}
				}else {
					event.getServer().get().createTextChannelBuilder().setName(channelName).create();
				}



			}

			else{
				if (getUser(event.getMessageAuthor().getId()) != null) {
					event.getChannel().sendMessage("You have " + getUser(event.getMessageAuthor().getId()).getPoints() + " pointless points. Good job.");
				} else {
                    event.getChannel().sendMessage("Wow your the best! You have no pointless points!");
				}
			}
		}





	}

	private void daily(Pair pair) {

		System.err.println( getUser((Long) pair.getValue0()).getTimeClaimed());
		Long UserLastCLamied = getUser((Long) pair.getValue0()).getTimeClaimed();
		if(UserLastCLamied + 86400000L <= System.currentTimeMillis() || getUser((Long) pair.getValue0()).getTimeClaimed() == 0 ){
			dailyPoints((Channel) pair.getValue1(),(Long) pair.getValue0());

		}else {
			Long TimeRemaining = (UserLastCLamied +  86400000L) - System.currentTimeMillis();


			String Timeremaining =String.format("%d Hours, %d Minutes, %d Seconds",
					TimeUnit.MILLISECONDS.toHours(TimeRemaining),
					TimeUnit.MILLISECONDS.toMinutes(TimeRemaining) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(TimeRemaining)),
					TimeUnit.MILLISECONDS.toSeconds(TimeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(TimeRemaining))

			);

            Channel channel = (Channel) pair.getValue1();

            channel.asTextChannel().get().sendMessage("Sorry you gotta wait till you can collect more daily pointless points" + "\n Time remaining: " + Timeremaining);
		}
	}

	private void dailyPoints(Channel channel, Long id) {
		int points = ThreadLocalRandom.current().nextInt(-100, 100);
		User user = getUser(id);
		user.addPoints(points);
		user.setTimeClaimed(System.currentTimeMillis());
		if (points == 0) {
			channel.asTextChannel().get().sendMessage("Oh so close, You didn't lose any Pointless points this time");
		}
		if (points > 0) {
            channel.asTextChannel().get().sendMessage("Here are some free ppoint's. +" + points + " ppoint's.");
		}
		if (points < 0) {
            channel.asTextChannel().get().sendMessage("Congrats! You lost " + points + " ppoint's!");
		}

	}


}


