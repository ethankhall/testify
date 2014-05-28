testify
=======

[![Build Status](https://travis-ci.org/ethankhall/testify.png?branch=master)](https://travis-ci.org/ethankhall/testify)

A easy way to dynamically create databases for testing

To use Testify you need to do two things. First add it to the classpath (to get the plugin).

```
buildscript {
    repositories {
        jcenter()
        maven { url "http://dl.bintray.com/ethankhall/maven" }
    }

    dependencies {
        classpath "io.ehdev.testify:testify-gradle:1.0.6"
    }
}

apply plugin: 'testify'
```

You may want to pick a newer version depending on how often the README is updated.

Second you need to add the second project to your testCompile so that it can be used to run the tests.

```
repositories {
    maven { url "http://dl.bintray.com/ethankhall/maven" }
    jcenter()
    mavenCentral()
}

dependencies {
    testCompile 'io.ehdev.testify:testify-java:1.0.6'
}
```

This will eventually make it to mavenCentral, but it could take a while.

```
testify {
    databaseName 'databaseName'
    scripts foo/bar/script1.sql, bar/foo/script2.sql
    excludeTestTasks 'test'
}
```

If you don't specify databaseName one will be created for you automatically. The other fields are optional. In this example it would stop the batabase from being created on normal tests. This would be useful if you wanted to keep all your DB tests in another task.
