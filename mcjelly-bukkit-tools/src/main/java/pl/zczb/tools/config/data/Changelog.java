package pl.zczb.tools.config.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Changelog {

    private String name;
    private List<String> lore;

}