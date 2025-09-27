package pl.zczb.cashblock.objects;

import dev.triumphteam.gui.guis.GuiItem;
import lombok.Data;

@Data
public class Pet {

    private final String name;
    private final String displayName;
    private final GuiItem item;
    private final int speed;
    private final double mnoznik;

    public Pet(String name, String displayName, GuiItem item, int speed, double mnoznik) {
        this.name = name;
        this.displayName = displayName;
        this.item = item;
        this.speed = speed;
        this.mnoznik = mnoznik;

    }

}
