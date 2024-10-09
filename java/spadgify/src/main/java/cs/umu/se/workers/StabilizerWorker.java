package cs.umu.se.workers;

import cs.umu.se.chord.ChordBackEnd;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The StabilizerWorker class implements a Runnable task that periodically
 * performs maintenance operations on a ChordBackEnd instance to ensure
 * the stability of the Chord distributed system.
 */
public class StabilizerWorker implements Runnable {
    private final ChordBackEnd backEnd;
    private final AtomicBoolean runStabilize = new AtomicBoolean(true);
    private final long delay;
    private int m = -1;
    private final AtomicBoolean isAlive = new AtomicBoolean(true);

    public StabilizerWorker(ChordBackEnd backEnd, int m, int delay) {
        this.backEnd = backEnd;
        this.m = m;
        this.delay = delay;
    }

    /**
     * Performs periodic stabilization tasks on a ChordBackEnd instance to maintain the network's stability.
     * Runs in a separate thread and continuously executes the stabilization, finger fixing,
     * and successor/predecessor checking operations until requested to stop.
     */
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
        return isAlive.get();
    }
}
