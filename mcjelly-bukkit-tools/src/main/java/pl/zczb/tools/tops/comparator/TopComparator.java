package pl.zczb.tools.tops.comparator;

import pl.zczb.tools.database.user.models.UserDataModel;

import java.util.Comparator;


public class TopComparator {
    public static class UserByTimeComparator implements Comparator<UserDataModel> {
        public int compare(UserDataModel o1, UserDataModel o2) {
            Long user1 = Long.valueOf(o1.getPlayerTime());
            Long user2 = Long.valueOf(o2.getPlayerTime());
            return user2.compareTo(user1);
        }
    }

    public static class UserByParkourComparator implements Comparator<UserDataModel> {
        public int compare(UserDataModel o1, UserDataModel o2) {
            long time1 = o1.getParkourTime();
            long time2 = o2.getParkourTime();

            if (time1 == 0L && time2 == 0L) return 0;
            if (time1 == 0L) return 1;
            if (time2 == 0L) return -1;

            return Long.compare(time1, time2);
        }
    }
}


