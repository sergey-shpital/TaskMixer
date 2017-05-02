package xyz.pagenet.concurent.sample;

import xyz.pagenet.concurent.CallableMixer;

import java.time.LocalDateTime;

/**
 * Created by Sergey on 30.04.2017.
 */
public class SampleProduser implements Runnable {

    private final CallableMixer queue;
    private boolean stop = false;

    public SampleProduser(CallableMixer queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (!stop) {
                //Thread.sleep(0,1);
                queue.add(LocalDateTime.now(), new SampleCallable());
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}

