package com.tree.slamJamBotV2.memeCommands;

import com.google.gson.Gson;
import com.tree.slamJamBotV2.SlamUtils;
import com.vdurmont.emoji.EmojiManager;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Pattern;

public class MemeCommands  {

    MemeCommand memeCommands[];

    public MemeCommands(){


        System.err.println(containsWord("nito","nito"));


        Gson gson = new Gson();
        FileReader fr = null;
        try {
            fr = new FileReader("Commands");
          memeCommands =  gson.fromJson(fr,MemeCommand[].class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }




    }


    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        for (int i = 0; i < memeCommands.length; i++) {
            if(memeCommands[i].exact == null && memeCommands[i] != null){
                memeCommands[i].exact = true;
            }

            try {


                for (int j = 0; j < memeCommands[i].names.length; j++) {


                    if (!memeCommands[i].exact && event.getMessage().getContent().contains(memeCommands[i].names[j]) || containsWord(event.getMessage().getContent(), memeCommands[i].names[j])) {
                        System.err.println(memeCommands[i].names[j]);

                        if (memeCommands[i].emotes != null) {
                            for (int k = 0; k < memeCommands[i].emotes.length; k++) {

                                int finalI = i;
                                int finalK = k;
                                RequestBuffer.request(() -> {
                                    try {
                                        event.getMessage().addReaction(EmojiManager.getForAlias(memeCommands[finalI].emotes[finalK]));
                                    } catch (DiscordException e) {
                                        e.printStackTrace();
                                    }
                                });


                            }
                        }

                        if (memeCommands[i].message != null) {
                            if (memeCommands[i].message.contains("$mention")) {
                                memeCommands[i].message = memeCommands[i].message.replace("$mention", event.getAuthor().mention());
                            }
                            SlamUtils.sendMessage(event.getChannel(), memeCommands[i].message);
                        }

                        if (memeCommands[i].filePaths != null) {
                            for (int k = 0; k < memeCommands[i].filePaths.length; k++) {
                                SlamUtils.sendFile(event.getChannel(), new File(memeCommands[i].filePaths[k]));
                            }
                        }

                    }
                }
            }catch (NullPointerException e){

            }
        }

    }



    boolean containsWord(String sentence, String word){
        String regex = ".*\\b" + Pattern.quote(word) + "\\b.*";
        return sentence.matches(regex);
    }






}



class MemeCommand{
    String emotes[];
    String names[];
    String message;
    String filePaths[];
    Boolean exact;


}

