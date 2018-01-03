package com.tree.slamJamBotV2;

import com.google.gson.stream.JsonReader;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Keith on 11/18/2017.
 */
public class RealCommands implements CommandExecutor {





    @Command(aliases = "help",async = true)
    public String help(String args[]){
        StringBuilder stringBuilder = new StringBuilder();
        if(args.length > 0){
            if (args[0].equals("real")){
                stringBuilder.append("StarCheat : Links the StarFinder cheat sheet");
                stringBuilder.append("\n");
                stringBuilder.append("VampCheat : Links the Vampire the Masquerade cheat sheet ");
                stringBuilder.append("\n");
                stringBuilder.append("Roll : Dice roller to use do (amountOfDice)4D20(diceNumber) and then + or - number.");
                return stringBuilder.toString();

            }
        }
        return "I REEEEEE I AAAAAAAAA but I also Easy Breezy Beautiful Crocodile";
    }

    @Command(aliases = "vampcheat", async =  true)
    public String vampCheat(){
        return "https://docs.google.com/document/d/17QWOq7o9fdCFIl-LAT1VCI32ZqgLAG2uvc-j22oMvos/edit";
    }

    @Command(aliases = "starcheat", async =  true)
    public String starCheat(){
        return  " https://drive.google.com/open?id=1rUgfzCEqdaLkO0bdLK2bVtNHF2Wtm-QoqTqUTfiOnZ8";
    }

    @Command(aliases = "roll",async =  true)
    public String rollDice(String args[]){
        if(args.length < 1){
            return "";
        }else {
            try {
                ArrayList<Integer> diceToRoll = new ArrayList<>();
                int dieToRoll;

                if(args[0].contains("d")){

                    String[] dice = args[0].split("[d]");
                    int amountofDice = Integer.valueOf(dice[0]);
                    dieToRoll = Integer.valueOf(dice[1]);

                    int totalRolled = 0;
                    for (int i = 0; i < amountofDice; i++) {
                        diceToRoll.add(ThreadLocalRandom.current().nextInt(1, dieToRoll+1));
                    }
                    for (Integer aDiceToRoll : diceToRoll) {
                        totalRolled += aDiceToRoll;
                    }

                    StringBuilder mb = new StringBuilder();

                    if(args.length > 1){
                        mb.append("Rolled: ");
                        if(args[1].equals("+")){
                            int add = Integer.parseInt(args[2]);
                            if(diceToRoll.size() > 1){
                                for (int i = 0; i < diceToRoll.size(); i++) {
                                    if(i == diceToRoll.size()-1){
                                        mb.append(String.valueOf(diceToRoll.get(i)));
                                    }else {
                                        mb.append(diceToRoll.get(i) + "+");
                                    }
                                }
                                mb.append(" For a total of: " + (totalRolled+add));
                                return mb.toString();
                            }else {
                                return "Rolled: "+totalRolled+ " For a total of: "+(totalRolled+add);
                            }
                        }else if(args[1].equals("-")){
                            int sub = Integer.parseInt(args[2]);
                            if(diceToRoll.size() > 1){
                                for (int i = 0; i < diceToRoll.size(); i++) {
                                    if(i == diceToRoll.size()-1){

                                        mb.append(String.valueOf(diceToRoll.get(i)));
                                    }else {
                                        mb.append(diceToRoll.get(i) + "+");

                                    }
                                }
                                mb.append(" For a total of: " + (totalRolled+sub));
                                return mb.toString();
                            }else {

                                return "Rolled: "+totalRolled+ " For a total of: "+(totalRolled-sub);
                            }
                        }
                    }else {
                        for (int i = 0; i < diceToRoll.size(); i++) {
                            if(i == diceToRoll.size()-1){

                                mb.append(String.valueOf(diceToRoll.get(i)));
                            }else {
                                mb.append(diceToRoll.get(i) + "+");

                            }
                        }
                        mb.append(" For a total of: " + totalRolled);
                        return mb.toString();
                    }
                }else {
                    dieToRoll = Integer.parseInt(args[0]);
                    int randomNum = ThreadLocalRandom.current().nextInt(1, dieToRoll+1);
                    if(args.length > 1){
                        if(args[1].equals("+")){
                            int add = Integer.parseInt(args[2]);
                            return "Rolled: "+randomNum+ " For a total of: "+(randomNum+add);
                        }else if(args[1].equals("-")){
                            int sub = Integer.parseInt(args[2]);
                            return "Rolled: "+randomNum+ " For a total of: "+(randomNum+sub);
                        }
                    }
                    return "Rolled: "+ randomNum;
                }

            }catch (NumberFormatException e){
                return "";
            }
        }
        return "";
    }

   // @Command(aliases = "r6stats", async = true)
    public void r6stats(String args[],IChannel channel){


        try {
            String urlString = "https://r6db.com/api/v2/players?name=a_pinecone_man&platform=pc&exact=false";
            URL url = new URL(urlString);

            URLConnection connection = url.openConnection();

            connection.setRequestProperty("x-app-id","SlamJamBot");

            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");


            InputStream is = connection.getInputStream();
            JsonReader jsonReader = new JsonReader(new InputStreamReader(is));
            String id = "1";
            jsonReader.beginArray();
            jsonReader.beginObject();
            while (jsonReader.hasNext()){
                String name = jsonReader.nextName();
                if(name.equals("id")){
                    id = jsonReader.nextString();
                }else {
                    jsonReader.skipValue();
                }
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.withTitle("test");
            embedBuilder.withThumbnail("https://uplay-avatars.s3.amazonaws.com/"+ id+ "/default_146_146.png");
            SlamUtils.sendEmbed(channel,embedBuilder.build());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
