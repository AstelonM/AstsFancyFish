package com.astelon.astsfancyfish.fish.conditions;

import java.util.List;

public class FishConditions {

    private final List<TimePeriod> timePeriods;
    //TODO multiple weather support
    private final Weather weather;
    private final boolean surface;

    public FishConditions(List<TimePeriod> timePeriods, Weather weather, boolean surface) {
        this.timePeriods = timePeriods;
        this.weather = weather;
        this.surface = surface;
    }

    public boolean match(int hour, Weather currentWeather, boolean isOnSurface) {
        boolean rightTime = false;
        for (TimePeriod timePeriod: timePeriods) {
            if (timePeriod.isWithin(hour)) {
                rightTime = true;
                break;
            }
        }
        boolean rightPlace = isOnSurface || !surface;
        return rightTime && weather == currentWeather && rightPlace;
    }
}
