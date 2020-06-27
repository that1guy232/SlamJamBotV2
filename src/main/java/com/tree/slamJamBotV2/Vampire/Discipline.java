package com.tree.slamJamBotV2.Vampire;

public class Discipline {
    String Name;
    String Discipline;
    String Amalgam;
    String Res;
    String Cost;
    String castRoll;
    String TargetRoll;
    String Duration;
    String PG;
    String Notes;
    String Caster;

    public Discipline(String name, String discipline, String amalgam, String res, String cost, String castRoll, String targetRoll, String duration, String PG, String notes, String caster, String target, int LVL) {
        Name = name;
        Discipline = discipline;
        Amalgam = amalgam;
        Res = res;
        Cost = cost;
        this.castRoll = castRoll;
        TargetRoll = targetRoll;
        Duration = duration;
        this.PG = PG;
        Notes = notes;
        Caster = caster;
        Target = target;
        this.LVL = LVL;
    }

    String Target;
    int LVL;










    public boolean contains(String messageContent) {

        messageContent = messageContent.toLowerCase();

        System.err.println(messageContent);

        if(Name.toLowerCase().contains(messageContent) || Discipline.toLowerCase().contains(messageContent) || String.valueOf(LVL).toLowerCase().contains(messageContent)){
            return true;
        }
        return false;
    }
}
