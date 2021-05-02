package com.coditory.quark.common.manifest;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.jar.Manifest;

import static com.coditory.quark.common.check.Args.checkNotBlank;
import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.throwable.Throwables.sneakyThrow;
import static java.util.Map.entry;
import static java.util.jar.Attributes.Name.IMPLEMENTATION_TITLE;
import static java.util.stream.Collectors.toUnmodifiableMap;

public class ManifestReader {
    private static final String MANIFEST_PATH = "META-INF/MANIFEST.MF";
    private final ClassLoader classLoader;

    public ManifestReader() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ManifestReader(ClassLoader classLoader) {
        checkNotNull(classLoader, "classLoader");
        this.classLoader = classLoader;
    }

    public Map<String, String> readAttributes(String title) {
        checkNotBlank(title, "title");
        Manifest manifest = readManifest(title);
        return manifestToMap(manifest);
    }

    @Nullable
    public Map<String, String> readAttributesOrNull(String title) {
        checkNotBlank(title, "title");
        Manifest manifest = readManifestOrNull(title);
        return manifest == null ? null : manifestToMap(manifest);
    }

    public Map<String, String> readAttributesWithProperty(String name, String value) {
        checkNotBlank(name, "name");
        Manifest manifest = readManifestWithProperty(name, value);
        return manifestToMap(manifest);
    }

    @Nullable
    public Map<String, String> readAttributesWithPropertyOrNull(String name, String value) {
        checkNotBlank(name, "name");
        Manifest manifest = readManifestWithPropertyOrNull(name, value);
        return manifest == null ? null : manifestToMap(manifest);
    }

    public Map<String, String> readAttributes(Predicate<Manifest> manifestPredicate) {
        checkNotNull(manifestPredicate, "manifestPredicate");
        Manifest manifest = readManifest(manifestPredicate);
        return manifestToMap(manifest);
    }

    @Nullable
    public Map<String, String> readAttributesOrNull(Predicate<Manifest> manifestPredicate) {
        checkNotNull(manifestPredicate, "manifestPredicate");
        Manifest manifest = readManifestOrNull(manifestPredicate);
        return manifest == null ? null : manifestToMap(manifest);
    }

    private Map<String, String> manifestToMap(Manifest manifest) {
        return manifest.getMainAttributes().entrySet()
                .stream()
                .filter(e -> e.getKey() != null && e.getValue() != null)
                .map(e -> entry(Objects.toString(e.getKey()), Objects.toString(e.getValue())))
                .collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Manifest readManifest(String title) {
        checkNotBlank(title, "title");
        return readManifestWithProperty(IMPLEMENTATION_TITLE.toString(), title);
    }

    @Nullable
    public Manifest readManifestOrNull(String title) {
        checkNotBlank(title, "title");
        return readManifestWithPropertyOrNull(IMPLEMENTATION_TITLE.toString(), title);
    }

    public Manifest readManifestWithProperty(String name, String value) {
        checkNotBlank(name, "name");
        Manifest manifest = readManifestWithPropertyOrNull(name, value);
        if (manifest == null) {
            throw new IllegalStateException("Could not find " + MANIFEST_PATH + " file with property " + name + ": " + value);
        }
        return manifest;
    }

    @Nullable
    public Manifest readManifestWithPropertyOrNull(String name, String value) {
        checkNotBlank(name, "name");
        return readManifestOrNull(candidate -> {
            String actual = candidate.getMainAttributes().getValue(name);
            return Objects.equals(actual, value);
        });
    }

    public Manifest readManifest(Predicate<Manifest> manifestPredicate) {
        checkNotNull(manifestPredicate, "manifestPredicate");
        Manifest manifest = readManifestOrNull(manifestPredicate);
        if (manifest == null) {
            throw new IllegalStateException("Could not find " + MANIFEST_PATH + " file that satisfies a predicate.");
        }
        return manifest;
    }

    @Nullable
    public Manifest readManifestOrNull(Predicate<Manifest> manifestPredicate) {
        checkNotNull(manifestPredicate, "manifestPredicate");
        Enumeration<URL> manifestUrls = sneakyThrow(
                () -> classLoader.getResources(MANIFEST_PATH),
                e -> new RuntimeException("Could not open " + MANIFEST_PATH, e)
        );
        while (manifestUrls.hasMoreElements()) {
            URL url = manifestUrls.nextElement();
            Manifest manifest = toManifest(url);
            if (manifestPredicate.test(manifest)) {
                return manifest;
            }
        }
        return null;
    }

    private Manifest toManifest(URL manifestUrl) {
        try (InputStream inputStream = manifestUrl.openConnection().getInputStream()) {
            return new Manifest(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to open manifest file: " + manifestUrl, e);
        }
    }
}
