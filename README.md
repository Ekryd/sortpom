# Sortpom Maven Plugin ![Icon](https://raw.githubusercontent.com/Ekryd/sortpom/master/misc/Sortpom.png)

[![Build Status](https://circleci.com/gh/Ekryd/sortpom.svg?style=svg)](https://app.circleci.com/pipelines/github/Ekryd/sortpom)
[![Coverage Status](https://coveralls.io/repos/github/Ekryd/sortpom/badge.svg?branch=master)](https://coveralls.io/github/Ekryd/sortpom?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.sortpom/sortpom-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.sortpom/sortpom-maven-plugin)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.github.ekryd.sortpom%3Asortpom-parent&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.github.ekryd.sortpom%3Asortpom-parent)
[![Licence](https://img.shields.io/github/license/Ekryd/sortpom?color=success)](https://github.com/Ekryd/sortpom/blob/master/LICENSE.md)
[![Known Vulnerabilities](https://snyk.io/test/github/Ekryd/sortpom/badge.svg)](https://snyk.io/test/github/Ekryd/sortpom)

Maven plugin that helps the user sort pom.xml by formatting the XML and organizing XML sections in a predefined order. 
The main advantages to have standardized sorted poms are that they become more readable and that comparisons between different module poms becomes much easier.

## Goals Overview ##
The SortPom Plugin has two goals.

  * **mvn sortpom:sort** sorts the current pom.xml file. This goal will always sort the pom.xml file.

  * **mvn sortpom:verify** only sorts the current pom.xml file if the xml elements are unsorted. This goal ignores text formatting (such as indentation and line breaks) when it verifies if the pom is sorted or not.

![Icon](https://raw.githubusercontent.com/Ekryd/sortpom/master/misc/sortpom.jpg)

## Usage ##

The Sortpom plugin will reorder the pom elements and format the xml structure in the pom-file. The plugin can be [configured](https://github.com/Ekryd/sortpom/wiki/Parameters) to sort by different standards or by a custom format. A backup file will be created by default, so that you can check how the pom-file has changed.

Sortpom works best if it is run every time during Maven compilation. [Configure](https://github.com/Ekryd/sortpom/wiki/Parameters) it once and then forget about it. Replace the plugin version with the latest one:
```xml
<build>
  <plugins>
    <plugin>
      <groupId>com.github.ekryd.sortpom</groupId>
      <artifactId>sortpom-maven-plugin</artifactId>
      <version>3.4.0</version>
      <executions>
        <execution>
          <goals>
            <goal>sort</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
    ...
  </plugins>
</build>
```

If you just want to perform a simple test what the plugin does then open a command prompt in your project home and enter
```
mvn com.github.ekryd.sortpom:sortpom-maven-plugin:3.3.0:sort -Dsort.predefinedSortOrder=custom_1
```

Detailed example of how the plugin can be configured to run every time you build your project; see [recommended configuration](https://github.com/Ekryd/sortpom/wiki/Recommended-configuration) wiki page

The plugin will not change how your Maven project is compiled  ([Exception](https://github.com/Ekryd/sortpom/wiki/Parameters-that-can-affect-your-build))

## News ##
Added 
  * 2024-05-19: Released version 4.0.0. **THIS IS A BREAKING CHANGE!** Indentation for attributes are now 2 * indent size [#413](/../../issues/413), and the parameter `indentSchemaLocation` has been deprecated in favor of `indentAttribute`. [#412](/../../issues/412).
  * 2024-03-10: Released version 3.4.1. Solves a bug, where whitespace was removed even if `xml:space="preserve"` was used [#402](/../../issues/402).
  * 2024-02-20: Released version 3.4.0. Added parameter for omitting newline at end of file [#399](/../../issues/399).
  * 2023-08-04: Received an [Open Source Licence](https://jb.gg/OpenSourceSupport) for IntelliJ Ultimate. Once again, thank you [JetBrains](http://www.jetbrains.com/idea/)!!
  * 2023-07-24: Released version 3.3.0. Added parameter for quiet output from the plugin [#338](/../../issues/338). Thanks, [gnodet](https://github.com/gnodet) for the PR!
  * 2023-01-29: Released version 3.2.1. Dependency updates. No new functionality
  * 2022-07-17: Released version 3.2.0. Added parameter `sortDependencyManagement` where dependency management can be sorted independently of dependencies [#210](/../../issues/210). Thanks, [Ssquan](https://github.com/ssquan) for the PR!
  * 2022-05-29: Released version 3.1.3. Fix formatting if text and other content is placed together in a xml tag [#209](/../../issues/209)
  * 2022-05-22: Released version 3.1.0. **THIS IS A BREAKING CHANGE!** Dropped support for Java 8. Updated underlying xml framework due to vulnerabilities. Updated other libraries and plugins. 
  * 2021-04-20: Released version 3.0.0. **THIS IS A BREAKING CHANGE!** The predefined sort order is now according to the recommended pom order (as decided in 2008). Blank lines in the POM are now kept by default. If dependencies are sorted by SCOPE, then imported bom-files will be sorted towards the top. [#93](/../../issues/93) [#105](/../../issues/105)
  * 2020-02-24: Renewed Open Source Licence for Araxis Merge. Thank you [Araxis](https://www.araxis.com/merge/)!
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
