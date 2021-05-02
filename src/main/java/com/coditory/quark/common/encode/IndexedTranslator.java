package com.coditory.quark.common.encode;

import static com.coditory.quark.common.check.Args.checkNotNull;

public abstract class IndexedTranslator implements Translator {
    protected abstract int translate(String input, int index, StringBuilder out);

    @Override
    public boolean translate(String input, StringBuilder out) {
        checkNotNull(input, "input");
        checkNotNull(out, "out");
        boolean changed = false;
        int pos = 0;
        int len = input.length();
        while (pos < len) {
            int consumed = translate(input, pos, out);
            if (consumed == 0) {
                // inlined implementation of Character.toChars(Character.codePointAt(input, pos))
                // avoids allocating temp char arrays and duplicate checks
                char c1 = input.charAt(pos);
                out.append(c1);
                pos++;
                if (Character.isHighSurrogate(c1) && pos < len) {
                    char c2 = input.charAt(pos);
                    if (Character.isLowSurrogate(c2)) {
                        out.append(c2);
                        pos++;
                    }
                }
            } else {
                changed = true;
                // contract with translators is that they have to understand codepoints
                // and they just took care of a surrogate pair
                for (int pt = 0; pt < consumed; pt++) {
                    pos += Character.charCount(Character.codePointAt(input, pos));
                }
            }
        }
        return changed;
    }
}
