package com.coditory.quark.common.base

import groovy.transform.CompileStatic

import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Supplier

import static java.util.Objects.requireNonNull

@CompileStatic
class ClassLoaderStub extends ClassLoader {
    private final Map<String, List<URL>> overrides = new ConcurrentHashMap<>()
    private final Set<String> errorLocations = new HashSet<>()

    void addUrl(String name, URL url) {
        requireNonNull(name)
        requireNonNull(url)
        overrides.put(name, [url])
    }

    void addUrls(String name, List<URL> urls) {
        requireNonNull(name)
        requireNonNull(urls)
        overrides.put(name, urls)
    }

    void addFile(String name, File file) {
        try {
            addUrl(name, file.toURI().toURL())
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid file", e)
        }
    }

    void addFiles(String name, List<File> files) {
        List<URL> urls = files.collect { it.toURI().toURL() }
        addUrls(name, urls)
    }

    void addFilePath(String name, String filePath) {
        addFile(name, Path.of(filePath).toFile())
    }

    void addFilePaths(String name, List<String> filePaths) {
        List<File> files = filePaths.collect { Path.of(it).toFile() }
        addFiles(name, files)
    }

    public <T> T setupInThreadContext(Supplier<T> supplier) {
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader()
        Thread.currentThread().setContextClassLoader(this)
        try {
            return supplier.get()
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader)
        }
    }

    @Override
    URL getResource(String name) {
        simulateError(name)
        List<URL> urls = overrides.get(name)
        return urls != null && urls.size() > 0
                ? urls.get(0)
                : null
    }

    @Override
    Enumeration<URL> getResources(String name) {
        simulateError(name)
        List<URL> urls = overrides.getOrDefault(name, List.of())
        return Collections.enumeration(urls)
    }

    void addErrorLocation(String name) {
        errorLocations.add(name)
    }

    private void simulateError(String name) {
        if (errorLocations.contains(name)) {
            throw new IOException("Simulated exception from ClassLoaderStub for location: " + name)
        }
    }
}
