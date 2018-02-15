package com.tree.slamJamBotV2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
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





}
