package com.tree.slamJamBotV2.miniGames;

import com.tree.slamJamBotV2.SlamUtils;
import org.javatuples.Triplet;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class wordScramble {
	private  List<String> words;

	private ScheduledExecutorService execService;

	private Map<String,Triplet<String,Long,IChannel>> games = new HashMap<>();

	private PointlessPoints pointlessPoints;

	public wordScramble(PointlessPoints pointlessPoints){

		 execService = Executors.newScheduledThreadPool(3);

		 this.pointlessPoints = pointlessPoints;


		try {
			words = Files.readAllLines(Paths.get("words.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkGames() {
			if(games.size() != 0) {
				games.forEach((k, v) -> {
					if (System.currentTimeMillis() - v.getValue1() >= 60000) {
						games.remove(k);
						SlamUtils.sendMessage(v.getValue2(), "Game over, Try again!, The word was: " + v.getValue0());
					}
				});
			}
	}


	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getMessage().getContent().toLowerCase().equals("!scramble")){

			if(games.get(event.getChannel().getName()) == null){
				String word = randomWord();
				System.err.println(word);
				games.put(event.getChannel().getName(),Triplet.with(word,System.currentTimeMillis(),event.getChannel()));
				SlamUtils.sendMessage(event.getChannel(), "``"+shuffleString(word) + "``, You have a minute to solve this!");
				execService.scheduleAtFixedRate(this::checkGames, 0L, 5L, TimeUnit.SECONDS);
			}else {
				SlamUtils.sendMessage(event.getChannel(), "Please wait till the current game is finished to start a new one!");
			}
		}
		if(games.get(event.getChannel().getName()) != null){
			if(games.get(event.getChannel().getName()).getValue0().equals(event.getMessage().getContent().toLowerCase())){

				int points = ThreadLocalRandom.current().nextInt(25,150) + event.getMessage().getContent().length();

				SlamUtils.sendMessage(event.getChannel(), event.getAuthor().mention() + " A winner is you! You have earned " + points + " Points!");
				try {
					pointlessPoints.getUser(event.getAuthor().getLongID()).addPoints(points);
					pointlessPoints.saveUsers();
				}catch (NullPointerException e){
					pointlessPoints.users.add(new User(points,event.getAuthor().getLongID()));
				}

				System.err.println(pointlessPoints.users.get(0).getPoints());

				games.remove(event.getChannel().getName());
			}
		}
	}

	private String randomWord() {
		return words.get(ThreadLocalRandom.current().nextInt(0,words.size()));
	}

	static String shuffleString(String string){
		List<String> letters = Arrays.asList(string.split(""));
		Collections.shuffle(letters);
		StringBuilder shuffled = new StringBuilder();
		for (String letter : letters) {
			shuffled.append(letter);
		}
		return shuffled.toString();
	}


}
