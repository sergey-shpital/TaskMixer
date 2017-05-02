package xyz.pagenet.concurent.sample;

import xyz.pagenet.concurent.CallableMixer;

import java.time.LocalDateTime;

/**
 * sample usage
 *
 */
public class App 
{
    public static void main(String[] args) {

        System.out.println("CallableMixer");

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
}
