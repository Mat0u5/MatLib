package net.mat0u5.matlib.utils.other;

public class Triple<X, Y, Z> {
    public final X x;
    public final Y y;
    public final Z z;
    private Triple(X x, Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public static <X, Y, Z> Triple<X, Y, Z> of(X x, Y y, Z z) {
        return new Triple<>(x, y, z);
    }
}