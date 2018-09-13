package com.tree.slamJamBotV2.miniGames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.tree.slamJamBotV2.SlamUtils;
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

	 void saveUsers() {
		try {
			Files.write(Paths.get("pointlesspoints.json"),gson.toJson(users).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContent().toLowerCase();


		if(message.startsWith("!ppoints")){
			if(message.contains("daily")){
				System.err.println( getUser(event.getAuthor().getLongID()).getTimeClaimed());
				if(getUser(event.getAuthor().getLongID()).getTimeClaimed() + 86400000L == System.currentTimeMillis() || getUser(event.getAuthor().getLongID()).getTimeClaimed() == 0 ){
					dailyPoints(event.getChannel(),event.getAuthor().getLongID());
				}else {
					SlamUtils.sendMessage(event.getChannel(),"Sorry you gotta wait till you can collect more daily pointless points");
				}

			}else {
				if (getUser(event.getAuthor().getLongID()) != null) {
					SlamUtils.sendMessage(event.getChannel(), "You have " + getUser(event.getAuthor().getLongID()).getPoints() + " pointless points. Good job.");
				} else {
					SlamUtils.sendMessage(event.getChannel(), "Wow your the best! You have no pointless points!");
				}
			}
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


class User{


	private long points;
	private long userID;



	private long timeClaimed = 0L;

	 long getPoints() {
		return points;
	}
	 void addPoints(int points) {
		this.points = this.points + points;
	}
	 void setTimeClaimed(long timeClaimed){this.timeClaimed = timeClaimed;}
	 long getTimeClaimed() {
		return timeClaimed;
	}
	 long getUserID() {
		return userID;
	}

	 User(int points, long userID) {
		this.points = points;
		this.userID = userID;
	}
}