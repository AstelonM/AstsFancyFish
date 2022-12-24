package com.astelon.astsfancyfish.utils;

import java.util.Random;
import java.util.TreeMap;

public class WeightedList<T> {

    private final TreeMap<Integer, T> map;
    private int total;

    public WeightedList() {
        map = new TreeMap<>();
    }

    public void put(int weight, T value) {
        total += weight;
        map.put(total, value);
    }

    public T getRandom(Random random) {
        int weight = random.nextInt(total);
        return map.higherEntry(weight).getValue();
    }

    public boolean isEmpty() {
        return total == 0;
    }
}
