package com.coditory.quark.common.base

import groovy.transform.CompileStatic

@CompileStatic
class MapAssertions {
    static boolean assertMapEqual(Map<Object, Object> actual, Map<Object, Object> expected) {
        assertMapKeysEqual(actual, expected)
        Map<Object, List<Object>> mapDiff = [:]
        actual.entrySet()
                .findAll { expected[it.key] != it.value }
                .each { mapDiff[it.key] = [it.value, expected[it.key]] }
        assert mapDiff.isEmpty(): "Different map values: " + mapDiff
        return true
    }

    private static boolean assertMapKeysEqual(Map<Object, Object> actual, Map<Object, Object> expected) {
        Set<Object> actualKeys = actual.keySet()
        Set<Object> expectedKeys = expected.keySet()
        Set<Object> diffActualKeys = actualKeys - expectedKeys
        Set<Object> diffExpectedKeys = expectedKeys - actualKeys
        assert diffExpectedKeys.isEmpty(): "Missing keys: " + diffExpectedKeys
        assert diffActualKeys.isEmpty(): "Too many keys: " + diffActualKeys
        return true
    }
}
