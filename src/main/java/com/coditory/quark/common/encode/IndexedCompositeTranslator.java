package com.coditory.quark.common.encode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.coditory.quark.common.check.Args.checkNotNull;

final class IndexedCompositeTranslator extends IndexedTranslator {
    private final List<IndexedTranslator> translators = new ArrayList<>();

    public IndexedCompositeTranslator(final IndexedTranslator... translators) {
        this(Arrays.asList(translators));
    }

    public IndexedCompositeTranslator(List<IndexedTranslator> translators) {
        checkNotNull(translators, "translators");
        for (final IndexedTranslator translator : translators) {
            if (translator != null) {
                this.translators.add(translator);
            }
        }
    }

    @Override
    public int translate(String input, int index, StringBuilder out) {
        for (IndexedTranslator translator : translators) {
            int modified = translator.translate(input, index, out);
            if (modified > 0) {
                return modified;
            }
        }
        return 0;
    }
}
