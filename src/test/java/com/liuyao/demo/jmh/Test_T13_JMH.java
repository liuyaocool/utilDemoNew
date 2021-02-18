package com.liuyao.demo.jmh;

import com.liuyao.demo.mashibing.thread.T13_JMH;
import org.openjdk.jmh.annotations.*;

/**
 * 修改启动配置
 *  Environment variables → 勾选 Include system environment variables
 * 右键 run 启动
 * 预热 虚拟机保存本地 会提高效率
 */
public class Test_T13_JMH {

    @Benchmark
    @Warmup(iterations = 1, time = 3) // 预热 虚拟机先起来,调用1次,等3秒
    @Fork(5) // 多少线程
    @BenchmarkMode(Mode.Throughput) // 模式 Mode.Throughput吞吐量
    @Measurement(iterations = 1, time = 3) // 测试多少遍
    public void test(){
        T13_JMH.foreach();
    }

}
