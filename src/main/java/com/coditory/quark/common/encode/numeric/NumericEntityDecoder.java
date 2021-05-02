package com.coditory.quark.common.encode.numeric;

import com.coditory.quark.common.encode.IndexedTranslator;

import static com.coditory.quark.common.check.Args.checkNotNull;

final class NumericEntityDecoder extends IndexedTranslator {
    private static final NumericEntityDecoder INSTANCE = new NumericEntityDecoder();

    static NumericEntityDecoder getInstance() {
        return INSTANCE;
    }

    public enum SemiColonPolicy {
        REQUIRED,
        OPTIONAL,
        ERROR_IF_MISSING
    }

    private final SemiColonPolicy semiColonPolicy;

    NumericEntityDecoder() {
        this(SemiColonPolicy.REQUIRED);
    }

    NumericEntityDecoder(SemiColonPolicy semiColonPolicy) {
        this.semiColonPolicy = checkNotNull(semiColonPolicy, "semiColonPolicy");
    }

    @Override
    public int translate(String input, int index, StringBuilder out) {
        int seqEnd = input.length();
        if (input.charAt(index) == '&' && index < seqEnd - 2 && input.charAt(index + 1) == '#') {
            int start = index + 2;
            boolean isHex = false;
            char firstChar = input.charAt(start);
            if (firstChar == 'x' || firstChar == 'X') {
                start++;
                isHex = true;
                if (start == seqEnd) {
                    return 0;
                }
            }
            int end = start;
            while (end < seqEnd && (input.charAt(end) >= '0' && input.charAt(end) <= '9'
                    || input.charAt(end) >= 'a' && input.charAt(end) <= 'f'
                    || input.charAt(end) >= 'A' && input.charAt(end) <= 'F')) {
                end++;
            }

            boolean semiNext = end != seqEnd && input.charAt(end) == ';';
            if (!semiNext) {
                if (semiColonPolicy == SemiColonPolicy.REQUIRED) {
                    return 0;
                }
                if (semiColonPolicy == SemiColonPolicy.ERROR_IF_MISSING) {
                    throw new IllegalArgumentException("Semi-colon required at end of numeric entity");
                }
            }

            String entityStringValue = input.substring(start, end);
            int entityValue = parse(entityStringValue, isHex ? 16 : 10);
            if (entityValue > 0xFFFF) {
                char[] chars = Character.toChars(entityValue);
                out.append(chars[0]);
                out.append(chars[1]);
            } else {
                out.append((char) entityValue);
            }
            return 2 + end - start + (isHex ? 1 : 0) + (semiNext ? 1 : 0);
        }
        return 0;
    }

    private int parse(String value, int base) {
        try {
            return Integer.parseInt(value, base);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }
}
