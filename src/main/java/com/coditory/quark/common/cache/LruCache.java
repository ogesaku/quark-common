package com.coditory.quark.common.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

import static com.coditory.quark.common.check.Args.checkPositive;

/**
 * Simple Least Recently Used cache, bounded by the maximum size given
 * to the class constructor.
 * <p>This implementation is backed by a {@code ConcurrentHashMap} for storing
 * the cached values and a {@code ConcurrentLinkedQueue} for ordering the keys
 * and choosing the least recently used key when the cache is at full capacity.
 */
public class LruCache<K, V> {
    private final int maxSize;
    private final ConcurrentLinkedDeque<K> queue = new ConcurrentLinkedDeque<>();
    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile int size = 0;

    public LruCache(int maxSize) {
        this.maxSize = checkPositive(maxSize, "maxSize");
    }

    public V get(K key) {
        V cached = cache.get(key);
        if (cached == null) {
            return null;
        }
        if (size < maxSize) {
            return cached;
        }
        lock.readLock().lock();
        try {
            if (queue.removeLastOccurrence(key)) {
                queue.offer(key);
            }
        } finally {
            lock.readLock().unlock();
        }
        return cached;
    }

    public int size() {
        return size;
    }

    public V get(K key, Function<K, V> generator) {
        V cached = get(key);
        if (cached != null) {
            return cached;
        }
        this.lock.writeLock().lock();
        try {
            // Retrying in case of concurrent reads on the same key
            cached = cache.get(key);
            if (cached != null) {
                if (queue.removeLastOccurrence(key)) {
                    queue.offer(key);
                }
                return cached;
            }
            // Generate value first, to prevent size inconsistency
            V value = generator.apply(key);
            int cacheSize = size;
            if (cacheSize == maxSize) {
                K leastUsed = queue.poll();
                if (leastUsed != null) {
                    cache.remove(leastUsed);
                    cacheSize--;
                }
            }
            queue.offer(key);
            cache.put(key, value);
            size = cacheSize + 1;
            return value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void put(K key, V value) {
        this.lock.writeLock().lock();
        try {
            // Generate value first, to prevent size inconsistency
            int cacheSize = size;
            if (cacheSize == maxSize) {
                K leastUsed = queue.poll();
                if (leastUsed != null) {
                    cache.remove(leastUsed);
                    cacheSize--;
                }
            }
            queue.offer(key);
            cache.put(key, value);
            size = cacheSize + 1;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear() {
        this.lock.writeLock().lock();
        try {
            queue.clear();
            cache.clear();
            size = 0;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
