package com.astelon.astsfancyfish.fish.conditions;

public class TimePeriod {

    private final int startHour;
    private final int endHour;

    public TimePeriod(int startHour, int endHour) {
        if (isInvalid(startHour))
            throw new IllegalArgumentException("Invalid start hour: " + startHour);
        if (isInvalid(endHour))
            throw new IllegalArgumentException("Invalid end hour: " + endHour);
        if (startHour < endHour) {
            this.startHour = startHour;
            this.endHour = endHour;
        } else {
            this.startHour = endHour;
            this.endHour = startHour;
        }
    }

    private boolean isInvalid(int hour) {
        return hour < 0 || hour >= 24;
    }

    public boolean isWithin(int hour) {
        return startHour <= hour && hour <= endHour;
    }

    public static TimePeriod fromText(String text) {
        String[] hours = text.split("-");
        if (hours.length != 2)
            throw new IllegalArgumentException("Invalid time period format " + text);
        int startHour = Integer.parseInt(hours[0]);
        int endHour = Integer.parseInt(hours[1]);
        return new TimePeriod(startHour, endHour);
    }
}
