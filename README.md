# Sortpom Maven Plugin ![Icon](https://raw.githubusercontent.com/Ekryd/sortpom/master/misc/Sortpom.png)

[![Build Status](https://circleci.com/gh/Ekryd/sortpom.svg?style=svg)](https://app.circleci.com/pipelines/github/Ekryd/sortpom)
[![Coverage Status](https://coveralls.io/repos/github/Ekryd/sortpom/badge.svg?branch=master)](https://coveralls.io/github/Ekryd/sortpom?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.sortpom/sortpom-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.sortpom/sortpom-maven-plugin)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.github.ekryd.sortpom%3Asortpom-parent&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.ekryd.sortpom%3Asortpom-parent)
[![Licence](https://img.shields.io/github/license/Ekryd/sortpom?color=success)](https://github.com/Ekryd/sortpom/blob/master/LICENSE.md)

Maven plugin that helps the user sort pom.xml by formatting the XML and organizing XML sections in a predefined order. 
The main advantages to have standardized sorted poms are that they become more readable and that comparisons between different module poms becomes much easier.

## Goals Overview ##
The SortPom Plugin has two goals.

  * **mvn sortpom:sort** sorts the current pom.xml file. This goal will always sort the pom.xml file.

  * **mvn sortpom:verify** only sorts the current pom.xml file if the xml elements are unsorted. This goal ignores text formatting (such as indentation and line breaks) when it verifies if the pom is sorted or not.

![Icon](https://raw.githubusercontent.com/Ekryd/sortpom/master/misc/sortpom.jpg)

## Usage ##

The Sortpom plugin will reorder the pom elements and format the xml structure in the pom-file. The plugin can be [configured](https://github.com/Ekryd/sortpom/wiki/Parameters) to sort by different standards or by a custom format. A backup file will be created, by default, so that you can check how the pom-file has changed.

Sortpom works best if it is run every time during Maven compilation. [Configure](https://github.com/Ekryd/sortpom/wiki/Parameters) it once and then forget about it. If you want to perform a simple test what the plugin does then open a command prompt in your project home and enter
```
mvn com.github.ekryd.sortpom:sortpom-maven-plugin:sort -Dsort.keepBlankLines -Dsort.predefinedSortOrder=custom_1
```

An example of how the plugin can be configured to run every time you build your project see [recommended configuration](https://github.com/Ekryd/sortpom/wiki/Recommended-configuration) wiki page

The plugin will not change how your Maven project is compiled  ([Exception](https://github.com/Ekryd/sortpom/wiki/Parameters-that-can-affect-your-build))

## News ##
Added 
  * 2021-12-20: Released version 3.0.1. Fixed bug where ignoreLineSeparators was not respected during the 'verify' goal. Thanks, [Zhanhb](https://github.com/zhanhb) for spotting it! 
  * 2021-05-17: Received an Open Source Licence for IntelliJ Ultimate. Once again, thank you [JetBrains](http://www.jetbrains.com/idea/)!!
  * 2021-04-20: Released version 3.0.0. THIS IS A BREAKING CHANGE! The predefined sort order is now according to the recommended pom order (as decided in 2008). Blank lines in the POM are now kept by default. If dependencies are sorted by SCOPE, then imported bom-files will be sorted towards the top. [#93](/../../issues/93) [#105](/../../issues/105)
  * 2021-04-06: Released version 2.15.0 that adds support for sortDependencyExclusions to sort dependency exclusions by groupId and/or artifactId
  * 2021-03-30: Released version 2.14.1 that fixes sorting of dependencies and plugins if artifactId or groupId contains a line break [#99](/../../issues/99)
  * 2021-03-18: Released version 2.14.0 that adds support for indentSchemaLocation to break apart the long project declaration line at the top of the pom [#85](/../../issues/85)
  * 2021-01-16: Released version 2.13.1 that adds support for sortExecutions (to sort executions) and verifyFailOn (to have the option of a strict verify goal) [#81](/../../issues/81) [#82](/../../issues/82)
  * 2021-01-10: Moved build to [CircleCI](https://circleci.com)
  * 2020-07-02: Released version 2.12.0 that adds support to remove the space before self-closing xml tags. [#75](/../../issues/75)
  * 2020-02-29: John Patrick converted all JUnit tests to version 5. Thank you! [#72](/../../issues/72) [#73](/../../issues/73)
  * 2020-02-24: Renewed Open Source Licence for Araxis Merge. Thank you [Araxis](https://www.araxis.com/merge/)!
  * 2020-02-03: Released version 2.11.0 that adds support to retain the file creation time of a sorted pom file. Thanks, Christoph for your pull request! [#68](/../../issues/68) [#69](/../../issues/69)
  * 2015-04-06: Released version 2.4.0 with new github location and updated libraries.
  * 2015-03-31: Moved the SortPom plugin to GitHub.

## Versions ##
https://github.com/Ekryd/sortpom/wiki/Versions

## Plugin parameters ##
https://github.com/Ekryd/sortpom/wiki/Parameters

## Download ##
The plugin is hosted i [Maven Central](http://mvnrepository.com/artifact/com.github.ekryd.sortpom/sortpom-maven-plugin) and will be downloaded automatically if you include it as a plugin in your pom file.

## Donations ##
If you use it, then please consider some encouragement. ⭐️ Star it in GitHub!  

[![](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=JB25X84DDG5JW&lc=SE&item_name=Encourage%20the%20development&item_number=sortpom&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)
