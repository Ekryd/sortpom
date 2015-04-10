# Sortpom Maven Plugin ![Icon](https://raw.githubusercontent.com/Ekryd/sortpom/master/misc/Sortpom.png)

[![Build Status](https://travis-ci.org/Ekryd/sortpom.svg?branch=master)](https://travis-ci.org/Ekryd/sortpom-utils)
[![Coverage Status](https://coveralls.io/repos/Ekryd/sortpom/badge.svg?branch=master)](https://coveralls.io/r/Ekryd/sortpom?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.sortpom/sortpom-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.sortpom/sortpom-maven-plugin)
[![Coverity](https://scan.coverity.com/projects/4726/badge.svg)](https://scan.coverity.com/projects/4726)

Maven plugin that helps the user sort pom.xml. 
The main advantages to have standardized sorted poms are that they become more readable and that comparisons between different module poms becomes much easier.

## Goals Overview ##
The SortPom Plugin has two goals.

  * **mvn sortpom:sort** sorts the current pom.xml file. This goal will always sort the pom.xml file.

  * **mvn sortpom:verify** only sorts the current pom.xml file if the xml elements are unsorted. This goal ignores text formatting (such as indentation and line breaks) when it verifies if the pom is sorted or not.

## Usage ##

The Sortpom plugin will reorder the pom elements and format the xml structure in the pom-file. The plugin can be configured to sort by by different standards or by custom format. By default a backup file will be created, so that you can check how the pom-file has changed.

Sortpom works best if it is run every time during Maven compilation. Configure it once and then forget about it. If you want to see what the plugin does then open a command prompt in your project home and enter
```
mvn com.github.ekryd.sortpom:sortpom-maven-plugin:sort -Dsort.keepBlankLines -Dsort.predefinedSortOrder=custom_1
```

For a example how the plugin can be configured to run every time you build your project see [this](https://github.com/Ekryd/sortpom/wiki/Recommended-configuration) wiki page

The plugin will not change how your Maven project is compiled  ([Exception](https://github.com/Ekryd/sortpom/wiki/Parameters-that-can-affect-your-build))

# UNDER CONSTRUCTION #
It will take some time to do things right.

## Donations ##
If you use it, then please consider some encouragement.  
Especially now that I have move everything to GitHub. 

[![](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=JB25X84DDG5JW&lc=SE&item_name=Encourage%20the%20development&item_number=sortpom&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)
