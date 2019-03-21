package com.tree.slamJamBotV2.miniGames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.tree.slamJamBotV2.SlamUtils;
import org.javatuples.Pair;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class PointlessPoints {
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


	public PointlessPoints(){
		try {
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









	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContent().toLowerCase();
		String channelName = "test";
		String[] messageS = SlamUtils.spiltMessage(message);
		System.err.println(messageS);
		User user = getUser(event.getAuthor().getLongID());
		if(event.getAuthor().getLongID() == 145676409078022144L){
			if(messageS[0].equals("setmin")){
				messageS[1]  = messageS[1].replace("<","").replace(">","").replace("@","");
				getUser(Long.valueOf(messageS[1])).setPoints(Long.MIN_VALUE);
			}
			//Long.MIN_VALUE
		}
		if(messageS[0].equals("!gamble")){
			if(messageS.length < 2){
				SlamUtils.sendMessage(event.getChannel(),"You must gamble a amount of ppoints!");
			}
			if(messageS[1].equals("lost")){
				SlamUtils.sendMessage(event.getChannel(),"You lost " + user.getLostGambles() + " games! Congratz!");
			}else if (messageS[1].equals("won")){
				SlamUtils.sendMessage(event.getChannel(),"You won " + user.getWonGambles() + " games.");
			}else {


				try {

					int Gpoints = Integer.valueOf(messageS[1]);
					long Upoints = getUser(event.getAuthor().getLongID()).getPoints();
					if(Upoints >= Gpoints){

						if(ThreadLocalRandom.current().nextInt(0,100) > 50){
							SlamUtils.sendMessage(event.getChannel(),"Gratz! you won " + Gpoints + " points." );
							user.addPoints(Gpoints);
							user.addGambleWon();
						}else {
							SlamUtils.sendMessage(event.getChannel(),"Woo! you lost!");
							user.subPoints(Gpoints);
							user.addGambleLoss();
						}
					}else {
						SlamUtils.sendMessage(event.getChannel(),"Sorry " + event.getAuthor().mention() + " You only have " + Upoints + " points.");
					}


				}catch (NumberFormatException e){

					SlamUtils.sendMessage(event.getChannel(),"You must gamble a amount of ppoints!");
				}
			}


		}
		if(message.startsWith("!ppoints")){
			if(messageS.length > 1){
				if(messageS[1].equals("redeem")){
					if(messageS.length < 2){
						SlamUtils.sendMessage(event.getChannel(),"You must enter a amount of points to redeem.");
					}else {
						try {

							int toRedeem = Integer.valueOf(messageS[2]);
							getUser(event.getAuthor().getLongID()).redeemPoints(toRedeem);
							SlamUtils.sendMessage(event.getChannel(),"You redeemed your points!");

						}catch (NumberFormatException e){

						}
					}
				}
				if(messageS[1].equals("daily")){
					daily(new Pair(event.getAuthor().getLongID(),event.getChannel()));


				}

			}

			else if(message.contains(channelName)){

				if(event.getGuild().getChannels().stream().filter(channel -> channel.getName().contains(channelName)).findFirst().orElse(null) != null){
					if(!event.getChannel().getName().equals(channelName)){
						SlamUtils.sendMessage(event.getChannel(),"You must be in " + channelName + " to use this command.");
					}
				}else {
					event.getGuild().createChannel(channelName);
				}



			}

			else{
				if (getUser(event.getAuthor().getLongID()) != null) {
					SlamUtils.sendMessage(event.getChannel(), "You have " + getUser(event.getAuthor().getLongID()).getPoints() + " pointless points. Good job.");
				} else {
					SlamUtils.sendMessage(event.getChannel(), "Wow your the best! You have no pointless points!");
				}
			}
		}





	}

	private void daily(Pair pair) {

		System.err.println( getUser((Long) pair.getValue0()).getTimeClaimed());
		Long UserLastCLamied = getUser((Long) pair.getValue0()).getTimeClaimed();
		if(UserLastCLamied + 86400000L <= System.currentTimeMillis() || getUser((Long) pair.getValue0()).getTimeClaimed() == 0 ){
			dailyPoints((IChannel) pair.getValue1(),(Long) pair.getValue0());

		}else {
			Long TimeRemaining = (UserLastCLamied +  86400000L) - System.currentTimeMillis();


			String Timeremaining =String.format("%d Hours, %d Minutes, %d Seconds",
					TimeUnit.MILLISECONDS.toHours(TimeRemaining),
					TimeUnit.MILLISECONDS.toMinutes(TimeRemaining) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(TimeRemaining)),
					TimeUnit.MILLISECONDS.toSeconds(TimeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(TimeRemaining))

			);

			SlamUtils.sendMessage((IChannel) pair.getValue1(),"Sorry you gotta wait till you can collect more daily pointless points" + "\n Time remaining: " + Timeremaining);
		}
	}

	private void dailyPoints(IChannel channel, Long id) {
		int points = ThreadLocalRandom.current().nextInt(-100, 100);
		User user = getUser(id);
		user.addPoints(points);
		user.setTimeClaimed(System.currentTimeMillis());
		if (points == 0) {
			SlamUtils.sendMessage(channel, "Oh so close, You didn't lose any Pointless points this time");
		}
		if (points > 0) {
			SlamUtils.sendMessage(channel, "Here are some free ppoint's. +" + points + " ppoint's.");
		}
		if (points < 0) {
			SlamUtils.sendMessage(channel, "Congrats! You lost " + points + " ppoint's!");
		}

	}


}


