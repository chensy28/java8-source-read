/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent.atomic;
import sun.misc.Unsafe;

/**
 * A {@code boolean} value that may be updated atomically. See the
 * {@link java.util.concurrent.atomic} package specification for
 * description of the properties of atomic variables. An
 * {@code AtomicBoolean} is used in applications such as atomically
 * updated flags, and cannot be used as a replacement for a
 * {@link java.lang.Boolean}.
 *
 * @since 1.5
 * @author Doug Lea
 */
public class AtomicBoolean implements java.io.Serializable {
    private static final long serialVersionUID = 4654671469794556979L;
    // setup to use Unsafe.compareAndSwapInt for updates
    /**
     * todo @chenSy Unsafe使用 以及native源码查看
     * Java魔法类：Unsafe应用解析 https://tech.meituan.com/2019/02/14/talk-about-java-magic-class-unsafe.html
     * 什么是CAS? 即比较并替换，实现并发算法时常用到的一种技术。CAS操作包含三个操作数——内存位置、预期原值及新值。执行CAS操作的时候，
     * 将内存位置的值与预期原值比较，如果相匹配，那么处理器会自动将该位置值更新为新值，否则，处理器不做任何操作。
     * 我们都知道，CAS是一条CPU的原子指令（cmpxchg指令），不会造成所谓的数据不一致问题，Unsafe提供的CAS方法（如compareAndSwapXXX）底层实现即为CPU指令cmpxchg。
     */
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;

    /**
     * todo @chenSy 引入Open Jdk能查看到源码
     */
    static {
        try {
            valueOffset = unsafe.objectFieldOffset //返回对象成员属性在内存地址相对于此对象的内存地址的偏移量
                (AtomicBoolean.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    /**
     * todo @chenSy volatile原理
     * volatile变量的修改可以立刻让所有的线程可见，保证了可见性。而不加volatile变量的字段，JMM不保证普通变量的修改立刻被所有的线程可见
     */
    private volatile int value;

    /**
     * Creates a new {@code AtomicBoolean} with the given initial（初始值） value.
     *
     * @param initialValue the initial value
     */
    public AtomicBoolean(boolean initialValue) {
        value = initialValue ? 1 : 0;
    }

    /**
     * Creates a new {@code AtomicBoolean} with initial value {@code false}.
     */
    public AtomicBoolean() {
    }

    /**
     * Returns the current value.
     *
     * @return the current value
     */
    public final boolean get() {
        return value != 0;
    }

    /**
     * Atomically sets the value to the given updated value
     * if the current value {@code ==} the expected value.
     * 如果当前的值与预期的值相等，就能更新为给定的值，否则更新不成功
     *
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful. False return indicates that
     * the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(boolean expect, boolean update) {
        // 将boolean类型值换为数值
        int e = expect ? 1 : 0;
        int u = update ? 1 : 0;
        return unsafe.compareAndSwapInt(this, valueOffset, e, u);
    }

    /**
     * todo @chenSy 与compareAndSet有啥区分？本函数的用途？
     * Atomically sets the value to the given updated value
     * if the current value {@code ==} the expected value.
     *
     * <p><a href="package-summary.html#weakCompareAndSet">May fail
     * spuriously and does not provide ordering guarantees(可以虚假失败，不提供订购保证)
     * </a>, so is
     * only rarely an appropriate alternative to {@code compareAndSet}.
     *
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful
     */
    public boolean weakCompareAndSet(boolean expect, boolean update) {
        int e = expect ? 1 : 0;
        int u = update ? 1 : 0;
        return unsafe.compareAndSwapInt(this, valueOffset, e, u);
    }

    /**
     * Unconditionally(无条件地) sets to the given value.
     *
     * @param newValue the new value
     */
    public final void set(boolean newValue) { // 值是立即可见的
        value = newValue ? 1 : 0;
    }

    /**
     * Eventually（延迟设置） sets to the given value.
     * 聊聊高并发理解AtomicXXX.lazySet方法_ITer_ZC的专栏 https://blog.csdn.net/ITer_ZC/article/details/40744485
     *
     * @param newValue the new value
     * @since 1.6
     */
    public final void lazySet(boolean newValue) {
        int v = newValue ? 1 : 0;
        unsafe.putOrderedInt(this, valueOffset, v);
    }

    /**
     * Atomically sets to the given value and returns the previous value.
     * 自动获取预期的值，不需要填写预期的值
     * @param newValue the new value
     * @return the previous value
     */
    public final boolean getAndSet(boolean newValue) {
        boolean prev;
        do {
            prev = get();
        } while (!compareAndSet(prev, newValue));
        return prev;
    }

    /**
     * Returns the String representation of the current value.
     * @return the String representation of the current value
     */
    public String toString() {
        return Boolean.toString(get());
    }

}
