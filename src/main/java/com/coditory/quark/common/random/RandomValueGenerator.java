package com.coditory.quark.common.random;

import com.coditory.quark.common.util.Strings;
import com.coditory.quark.common.check.Args;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static com.coditory.quark.common.check.Args.check;
import static com.coditory.quark.common.check.Args.checkNotNegative;
import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.text.Alphabets.ALPHABETIC;
import static com.coditory.quark.common.text.Alphabets.ALPHANUMERIC;
import static com.coditory.quark.common.text.Alphabets.NUMERIC;
import static com.coditory.quark.common.text.Alphabets.ASCII_PRINTABLE;

public final class RandomValueGenerator {
    public static RandomValueGenerator threadLocalRandomGenerator() {
        return new RandomValueGenerator(ThreadLocalRandom::current);
    }

    public static RandomValueGenerator secureRandomGenerator() {
        Random random = new SecureRandom();
        return new RandomValueGenerator(() -> random);
    }

    public static RandomValueGenerator randomGenerator(long seed) {
        Random random = new Random(seed);
        return new RandomValueGenerator(() -> random);
    }

    public static RandomValueGenerator randomGenerator() {
        Random random = new Random();
        return new RandomValueGenerator(() -> random);
    }

    private final Supplier<Random> randomSupplier;

    private RandomValueGenerator(Supplier<Random> randomSupplier) {
        this.randomSupplier = randomSupplier;
    }

    private Random random() {
        return randomSupplier.get();
    }

    public boolean randomBoolean() {
        return random().nextBoolean();
    }

    public byte[] randomBytes(int count) {
        Args.checkNotNegative(count, "count");
        byte[] result = new byte[count];
        random().nextBytes(result);
        return result;
    }

    public int randomInt() {
        return randomInt(0, Integer.MAX_VALUE);
    }

    public int randomInt(int endExclusive) {
        return randomInt(0, endExclusive);
    }

    public int randomInt(int startInclusive, int endExclusive) {
        Args.checkNotNegative(startInclusive, "startInclusive");
        Args.checkNotNegative(endExclusive, "endExclusive");
        check(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        return startInclusive == endExclusive
                ? startInclusive
                : startInclusive + random().nextInt(endExclusive - startInclusive);
    }

    public long randomLong() {
        return randomLong(Long.MAX_VALUE);
    }

    public long randomLong(long startInclusive, long endExclusive) {
        checkNotNegative(startInclusive, "startInclusive");
        checkNotNegative(endExclusive, "endExclusive");
        check(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        return startInclusive == endExclusive
                ? startInclusive
                : startInclusive + randomLong(endExclusive - startInclusive);
    }

    public long randomLong(long endExclusive) {
        long bits;
        long val;
        do {
            bits = random().nextLong() >>> 1;
            val = bits % endExclusive;
        } while (bits - val + (endExclusive - 1) < 0);
        return val;
    }

    public double randomDouble(double endExclusive) {
        return randomDouble(0, endExclusive);
    }

    public double randomDouble(double startInclusive, double endExclusive) {
        Args.checkNotNegative(startInclusive, "startInclusive");
        Args.checkNotNegative(endExclusive, "endExclusive");
        check(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        return startInclusive == endExclusive
                ? startInclusive
                : startInclusive + ((endExclusive - startInclusive) * random().nextDouble());
    }

    public double randomDouble() {
        return randomDouble(0, Double.MAX_VALUE);
    }

    public float randomFloat(float startInclusive, float endExclusive) {
        Args.checkNotNegative(startInclusive, "startInclusive");
        Args.checkNotNegative(endExclusive, "endExclusive");
        check(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        return startInclusive == endExclusive
                ? startInclusive
                : startInclusive + ((endExclusive - startInclusive) * random().nextFloat());
    }

    public float randomFloat(float endExclusive) {
        return randomFloat(0, endExclusive);
    }

    public float randomFloat() {
        return randomFloat(0, Float.MAX_VALUE);
    }

    public String randomAsciiString(int count) {
        return randomString(count, ASCII_PRINTABLE);
    }

    public String randomAlphabeticString(int count) {
        return randomString(count, ALPHABETIC);
    }

    public String randomAlphanumericString(int count) {
        return randomString(count, ALPHANUMERIC);
    }

    public String randomNumericString(int count) {
        return randomString(count, NUMERIC);
    }

    public String randomString(int count, String alphabet) {
        Args.checkNotNegative(count, "count");
        Args.checkNotBlank(alphabet, "alphabet");
        return randomString(count, alphabet.toCharArray());
    }

    public String randomString(int count, char... alphabet) {
        Args.checkNotNegative(count, "count");
        checkNotNull(alphabet, "alphabet");
        check(alphabet.length >= 1, "Expected non empty alphabet");
        if (count == 0) {
            return "";
        }
        if (alphabet.length == 1) {
            return Strings.repeat(alphabet[0], count);
        }
        Random random = random();
        StringBuilder builder = new StringBuilder(count);
        count--;
        while (count >= 0) {
            int codePoint = alphabet[random.nextInt(alphabet.length)];
            int numberOfChars = Character.charCount(codePoint);
            if (count == 0 && numberOfChars > 1) {
                count++;
                continue;
            }
            builder.appendCodePoint(codePoint);
            if (numberOfChars == 2) {
                count--;
            }
            count--;
        }
        return builder.toString();
    }
}
