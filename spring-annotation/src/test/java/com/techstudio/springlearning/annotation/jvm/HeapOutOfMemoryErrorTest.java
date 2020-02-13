package com.techstudio.springlearning.annotation.jvm;

import com.techstudio.springlearning.annotation.thread.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟堆内存oom
 * java.lang.OutOfMemoryError: Java heap space
 *
 * @author lj
 * @date 2020/1/21
 */
public class HeapOutOfMemoryErrorTest {

    /**
     * vm args:
     * -Xms20m 堆最大值（和堆最大值最小值设置为一样，避免堆自动扩展）
     * -Xmx20m 堆最小值
     * -XX:+HeapDumpOnOutOfMemoryError 当出现oom时dump出当前内存堆的快照
     * -XX:-UseGCOverheadLimit 限制GC的运行时间。如果GC耗时过长，就抛OOM GC overhead limit exceeded
     * 超过98%的时间用来做GC并且回收了不到2%的堆内存时会抛出此异常（测试先关掉）
     * -XX:HeapDumpPath=F:\dump dump目录
     * ----------------------------------------------------------------
     * 解决堆内存oom要区分是内存泄漏（memory leak）还是内存溢出（memory overflow）
     * <p>
     * 内存泄漏：
     * 是指程序在申请内存后，无法释放已申请的内存空间，一次内存泄漏似乎不会有大的影响，
     * 但内存泄漏堆积后的后果就是内存溢出。
     * 思路：
     * 工具查看泄漏对象到GC Roots的引用链，于是就能找到泄漏对象是通过怎样的路径与与GC Roots相关联
     * 并导致垃圾收集器无法回收他们，掌握x泄漏对象的类型信息以及GC Roots引用链，就能定位泄漏代码的具体位置
     * <p>
     * 内存溢出：
     * 指程序申请内存时，没有足够的内存供申请者使用，或者说，给了你一块存储int类型数据的存储空间，
     * 但是你却存储long类型的数据，那么结果就是内存不够用，此时就会报错OOM,即所谓的内存溢出。
     * 思路：
     * 如果不存在泄漏，那就是内存中的对象确实是需要用的，检查堆内存的参数-Xms -Xmx设置是否合理，
     * 代码优化减少不必要的对象存活时间太长
     *
     * @param args
     */
    public static void main(String[] args) {
        List<Ticket> tickets = new ArrayList<>();
        while (true) {
            tickets.add(new Ticket(100, true));
        }
    }

}
