package xyz.pagenet.concurent;

import java.util.concurrent.Exchanger;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Sergey on 17.05.2017.
 */
public class PingPong {


    private static AtomicLong checker = new AtomicLong(0L);
    private static AtomicLong counter = new AtomicLong(0L);
    //Exchanger<String> table;
    //
    private final SynchronousQueue<Boolean> semaphore = new SynchronousQueue<>();
    private final Boolean ball = new Boolean(true);
    private volatile boolean fly = true;


    public PingPong() {
        this.fly = true;
        counter.set(0L);
    }

    public void end() {
        fly=false;
    }

    public void start() {
        fly=true;
        counter.set(0L);
    }

    //fastly

    public void ping(){
        try {
            while(fly) {
                semaphore.put(ball);
                checker.incrementAndGet();
                counter.incrementAndGet();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pong() {
        try {
            while(fly) {
                semaphore.take();//Pong
                checker.decrementAndGet();
                counter.incrementAndGet();
             }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    //slowly
    public void pingS(){
        try {
            while(fly) {
                synchronized(this) {
                    notify();
                    checker.incrementAndGet();
                    counter.incrementAndGet();
                    wait();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void pongS() {
        try {
            while(fly) {
               synchronized(this) {
                    notify();
                    checker.decrementAndGet();
                    counter.incrementAndGet();
                    wait();
                }
             }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dumpStat() {
        System.out.println("checker="+checker.get());
        System.out.println("counter="+counter.get());
    }

}
