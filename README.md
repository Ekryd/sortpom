# sortpom-maven-plugin ![Icon](https://raw.githubusercontent.com/Ekryd/sortpom/master/Sortpom.png)

[![Build Status](https://travis-ci.org/Ekryd/sortpom.svg?branch=master)](https://travis-ci.org/Ekryd/sortpom-utils)
[![Coverage Status](https://coveralls.io/repos/Ekryd/sortpom/badge.svg?branch=master)](https://coveralls.io/r/Ekryd/sortpom?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.google.code.sortpom/maven-sortpom-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.google.code.sortpom/maven-sortpom-plugin)
[![Coverity](https://scan.coverity.com/projects/4726/badge.svg)](https://scan.coverity.com/projects/4726)

Maven plugin that helps the user sort pom.xml. 
The main advantages to have standardized sorted poms are that they become more readable and that comparisons between different module poms becomes much easier.


The SortPom Plugin has two goals.

mvn sortpom:sort sorts the current pom.xml file. This goal will always sort the pom.xml file.

mvn sortpom:verify only sorts the current pom.xml file if the xml elements are unsorted. This goal ignores text formatting (such as indentation and line breaks) when it verifies if the pom is sorted or not.
