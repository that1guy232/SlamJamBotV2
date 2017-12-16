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
import sx.blah.discord.handle.impl.obj.Reaction;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keith on 11/24/2017.
 */
public class StarFinderCommands {
    List<CSVRecord> starfinderFeats;
    ArrayList<EmbedObject> featEmbeds;

    long starFinderFeatsMessageID;
    int starFinderFeatsMessagePage;

    public StarFinderCommands(){

        try {
            starfinderFeats = new ArrayList<>(CSVFormat.RFC4180.withFirstRecordAsHeader().parse(new FileReader("starfinder_feats.csv")).getRecords());
        } catch (IOException e) {
            e.printStackTrace();
        }

        buildFeatList();
    }

    private void buildFeatList() {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        featEmbeds = new ArrayList<>();
        List<List<CSVRecord>> featnamesLists = Lists.partition(starfinderFeats,25);

        StringBuilder stringBuilder = new StringBuilder();




        for (int i = 0; i < featnamesLists.size(); i++) {
            embedBuilder.withTitle("Page"+ (i+1) + "/" + featnamesLists.size());
            for (int j = 0; j < featnamesLists.get(i).size(); j++) {

                stringBuilder.append(featnamesLists.get(i).get(j).get("Name"));
                stringBuilder.append("\n");
            }
            embedBuilder.appendField("Feats",stringBuilder.toString(),false);

            embedBuilder.withFooterText("To lern more about a feat type \"starfinder feat name\"");

            stringBuilder.delete(0,stringBuilder.length());

            featEmbeds.add(embedBuilder.build());

            embedBuilder.clearFields();
        }
    }


    private void starFinderCommands(IChannel channel, String message){
        String starfinderMessage[] = message.split("\\s");

        if(starfinderMessage[1].equals("feats")){
            ArrayList<String> emojiList = new ArrayList<>();
            emojiList.add("arrow_left");
            emojiList.add("arrow_right:");
            emojiList.add("x");

            if(starFinderFeatsMessageID != 0){
                channel.getMessageByID(starFinderFeatsMessageID).delete();
            }

            starFinderFeatsMessageID = SlamUtils.sendEmbedWithReactions(channel,featEmbeds.get(0));
            starFinderFeatsMessagePage = 0;

            RequestBuffer.request(() -> channel.getMessageByID(starFinderFeatsMessageID).addReaction(EmojiManager.getForAlias(emojiList.get(0)))).get();
            RequestBuffer.request(() -> channel.getMessageByID(starFinderFeatsMessageID).addReaction(EmojiManager.getForAlias(emojiList.get(1)))).get();
            RequestBuffer.request(() -> channel.getMessageByID(starFinderFeatsMessageID).addReaction(EmojiManager.getForAlias(emojiList.get(2)))).get();




        }





        if(starfinderMessage[1].equals("feat")){
            if(starfinderMessage[2] != null){

                StringBuilder stringBuilder = new StringBuilder();




                for (int i = 2; i < starfinderMessage.length-1; i++) {
                    stringBuilder.append(starfinderMessage[i]);

                       stringBuilder.append(" ");

                }
                stringBuilder.append(starfinderMessage[starfinderMessage.length - 1]);
                for (CSVRecord starfinderFeat : starfinderFeats) {



                    if (starfinderFeat.get("Name").toLowerCase().contains(stringBuilder.toString())) {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.withTitle(starfinderFeat.get("Name"));
                        embedBuilder.withDescription(starfinderFeat.get("Description"));
                        embedBuilder.appendField("Prerequisites",starfinderFeat.get("Prerequisites"),false);
                        embedBuilder.appendField("Benefit",starfinderFeat.get("Benefit"),false);
                        SlamUtils.sendEmbed(channel,embedBuilder.build());
                        break;

                    }
                }
            }
        }

    }

    @EventSubscriber
    public void reactionAddEvent(ReactionAddEvent reactionAddEvent){
        System.out.println(reactionAddEvent.getReaction().getEmoji().isUnicode());
        long messageID = reactionAddEvent.getMessageID();
        IReaction reaction = reactionAddEvent.getReaction();
        IUser user= reactionAddEvent.getUser();
        if(messageID == starFinderFeatsMessageID && !user.isBot()){
            if(EmojiParser.parseToAliases(reactionAddEvent.getReaction().getEmoji().toString()).equals(":arrow_left:")){
                System.err.println("TES TEST TEST TEST TEST");
                starFinderFeatsMessagePage--;
                if (starFinderFeatsMessagePage < 0){
                    starFinderFeatsMessagePage = 3;
                    RequestBuffer.request(() -> reaction.getMessage().edit(featEmbeds.get(starFinderFeatsMessagePage)));

                }else {

                    RequestBuffer.request(() -> reaction.getMessage().edit(featEmbeds.get(starFinderFeatsMessagePage)));
                }
            }
            if(EmojiParser.parseToAliases(reactionAddEvent.getReaction().getEmoji().toString()).equals(":arrow_right:")){
                starFinderFeatsMessagePage++;
                if(starFinderFeatsMessagePage > featEmbeds.size()-1){
                    starFinderFeatsMessagePage = 0;
                    RequestBuffer.request(() -> reaction.getMessage().edit(featEmbeds.get(starFinderFeatsMessagePage)));
                }else {
                    RequestBuffer.request(() -> reaction.getMessage().edit(featEmbeds.get(starFinderFeatsMessagePage)));
                }

            }


            if(EmojiParser.parseToAliases(reactionAddEvent.getReaction().getEmoji().toString()).equals(":x:")){
                reaction.getMessage().delete();
                starFinderFeatsMessageID = 0;
            }
            RequestBuffer.request(() -> reaction.getMessage().removeReaction(user,reaction));


        }

    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        String message = event.getMessage().getContent();
        IChannel channel = event.getChannel();
        message = message.toLowerCase();
        if(message.startsWith("starfinder")){


            starFinderCommands(channel,message);
        }
    }
}
