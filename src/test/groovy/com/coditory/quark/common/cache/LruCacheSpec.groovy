package com.coditory.quark.common.cache

import spock.lang.Specification

class LruCacheSpec extends Specification {
    def "should generate missing value and cache it"() {
        given:
            LruCache<String, String> cache = new LruCache<>(10)

        when:
            String result = cache.get("a")
        then:
            result == null

        when:
            result = cache.get("a", { it + "X" })
        then:
            result == "aX"

        when:
            result = cache.get("a")
        then:
            result == "aX"
    }

    def "should put value to the cache"() {
        given:
            LruCache<String, String> cache = new LruCache<>(10)
        when:
            cache.put("a", "A")
        then:
            cache.get("a") == "A"
    }

    def "should remove oldest value when LRU limit is reached"() {
        given:
            LruCache<String, String> cache = new LruCache<>(3)
            cache.put("a", "A")
            cache.put("b", "B")
            cache.put("c", "C")
        when:
            cache.put("d", "D")
        then:
            cache.get("a") == null
            cache.get("b") == "B"
            cache.get("c") == "C"
            cache.get("d") == "D"
    }

    def "should remove least recently used value when LRU limit is reached"() {
        given:
            LruCache<String, String> cache = new LruCache<>(3)
            cache.put("a", "A")
            cache.put("b", "B")
            cache.put("c", "C")
        when:
            cache.get("a")
            cache.put("d", "D")
        then:
            cache.get("a") == "A"
            cache.get("b") == null
            cache.get("c") == "C"
            cache.get("d") == "D"
    }

    def "should cleanup cache"() {
        given:
            LruCache<String, String> cache = new LruCache<>(3)
            cache.put("a", "A")
            cache.put("b", "B")
        when:
            cache.clear()
        then:
            cache.get("a") == null
            cache.get("b") == null
    }

    def "should serve cache size"() {
        given:
            LruCache<String, String> cache = new LruCache<>(3)
        when:
            cache.put("a", "A")
            cache.put("b", "B")
        then:
            cache.size() == 2

        when:
            cache.clear()
        then:
            cache.size() == 0
    }
}
