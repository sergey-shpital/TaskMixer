package xyz.pagenet.concurent;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * Created by Sergey on 30.04.2017.
 */
public class CallableMixer implements Runnable {

    private final DelayQueue queue = new DelayQueue<CallableEntry>();
    private final ExecutorService pool;

    public CallableMixer(int poolSize) {
        pool = Executors.newFixedThreadPool( poolSize );
    }

    public void add(LocalDateTime targetTime, Callable task ) {
        queue.add( new CallableEntry(targetTime, task) );
    }

    public long size() {
        return queue.size();
    }

    @Override
    public void run() {
        try {
            for (;;) {
                CallableEntry entry = (CallableEntry)queue.take();
                pool.submit(entry.getCallable());
            }
        } catch (Exception ex) {
            pool.shutdown();
        }
    }
}
