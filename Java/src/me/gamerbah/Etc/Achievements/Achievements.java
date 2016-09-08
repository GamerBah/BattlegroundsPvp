package me.gamerbah.Etc.Achievements;
/* Created by GamerBah on 9/4/2016 */


import lombok.AllArgsConstructor;
import lombok.Getter;

public class Achievements {

    public static Combat combatTypeFromName(String string) {
        for (Combat type : Combat.values()) {
            if (type.getName().equals(string)) {
                return type;
            }
        }
        return null;
    }

    @AllArgsConstructor
    @Getter
    public enum Combat {
        BRUTALITY_I("Brutality I", "Kill 100 Players", 100, 150, 25, null),
        BRUTALITY_II("Brutality II", "Kill 500 Players", 500, 300, 100, null),
        BRUTALITY_III("Brutality III", "Kill 1,000 Players", 1000, 600, 250, null),
        BRUTALITY_IV("Brutality IV", "Kill 2,500 Players", 2500, 1200, 500, null),
        BRUTALITY_V("Brutality V", "Kill 5,000 Players", 5000, 2400, 1000, null),
        BRUTALITY_MASTER("Brutality Mastery", "Kill 12,500 Players", 12500, 6000, 3250, "Killer"),

        VENGEFUL_I("Vengeful I", "Get revenge on 10 players", 10, 100, 20, null),
        VENGEFUL_II("Vengeful II", "Get revenge on 30 players", 30, 300, 60, null),
        VENGEFUL_III("Vengeful III", "Get revenge on 90 players", 90, 900, 180, null),
        VENGEFUL_IV("Vengeful IV", "Get revenge on 180 players", 180, 1800, 260, null),
        VENGEFUL_V("Vengeful V", "Get revenge on 300 players", 300, 3000, 520, null),
        VENGEFUL_MASTER("Vengeful Mastery", "Get revenge on 500 players", 500, 5000, 600, "Vengeful"),

        BUZZKILL_I("Buzzkill I", "End 1 Killstreak", 1, 150, 10, null),
        BUZZKILL_II("Buzzkill II", "End 3 Killstreaks", 3, 310, 20, null),
        BUZZKILL_III("Buzzkill III", "End 7 Killstreaks", 7, 500, 40, null),
        BUZZKILL_IV("Buzzkill IV", "End 15 Killstreaks", 15, 750, 80, null),
        BUZZKILL_V("Buzzkill V", "End 25 Killstreaks", 25, 1000, 150, null),
        BUZZKILL_MASTER("Buzzkill Mastery", "End 35 Killstreaks", 35, 1500, 225, "Buzzkill"),

        SADIST_I("Sadist I", "Go on a 5 killstreak", 5, 30, 15, null),
        SADIST_II("Sadist II", "Go on a 10 killstreak", 10, 65, 35, null),
        SADIST_III("Sadist III", "Go on a 15 killstreak", 15, 100, 50, null),
        SADIST_IV("Sadist IV", "Go on a 20 killstreak", 20, 145, 90, null),
        SADIST_V("Sadist V", "Go on a 25 killstreak", 25, 200, 135, null),
        SADIST_MASTER("Sadist Mastery", "Go on a 30 killstreak", 30, 350, 200, "Sadist"),

        DEATHKNELL("Deathknell", "Die a total of 1000 times", 1000, 999, 99, "Fragile");


        private String name;
        private String description;
        private int requirement;
        private int soulReward;
        private int coinReward;
        private String title;
    }

    @AllArgsConstructor
    @Getter
    public enum Challenge {
        CHALLENGER_I("Challenger I", "Complete a Daily Challenge", 1, 100, 10, null),
        CHALLENGER_II("Challenger II", "Complete 5 Daily Challenges", 5, 200, 25, null),
        CHALLENGER_III("Challenger II", "Complete 15 Daily Challenges", 15, 300, 40, null),
        CHALLENGER_IV("Challenger IV", "Complete 35 Daily Challenges", 35, 400, 55, null),
        CHALLENGER_V("Challenger V", "Complete 75 Daily Challenges", 75, 500, 75, null),
        CHALLENGER_MASTER("Challenger Mastery", "Complete 155 Daily Challenges", 150, 750, 125, null);

        private String name;
        private String description;
        private int requirement;
        private int soulReward;
        private int coinReward;
        private String title;
    }

    /*@AllArgsConstructor
    @Getter
    public enum Collection {


        private String name;
        private String description;
        private int requirement;
        private int soulReward;
        private int coinReward;
        private String title;
    }*/

    @AllArgsConstructor
    @Getter
    public enum Recruitment {
        RECRUITER_I("Recruiter I", "Recruit a friend", 1, 50, 10, null),
        RECRUITER_II("Recruiter II", "Recruit 5 friends", 5, 75, 25, null),
        RECRUITER_III("Recruiter III", "Recruit 10 friends", 10, 150, 50, null),
        RECRUITER_IV("Recruiter IV", "Recruit 15 friends", 15, 300, 75, null),
        RECRUITER_V("Recruiter V", "Recruit 20 friends", 20, 500, 125, null),
        RECRUITER_MASTER("Recruiter Mastery", "Recruit 30 friends", 30, 1000, 200, "Ambassador");

        private String name;
        private String description;
        private int requirement;
        private int soulReward;
        private int coinReward;
        private String title;
    }

}
