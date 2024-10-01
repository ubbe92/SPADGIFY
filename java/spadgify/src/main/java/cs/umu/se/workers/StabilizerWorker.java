package cs.umu.se.workers;

import cs.umu.se.chord.ChordBackEnd;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class StabilizerWorker implements Runnable {
    private final ChordBackEnd backEnd;
    private final AtomicBoolean runStabilize = new AtomicBoolean(true);
    private long delay = 2000;
    private int m = -1;
    private final AtomicBoolean isAlive = new AtomicBoolean(true);

    public StabilizerWorker(ChordBackEnd backEnd, int m) {
        this.backEnd = backEnd;
        this.m = m;
    }

    @Override
    public void run() {
        System.out.println("Thread in worker: " + Thread.currentThread().getName());
        // called periodically
        while (runStabilize.get()) {
            backEnd.stabilizeWIKI();

//            for (int i = 0; i < m; i++)
            backEnd.fixFingersWIKI();

            backEnd.checkPredecessorWIKI();

            backEnd.checkSuccessorWIKI(); // Check if successor has crashed

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
                stopStabilize();
            }
        }
        isAlive.set(false);
    }

    public void stopStabilize() {
        this.runStabilize.set(false);
    }
    public boolean isAlive() {
//        System.out.println("isAlive: " + isAlive.get());
        return isAlive.get();
    }
}
