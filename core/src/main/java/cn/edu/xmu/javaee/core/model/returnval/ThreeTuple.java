package cn.edu.xmu.javaee.core.model.returnval;

public class ThreeTuple <A, B, C> {
    public final A first;
    public final B second;

    public final C third;

    public ThreeTuple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
