package com.tree.slamJamBotV2.Vampire;

public class PredatorType {
    String Name;
    String Description;
    int Humanity;
    int BloodPotency;
    String page;
    String[] Discipline;
    String[] Merits;
    String[] Flaws;
    Specialties[] Specialties;

    public PredatorType(String name, String description, int humanity, int bloodPotency, String page, String[] discipline, String[] merits, String[] flaws, Specialties[] specialties) {
        this.Name = name;
        Description = description;
        Humanity = humanity;
        BloodPotency = bloodPotency;
        this.page = page;
        Discipline = discipline;
        Merits = merits;
        Flaws = flaws;
        Specialties = specialties;
    }
}
class Specialties{
    String Skill;
    String Specialty;
}
