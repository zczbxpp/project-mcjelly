package pl.zczb.cashblock.brush;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.List;

public interface BrushArea {
    static List<Block> getBlocks(Location base, Matrix2x3D matrix) {
        List<Block> blocks = Lists.newArrayList();

        int xStart = base.getBlockX() + (int) matrix.xMin();
        int xEnd = base.getBlockX() + (int) matrix.xMax();
        int yStart = base.getBlockY() + (int) matrix.yMin();
        int yEnd = base.getBlockY() + (int) matrix.yMax();
        int zStart = base.getBlockZ() + (int) matrix.zMin();
        int zEnd = base.getBlockZ() + (int) matrix.zMax();

        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                for (int z = zStart; z <= zEnd; z++) {
                    blocks.add(base.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }


    default List<Block> getAreaBlocks(Location location, BlockFace face) {
        return getBlocks(location, getArea(face));
    }

    default Matrix2x3D getArea(BlockFace face) {
        throw new UnsupportedOperationException("Not implemented!");
    }
}