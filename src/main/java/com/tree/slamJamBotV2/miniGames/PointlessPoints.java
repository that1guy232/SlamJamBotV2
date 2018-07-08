package com.tree.slamJamBotV2.miniGames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.tree.slamJamBotV2.SlamUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PointlessPoints {
	ArrayList<User> users = new ArrayList<>();
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
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

		if(message.equals("!ppoints")){
			if(getUser(event.getAuthor().getLongID()) != null){
				SlamUtils.sendMessage(event.getChannel(),"You have " + getUser(event.getAuthor().getLongID()).getPoints() + " pointless points. Good job.");
			}else {
				SlamUtils.sendMessage(event.getChannel(),"Wow your the best! You have no pointless points!");
			}
		}

	}


}


class User{


	private int points;
	private long userID;


	public int getPoints() {
		return points;
	}
	public void addPoints(int points) {
		this.points = this.points + points;
	}

	public long getUserID() {
		return userID;
	}

	public User(int points, long userID) {
		this.points = points;
		this.userID = userID;
	}
}