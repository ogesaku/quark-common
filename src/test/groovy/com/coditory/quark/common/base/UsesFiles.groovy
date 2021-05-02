package com.coditory.quark.common.base

import groovy.transform.CompileStatic

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Supplier

@CompileStatic
trait UsesFiles {
    File tempDirectory

    ClassLoaderStub classLoader = new ClassLoaderStub()

    void setup() {
        tempDirectory = File.createTempDir("temp", Long.toString(System.nanoTime()))
    }

    void cleanup() {
        tempDirectory.delete()
    }

    public <T> T stubClassLoader(Supplier<T> supplier) {
        return classLoader.setupInThreadContext(supplier)
    }

    File writeClasspathFile(String fileName, String content) {
        File file = writeFile(fileName, content)
        classLoader.addFile(fileName, file)
        return file
    }

    List<File> writeClasspathFiles(String fileName, List<String> contents) {
        List<File> files = contents.withIndex().collect { String it, int index ->
            writeFile(fileName + "_" + index, it)
        }
        classLoader.addFiles(fileName, files)
        return files
    }

    File writeFile(String fileName, String content) {
        Path path = Paths.get(tempDirectory.path, fileName)
        Files.createDirectories(path.parent)
        File file = Files.createFile(path).toFile()
        file.write(content)
        return file
    }
}
