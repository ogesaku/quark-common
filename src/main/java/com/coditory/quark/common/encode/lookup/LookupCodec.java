package com.coditory.quark.common.encode.lookup;

import com.coditory.quark.common.encode.TranslationCodec;
import com.coditory.quark.common.encode.Translator;
import com.coditory.quark.common.collection.Maps;

import java.util.Map;

import static com.coditory.quark.common.check.Args.checkNotNull;

public final class LookupCodec {
    public static TranslationCodec forLookupMap(Map<String, String> lookupMap) {
        checkNotNull(lookupMap, "lookupMap");
        Translator encoder = LookupTranslator.of(lookupMap);
        Translator decoder = LookupTranslator.of(Maps.invert(lookupMap));
        return new TranslationCodec(encoder, decoder);
    }
}
