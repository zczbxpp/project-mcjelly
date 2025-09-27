package pl.zczb.cashblock.brush;

import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
public class WallBrushArea implements BrushArea {

    private final int radius;

    public WallBrushArea(int radius) {
        this.radius = radius;
    }

    public WallBrushArea(ConfigurationSection section) {
        this(section.getInt("radius"));
    }

    @Override
    public Matrix2x3D getArea(BlockFace face) {
        int r = this.radius;

        if (face == BlockFace.UP || face == BlockFace.DOWN) {
            return new Matrix2x3D(-r, r, 0, 0, -r, r);
        } else if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            return new Matrix2x3D(-r, r, -r, r, 0, 0);
        } else if (face == BlockFace.EAST || face == BlockFace.WEST) {
            return new Matrix2x3D(0, 0, -r, r, -r, r);
        } else {
            throw new IllegalArgumentException("Illegal block face.");
        }
    }
}



