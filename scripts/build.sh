#!/bin/bash

if [ "${SONAR_SCANNER_HOME}" != "" ]; then
    COMMAND="mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar -B"
else
    COMMAND="mvn clean package -B"
fi

echo ${COMMAND}
${COMMAND}
