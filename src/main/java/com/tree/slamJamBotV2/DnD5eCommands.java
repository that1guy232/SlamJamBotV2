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

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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



    private void spellFinderCommands(IChannel channel, String message) {
        String spellMessage[] = message.split("\\s");

        if (spellMessage[1].equals("spells")) {
            ArrayList<String> emojiList = new ArrayList<>();
            emojiList.add("arrow_left");
            emojiList.add("arrow_right:");
            emojiList.add("x");



            if (spellListMessageID != 0) {
                channel.getMessageByID(spellListMessageID).delete();
            }

            spellListMessageID = SlamUtils.sendEmbedWithReactions(channel, spellEmbeds.get(0));
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


            spellFinderCommands(channel,message);
        }
    }
}
