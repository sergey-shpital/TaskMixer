package xyz.pagenet.concurent.sample;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Sergey on 30.04.2017.
 */
public class SampleCallable implements Callable<Void> {

    private static AtomicLong order = new AtomicLong(0L);
    private final long id;

    public SampleCallable() {
        id = order.getAndIncrement();
    }

    @Override
    public Void call() throws Exception {
        if((id%100000)==0)  System.out.println("complete task id="+id);
        return null;
    }

}
