package com.example.sys.config;


import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncTaskConfig implements AsyncConfigurer {

    //根据cpu的数量动态的配置核心线程数和最大线程数
    private static final int CPU_COUNT         = Runtime.getRuntime().availableProcessors();
    //核心线程数 = CPU核心数 + 1
    private static final int CORE_POOL_SIZE    = CPU_COUNT + 1050;
    //线程池最大线程数 = CPU核心数 * 2 + 1
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 100000;



    /**
     * The {@link Executor} instance to be used when processing async
     * method invocations.
     */
    /**
     * corePoolSize:核心线程数
     * maximumPoolSize：线程池所容纳最大线程数(workQueue队列满了之后才开启)
     * keepAliveTime：非核心线程闲置时间超时时长
     * unit：keepAliveTime的单位
     * workQueue：等待队列，存储还未执行的任务
     * threadFactory：线程创建的工厂
     * handler：异常处理机制
     */

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数目
        executor.setCorePoolSize(CORE_POOL_SIZE);
        //指定最大线程数
        executor.setMaxPoolSize(MAXIMUM_POOL_SIZE);
        //队列中最大的数目
        executor.setQueueCapacity(999999);
        //调度器shutdown被调用时等待当前被调度的任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("sys-taskExecutor-");
        // 线程池对拒绝任务（无线程可用）的处理策略，目前只支持AbortPolicy、CallerRunsPolicy；默认为后者
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化执行器
        executor.initialize();

        return executor;
    }

    /**
     * The {@link AsyncUncaughtExceptionHandler} instance to be used
     * when an exception is thrown during an asynchronous method execution
     * with {@code void} return type.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
