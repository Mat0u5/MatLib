package net.mat0u5.matlib.util.other;

public class Tuple<X, Y> {
    public final X x;
    public final Y y;
    private Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
    public static <X, Y> Tuple<X, Y> of(X x, Y y) {
        return new Tuple<>(x, y);
    }
}