package com.tree.slamJamBotV2;

public class Item {
    public String getName() {
        return name;
    }

    public String getLimits() {
        return limits;
    }

    public String getAttunement() {
        return attunement;
    }

    public String getRarity() {
        return rarity;
    }

    public String getPage() {
        return page;
    }

    public String getType() {
        return type;
    }

    String name;
    String limits;
    String attunement;
    String rarity;
    String page;
    String type;


    public Item(String name, String limits, String attunement, String rarity, String page, String type) {
        this.name = name;
        this.limits = limits;
        this.attunement = attunement;
        this.rarity = rarity;
        this.page = page;
        this.type = type;
    }
}
