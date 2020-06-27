package com.tree.slamJamBotV2.miniGames.games;

import com.tree.slamJamBotV2.Item;
import com.tree.slamJamBotV2.miniGames.PointlessPoints;
import com.tree.slamJamBotV2.miniGames.User;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javatuples.Triplet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class wordScramble implements MessageCreateListener {
	private  List<String> words;

	private ScheduledExecutorService execService;

	private Map<String,Triplet<String,Long, Channel>> games = new HashMap<>();

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
						Channel channel = v.getValue2();
						channel.asTextChannel().get().sendMessage( "Game over, Try again!, The word was: " + v.getValue0());
					}
				});
			}
	}



		public void onMessageCreate(MessageCreateEvent event) {
            String eventChannel = event.getChannel().asServerChannel().get().getName();
		if(event.getMessage().getContent().toLowerCase().equals("!scramble") && !event.isPrivateMessage()){

			if(games.get(eventChannel) == null){
				String word = randomWord();
				System.err.println(word);
				games.put(eventChannel,Triplet.with(word,System.currentTimeMillis(),event.getChannel()));

				event.getChannel().sendMessage( "``"+shuffleString(word) + "``, You have a minute to solve this!");
				execService.scheduleAtFixedRate(this::checkGames, 0L, 5L, TimeUnit.SECONDS);
			}else {
				event.getChannel().sendMessage("Please wait till the current game is finished to start a new one!");
			}
		}
		if(games.get(eventChannel) != null){
			if(games.get(eventChannel).getValue0().equals(event.getMessage().getContent().toLowerCase())){

				int points = ThreadLocalRandom.current().nextInt(25,150) + event.getMessage().getContent().length();

				event.getChannel().sendMessage(event.getMessageAuthor().getDisplayName() + " A winner is you! You have earned " + points + " Points!");
				try {
					pointlessPoints.getUser(event.getMessageAuthor().getId()).addPoints(points);
					pointlessPoints.saveUsers();
				}catch (NullPointerException e){
					pointlessPoints.users.add(new User(points,event.getMessageAuthor().getId(), 0,0,new ArrayList<Item>()));
				}

				System.err.println(pointlessPoints.users.get(0).getPoints());

				games.remove(eventChannel);
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
