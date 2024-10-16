package cs.umu.se.client;

import cs.umu.se.interfaces.Storage;
import cs.umu.se.types.Song;
import cs.umu.se.util.BoxPlotXChart;
import cs.umu.se.util.MediaUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientPerformanceTest {
    private Storage storage;
    private MediaUtil mediaUtil;
    private String nodeIp;
    private int nodePort;

    public ClientPerformanceTest(Storage storage, int m, String nodeIp, int nodePort) {
        this.storage = storage;
        this.nodeIp = nodeIp;
        this.nodePort = nodePort;
        this.mediaUtil = new MediaUtil(m);
    }

    // Make boxplots
    // ---------------------------------------------
    public void makeBoxPlotSeqIncSongsNoCaching(String title, int nrBoxes, int nrSongs, int iterations, long songSize) {
        BoxPlotXChart boxPlot = new BoxPlotXChart();
        boxPlot.createPlot(title, storage.getClass().getName(), "Time (ms)");


        int j = 1;
        for (int i = 0; i < nrBoxes; i++) {
            // create dummy data
            Song[] songs = new Song[nrSongs * j];
            for (int k = 0; k < nrSongs * j; k++) {
                songs[k] = mediaUtil.createDummySong(songSize);
            }

            boxPlot.addToPlot(nrSongs * j     + " songs", testSongRetrievalSeqNTimes(songs, iterations));
            j = j * 2;
        }
        boxPlot.showPlot();
    }

    public void makeBoxPlotSeqIncSonsWithCache(String title, int nrBoxes, int nrSongs, int iterations, long songSize) {
        BoxPlotXChart boxPlot = new BoxPlotXChart();
        boxPlot.createPlot(title, storage.getClass().getName(), "Time (ms)");

        int j = 1;
        for (int i = 0; i < nrBoxes; i++) {
            // create dummy data
            Song[] songs = new Song[nrSongs * j];
            for (int k = 0; k < nrSongs * j; k++) {
                songs[k] = mediaUtil.createDummySong(songSize);
            }

            int nrPopularSongs = (int) Math.floor(Math.sqrt(songs.length));
            Song[] popularSongs = new Song[nrPopularSongs];
            for (int k = 0; k < nrPopularSongs; k++)
                popularSongs[k] = mediaUtil.createDummySong(songSize);

            for (int k = 0; k < songs.length; k++) {
                if (k % 2 == 0) {
                    Random rand = new Random();
                    int index = rand.nextInt(nrPopularSongs);
                    songs[k] = popularSongs[index];
                }
            }


            boxPlot.addToPlot(nrSongs * j     + " songs", testSongRetrievalSeqNTimes(songs, iterations));
            j = j * 2;
        }
        boxPlot.showPlot();
    }

    public List<Long> testSongRetrievalSeqNTimes(Song[] songs, int iterations) {
        List<Long> results = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            results.add(testSongRetrievalSeq(songs));
        }
        System.out.println("testSongRetrievalSeqNTimes: " + iterations + " done!");
        return results;
    }

    public long testSongRetrievalSeq(Song[] songs) {
        // store songs
        for (Song s : songs)
            storage.store(s);

        // perform test
        long t1 = System.currentTimeMillis();
        for (Song s : songs)
            storage.retrieve(s.getIdentifierString());
        long t2 = System.currentTimeMillis();

        // cleanup
        for (Song s : songs)
            storage.delete(s.getIdentifierString());

        return t2 - t1;
    }

}
