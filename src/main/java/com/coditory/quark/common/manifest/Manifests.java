package com.coditory.quark.common.manifest;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.coditory.quark.common.check.Args.checkNotBlank;

public final class Manifests {
    private Manifests() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    private static final ManifestReader INSTANCE = new ManifestReader();

    public static Map<String, String> readManifestAttributes(String implementationTitle) {
        checkNotBlank(implementationTitle, "implementationTitle");
        return INSTANCE.readAttributes(implementationTitle);
    }

    public static Map<String, String> readManifestAttributesOrEmpty(String implementationTitle) {
        checkNotBlank(implementationTitle, "implementationTitle");
        Map<String, String> attributes = INSTANCE.readAttributesOrNull(implementationTitle);
        return attributes == null ? Map.of() : attributes;
    }

    @Nullable
    public static Map<String, String> readManifestAttributesOrNull(String implementationTitle) {
        checkNotBlank(implementationTitle, "implementationTitle");
        return INSTANCE.readAttributesOrNull(implementationTitle);
    }
}
