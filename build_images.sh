#!/bin/bash
(cd accountings-service; mvn clean package -DskipTests)
(cd combifiles-service; mvn clean package -DskipTests)
(cd companies-service; mvn clean package -DskipTests)
(cd draws-service; mvn clean package -DskipTests)
(cd gateway-service; mvn clean package -DskipTests)
(cd service-registry; mvn clean package -DskipTests)
(cd winnerevaluation-service; mvn clean package -DskipTests)
(cd winningnumbers-service; mvn clean package -DskipTests)
(cd logging-service; mvn clean package -DskipTests)