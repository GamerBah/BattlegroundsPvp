package me.gamerbah.Utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

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

    public static long punishmentTimeRemaining(LocalDateTime expiration) {
        int hourNow = LocalDateTime.now().getHour(), minuteNow = LocalDateTime.now().getMinute(), secondNow = LocalDateTime.now().getSecond();
        int hourExp = expiration.getHour(), minuteExp = expiration.getMinute(), secondExp = expiration.getSecond();
        long millseconds = ((hourExp - hourNow) * 60 * 60 * 1000) + ((minuteExp - minuteNow) * 60 * 1000) + ((secondExp - secondNow) * 1000);
        return millseconds;
    }

    public static String toString(long milliseconds, boolean shortened) {
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

        if (shortened) {
            if (weeks <= 0) {
                if (days <= 0) {
                    if (hours <= 0) {
                        if (minutes <= 0) {
                            return seconds + sl;
                        }
                        return minutes + " min" + (seconds == 0 ? "" : ", " + seconds + " sec");
                    }
                    return hours + (hours == 1 ? " hr" : " hrs") + (minutes == 0 ? "" : ", " + minutes + " min");
                }
                return days + dl + ", " + (hours == 1 ? " hr" : " hrs") + ", " + minutes + " min";
            }
            return weeks + wl + ", " + days + dl;
        } else {
            if (weeks <= 0) {
                if (days <= 0) {
                    if (hours <= 0) {
                        if (minutes <= 0) {
                            return seconds + sl;
                        }
                        return minutes + ml + (seconds == 0 ? "" : " and " + seconds + sl);
                    }
                    return hours + hl + (minutes == 0 ? "" : " and " + minutes + ml);
                }
                return days + dl + ", " + hours + hl + " and " + minutes + ml;
            }
            return weeks + wl + " and " + days + dl;
        }
    }
}