package com.tree.slamJamBotV2.memeCommands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tree.slamJamBotV2.SlamUtils;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class MemeCommands  {

    ArrayList<MemeCommand> memeCommands;
    //MemeCommand memeCommands[];
    Gson gson;
    public MemeCommands(){

        MemeCommand tmp[];

        gson = new GsonBuilder().setPrettyPrinting().create();
        FileReader fr = null;
        try {
            fr = new FileReader("Commands.json");
          tmp = gson.fromJson(fr,MemeCommand[].class);

          memeCommands = new ArrayList<>(Arrays.asList(tmp));



            System.err.println(gson.toJson(memeCommands));



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tmp = null;


    }


    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContent().toLowerCase();

        String[] command = spiltMessage(message);

        if (message.startsWith("!addcommand")) {
            //!addcommand names: Test, Test2 message: this would do that emotes: :thumbsup: :sweat_drops: exact: true/false default true



            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> commandMessageWords = new ArrayList<>();
            String commandmessage = null;
            ArrayList<String> tmpemotes = new ArrayList<>();
            String[] filePath = null;
            boolean exact = true;

            /*
                0 = none
                1 = names
                2 = message
                3 = emotes
                4 = exact;
             */
            int state = 0;
            boolean justset = false;
            for (int i = 0; i < command.length; i++) {
                if (command[i].equalsIgnoreCase("names:")) {
                    justset = true;
                    state = 1;
                }
                if (command[i].equalsIgnoreCase("message:")) {
                    justset = true;
                    state = 2;
                }
                if (command[i].equalsIgnoreCase("emotes:")) {
                    justset = true;
                    state = 3;
                }
                if (command[i].equalsIgnoreCase("exact:")) {
                    justset = true;
                    state = 4;
                }


                if (!justset) {
                    switch (state) {
                        case 1:
                            names.add(command[i]);
                            break;
                        case 2:
                            commandMessageWords.add(command[i]);
                            break;
                        case 3:
                            tmpemotes.add(command[i]);
                            break;
                        case 4:
                            exact = Boolean.parseBoolean(command[i]);
                            break;
                    }
                }
                justset = false;


            }

            if (names.size() <= 0) {
                SlamUtils.sendMessage(event.getChannel(), "You must have at least one name for the command to trigger.");
            } else {

                if (event.getMessage().getAttachments().size() > 0) {
                    filePath = new String[]{event.getMessage().getAttachments().get(0).getFilename()};
                    try {

                        URL url = new URL(event.getMessage().getAttachments().get(0).getUrl());

                        URLConnection hc = url.openConnection();

                        hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

                        ImageIO.write(ImageIO.read(hc.getInputStream()), "png", new File(event.getMessage().getAttachments().get(0).getFilename()));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }



                ArrayList<String> tmpnms = new ArrayList<>();

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < names.size(); i++) {
                    stringBuilder.append(names.get(i));
                    if (names.get(i).contains(",") || i == names.size() - 1) {
                        tmpnms.add(stringBuilder.toString().replace(",", ""));
                        stringBuilder = new StringBuilder();
                    } else {
                        stringBuilder.append(" ");
                    }

                }
                if (commandMessageWords.size() > 0) {
                    commandmessage = makeSentence(commandMessageWords);
                } else {
                    commandmessage = null;
                }
                String[] emotes;
                if (tmpemotes.size() > 0) {
                    emotes = tmpemotes.toArray(new String[0]);
                } else {
                    emotes = null;
                }
                memeCommands.add(new MemeCommand(tmpnms.toArray(new String[0]), commandmessage, emotes, exact, filePath));

                System.err.println(gson.toJson(memeCommands.get(memeCommands.size() - 1)));


                saveCommands();

            }
        }else  if(message.startsWith("!removecommand")){
            for (MemeCommand memeCommand : memeCommands) {
                for (int j = 0; j < memeCommand.names.length; j++){
                    if(memeCommand.names[j].equalsIgnoreCase(command[1])){
                        memeCommands.remove(memeCommand);
                        saveCommands();
                        break;
                    }
                }
            }


        }else {


            for (int i = 0; i < memeCommands.size(); i++) {
                if (memeCommands.get(i).exact == null && memeCommands.get(i) != null) {
                    memeCommands.get(i).exact = true;
                }

                try {
                    for (int j = 0; j < memeCommands.get(i).names.length; j++) {
                        if (!memeCommands.get(i).exact && message.contains(memeCommands.get(i).names[j]) || containsWord(message, memeCommands.get(i).names[j])) {
                            if (memeCommands.get(i).emotes != null) {
                                for (int k = 0; k < memeCommands.get(i).emotes.length; k++) {

                                    int finalI = i;
                                    int finalK = k;
                                    RequestBuffer.request(() -> {
                                        try {

                                            event.getMessage().addReaction(EmojiManager.getForAlias(memeCommands.get(finalI).emotes[finalK]));
                                        } catch (DiscordException e) {
                                            e.printStackTrace();
                                        }
                                    });


                                }
                            }
                            if (memeCommands.get(i).message != null) {
                                if (memeCommands.get(i).message.contains("$mention")) {
                                    memeCommands.get(i).message = memeCommands.get(i).message.replace("$mention", event.getAuthor().mention());
                                }
                                SlamUtils.sendMessage(event.getChannel(), memeCommands.get(i).message);
                            }

                            if (memeCommands.get(i).filePaths != null) {
                                for (int k = 0; k < memeCommands.get(i).filePaths.length; k++) {
                                    SlamUtils.sendFile(event.getChannel(), new File(memeCommands.get(i).filePaths[k]));
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {

                }
            }
        }
    }

    private void saveCommands() {
        try {
            FileWriter fileWriter = new FileWriter("Commands.json");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(gson.toJson(memeCommands));
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean containsWord(String sentence, String word){
        String regex = ".*\\b" + Pattern.quote(word) + "\\b.*";
        return sentence.matches(regex);
    }

    String[] spiltMessage(String message){
        String regex = "\\s";
        return message.split(regex);
    }

    String makeSentence(ArrayList<String> strings){
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}


class MemeCommand{
    String emotes[];

    public MemeCommand(String[] names,String message,  String[] emotes, Boolean exact, String[] filePaths) {
        this.names = names;
        this.message = message;
        this.emotes = emotes;
        this.filePaths = filePaths;
        this.exact = exact;
    }

    String names[];
    String message;
    String filePaths[];
    Boolean exact;



}

