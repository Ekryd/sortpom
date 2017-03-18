# Sortpom Maven Plugin ![Icon](https://raw.githubusercontent.com/Ekryd/sortpom/master/misc/Sortpom.png)

[![Build Status](https://travis-ci.org/Ekryd/sortpom.svg?branch=master)](https://travis-ci.org/Ekryd/sortpom)
[![Coverage Status](https://coveralls.io/repos/github/Ekryd/sortpom/badge.svg?branch=master)](https://coveralls.io/github/Ekryd/sortpom?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.sortpom/sortpom-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.sortpom/sortpom-maven-plugin)
[![Coverity](https://scan.coverity.com/projects/4726/badge.svg)](https://scan.coverity.com/projects/4726)

Maven plugin that helps the user sort pom.xml. 
The main advantages to have standardized sorted poms are that they become more readable and that comparisons between different module poms becomes much easier.

## Goals Overview ##
The SortPom Plugin has two goals.

  * **mvn sortpom:sort** sorts the current pom.xml file. This goal will always sort the pom.xml file.

  * **mvn sortpom:verify** only sorts the current pom.xml file if the xml elements are unsorted. This goal ignores text formatting (such as indentation and line breaks) when it verifies if the pom is sorted or not.

## Usage ##

The Sortpom plugin will reorder the pom elements and format the xml structure in the pom-file. The plugin can be [configured](https://github.com/Ekryd/sortpom/wiki/Parameters) to sort by by different standards or by custom format. By default a backup file will be created, so that you can check how the pom-file has changed.

Sortpom works best if it is run every time during Maven compilation. [Configure](https://github.com/Ekryd/sortpom/wiki/Parameters) it once and then forget about it. If you want to perform a simple test what the plugin does then open a command prompt in your project home and enter
```
mvn com.github.ekryd.sortpom:sortpom-maven-plugin:sort -Dsort.keepBlankLines -Dsort.predefinedSortOrder=custom_1
```

For a example how the plugin can be configured to run every time you build your project see [recommended configuration](https://github.com/Ekryd/sortpom/wiki/Recommended-configuration) wiki page

The plugin will not change how your Maven project is compiled  ([Exception](https://github.com/Ekryd/sortpom/wiki/Parameters-that-can-affect-your-build))

## News ##
  * 2017-03-18: Released version 2.6.0 that adds support to force sorting if only line breaks differ. Thanks again Benoit Guerin for your pull requests!
  * 2017-02-18: Benoit Guerin supplied Maven invoker tests to the plugin. Thank you! 
  * 2016-05-06: Received an Open Source license for JProfiler. Thank you [ej-technologies](http://www.ej-technologies.com/products/jprofiler/overview.html)!
  * 2016-01-11: Renewed Open Source Licence for IntelliJ Ultimate. Once again, thank you [JetBrains](http://www.jetbrains.com/idea/)!!
  * 2015-11-21: Released version 2.5.0. The plugin now uses Java 8, as some dependant plugins demand Java 8. Users of previous versions of Java will have to use version 2.4.0.
  * 2015-04-06: Released version 2.4.0 with new github location and updated libraries.
  * 2015-03-31: Moved the SortPom plugin to GitHub.
  * 2015-02-04: Received an Open Source license for Structure101. Thank you [Structure101](http://structure101.com/)!
  * 2014-08-11: Received an Open Source license for Araxis Merge. Thank you [Araxis](http://www.araxis.com/)!
  * 2014-04-18: Released [Echo-maven-plugin](https://code.google.com/p/echo-maven-plugin/), another Maven plugin. Please check it out!
  * 2013-11-23: Hurrah! Got a donation. Thank you Bastian!
  * 2013-10-01: Hurrah! Got an donation. Thank you Khalid!
  * 2012-11-05: Hurrah! Got an donation. Thank you Michael!
  * 2012-06-12: Hurrah! Got an donation. Thank you Reuben!

## Versions ##
https://github.com/Ekryd/sortpom/wiki/Versions

## Plugin parameters ##
https://github.com/Ekryd/sortpom/wiki/Parameters

## Download ##
The plugin is hosted i [Maven Central](http://mvnrepository.com/artifact/com.github.ekryd.sortpom/sortpom-maven-plugin) and will be downloaded automatically if you include it as a plugin in your pom file.

## Donations ##
If you use it, then please consider some encouragement.  
Especially now that I have made the effort to move everything to GitHub. 

[![](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=JB25X84DDG5JW&lc=SE&item_name=Encourage%20the%20development&item_number=sortpom&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)
