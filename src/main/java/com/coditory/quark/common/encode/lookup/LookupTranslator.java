package com.coditory.quark.common.encode.lookup;

import com.coditory.quark.common.encode.IndexedTranslator;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import static com.coditory.quark.common.check.Args.checkNotNull;

public final class LookupTranslator extends IndexedTranslator {
    public static LookupTranslator of(Map<String, String> lookupMap) {
        checkNotNull(lookupMap, "lookupMap");
        return new LookupTranslator(lookupMap);
    }

    private final Map<String, String> lookupMap;
    private final BitSet prefixSet;
    private final int shortest;
    private final int longest;

    private LookupTranslator(Map<String, String> lookupMap) {
        this.lookupMap = new HashMap<>();
        this.prefixSet = new BitSet();
        int currentShortest = Integer.MAX_VALUE;
        int currentLongest = 0;

        for (final Map.Entry<String, String> pair : lookupMap.entrySet()) {
            this.lookupMap.put(pair.getKey(), pair.getValue());
            this.prefixSet.set(pair.getKey().charAt(0));
            final int sz = pair.getKey().length();
            if (sz < currentShortest) {
                currentShortest = sz;
            }
            if (sz > currentLongest) {
                currentLongest = sz;
            }
        }
        this.shortest = currentShortest;
        this.longest = currentLongest;
    }

    @Override
    protected int translate(String input, int index, StringBuilder out) {
        if (!prefixSet.get(input.charAt(index))) {
            return 0;
        }
        int max = longest;
        if (index + longest > input.length()) {
            max = input.length() - index;
        }
        for (int i = max; i >= shortest; i--) {
            CharSequence subSeq = input.subSequence(index, index + i);
            String result = lookupMap.get(subSeq.toString());
            if (result != null) {
                out.append(result);
                return i;
            }
        }
        return 0;
    }
}
