package cs.umu.se.workers;

import cs.umu.se.chord.ChordBackEnd;

import java.util.concurrent.atomic.AtomicBoolean;

public class StabilizerWorker implements Runnable {
    private final ChordBackEnd backEnd;
    private final AtomicBoolean runStabilize = new AtomicBoolean(true);
    private long delay;
    private int m = -1;
    private final AtomicBoolean isAlive = new AtomicBoolean(true);

    public StabilizerWorker(ChordBackEnd backEnd, int m, int delay) {
        this.backEnd = backEnd;
        this.m = m;
        this.delay = delay;
    }

    @Override
    public void run() {
        System.out.println("Thread in worker: " + Thread.currentThread().getName());
        // called periodically
        while (runStabilize.get()) {
            backEnd.stabilize();

            backEnd.fixFingers();

            backEnd.checkPredecessor();

            backEnd.checkSuccessor(); // Check if successor has crashed

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
