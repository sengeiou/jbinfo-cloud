package cn.jbinfo.cloud.core.copier;

/**
 * Created by xiaobin on 2016/11/2.
 */
public class Pair<A, B> {

    private A a;

    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }
}