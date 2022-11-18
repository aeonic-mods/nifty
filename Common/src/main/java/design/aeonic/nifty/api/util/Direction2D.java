package design.aeonic.nifty.api.util;

import net.minecraft.util.StringRepresentable;

public enum Direction2D implements StringRepresentable {
    UP("up", 0, -1),
    DOWN("down", 0, 1),
    LEFT("left", -1, 0),
    RIGHT("right", 1, 0);

    public static final StringRepresentable.EnumCodec<Direction2D> CODEC = StringRepresentable.fromEnum(Direction2D::values);

    private final String name;
    private final int x;
    private final int y;

    Direction2D(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public static Direction2D byName(String name) {
        return CODEC.byName(name);
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isHorizontal() {
        return !isVertical();
    }

    public boolean isVertical() {
        return this == UP || this == DOWN;
    }
}
