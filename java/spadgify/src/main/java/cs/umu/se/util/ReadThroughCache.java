package cs.umu.se.util;

import cs.umu.se.chord.Hash;
import cs.umu.se.types.Song;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReadThroughCache {
    private final int capacity;
    private HashMap<String, Long> requestedSongs = new HashMap<>();
    private HashMap<String, Song> cache = new HashMap<>();

    public ReadThroughCache(int capacity) {
        this.capacity = capacity;
    }

    public void put(String key, Song song) {
        synchronized (this) {
            Long nrTimes = requestedSongs.get(key);

            if (nrTimes == null) {
                requestedSongs.put(key, 1L);
            } else {
                long val = nrTimes + 1;
                requestedSongs.put(key, val);
            }

            if (cache.get(key) != null)
                return;

            System.out.println("Cache size: " + cache.size());
            if (cache.size() < capacity) {
                cache.put(key, song);
            } else {
                long lowestValue = requestedSongs.get(key);
                String lowestKey = key;

                for (Map.Entry<String, Song> e : cache.entrySet()) {
                    String currentKey = e.getKey();
                    long currentValue = requestedSongs.get(currentKey);

                    if (currentValue < lowestValue) {
                        lowestKey = currentKey;
                        lowestValue = currentValue;
                    }
                }

                if (!lowestKey.equals(key)) {
                    cache.remove(lowestKey);
                    cache.put(key, song);
                }
            }
        }
    }

    public Song get(String key) {
        synchronized (this) {
            return cache.get(key);
        }
    }

    public void remove(String key) {
        synchronized (this) {
            cache.remove(key);
        }
    }

    public Song[] getCachedSongs() {
        synchronized (this) {
            ArrayList<Song> songs = new ArrayList<>();
            cache.forEach((key, value) -> {
                songs.add(value);
            });

            Song[] s = new Song[songs.size()];
            return songs.toArray(s);
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            Song[] songs = getCachedSongs();
            return "capacity: " + capacity + "\n\t@songs: " + Arrays.toString(songs);
        }
    }
}
