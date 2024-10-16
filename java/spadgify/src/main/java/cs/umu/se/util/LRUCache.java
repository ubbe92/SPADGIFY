package cs.umu.se.util;

import cs.umu.se.types.Song;

import java.util.HashMap;

/**
 * Implementation of a Least Recently Used (LRU) cache.
 * Code found at:
 * https://medium.com/@germainnsibula/implementing-an-lru-cache-in-java-a-comprehensive-guide-94e8884ff17b
 *
 * The cache has a fixed capacity and uses a doubly linked list and a hash map
 * to store the entries efficiently.
 *
 * @param <K> The type of keys maintained by this cache
 * @param <V> The type of mapped values
 */
public class LRUCache<K, V> {
    private final int capacity;
    private final DoublyLinkedList<K, V> cacheList;
    private final HashMap<K, Node<K, V>> cacheMap;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cacheList = new DoublyLinkedList<>();
        this.cacheMap = new HashMap<>();
    }

    public V get(K key) {
        Node<K, V> node = cacheMap.get(key);
        if (node == null) {
            return null;
        }

        moveToHead(node);
        return node.value;
    }

    public void put(K key, V value) {
        Node<K, V> existingNode = cacheMap.get(key);
        if (existingNode != null) {
            System.out.println("LRU CACHE: replacing");
            existingNode.value = value;
            moveToHead(existingNode);
            return;
        }

        Node<K, V> newNode = new Node<>(key, value);
        cacheList.addFirst(newNode);
        cacheMap.put(key, newNode);
        System.out.println("LRU CACHE: inserting");

        System.out.println("WTFFFFFFFFFFF");
        if (cacheList.size() > capacity) {
            System.out.println("LRU CACHE: remove LRU entry");
            removeLeastRecentlyUsed();
        }
        System.out.println("LRU CACHE: done put");
    }

    public void remove(K key) {
        Node<K, V> node = cacheMap.get(key);
        cacheList.remove(node);
        cacheMap.remove(key); // we added, breaks implementation?
    }

    private void moveToHead(Node<K, V> node) {
        cacheList.remove(node);
        cacheList.addFirst(node);
    }

    private void removeLeastRecentlyUsed() {
        System.out.println("removeLeastRecentlyUsed1");
        Node<K, V> tail = cacheList.removeLast();
        System.out.println("removeLeastRecentlyUsed2");
        cacheMap.remove(tail.key);
        System.out.println("removeLeastRecentlyUsed3");
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static class DoublyLinkedList<K, V> {

        private Node<K, V> head;
        private Node<K, V> tail;

        public void addFirst(Node<K, V> node) {
            if (isEmpty()) {
                head = tail = node;
            } else {
                node.next = head;
                head.prev = node;
                head = node;
            }
        }

        public void remove(Node<K, V> node) {
            if (node == head) {
                head = head.next;
            } else if (node == tail) {
                tail = tail.prev;
            }

            if (node.prev != null) {
                node.prev.next = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }

        public Node<K, V> removeLast() {
            if (isEmpty()) {
                throw new IllegalStateException("List is empty");
            }

            Node<K, V> last = tail;
            remove(last);
            return last;
        }

        public boolean isEmpty() {
            return head == null;
        }

        public int size() {
            int size = 0;
            Node<K, V> current = head;
            System.out.println("head: " + head.value);
            while (current != null) {
                System.out.println("current: " + current.value);
                size++;
                current = current.next;
            }
            return size;
        }
    }
}
