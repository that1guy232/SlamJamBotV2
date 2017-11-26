package com.tree.slamJamBotV2;

import com.google.common.collect.Lists;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

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
                System.out.println("Test#: "+i+"FeatName: "+featnamesLists.get(i).get(j).get("Name"));
                stringBuilder.append(featnamesLists.get(i).get(j).get("Name"));
                stringBuilder.append("\n");
            }
            embedBuilder.appendField("Feats",stringBuilder.toString(),false);

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
            SlamUtils.sendEmbedWithReactions(channel,featEmbeds.get(0),emojiList);

            SlamUtils.sendMessage(channel," Here is a google link to all starfinder feats shit\n" + "https://docs.google.com/spreadsheets/d/1s3W_CTGPk7Wt8zr-sLRB4WI1MNqWGhchTvEItDBf3uE/");


        }





        if(starfinderMessage[1].equals("feat")){
            if(starfinderMessage[2] != null){
                for (CSVRecord starfinderFeat : starfinderFeats) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 2; i < starfinderMessage.length; i++) {
                        stringBuilder.append(starfinderMessage[i]);
                        stringBuilder.append(" ");
                    }

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
        System.out.println(reactionAddEvent.getReaction().toString());

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
