package com.astelon.astsfancyfish.managers;

import com.astelon.astsfancyfish.fish.BaseFish;

import java.util.HashMap;

public class FishManager {

    private HashMap<String, BaseFish> fish;

    public void setFish(HashMap<String, BaseFish> fish) {
        this.fish = fish;
    }

    public BaseFish getFish(String name) {
        return fish.get(name);
    }
}
