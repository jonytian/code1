package com.legaoyi.common.disruptor;

import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DisruptorEventBatchProducer {

    private final RingBuffer<DisruptorMessage> ringBuffer;

    public DisruptorEventBatchProducer(DisruptorEventBatchConsumer eventConsumer) {
        this(eventConsumer,1024);
    }

    public DisruptorEventBatchProducer(DisruptorEventBatchConsumer eventConsumer, int bufferSize) {
        DisruptorEventFactory factory = new DisruptorEventFactory();

        /**
                     指定等待策略
        Disruptor 定义了 com.lmax.disruptor.WaitStrategy 接口用于抽象 Consumer 如何等待新事件，这是策略模式的应用。
        Disruptor 提供了多个 WaitStrategy 的实现，每种策略都具有不同性能和优缺点，根据实际运行环境的 CPU 的硬件特点选择恰当的策略，并配合特定的 JVM 的配置参数，能够实现不同的性能提升。
                    例如，BlockingWaitStrategy、SleepingWaitStrategy、YieldingWaitStrategy 等，其中，
        1、Disruptor默认的等待策略是BlockingWaitStrategy。这个策略的内部适用一个锁和条件变量来控制线程的执行和等待（Java基本的同步方法）。BlockingWaitStrategy是最慢的等待策略，但也是CPU使用率最低和最稳定的选项。然而，可以根据不同的部署环境调整选项以提高性能（阻塞等待，CPU占用小，但是会切换线程，延迟较高）。
        2、SleepingWaitStrategy和BlockingWaitStrategy一样，SpleepingWaitStrategy的CPU使用率也比较低。它的方式是循环等待并且在循环中间调用LockSupport.parkNanos(1)来睡眠，（在Linux系统上面睡眠时间60µs）.然而，它的优点在于生产线程只需要计数，而不执行任何指令。并且没有条件变量的消耗。但是，事件对象从生产者到消费者传递的延迟变大了。SleepingWaitStrategy最好用在不需要低延迟，而且事件发布对于生产者的影响比较小的情况下。比如异步日志功能（先自旋，然后Thread.yield()，最后调用LockSupport.parkNanos(1L)，CPU占用和延迟比较均衡）。
        3、YieldingWaitStrategy是可以被用在低延迟系统中的两个策略之一，这种策略在减低系统延迟的同时也会增加CPU运算量。YieldingWaitStrategy策略会循环等待sequence增加到合适的值。循环中调用Thread.yield()允许其他准备好的线程执行。如果需要高性能而且事件消费者线程比逻辑内核少的时候，推荐使用YieldingWaitStrategy策略。例如：在开启超线程的时候（先自旋等待，然后使用Thread.yield()唤醒其他线程，CPU占用和延迟比较均衡）。linux下会导致CPU 100%
        4、BusySpinWaitStrategy是性能最高的等待策略，同时也是对部署环境要求最高的策略。这个性能最好用在事件处理线程比物理内核数目还要小的时候。例如：在禁用超线程技术的时候（自旋等待，CPU占用高，但是无需切换线程，延迟低）。
        5、PhasedBackoffWaitStrategy ： 上面多种策略的综合，CPU资源的占用少，延迟大。
        */
        Disruptor<DisruptorMessage> disruptor =
                new Disruptor<DisruptorMessage>(factory, bufferSize, Executors.defaultThreadFactory(),
                        ProducerType.MULTI, new SleepingWaitStrategy());
        disruptor.handleEventsWith(new DisruptorEventBatchHandler(eventConsumer));
        disruptor.start();
        // Get the ring buffer from the Disruptor to be used for publishing.
        ringBuffer = disruptor.getRingBuffer();
    }

    public void produce(Object message) {
        if (message == null) {
            return;
        }
        long seq = ringBuffer.next();
        ringBuffer.get(seq).setMessage(message);
        ringBuffer.publish(seq);
    }

}
