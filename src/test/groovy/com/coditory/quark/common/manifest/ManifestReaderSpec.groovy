package com.coditory.quark.common.manifest

import com.coditory.quark.common.base.UsesFiles
import spock.lang.Specification

import static com.coditory.quark.common.base.MapAssertions.assertMapEqual

class ManifestReaderSpec extends Specification implements UsesFiles {
    ManifestReader reader = new ManifestReader(classLoader)

    def "should read MANIFEST.MF file"() {
        given:
            String appManifest = manifestContent(
                    "Manifest-Version: 1.0",
                    "Implementation-Title: my-app",
                    "Implementation-Version: unspecified",
                    "Built-By: mendlik",
                    "Built-Host: mendlik-dell-e6540",
                    "Built-Date: 2021-02-20T08:31:33Z",
                    "Built-OS: Linux 5.8.0-43-generic amd64",
                    "Built-JDK: 14.0.1 AdoptOpenJDK",
            )
            writeClasspathFile("META-INF/MANIFEST.MF", appManifest)
        when:
            Map<String, String> manifest = reader.readAttributes("my-app")
        then:
            assertMapEqual(manifest, [
                    'Manifest-Version'      : '1.0',
                    'Implementation-Title'  : 'my-app',
                    'Implementation-Version': 'unspecified',
                    'Built-By'              : 'mendlik',
                    'Built-Host'            : 'mendlik-dell-e6540',
                    'Built-Date'            : '2021-02-20T08:31:33Z',
                    'Built-OS'              : 'Linux 5.8.0-43-generic amd64',
                    'Built-JDK'             : '14.0.1 AdoptOpenJDK'
            ])
    }

    def "should return null when there is no manifest.mf file with specified title"() {
        given:
            String libManifest = manifestContent(
                    "Implementation-Title: some-lib",
                    "Built-Host: mendlik-dell-e6540"
            )
            writeClasspathFile("META-INF/MANIFEST.MF", libManifest)
        when:
            Map<String, String> manifest = reader.readAttributesOrNull("my-app")
        then:
            manifest == null

        when:
            reader.readAttributes("my-app")
        then:
            IllegalStateException e = thrown(IllegalStateException)
            e.message == "Could not find META-INF/MANIFEST.MF file with property Implementation-Title: my-app"
    }

    def "should return manifest with specified title when there are multiple MANIFEST.MF on classpath"() {
        given:
            String libManifest = manifestContent(
                    "Implementation-Title: some-lib",
                    "Built-Host: mendlik-dell-e6540"
            )
            String appManifest = manifestContent(
                    "Implementation-Title: my-app",
                    "Built-By: mendlik"
            )
            writeClasspathFiles("META-INF/MANIFEST.MF", [libManifest, appManifest])
        when:
            Map<String, String> manifest = reader.readAttributes("my-app")
        then:
            assertMapEqual(manifest, [
                    'Implementation-Title': 'my-app',
                    'Built-By'            : 'mendlik'
            ])
    }

    def "should return manifest with specified property"() {
        given:
            String libManifest = manifestContent(
                    "Implementation-Title: some-lib",
                    "Built-Host: mendlik-dell-e6540"
            )
            String appManifest = manifestContent(
                    "Implementation-Title: my-app",
                    "Built-By: mendlik"
            )
            writeClasspathFiles("META-INF/MANIFEST.MF", [libManifest, appManifest])
        when:
            Map<String, String> manifest = reader.readAttributesWithProperty("Built-By", "mendlik")
        then:
            assertMapEqual(manifest, [
                    'Implementation-Title': 'my-app',
                    'Built-By'            : 'mendlik'
            ])
    }

    def "should return manifest by predicate"() {
        given:
            String libManifest = manifestContent(
                    "Implementation-Title: some-lib",
                    "Built-Host: mendlik-dell-e6540"
            )
            String appManifest = manifestContent(
                    "Implementation-Title: my-app",
                    "Built-By: mendlik"
            )
            writeClasspathFiles("META-INF/MANIFEST.MF", [libManifest, appManifest])
        when:
            Map<String, String> manifest = reader.readAttributes {
                it.mainAttributes.getValue("Built-By") == "mendlik"
            }
        then:
            assertMapEqual(manifest, [
                    'Implementation-Title': 'my-app',
                    'Built-By'            : 'mendlik'
            ])
    }

    def "should return null when there is no MANIFEST.MF file"() {
        expect:
            reader.readAttributesOrNull("my-app") == null
        and:
            reader.readAttributesWithPropertyOrNull('Built-By', 'mendlik') == null
        and:
            reader.readAttributesOrNull({ true }) == null
    }

    def "should throw error when there is no MANIFEST.MF file"() {
        when:
            reader.readAttributes("my-app")
        then:
            thrown(IllegalStateException)

        when:
            reader.readAttributesWithProperty('Built-By', 'mendlik')
        then:
            thrown(IllegalStateException)

        when:
            reader.readAttributes({ true })
        then:
            thrown(IllegalStateException)
    }

    def "should throw error on io problem"() {
        given:
            String libManifest = manifestContent(
                    "Implementation-Title: some-lib",
                    "Built-Host: mendlik-dell-e6540"
            )
            writeClasspathFile("META-INF/MANIFEST.MF", libManifest)
        and:
            classLoader.addErrorLocation("META-INF/MANIFEST.MF")

        when:
            reader.readAttributes("my-app")
        then:
            RuntimeException e = thrown(RuntimeException)
            e.message == "Could not open META-INF/MANIFEST.MF"

        when:
            reader.readAttributesOrNull("my-app")
        then:
            RuntimeException e2 = thrown(RuntimeException)
            e2.message == "Could not open META-INF/MANIFEST.MF"
    }

    private String manifestContent(String... lines) {
        // must end with new line
        return lines.join("\n") + "\n"
    }
}

