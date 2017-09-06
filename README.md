[![Build Status](https://travis-ci.org/falkoschumann/java-javafx.svg?branch=master)](https://travis-ci.org/falkoschumann/java-javafx)
[![license](https://img.shields.io/github/license/falkoschumann/java-javafx.svg)]()


Muspellheim JavaFX
==================

TODO write description


Installation
------------

### Gradle

Add the the repository _jcenter_ to your `build.gradle`

    repositories {
        jcenter()
    }

and add the dependency

    compile 'de.muspellheim:muspellheim-javafx:1.2.0'


### Maven

Add the the repository _jcenter_ to your `pom.xml`
    
    <repositories>
        <repository>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <id>central</id>
            <name>bintray</name>
            <url>http://jcenter.bintray.com</url>
        </repository>
    </repositories>

and add the dependency

    <dependencies>
        <dependency>
            <groupId>de.muspellheim</groupId>
            <artifactId>muspellheim-javafx</artifactId>
            <version>1.2.0</version>
        </dependency>
    </dependencies>


### Download

You can download JARs with binary, source and JavaDoc from GitHub under
https://github.com/falkoschumann/java-javafx/releases.


Usage
-----

TODO document usage


Contributing
------------

### Publish artifacts to Bintray

1.  Create file `gradle.properties` and set properties `bintrayUser` and
    `bintrayApiKey`.
2.  Run `./gradlew uploadArchives`.
3.  Check uploaded files and publish.

### Publish distribution to GitHub

1.  Run `./gradle distZip`.
2.  Upload created ZIP to GitHub releases.
