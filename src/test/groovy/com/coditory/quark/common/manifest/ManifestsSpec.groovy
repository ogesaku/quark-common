package com.coditory.quark.common.manifest

import spock.lang.Specification

import static com.coditory.quark.common.base.MapAssertions.assertMapEqual

class ManifestsSpec extends Specification {
    def "should read MANIFEST.MF file"() {
        when:
            Map<String, String> manifest = Manifests.readManifestAttributes("sample-project")
        then:
            assertMapEqual(manifest, [
                    'Manifest-Version'      : '1.0',
                    'Main-Class'            : 'com.coditory.Application',
                    'Implementation-Title'  : 'sample-project',
                    'Implementation-Group'  : 'com.coditory',
                    'Implementation-Version': '0.0.1-SNAPSHOT',
                    'Built-By'              : 'john.doe',
                    'Built-Host'            : 'john-pc',
                    'Built-Date'            : '2020-03-25T20:46:59Z',
                    'Built-OS'              : 'Linux 4.15.0-91-generic amd64',
                    'Built-JDK'             : '12.0.2 AdoptOpenJDK',
                    'SCM-Repository'        : 'git@github.com:coditory/gradle-manifest-plugin.git',
                    'SCM-Branch'            : 'refs/heads/master',
                    'SCM-Commit-Message'    : 'Very important commit',
                    'SCM-Commit-Hash'       : 'ef2c3dcabf1b0a87a90e098d1a6f0341f0ae1adf',
                    'SCM-Commit-Author'     : 'John Doe <john.doe@acme.com>',
                    'SCM-Commit-Date'       : '2020-03-24T19:46:03Z'
            ])
    }

    def "should return the same values from complimentary methods"() {
        when:
            Map<String, String> manifest = Manifests.readManifestAttributes("sample-project")
        then:
            Manifests.readManifestAttributesOrNull("sample-project") == manifest
        and:
            Manifests.readManifestAttributesOrEmpty("sample-project") == manifest
    }

    private String manifestContent(String... lines) {
        // must end with new line
        return lines.join("\n") + "\n"
    }
}
