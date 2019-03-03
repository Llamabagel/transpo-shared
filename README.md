# Transpo-Shared
[ ![Download](https://api.bintray.com/packages/dellisd/transpo/transpo-shared/images/download.svg) ](https://bintray.com/dellisd/transpo/transpo-shared/_latestVersion)
[![Build Status](https://travis-ci.com/dellisd/transpo-shared.svg?branch=master)](https://travis-ci.com/dellisd/transpo-shared)

A library of shared data classes, interfaces, and interface implementations used across Route 613 components.
These classes are used in the [server](https://github.com/dellisd/transpo-server), [Android app](https://github.com/dellisd/Transpo), 
and data management tools (to be written).

##Quick Start
To include the shared library in your project add the following to the `repositories` block of your gradle config:
```groovy
maven { 
    url  "https://dl.bintray.com/dellisd/transpo" 
}
```
Now you can add the `shared` artifact:
```groovy
implementation 'ca.llamabagel.transpo:shared:1.0.0-alpha02'
```