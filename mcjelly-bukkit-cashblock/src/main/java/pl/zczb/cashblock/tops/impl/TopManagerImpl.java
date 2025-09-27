package pl.zczb.cashblock.tops.impl;


import pl.zczb.Cashblock;
import pl.zczb.Controller;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.tops.api.TopManager;
import pl.zczb.cashblock.tops.comparator.TopComparator;
import pl.zczb.cashblock.tops.enums.TopType;
import pl.zczb.packets.TopTrybPacket;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;

public class TopManagerImpl implements TopManager {
    private final Cashblock plugin;

    private final Map<TopType, TopList> tops;
    static DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final Map<TopType, Comparator> comparatorMap;

    public TopManagerImpl(Cashblock plugin) {
        List<Top> topLists;
        int i;
        for (this.plugin = plugin, this.tops = new HashMap<>(), topLists = new ArrayList<>(), i = 0; i < 16; ) {
            topLists.add(new Top("Brak", String.valueOf(i)));
            i++;
        }
        for (TopType topType : TopType.values())
            this.tops.put(topType, new TopList(new ArrayList<>(topLists)));
        this.comparatorMap = new HashMap<>();
        this.comparatorMap.put(TopType.PLN, new TopComparator.UserByPlnComparator());
        this.comparatorMap.put(TopType.LVL, new TopComparator.UserByLvlComparator());
        this.comparatorMap.put(TopType.STONE, new TopComparator.UserByStoneComparator());
        this.comparatorMap.put(TopType.PRESTIGE, new TopComparator.UserByPrestizComparator());

    }

    public TopList getTopList(TopType topType) {
        return this.tops.get(topType);
    }

    public void replaceTop(TopType topType, TopList topList) {
        this.tops.put(topType, topList);
    }

    public void refreshTops() {
        if (!Cashblock.getCashblockConfig().getSector_name().equals("cashblock_1"))
            return;

        List<UserDataModel> sortedUsers = new ArrayList<>(Cashblock.getInstance().getUserHandler().getUsers());

        this.comparatorMap.forEach((topType, comparator) -> {
            List<Top> tops = new LinkedList<>();

            sortedUsers.sort(comparator);
            for (int i = 0; i < 27; i++) {
                if (sortedUsers.size() > i) {
                    UserDataModel user = sortedUsers.get(i);
                    Object result = null;
                    try {
                        Method method = user.getClass().getMethod(topType.getMethodName());
                        result = method.invoke(user);

                        if (result instanceof Integer || result instanceof Long) {

                            tops.add(i, new Top(user.getNick(), String.valueOf(result)));
                        } else if (result instanceof Double) {

                            tops.add(i, new Top(user.getNick(), String.valueOf(decimalFormat.format(result) + " vPLN")));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            Controller.getInstance().getRedis().publish("CH|cashblock_tryb", new TopTrybPacket(topType, new TopList(tops)));
        });
    }
}