package pl.zczb.tools.tops.impl;

import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.packets.TopPacket;
import pl.zczb.redis.packet.Packet;
import pl.zczb.tools.database.user.models.UserDataModel;
import pl.zczb.tools.tops.api.TopManager;
import pl.zczb.tools.tops.comparator.TopComparator;
import pl.zczb.tools.tops.enums.TopType;

import java.lang.reflect.Method;
import java.util.*;

public class TopManagerImpl implements TopManager {
    private final Tools plugin;

    public TopManagerImpl(Tools plugin) {
        List<Top> topLists;
        int i;
        for (this.plugin = plugin, this.tops = new HashMap<>(), topLists = new ArrayList<>(), i = 0; i < 27; ) {
            topLists.add(new Top("Brak", String.valueOf(i)));
            i++;
        }
        for (TopType topType : TopType.values())
            this.tops.put(topType, new TopList(new ArrayList<>(topLists)));
        this.comparatorMap = new HashMap<>();
        this.comparatorMap.put(TopType.PARKOUR, new TopComparator.UserByParkourComparator());
        this.comparatorMap.put(TopType.TIME, new TopComparator.UserByTimeComparator());
    }

    private final Map<TopType, TopList> tops;
    private final Map<TopType, Comparator> comparatorMap;

    public TopList getTopList(TopType topType) {
        return this.tops.get(topType);
    }

    public void replaceTop(TopType topType, TopList topList) {
        this.tops.put(topType, topList);
    }

    public void refreshTops() {
        if (!Tools.getSectorConfig().getCurrentSector().getSectorName().endsWith("_1")) {
            return;
        }
        List<UserDataModel> sortedUsers = new ArrayList<>(Tools.getInstance().getUserHandler().getUsers());

        this.comparatorMap.forEach((topType, comparator) -> {
            List<Top> tops = new LinkedList<>();

            sortedUsers.sort(comparator);

            for (int i = 0; i < 27; i++) {
                if (sortedUsers.size() > i) {
                    UserDataModel user = sortedUsers.get(i);

                    Object result = null;
                    try {
                        Method method = user.getClass().getMethod(topType.getMethodName(), new Class[0]);
                        result = method.invoke(user, new Object[0]);
                        tops.add(i, new Top(user.getNick(), String.valueOf(result)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new TopPacket(topType, new TopList(tops)));
        });
    }
}


