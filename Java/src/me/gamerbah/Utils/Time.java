package me.gamerbah.Utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum Time {
    SECONDS(1000, 's'),
    MINUTES(SECONDS.milliseconds * 60, 'm'),
    HOURS(MINUTES.milliseconds * 60, 'h'),
    DAYS(HOURS.milliseconds * 24, 'd'),
    WEEKS(DAYS.milliseconds * 7, 'w');

    @Getter
    private final long milliseconds;
    private final char id;

    public static Time fromId(char id) {
        for (Time time : values()) {
            if (id == time.id) {
                return time;
            }
        }
        return null;
    }

    public static String toString(long milliseconds) {
        long seconds = (milliseconds / SECONDS.milliseconds) % 60;
        long minutes = (milliseconds / MINUTES.milliseconds) % 60;
        long hours = (milliseconds / HOURS.milliseconds) % 24;
        long days = (milliseconds / DAYS.milliseconds) % 24;
        long weeks = (milliseconds / WEEKS.milliseconds) % 7;
        String sl;
        String ml;
        String hl;
        String dl;
        String wl;

        if (seconds == 1) {
            sl = " second";
        } else sl = " seconds";
        if (minutes == 1) {
            ml = " minute";
        } else ml = " minutes";
        if (hours == 1) {
            hl = " hour";
        } else hl = " hours";
        if (days == 1) {
            dl = " day";
        } else dl = " days";
        if (weeks == 1) {
            wl = " week";
        } else wl = " weeks";

        if (weeks <= 0) {
            if (days <= 0) {
                if (hours <= 0) {
                    if (minutes <= 0) {
                        return seconds + sl;
                    }
                    return minutes + ml + " and " + seconds + sl;
                }
                return hours + hl + " and " + minutes + ml;
            }
            return days + dl + ", " + hours + hl + " and " + minutes + ml;
        }
        return weeks + wl + " and " + days + dl;
    }
}