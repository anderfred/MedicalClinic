#!/bin/bash
JAVA_OPTS="-Xms256m -Xmx512m -XX:MaxMetaspaceSize=192m"
java $JAVA_OPTS \
-Dspring.datasource.url=jdbc:mysql://localhost:3306/testdb \
-Dspring.datasource.username=test \
-Dspring.datasource.password=test \
-Dspring.datasource.driver.class.name=com.mysql.cj.jdbc.Driver \
-jar clinic-0.0.1-SNAPSHOT.jar