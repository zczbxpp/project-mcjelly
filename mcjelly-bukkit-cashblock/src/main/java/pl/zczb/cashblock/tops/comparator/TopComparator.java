package pl.zczb.cashblock.tops.comparator;


import pl.zczb.cashblock.database.user.models.UserDataModel;

import java.util.Comparator;

public class TopComparator {

    public static class UserByPlnComparator implements Comparator<UserDataModel> {
        public int compare(UserDataModel o1, UserDataModel o2) {
            Double user1 = o1.getPln();
            Double user2 = o2.getPln();
            return user2.compareTo(user1);
        }
    }

    public static class UserByStoneComparator implements Comparator<UserDataModel> {
        public int compare(UserDataModel o1, UserDataModel o2) {
            Integer user1 = o1.getStone();
            Integer user2 = o2.getStone();
            return user2.compareTo(user1);
        }
    }

    public static class UserByLvlComparator implements Comparator<UserDataModel> {
        public int compare(UserDataModel o1, UserDataModel o2) {
            Integer user1 = o1.getLvl();
            Integer user2 = o2.getLvl();
            return user2.compareTo(user1);
        }
    }

    public static class UserByPrestizComparator implements Comparator<UserDataModel> {
        public int compare(UserDataModel o1, UserDataModel o2) {
            Integer user1 = o1.getPrestigeLevel();
            Integer user2 = o2.getPrestigeLevel();
            return user2.compareTo(user1);
        }
    }
}