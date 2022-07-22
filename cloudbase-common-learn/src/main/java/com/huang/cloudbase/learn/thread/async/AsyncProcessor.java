package com.huang.cloudbase.learn.thread.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 本类用于实现异步执行，内置一个支持去重的任务队列（FIFO），同时运行多于1个线程运行。<br>
 *
 * @param <K> 任务数据类主键类型
 * @param <V> 任务数据类
 * @author huangjunbiao
 */
public class AsyncProcessor<K, V> {

    private static class Entry<K, V> {

        private K jobId;

        private V job;

        private final long ts;

        private boolean discarded = false;

        public K getJobId() {
            return this.jobId;
        }

        public void setJobId(K jobId) {
            this.jobId = jobId;
        }

        public V getJob() {
            return this.job;
        }

        public void setJob(V job) {
            this.job = job;
        }

        public long getTs() {
            return ts;
        }

        public boolean isDiscarded() {
            return discarded;
        }

        public void setDiscarded(boolean discarded) {
            this.discarded = discarded;
        }

        public Entry(K jobId, V job) {
            this.jobId = jobId;
            this.job = job;
            this.ts = System.currentTimeMillis();
        }

    }

    public enum Mode {
        /**
         * 不去重，直接追加。FIFO
         */
        append,
        /**
         * 去重，如果存在，则不添加
         */
        reserve,
        /**
         * 去重，如果存在，更新老的
         */
        refill,
        /**
         * 去重，如果存在，废弃老的，追加新的
         */
        discard
    }

    /**
     * 异步任务执行接口
     *
     * @param <T> 任务数据类
     * @author WangYicheng
     */
    public interface Actor<T> {

        /**
         * 当返回true时，才开始分发job。
         * 注意：若尚未就绪，并需要继续等待，则不要返回
         *
         * @return 是否就绪
         */
        default boolean waitReady() {
            return true;
        }

        /**
         * 处理一个job
         *
         * @param job job
         * @throws Exception 异常
         */
        void call(T job) throws Exception;

    }

    private class JobRunner implements Runnable {

        private final Actor<V> actor;

        private JobRunner(Actor<V> actor) {
            this.actor = actor;
        }

        @Override
        public void run() {
            while (AsyncProcessor.this.runningFlag.get() != AsyncProcessor.RUNNING_FLAG_STOP) {
                // 检测action是否准备好了
                if (!this.actor.waitReady()) {
                    continue;
                }

                Entry<K, V> entry = this.pollEntry();

                // 延时
                if (AsyncProcessor.this.delayedMillis > 0 && entry != null) {
                    this.delay(entry);
                }

                if (entry != null && AsyncProcessor.this.runningFlag.get() != AsyncProcessor.RUNNING_FLAG_STOP) {
                    try {
                        // 注意：这里不管执行结果如何，任务会从队列中删除，如果需要失败重试，需要在action的call的实现中，自己主动addJob
                        if (!entry.isDiscarded()) {
                            actor.call(entry.getJob());
                        }
                    } catch (Exception t) {
                        if (AsyncProcessor.logger.isDebugEnabled()) {
                            AsyncProcessor.logger.warn("Process job error: {}", t, t);
                        } else {
                            AsyncProcessor.logger.warn("Process job error: {}", t.toString());
                        }
                    }
                }
            }
        }

        private void delay(Entry<K, V> entry) {
            while (System.currentTimeMillis() - entry.getTs() < AsyncProcessor.this.delayedMillis
                    && AsyncProcessor.this.runningFlag.get() != AsyncProcessor.RUNNING_FLAG_STOP) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Interrupted");
                }
            }
        }

        private Entry<K, V> pollEntry() {
            Entry<K, V> entry = null;
            synchronized (AsyncProcessor.this.mutex) {
                while (AsyncProcessor.this.runningFlag.get() != AsyncProcessor.RUNNING_FLAG_STOP
                        && entry == null) {
                    entry = AsyncProcessor.this.queue.pollFirst();
                    if (entry != null) {
                        AsyncProcessor.this.map.remove(entry.getJobId());
                        AsyncProcessor.this.mutex.notifyAll();
                    } else {
                        try {
                            AsyncProcessor.this.mutex.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new IllegalStateException("Interrupted");
                        }
                    }
                }
            }
            return entry;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(AsyncProcessor.class);

    private static final AtomicInteger GLOBAL_THREAD_COUNTER = new AtomicInteger(0);

    private static final int RUNNING_FLAG_STOP = 0;

    private static final int RUNNING_FLAG_RUNNING = 1;

    private static final int RUNNING_FLAG_STOPPING = 2;

    private static final int DEFAULT_MAX_JOB_COUNT = 1000;

    private static final int DEFAULT_DELAYED_MILLIS = 0;

    final LinkedList<Entry<K, V>> queue = new LinkedList<>();

    final Map<K, Entry<K, V>> map = new HashMap<>();

    final Object mutex;

    final Mode mode;

    final int threadCount;

    final AtomicInteger runningFlag = new AtomicInteger(AsyncProcessor.RUNNING_FLAG_STOP);

    final List<Thread> threads = new ArrayList<>();

    int maxJobCount;

    long delayedMillis;

    String threadNamePrefix = AsyncProcessor.class.getSimpleName();

    /**
     * 构造函数。默认会对待处理的任务去重（reserve模式），并且最大线程数为1，最大队列长度为1000。
     */
    public AsyncProcessor() {
        this(Mode.reserve, AsyncProcessor.DEFAULT_MAX_JOB_COUNT, AsyncProcessor.DEFAULT_DELAYED_MILLIS, 1);
    }

    /**
     * 构造函数。
     *
     * @param mode          是否去重，以及去重的方式。
     * @param maxJobCount   最大待处理任务处理
     * @param delayedMillis 延迟时间（毫秒），默认0
     * @param threadCount   最大线程（并行处理）数量
     */
    public AsyncProcessor(Mode mode, int maxJobCount, long delayedMillis, int threadCount) {
        this.mutex = this;
        this.mode = mode;
        this.threadCount = threadCount;
        this.maxJobCount = maxJobCount;
        this.delayedMillis = delayedMillis;
    }

    /**
     * 添加一个任务。<br>
     * 如果添加成功返回true。添加的方式和mode有关。
     * 如果AsyncProcessor被调用过stopAndWaitFinished，那么在彻底停下来之前，不能向队列添加任务， 且该函数返回false。
     *
     * @param job   任务
     * @param jobId 任务Id
     * @return 任务是否添加成功
     * @throws InterruptedException 中断
     */
    public boolean addJob(K jobId, V job) throws InterruptedException {
        if (this.runningFlag.get() == AsyncProcessor.RUNNING_FLAG_STOPPING) {
            return false;
        }
        synchronized (this.mutex) {
            while (this.queue.size() >= this.maxJobCount) {
                this.mutex.wait();
            }
            boolean b = false;
            Entry<K, V> entry = new Entry<>(jobId, job);
            switch (this.mode) {
                case append:
                    this.queue.addLast(entry);
                    b = true;
                    break;
                case reserve:
                    if (this.map.put(jobId, entry) == null) {
                        this.queue.addLast(entry);
                        b = true;
                    }
                    break;
                case refill: {
                    Entry<K, V> old = this.map.put(jobId, entry);
                    if (old == null) {
                        this.queue.addLast(entry);
                    } else {
                        old.setJob(job);
                    }
                    b = true;
                    break;
                }
                case discard: {
                    Entry<K, V> old = this.map.put(jobId, entry);
                    if (old != null) {
                        old.setDiscarded(true);
                    }
                    this.queue.addLast(entry);
                    b = true;
                    break;
                }
            }
            this.mutex.notifyAll();
            return b;
        }
    }

    /**
     * 获取任务最大长度
     *
     * @return 最大长度
     */
    public int getMaxJobCount() {
        return this.maxJobCount;
    }

    /**
     * 设置队列最大长度
     *
     * @param maxJobCount 最大长度
     */
    public void setMaxJobCount(int maxJobCount) {
        this.maxJobCount = maxJobCount;
    }

    public long getDelayedMillis() {
        return delayedMillis;
    }

    public void setDelayedMillis(long delayedMillis) {
        this.delayedMillis = delayedMillis;
    }

    /**
     * 未处理的任务数量
     *
     * @return 未处理的任务数量
     */
    public int size() {
        synchronized (this.mutex) {
            return this.queue.size();
        }
    }

    /**
     * 处理线程的名称前缀
     *
     * @return 前缀
     */
    public String getThreadNamePrefix() {
        return this.threadNamePrefix;
    }

    /**
     * 处理线程的名称前缀
     *
     * @param threadNamePrefix 前缀
     */
    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    /**
     * 启动异步执行过程
     *
     * @param actor 处理任务的具体实现
     */
    public void start(Actor<V> actor) {
        this.runningFlag.set(AsyncProcessor.RUNNING_FLAG_RUNNING);
        for (int i = 0; i < this.threadCount; i++) {
            Thread thread = new Thread(new JobRunner(actor));
            thread.setName(this.threadNamePrefix + "-" + AsyncProcessor.GLOBAL_THREAD_COUNTER.incrementAndGet());
            thread.setDaemon(true);
            thread.start();

            this.threads.add(thread);
        }
    }

    /**
     * 立即停止，如果队列中有未处理的任务，则会被抛弃
     */
    public void stop() {
        this.runningFlag.set(AsyncProcessor.RUNNING_FLAG_STOP);
        synchronized (this.mutex) {
            this.mutex.notifyAll();
        }
        for (Thread t : this.threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * 等待所有任务执行完毕后停止
     *
     * @throws InterruptedException 中断
     */
    public void stopAndWaitFinishedAll() throws InterruptedException {
        this.runningFlag.set(AsyncProcessor.RUNNING_FLAG_STOPPING);
        synchronized (this.mutex) {
            while (!this.queue.isEmpty()) {
                this.mutex.wait();
            }
        }
        this.stop();
    }

}
