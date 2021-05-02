package com.coditory.quark.common.util;

import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.coditory.quark.common.check.Args.check;
import static com.coditory.quark.common.check.Args.checkNotNull;
import static java.util.Comparator.naturalOrder;

public final class Range<T> {
    private static final ComparableComparator DEFAULT_COMPARATOR = new ComparableComparator();
    private static final Pattern RANGE_PATTERN = Pattern.compile("([\\[(] *([0-9]+) *, *([0-9]) *([\\])]))?");

    public static Range<Integer> parse(String text) {
        Range<Integer> range = parseOrNull(text);
        check(range != null, "Could not parse int range: " + text);
        return range;
    }

    public static Range<Integer> parseOrNull(String text) {
        Matcher matcher = RANGE_PATTERN.matcher(text);
        if (!matcher.matches()) {
            return null;
        }
        boolean minInclusive = matcher.group(1).equals("[");
        Integer min = Integers.parseInteger(matcher.group(2));
        boolean maxInclusive = matcher.group(3).equals("]");
        Integer max = Integers.parseInteger(matcher.group(4));
        return new Range<>(min, minInclusive, max, maxInclusive, naturalOrder());
    }

    public static <T extends Comparable<T>> Range<T> ofExclusive(T minExclusive, T maxExclusive) {
        return ofExclusive(minExclusive, maxExclusive, null);
    }

    public static <T> Range<T> ofExclusive(T minExclusive, T maxExclusive, Comparator<T> comparator) {
        return new Range<>(minExclusive, false, maxExclusive, false, comparator);
    }

    public static <T extends Comparable<T>> Range<T> ofInclusive(T minInclusive, T maxInclusive) {
        return ofInclusive(minInclusive, maxInclusive, null);
    }

    public static <T> Range<T> ofInclusive(T minInclusive, T maxInclusive, Comparator<T> comparator) {
        return new Range<>(minInclusive, true, maxInclusive, true, comparator);
    }

    public static <T extends Comparable<T>> Range<T> ofMaxExclusive(T minInclusive, T maxExclusive) {
        return ofMaxExclusive(minInclusive, maxExclusive, null);
    }

    public static <T> Range<T> ofMaxExclusive(T minInclusive, T maxExclusive, Comparator<T> comparator) {
        return new Range<>(minInclusive, true, maxExclusive, false, comparator);
    }

    public static <T extends Comparable<T>> Range<T> ofMinExclusive(T minExclusive, T maxInclusive) {
        return ofMinExclusive(minExclusive, maxInclusive, null);
    }

    public static <T> Range<T> ofMinExclusive(T minExclusive, T maxInclusive, Comparator<T> comparator) {
        return new Range<>(minExclusive, false, maxInclusive, true, comparator);
    }

    public static <T extends Comparable<T>> Range<T> of(T element) {
        return ofInclusive(element, element);
    }

    public static <T> Range<T> of(T element, Comparator<T> comparator) {
        checkNotNull(element, "element");
        return new Range<>(element, true, element, true, comparator);
    }

    private transient String toString;
    private transient int hashCode;
    private final Comparator<T> comparator;
    private final boolean maxInclusive;
    private final boolean minInclusive;
    private final T max;
    private final T min;

    @SuppressWarnings("unchecked")
    private Range(T min, boolean minInclusive, T max, boolean maxInclusive, Comparator<T> comparator) {
        checkNotNull(min, "min");
        checkNotNull(max, "max");
        this.comparator = comparator == null
                ? DEFAULT_COMPARATOR
                : comparator;
        this.min = min;
        this.minInclusive = minInclusive;
        this.max = max;
        this.maxInclusive = maxInclusive;
        int cmp = this.comparator.compare(min, max);
        check(cmp <= 0, () -> "Expected min <= max. Got min: " + min + ", max: " + max);
        if (cmp == 0) {
            check(maxInclusive && minInclusive, "Expected maxInclusive && minInclusive");
        }
    }

    public boolean contains(T element) {
        checkNotNull(element, "element");
        return isMinBeforeOrEqual(element)
                && isMaxAfterOrEqual(element);
    }

    public boolean contains(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        return (contains(otherRange.min) || (min == otherRange.min && !minInclusive && !otherRange.minInclusive))
                && (contains(otherRange.max) || (max == otherRange.max && !maxInclusive && !otherRange.maxInclusive));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Range<T> range = (Range<T>) obj;
        return min.equals(range.min) &&
                max.equals(range.max) &&
                minInclusive == range.minInclusive &&
                maxInclusive == range.maxInclusive;
    }

    public T getMax() {
        return max;
    }

    public T getMin() {
        return min;
    }

    public boolean isMaxInclusive() {
        return maxInclusive;
    }

    public boolean isMinInclusive() {
        return minInclusive;
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (hashCode == 0) {
            result = 17;
            result = 37 * result + getClass().hashCode();
            result = 37 * result + min.hashCode();
            result = 37 * result + max.hashCode();
            result = 37 * result + Boolean.hashCode(maxInclusive);
            result = 37 * result + Boolean.hashCode(minInclusive);
            hashCode = result;
        }
        return result;
    }

    @Nullable
    public Range<T> intersectionOrNull(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        if (!this.overlaps(otherRange)) {
            return null;
        }
        if (this.equals(otherRange)) {
            return this;
        }
        T min = this.min;
        boolean minInclusive = this.minInclusive;
        int minCmp = comparator.compare(this.min, otherRange.min);
        if (minCmp < 0) {
            min = otherRange.min;
            minInclusive = otherRange.minInclusive;
        } else if (minCmp == 0) {
            minInclusive = minInclusive && otherRange.minInclusive;
        }
        T max = this.max;
        boolean maxInclusive = this.maxInclusive;
        int maxCmp = comparator.compare(this.max, otherRange.max);
        if (maxCmp > 0) {
            max = otherRange.max;
            maxInclusive = otherRange.maxInclusive;
        } else if (maxCmp == 0) {
            maxInclusive = maxInclusive && otherRange.maxInclusive;
        }
        return new Range<>(min, minInclusive, max, maxInclusive, comparator);
    }

    public Range<T> intersection(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        Range<T> result = intersectionOrNull(otherRange);
        if (result == null) {
            throw new IllegalArgumentException("Could create intersection of "
                    + this + " and " + otherRange);
        }
        return result;
    }

    @Nullable
    public Range<T> sumOrNull(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        if (!overlaps(otherRange) && !isAdjacent(otherRange)) {
            return null;
        }
        if (this.equals(otherRange)) {
            return this;
        }
        T min = this.min;
        boolean minInclusive = this.minInclusive;
        int minCmp = comparator.compare(this.min, otherRange.min);
        if (minCmp > 0) {
            min = otherRange.min;
            minInclusive = otherRange.minInclusive;
        } else if (minCmp == 0) {
            minInclusive = minInclusive || otherRange.minInclusive;
        }
        T max = this.max;
        boolean maxInclusive = this.maxInclusive;
        int maxCmp = comparator.compare(this.max, otherRange.max);
        if (maxCmp < 0) {
            max = otherRange.max;
            maxInclusive = otherRange.maxInclusive;
        } else if (maxCmp == 0) {
            maxInclusive = maxInclusive || otherRange.maxInclusive;
        }
        return new Range<>(min, minInclusive, max, maxInclusive, comparator);
    }

    public Range<T> sum(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        Range<T> result = sumOrNull(otherRange);
        if (result == null) {
            throw new IllegalArgumentException("Could create sum of "
                    + this + " and " + otherRange);
        }
        return result;
    }

    public boolean isAdjacent(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        return (this.minInclusive != otherRange.maxInclusive && this.min == otherRange.max)
                || (this.maxInclusive != otherRange.minInclusive && this.max == otherRange.min);
    }

    public Range<T> minusOrNull(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        if (otherRange.contains(this)) {
            return null;
        }
        if (this.contains(otherRange) && !this.minOrMaxEquals(otherRange)) {
            return null;
        }
        if (!this.overlaps(otherRange)) {
            return this;
        }
        T min = this.min;
        boolean minInclusive = this.minInclusive;
        int minMaxCmp = comparator.compare(this.min, otherRange.max);
        int minMinCmp = comparator.compare(this.min, otherRange.min);
        if (minMaxCmp < 0 && minMinCmp >= 0) {
            min = otherRange.max;
            minInclusive = !otherRange.maxInclusive;
        } else if (minMaxCmp == 0) {
            minInclusive = minInclusive && !otherRange.maxInclusive;
        }
        T max = this.max;
        boolean maxInclusive = this.maxInclusive;
        int maxMinCmp = comparator.compare(this.max, otherRange.min);
        int maxMaxCmp = comparator.compare(this.max, otherRange.max);
        if (maxMinCmp > 0 && maxMaxCmp <= 0) {
            max = otherRange.min;
            maxInclusive = !otherRange.minInclusive;
        } else if (maxMinCmp == 0) {
            maxInclusive = maxInclusive && !otherRange.minInclusive;
        }
        return new Range<>(min, minInclusive, max, maxInclusive, comparator);
    }

    public Range<T> minus(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        Range<T> result = minusOrNull(otherRange);
        if (result == null) {
            throw new IllegalArgumentException("Could not create difference between "
                    + this + " and " + otherRange);
        }
        return result;
    }

    public boolean minEquals(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        return min == otherRange.min
                && minInclusive == otherRange.minInclusive;
    }

    public boolean maxEquals(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        return max == otherRange.max
                && maxInclusive == otherRange.maxInclusive;
    }

    public boolean minOrMaxEquals(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        return minEquals(otherRange) || maxEquals(otherRange);
    }

    public boolean isMaxBefore(T element) {
        checkNotNull(element, "element");
        int cmp = comparator.compare(max, element);
        return cmp < 0 || (cmp == 0 && !maxInclusive);
    }

    public boolean isMaxBeforeOrEqual(T element) {
        checkNotNull(element, "element");
        int cmp = comparator.compare(max, element);
        return cmp < 0 || (cmp == 0 && maxInclusive);
    }

    public boolean isMaxAfter(T element) {
        checkNotNull(element, "element");
        int cmp = comparator.compare(max, element);
        return cmp > 0;
    }

    public boolean isMaxAfterOrEqual(T element) {
        checkNotNull(element, "element");
        int cmp = comparator.compare(max, element);
        return cmp > 0 || (cmp == 0 && maxInclusive);
    }

    public boolean isMinBefore(T element) {
        checkNotNull(element, "element");
        int cmp = comparator.compare(min, element);
        return cmp < 0;
    }

    public boolean isMinBeforeOrEqual(T element) {
        checkNotNull(element, "element");
        int cmp = comparator.compare(min, element);
        return cmp < 0 || (cmp == 0 && minInclusive);
    }

    public boolean isMinAfter(T element) {
        checkNotNull(element, "element");
        int cmp = comparator.compare(min, element);
        return cmp > 0 || (cmp == 0 && !minInclusive);
    }

    public boolean isMinAfterOrEqual(T element) {
        checkNotNull(element, "element");
        int cmp = comparator.compare(min, element);
        return cmp > 0 || (cmp == 0 && minInclusive);
    }

    public boolean overlaps(Range<T> otherRange) {
        checkNotNull(otherRange, "otherRange");
        int minCmp = comparator.compare(this.min, otherRange.min);
        int maxCmp = comparator.compare(this.max, otherRange.max);
        int maxMinCmp = comparator.compare(this.max, otherRange.min);
        int minMaxCmp = comparator.compare(this.min, otherRange.max);
        if (minInclusive && otherRange.minInclusive && minCmp == 0) {
            return true;
        }
        if (maxInclusive && otherRange.maxInclusive && maxCmp == 0) {
            return true;
        }
        if (maxInclusive && otherRange.minInclusive && maxMinCmp == 0) {
            return true;
        }
        if (minInclusive && otherRange.maxInclusive && minMaxCmp == 0) {
            return true;
        }
        return minCmp <= 0 && maxMinCmp > 0
                || minCmp >= 0 && minMaxCmp < 0;
    }

    @Override
    public String toString() {
        if (toString == null) {
            String left = minInclusive ? "[" : "(";
            String right = maxInclusive ? "]" : ")";
            toString = "Range" + left + min + ".." + max + right;
        }
        return toString;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final class ComparableComparator implements Comparator {
        @Override
        public int compare(Object obj1, Object obj2) {
            return ((Comparable) obj1).compareTo(obj2);
        }
    }

    public static <T> RangeBuilder<T> builder() {
        return new RangeBuilder<>();
    }

    public static final class RangeBuilder<T> {
        private T min;
        private boolean minInclusive;
        private T max;
        private boolean maxInclusive;
        private Comparator<T> comparator;

        public RangeBuilder<T> minInclusive(T min) {
            checkNotNull(min, "min");
            this.minInclusive = true;
            this.min = min;
            return this;
        }

        public RangeBuilder<T> maxInclusive(T max) {
            checkNotNull(max, "max");
            this.maxInclusive = true;
            this.max = max;
            return this;
        }

        public RangeBuilder<T> minExclusive(T min) {
            checkNotNull(min, "min");
            this.minInclusive = false;
            this.min = min;
            return this;
        }

        public RangeBuilder<T> maxExclusive(T max) {
            checkNotNull(max, "max");
            this.maxInclusive = false;
            this.max = max;
            return this;
        }

        public RangeBuilder<T> comparator(Comparator<T> comparator) {
            this.comparator = comparator;
            return this;
        }

        public Range<T> build() {
            checkNotNull(max, "max");
            checkNotNull(min, "min");
            return new Range<>(min, minInclusive, max, maxInclusive, comparator);
        }
    }
}
