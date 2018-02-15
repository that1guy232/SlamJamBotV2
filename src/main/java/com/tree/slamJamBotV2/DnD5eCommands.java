package com.tree.slamJamBotV2;

import com.google.common.collect.Lists;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DnD5eCommands {
    List<CSVRecord> dnd5eSpells;
    ArrayList<EmbedObject> spellEmbeds;
    private long spellListMessageID;
    private int spellListMessagePage;

    public DnD5eCommands(){
        try {
            FileReader fr = new FileReader("5eSpells.csv");
            dnd5eSpells = new ArrayList<>(CSVFormat.RFC4180.withFirstRecordAsHeader().parse(fr).getRecords());
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        buildSpellList();
    }

    private void buildSpellList() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        spellEmbeds = new ArrayList<>();
        List<List<CSVRecord>> spellNameList = Lists.partition(dnd5eSpells,25);

        StringBuilder stringBuilder = new StringBuilder();




        for (int i = 0; i < spellNameList.size(); i++) {
            embedBuilder.withTitle("Page"+ (i+1) + "/" + spellNameList.size());
            for (int j = 0; j < spellNameList.get(i).size(); j++) {

                stringBuilder.append(spellNameList.get(i).get(j).get("name"));
                stringBuilder.append("\n");
            }
            embedBuilder.appendField("Spells",stringBuilder.toString(),false);

            embedBuilder.withFooterText("To learn more about a spell type \"dnd spell name\"");

            stringBuilder.delete(0,stringBuilder.length());

            spellEmbeds.add(embedBuilder.build());

            embedBuilder.clearFields();
        }

    }



    private void DndCommands(IChannel channel, String message) {
        String spellMessage[] = message.split("\\s");

        if (spellMessage[1].equals("spells")) {
            ArrayList<String> emojiList = new ArrayList<>();
            emojiList.add("arrow_left");
            emojiList.add("arrow_right:");
            emojiList.add("x");



            if (spellListMessageID != 0) {
                channel.getMessageByID(spellListMessageID).delete();
            }

            spellListMessageID = SlamUtils.sendEmbed(channel, spellEmbeds.get(0));
            spellListMessagePage = 0;

            RequestBuffer.request(() -> channel.getMessageByID(spellListMessageID).addReaction(EmojiManager.getForAlias(emojiList.get(0)))).get();
            RequestBuffer.request(() -> channel.getMessageByID(spellListMessageID).addReaction(EmojiManager.getForAlias(emojiList.get(1)))).get();
            RequestBuffer.request(() -> channel.getMessageByID(spellListMessageID).addReaction(EmojiManager.getForAlias(emojiList.get(2)))).get();


        }


        if (spellMessage[1].equals("spell")) {
            if (spellMessage[2] != null) {

                StringBuilder stringBuilder = new StringBuilder();


                for (int i = 2; i < spellMessage.length - 1; i++) {
                    stringBuilder.append(spellMessage[i]);

                    stringBuilder.append(" ");

                }
                stringBuilder.append(spellMessage[spellMessage.length - 1]);
                for (CSVRecord spells : dnd5eSpells) {

                    //spells.get("name").toLowerCase().contains(stringBuilder.toString())
                    if (spells.get("name").toLowerCase().matches(stringBuilder.toString())) {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.withTitle(spells.get("name"));
                        embedBuilder.withDesc(spells.get("description"));

                        embedBuilder.appendField("Level",spells.get("level"),true);
                        embedBuilder.appendField("Casting time",spells.get("casting_time"),true);
                        embedBuilder.appendField("Duration",spells.get("duration"),true);
                        embedBuilder.appendField("Range",spells.get("range"),true);
                        embedBuilder.appendField("components", spells.get("components/raw"),true);
                        embedBuilder.appendField("School",spells.get("school"),true);

                        SlamUtils.sendEmbed(channel, embedBuilder.build());
                        break;

                    }
                }
            }
        }
    }
    @EventSubscriber
    public void reactionAddEvent(ReactionAddEvent reactionAddEvent){
        //EmojiManager.getForAlias(reactionAddEvent.getReaction().getEmoji().getName());
       System.err.println( );
        long messageID = reactionAddEvent.getMessageID();
        IReaction reaction = reactionAddEvent.getReaction();
        IUser user= reactionAddEvent.getUser();
        if(messageID == spellListMessageID && !user.isBot()){
            if(EmojiParser.parseToAliases(reactionAddEvent.getReaction().getEmoji().toString()).equals(":arrow_left:")){

                spellListMessagePage--;
                if (spellListMessagePage < 0){
                    spellListMessagePage = spellEmbeds.size()-1;
                    RequestBuffer.request(() -> reaction.getMessage().edit(spellEmbeds.get(spellListMessagePage)));

                }else {

                    RequestBuffer.request(() -> reaction.getMessage().edit(spellEmbeds.get(spellListMessagePage)));
                }
            }
            if(EmojiParser.parseToAliases(reactionAddEvent.getReaction().getEmoji().toString()).equals(":arrow_right:")){
                spellListMessagePage++;
                if(spellListMessagePage > spellEmbeds.size()-1){
                    spellListMessagePage = 0;
                    RequestBuffer.request(() -> reaction.getMessage().edit(spellEmbeds.get(spellListMessagePage)));
                }else {
                    RequestBuffer.request(() -> reaction.getMessage().edit(spellEmbeds.get(spellListMessagePage)));
                }

            }
            if(EmojiParser.parseToAliases(reactionAddEvent.getReaction().getEmoji().toString()).equals(":x:")){
                reaction.getMessage().delete();
                messageID = 0;
            }
            RequestBuffer.request(() -> reaction.getMessage().removeReaction(user,reaction));


        }

    }


    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        String message = event.getMessage().getContent();
        IChannel channel = event.getChannel();
        message = message.toLowerCase();
        if(message.startsWith("dnd")){

            DndCommands(channel,message);
        }

        if(message.startsWith("roll")){
			StringBuilder stringBuilder = new StringBuilder();
			ArrayList<String> rollCommand = new ArrayList<String>(Arrays.asList(message.split("((?<=\\+|\\-|\\*|\\/)|(?=\\+|\\-|\\*|\\/))|\\s")));
			ArrayList<ArrayList<Integer>> dicerolled = new ArrayList<>();
			rollCommand.remove(0);
			int x = 0;
			for (int i = 0; i < rollCommand.size(); i++) {
				if (rollCommand.get(i).contains("d")){
					dicerolled.add(new ArrayList<>());
					int dieToRoll;
					String[] dice = rollCommand.get(i).split("[d]");
					//System.err.println(Arrays.toString(dice));
					dice[0] = dice[0].replace(" ","");
					int amountofDice = Integer.valueOf(dice[0]);
					dieToRoll = Integer.valueOf(dice[1]);

					int total = 0;
					for (int j = 0; j < amountofDice; j++) {
						dicerolled.get(x).add(ThreadLocalRandom.current().nextInt(1, dieToRoll+1));

					}
					stringBuilder = new StringBuilder();
					for (int j = 0; j < dicerolled.get(x).size(); j++) {
						stringBuilder.append(dicerolled.get(x).get(j).toString());
						if(j != dicerolled.get(x).size()-1)
							stringBuilder.append("+");

					}
					System.err.println(stringBuilder.toString());
					rollCommand.set(i,stringBuilder.toString());
					x++;
				}
			}
			System.err.println(rollCommand);
			stringBuilder = new StringBuilder();
			for (int i = 0; i < rollCommand.size() ; i++) {
				stringBuilder.append(rollCommand.get(i));
			}
			System.err.println(stringBuilder.toString());
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			try {
				Object eq =  engine.eval(stringBuilder.toString());
				SlamUtils.sendMessage(channel, stringBuilder.toString() + " = " + eq);
			} catch (ScriptException e) {
				e.printStackTrace();
			}


		}


    }
}
