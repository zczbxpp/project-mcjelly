package pl.zczb.cashblock.tops.api;

import pl.zczb.cashblock.tops.enums.TopType;
import pl.zczb.cashblock.tops.impl.TopList;

public interface TopManager {
    TopList getTopList(TopType paramTopType);

    void replaceTop(TopType paramTopType, TopList paramTopList);

    void refreshTops();
}


