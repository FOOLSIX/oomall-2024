package cn.edu.xmu.javaee.core.model.returnval;

/**
 * 用于返回两个值的tuple
 * @param <A> 第一个值类型
 * @param <B> 第二个值类型
 */
public class TwoTuple <A,B>{
    public final A first;
    public final B second;

    public TwoTuple(A first, B second) {
        this.first = first;
        this.second = second;
    }
}
