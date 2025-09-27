package pl.zczb.tools.tops.api;

import pl.zczb.tools.tops.enums.TopType;
import pl.zczb.tools.tops.impl.TopList;

public interface TopManager {
    TopList getTopList(TopType paramTopType);

    void replaceTop(TopType paramTopType, TopList paramTopList);

    void refreshTops();
}


