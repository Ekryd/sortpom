#!/bin/bash

echo Sonar scanner home: "${SONAR_SCANNER_HOME}"
echo Java home: "${JAVA_HOME}"

if [ "${SONAR_SCANNER_HOME}" != "" ] && [ "${JAVA_HOME}" = "/home/travis/oraclejdk11" ]; then
    COMMAND="mvn clean jacoco:prepare-agent package jacoco:report sonar:sonar -B"
else
    COMMAND="mvn clean jacoco:prepare-agent package -B"
fi

echo ${COMMAND}
${COMMAND}
