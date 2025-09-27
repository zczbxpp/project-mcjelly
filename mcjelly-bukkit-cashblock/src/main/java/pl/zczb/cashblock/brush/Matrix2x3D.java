package pl.zczb.cashblock.brush;

import org.bukkit.configuration.ConfigurationSection;


public final class Matrix2x3D {
    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;
    private final double zMin;
    private final double zMax;

    Matrix2x3D(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    public Matrix2x3D(ConfigurationSection section) {
        this(
                Double.parseDouble(section.getString("x").split(":")[0]),
                Double.parseDouble(section.getString("x").split(":")[1]),
                Double.parseDouble(section.getString("y").split(":")[0]),
                Double.parseDouble(section.getString("y").split(":")[1]),
                Double.parseDouble(section.getString("z").split(":")[0]),
                Double.parseDouble(section.getString("z").split(":")[1]));
    }


    public double xMin() {
        return this.xMin;
    }

    public double xMax() {
        return this.xMax;
    }

    public double yMin() {
        return this.yMin;
    }

    public double yMax() {
        return this.yMax;
    }

    public double zMin() {
        return this.zMin;
    }

    public double zMax() {
        return this.zMax;
    }
}


