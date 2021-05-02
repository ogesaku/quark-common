package com.coditory.quark.common.encode;

import com.coditory.quark.common.encode.lookup.LookupTranslator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.coditory.quark.common.check.Args.checkNotNull;

public interface Translator {
    static Translator identity() {
        return (input, out) -> {
            out.append(input);
            return false;
        };
    }

    boolean translate(String input, StringBuilder out);

    default String translate(String input) {
        checkNotNull(input, "input");
        StringBuilder builder = new StringBuilder(input.length() * 2);
        boolean changed = translate(input, builder);
        return changed
                ? builder.toString()
                : input;
    }

    static TranslatorBuilder builder() {
        return new TranslatorBuilder();
    }

    class TranslatorBuilder {
        private final List<IndexedTranslator> translators = new ArrayList<>();

        private TranslatorBuilder() {
        }

        public TranslatorBuilder translate(IndexedTranslator translator) {
            checkNotNull(translator, "translator");
            this.translators.add(translator);
            return this;
        }

        public TranslatorBuilder translate(Map<String, String> lookupMap) {
            checkNotNull(lookupMap, "lookupMap");
            return translate(LookupTranslator.of(lookupMap));
        }

        public Translator build() {
            if (translators.isEmpty()) {
                return identity();
            }
            if (translators.size() == 1) {
                return translators.get(0);
            }
            return new IndexedCompositeTranslator(translators);
        }
    }
}
