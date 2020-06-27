package com.tree.slamJamBotV2.Vampire;

import com.google.gson.Gson;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javatuples.Triplet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Vampire implements MessageCreateListener, ReactionAddListener {

    Gson gson = new Gson();

    Discipline[] disciplines;

    PredatorType[] predatorTypes;


    HashMap<Long, Triplet> savedMessageIDByChannel;


    public Vampire(){

        savedMessageIDByChannel = new HashMap<>();


        try {
            disciplines = gson.fromJson(new FileReader("VTM5Disaplins.json"), Discipline[].class);
            System.err.println("AMount of Discipins: " + disciplines.length);
            //predatorTypes = gson.fromJson(new FileReader("VTMPredatorTypes.json"), PredatorType[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        String messageContent = messageCreateEvent.getMessageContent();


        ArrayList<EmbedBuilder> embedBuilders = new ArrayList<>();


        if(messageContent.startsWith("!sv")){
            messageContent = messageContent.replace("!sv ","");

            for (int i = 0; i < disciplines.length; i++) {
                Discipline cd = disciplines[i];
                System.err.println(disciplines[i].contains(messageContent));
                if(disciplines[i].contains(messageContent)){
                    EmbedBuilder eb = new EmbedBuilder();



                    eb.setTitle(cd.Name);
                    eb.addField("Discipline",cd.Discipline + " lvl " + cd.LVL );
                    if( cd.Target != null && cd.Caster != null &&!cd.Target.equals("") && !cd.Caster.equals("")){
                        eb.addField("Rolls", "Rouse? "  + cd.Cost + ". Roll: " + cd.Caster + " vs " + cd.Target);
                    }else if (cd.Caster != null && !cd.Caster.equals("")){
                        eb.addField("Rolls", "Rouse? " + cd.Cost + ". Roll: " + cd.Caster);
                    }else {
                        eb.addField("Rolls", "Rouse? " + cd.Cost);
                    }

                    eb.addField("Notes", "Duration: " + cd.Duration + "\nNotes: " + cd.Notes);



                    eb.setFooter("Hi!");
                    embedBuilders.add(eb);



                }

            }
            if(embedBuilders.size() > 0){

                messageCreateEvent.getChannel().sendMessage(embedBuilders.get(0)).whenComplete((message, throwable) -> {
                    System.err.println("test");
                    message.addReaction("😃");
                    message.addReactions("⬅", "➡");
                    savedMessageIDByChannel.put(messageCreateEvent.getChannel().getId(), new Triplet(embedBuilders.toArray(),message.getId(),0) );
                });
//new Pair(embedBuilders.toArray(),message.getId())

            }




        }
    }


    //Fuck unicode emojio characters Here i was trying to remake the spellist command for a search quraey on vampire disaplines.
    @Override
    public void onReactionAdd(ReactionAddEvent reactionAddEvent) {

        long channelID = reactionAddEvent.getChannel().getId();

        if(savedMessageIDByChannel.containsKey(reactionAddEvent.getChannel().getId())){

            if(reactionAddEvent.getEmoji().asUnicodeEmoji().equals("⬅") ){
                long messageID = (Long) savedMessageIDByChannel.get(reactionAddEvent.getChannel().getId()).getValue1();

//                reactionAddEvent.getChannel().getMessageById(messageID).whenComplete((message, throwable) -> {
//                    int cp = (Integer) savedMessageIDByChannel.get(channelID).getValue2();
//                    EmbedBuilder[] embedBuilders = (EmbedBuilder[]) savedMessageIDByChannel.get(channelID).getValue(0);
//                    cp--;
//                    if(cp < 0){
//                        cp = embedBuilders.length-1;
//                    }
//                    System.err.println("???????");
//                    message.edit(embedBuilders[cp]);
//
//                });


            }
        }
    }
}
