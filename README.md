# Transpo-Shared
[ ![Download](https://api.bintray.com/packages/dellisd/transpo/gtfs/images/download.svg) ](https://bintray.com/dellisd/transpo/gtfs/_latestVersion)
[![Build Status](https://travis-ci.com/dellisd/transpo-shared.svg?branch=master)](https://travis-ci.com/dellisd/transpo-shared)

A library of shared data classes, interfaces, and interface implementations used across Route 613 components.
These classes are used in the [server](https://github.com/dellisd/transpo-server), [Android app](https://github.com/dellisd/Transpo), 
and data management tools (to be written).

## Quick Start
To include the shared library in your project add the following to the `repositories` block of your gradle config:
```groovy
maven { 
    url  "https://dl.bintray.com/dellisd/transpo" 
}
```

There are several artifacts available. All artifacts share the same version numbers.

 | Artifact Name | Gradle | Contents
 | ---- | ---- | ---- |
 | shared | `implementation 'ca.llamabagel.transpo:shared:1.0.0-alpha10'` | Shared models between server and apps |
 | gtfs | `implementation 'ca.llamabagel.transpo:gtfs:1.0.0-alpha10'` | Gtfs models and code to interact with Gtfs data source. |
 | config | `implementation 'ca.llamabagel.transpo:config:1.0.0-alpha10'` | Server configuration models |
