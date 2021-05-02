package com.coditory.quark.common.random;

public final class RandomValues {
    private static final RandomValueGenerator generator = RandomValueGenerator.threadLocalRandomGenerator();

    private RandomValues() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public boolean randomBoolean() {
        return generator.randomBoolean();
    }

    public byte[] randomBytes(int count) {
        return generator.randomBytes(count);
    }

    public int randomInt() {
        return generator.randomInt();
    }

    public int randomInt(int startInclusive, int endExclusive) {
        return generator.randomInt(startInclusive, endExclusive);
    }

    public long randomLong() {
        return generator.randomLong();
    }

    public long randomLong(long startInclusive, long endExclusive) {
        return generator.randomLong(startInclusive, endExclusive);
    }

    public long randomLong(long n) {
        return generator.randomLong(n);
    }

    public double randomDouble(double startInclusive, double endExclusive) {
        return generator.randomDouble(startInclusive, endExclusive);
    }

    public double randomDouble() {
        return generator.randomDouble();
    }

    public float randomFloat(float startInclusive, float endExclusive) {
        return generator.randomFloat(startInclusive, endExclusive);
    }

    public float randomFloat() {
        return generator.randomFloat();
    }

    public String randomString(int count) {
        return generator.randomString(count);
    }

    public String randomAsciiString(int count) {
        return generator.randomAsciiString(count);
    }

    public String randomAlphabeticString(int count) {
        return generator.randomAlphabeticString(count);
    }

    public String randomAlphanumericString(int count) {
        return generator.randomAlphanumericString(count);
    }

    public String randomNumericString(int count) {
        return generator.randomNumericString(count);
    }

    public static String randomString(int count, String alphabet) {
        return generator.randomString(count, alphabet);
    }

    public static String randomString(int count, char... alphabet) {
        return generator.randomString(count, alphabet);
    }
}
