package pl.zczb.tools.config.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warp {
    private String name;
    private int slot;
    private String location;
    private float yaw;
    private String material;
    private List<String> lore;
}