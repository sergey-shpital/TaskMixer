package xyz.pagenet.concurent.sample;

import xyz.pagenet.concurent.CallableMixer;
import xyz.pagenet.concurent.PingPong;

import java.time.LocalDateTime;

/**
 * sample usage
 *
 */
public class App 
{
    static void sampleMixer() {

        System.out.println("== CallableMixer ==");

        try {
            //create and start mixer
            final CallableMixer mixer = new CallableMixer( Math.max(1, Runtime.getRuntime().availableProcessors()) );
            Thread mt = new Thread(mixer);
            mt.start();

            //create and start callable produsers
            SampleProduser produser1 = new SampleProduser(mixer);
            SampleProduser produser2 = new SampleProduser(mixer);

            new Thread( ()->mixer.add(  LocalDateTime.now().plusSeconds(2),
                    ()->{
                        System.out.println("XXX");
                        return null;
                    })).start();

            new Thread(produser1).start();
            new Thread(produser2).start();

            //live
            for(int i=0; i<20; i++){
                System.out.println("active state: current queue size: " + mixer.size());
                Thread.sleep(100);
            }

            //stop produsers
            produser1.setStop(true);
            produser2.setStop(true);


            //wait last event
            while( mixer.size() > 0 ) {
                System.out.println("idle state: current queue size: " + mixer.size());
                Thread.sleep(1000);
            }

            mt.interrupt();

            Thread.sleep(5000);

            System.out.println("The end "+mixer.size());

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void samplePingPongFast() {

        System.out.println("== samplePingPongFast ==");

        PingPong table = new PingPong();
        try {
            Thread pingT = new Thread(()->table.ping());
            Thread pongT = new Thread(()->table.pong());
            pingT.start();
            pongT.start();

            Thread.sleep(2000);
            table.end();
            Thread.sleep(1000);

//           pingT.join();
//           pongT.join();

            table.dumpStat();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    static void samplePingPongSlowly() {

        System.out.println("== samplePingPongSlowly ==");

        PingPong table = new PingPong();
        try {
            Thread pingTS = new Thread(()->table.pingS());
            Thread pongTS = new Thread(()->table.pongS());
            pingTS.start();
            pongTS.start();

            Thread.sleep(2000);
            table.end();
            Thread.sleep(1000);
            synchronized(table) {
                table.notifyAll();
            }

//            pingTS.join();
//            pongTS.join();

            table.dumpStat();


            Thread.sleep(1000);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        //ordered queue and execution of scheduled tasks
        sampleMixer();

        //task switcher
        samplePingPongFast();    //100% speed
        samplePingPongSlowly();  //20% speed
        //see also: http://www.javarticles.com/2016/06/java-synchronousqueue-example.html
     }
}
