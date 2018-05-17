package com.tree.slamJamBotV2.DnDCommands;

import com.tree.slamJamBotV2.DnDCommands.RandomCharacterGen.RandomCharacter;
import com.tree.slamJamBotV2.SlamUtils;
import com.vdurmont.emoji.EmojiParser;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class DnD5eCommands {

    SpellListCommand spellListCommand;


    public DnD5eCommands(){
		spellListCommand = new SpellListCommand();

    }




    @EventSubscriber
    public void reactionAddEvent(ReactionAddEvent reactionAddEvent){
        //EmojiManager.getForAlias(reactionAddEvent.getReaction().getEmoji().getName());
       System.err.println( );
        long messageID = reactionAddEvent.getMessageID();
        IReaction reaction = reactionAddEvent.getReaction();
        IUser user= reactionAddEvent.getUser();
        if(messageID == spellListCommand.getSpellListMessageID() && !user.isBot()){
            if(EmojiParser.parseToAliases(reactionAddEvent.getReaction().getEmoji().toString()).equals(":arrow_left:")){
				spellListCommand.decrementPage(reaction);

            }
            if(EmojiParser.parseToAliases(reactionAddEvent.getReaction().getEmoji().toString()).equals(":arrow_right:")){
            	spellListCommand.incrementPage(reaction);

            }
            if(EmojiParser.parseToAliases(reactionAddEvent.getReaction().getEmoji().toString()).equals(":x:")){
                reaction.getMessage().delete();
                spellListCommand.setSpellListMessageID(0);
            }
            RequestBuffer.request(() -> reaction.getMessage().removeReaction(user,reaction));
        }

    }


    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        String message = event.getMessage().getContent();
		String splitMessage[] = message.split("\\s");
        IChannel channel = event.getChannel();
        message = message.toLowerCase();
		if(splitMessage[0].equals("dnd")) {
			if (splitMessage[1].equals("spells")) {
				spellListCommand.sendSpellList(channel);
			}
			if (splitMessage[1].equals("spell")) {
				if (splitMessage[2] != null) {
					String[] test = Arrays.copyOfRange(splitMessage, 2, splitMessage.length );
					System.err.println(Arrays.toString(test));
					spellListCommand.getSpell(channel, test);
				}

			}
		}


        if(message.startsWith("roll")){
			roll(channel,message);
		}



		if(message.startsWith("give me a random character!")){
			randomCharacter(channel);

		}

    }

	private void randomCharacter(IChannel channel) {
		RandomCharacter rc = new RandomCharacter();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.withTitle("Your random character!");
		embedBuilder.withFooterText("This character was randomly generatored using Xanathar's Guide To Everything.");
		embedBuilder.appendField("Age",rc.getAge(),false);
		embedBuilder.appendField("Race & Class", rc.getRace() + "\n" + rc.getPClass(),false);
		String abilityScores = "```" +
				"Str:"+rc.getStr()+
				"\nDex:"+rc.getDex()+
				"\nCon:"+rc.getCon()+
				"\nInt:"+rc.getInt()+
				"\nWis:"+rc.getWis()+
				"\nCha:"+rc.getCha()+"```";
		embedBuilder.appendField("Why are you a "+rc.getPClass()+"?",rc.getWhyclass(),false);
		embedBuilder.appendField("Ability Scores",abilityScores,false);
		StringBuilder sb = new StringBuilder();
		sb.append("```");
		for (int i = 0; i < rc.getBackground().length - 1; i++) {
			sb.append(rc.getBackground()[i]).append("\n");
		}
		sb.append("```");
		embedBuilder.appendField("Background - " + rc.getBackground()[0],sb.toString(),false);
		int z = 1;
		if(rc.getLifeEvents().length == 1){
			embedBuilder.appendField("Life event #1",rc.getLifeEvents()[0],false);
		}else {
			for (int i = 0; i < rc.getLifeEvents().length - 1; i++) {
				embedBuilder.appendField("Life event #" + z, rc.getLifeEvents()[i], false);
				z++;
			}
		}
		SlamUtils.sendEmbed(channel,embedBuilder.build());
	}

	private void roll(IChannel channel, String message) {
		StringBuilder stringBuilder;
		//spilting the command to things i can use
		ArrayList<String> rollCommand = new ArrayList<>(Arrays.asList(message.split("((?<=\\+|\\-|\\*|\\/)|(?=\\+|\\-|\\*|\\/))|\\s")));
		ArrayList<ArrayList<Integer>> dicerolled = new ArrayList<>();
		//removing "roll"
		rollCommand.remove(0);
		boolean Meme = true;
		//int to know were i'm at in the dicerolled array list
		int x = 0;
		for (int i = 0; i < rollCommand.size(); i++) {
			String[] dice = rollCommand.get(i).split("[d]");

			//if it's a dice it contains a d so deal with the dice
			if (rollCommand.get(i).contains("d")){
				Meme = false;
				//add it to the list of the dice rolled
				dicerolled.add(new ArrayList<>());

				int dieToRoll;
				//spilt the dice into something i can use



				//if it has a space in it for some reason remove it.
				dice[0] = dice[0].replace(" ","");

				int amountofDice = Integer.valueOf(dice[0]);
				dieToRoll = Integer.valueOf(dice[1]);

				int total = 0;
				for (int j = 0; j < amountofDice; j++) {
					//roll each dice and put them in the list
					dicerolled.get(x).add(ThreadLocalRandom.current().nextInt(1, dieToRoll+1));

				}

				stringBuilder = new StringBuilder();
				//for each of the dice create a string
				for (int j = 0; j < dicerolled.get(x).size(); j++) {
					stringBuilder.append(dicerolled.get(x).get(j).toString());
					if(j != dicerolled.get(x).size()-1)
						stringBuilder.append("+");

				}
				System.err.println(stringBuilder.toString());
				//replace the dice with every thing it rolled
				rollCommand.set(i,stringBuilder.toString());
				x++;
			}
		}
		System.err.println(rollCommand);
		// make the entire thing a string so JS can deal with it.
		stringBuilder = new StringBuilder();
		for (int i = 0; i < rollCommand.size() ; i++) {
			stringBuilder.append(rollCommand.get(i));
		}
		//use javascript to calculate it because i'm a lazy fuck
		System.err.println(stringBuilder.toString());
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		try {
			int eq = (Integer) engine.eval(stringBuilder.toString());
			String tosend = stringBuilder.toString() + " = " + eq;

			if(Meme){
				System.err.println("test");
				SlamUtils.sendMessage(channel,stringBuilder.toString() + "+" + ThreadLocalRandom.current().nextInt(eq-20,eq+50));
			}else if (tosend.length() > 2000){
				SlamUtils.sendMessage(channel,"Message to long! here is the total: "+eq);

			}else {
				SlamUtils.sendMessage(channel, stringBuilder.toString() + " = " + eq);

			}
		} catch (ScriptException e) {
			e.printStackTrace();
		}
    }
}
