package com.liuyao.demo.mashibing.thread;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

/**
 * 高并发九
 *
 * 依赖
 * <!-- https://mvnrepository.com/artifact/com.lmax/disruptor -->
 * <dependency>
 *     <groupId>com.lmax</groupId>
 *     <artifactId>disruptor</artifactId>
 *     <version>3.4.2</version>
 * </dependency>
 *
 * disruptor
 * 分裂 瓦解
 * 单机最快的MQ
 * 性能极高 无锁cas 单机支持高并发
 * 内存中高效队列
 *
 * 观察者模式
 *
 * 核心 环形buffer(RingBufffer,size=2^n) 直接覆盖 降低GC
 *  有ConcurrentLinkedQueue,使用首尾指针,
 *  没有ConcurrentArrayQueue,因为array长度不变
 *  而disruptor只维护sequence(序列),用array(数组)实现,效率快
 *
 * 开发步骤
 *  1 定义Event 队列中需要处理的元素 --定义元素
 *  2 定义Event工厂,用于填充队列  --产生(new)元素的方法
 *      牵扯到效率问题: disruptor初始化时,会调用Event工厂,对ringBuffer进行内存的提前分配,
 *    覆盖时不会new,会直接修改值
 *      GC产生频率降低
 *  3 定义EventHandler(消费者), 处理容器中的元素 --使用(消费或处理)元素
 *
 *
 */
public class T14_Disruptor {

    public static void main(String[] args) {

        test1();
        test2();
    }

    private static void test2() {

        LongEventFactory factory = new LongEventFactory();
        int bufferSize = 1024;
        // 第三个参数:当产生消费者的时候,是在特定线程中执行,此参数为线程产生方式
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize,
                Executors.defaultThreadFactory());
        // 单线程 效率更高 sequence访问是无锁的
        Disruptor<LongEvent> singledisruptor = new Disruptor<LongEvent>(factory, bufferSize,
                Executors.defaultThreadFactory(), ProducerType.SINGLE,
                new BlockingWaitStrategy()); // 等待策略
        LongEventHandler handler = new LongEventHandler();
        disruptor.handleEventsWith(handler);
        // 异常处理
        disruptor.handleExceptionsFor(handler).with(new ExceptionHandler<LongEvent>() {
            @Override
            public void handleEventException(Throwable ex, long sequence, LongEvent event) {

            }

            @Override
            public void handleOnStartException(Throwable ex) {

            }

            @Override
            public void handleOnShutdownException(Throwable ex) {

            }
        });
        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        EventTranslator<LongEvent> translator = new EventTranslator<LongEvent>() {
            @Override
            public void translateTo(LongEvent longEvent, long l) {

            }
        };

        ringBuffer.publishEvent(translator);

        ringBuffer.publishEvent((event, seq, l)-> event.set(l), 9999L);

    }

    private static void test1() {

        LongEventFactory factory = new LongEventFactory();
        int bufferSize = 1024;
        // 第三个参数:当产生消费者的时候,是在特定线程中执行,此参数为线程产生方式
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize,
                Executors.defaultThreadFactory());

        // 消费者
        disruptor.handleEventsWith(new LongEventHandler());
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        // 生产者
        long sequence = ringBuffer.next(); // 下一个可用位置
        try{
            LongEvent event = ringBuffer.get(sequence);
            event.set(8888L);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            ringBuffer.publish(sequence);
        }
    }


}

class LongEventHandler extends Func implements EventHandler<LongEvent>{

    public static long count = 0;
    /**
     *
     * @param longEvent
     * @param l RingBuffer序号
     * @param b 是否最后一个元素
     * @throws Exception
     */
    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        count++;
        log(longEvent + " 序号:" + l + " 最后:" + b);
    }
}

class LongEventFactory implements EventFactory<LongEvent> {

    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
class LongEvent{

    private long v;

    public void set(long v){
        this.v = v;
    }

    @Override
    public String toString() {
        return "LongEvent{" +
                "v=" + v +
                '}';
    }
}
