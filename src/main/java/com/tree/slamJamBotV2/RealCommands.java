package com.tree.slamJamBotV2;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Keith on 11/18/2017.
 */
public class RealCommands implements CommandExecutor {
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

                                return "Rolled: "+totalRolled+ " For a total of: "+(totalRolled+sub);
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
}
