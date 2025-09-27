package pl.zczb.tools.config.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Help {

    private String name;
    private int slot;
    private String material;
    private List<String> lore;
    private String command;

}