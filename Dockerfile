FROM eclipse-temurin:17.0.8_7-jdk-ubi9-minimal

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="-Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2"

EXPOSE 8761 5701/udp
ADD target/clinic-0.0.1-SNAPSHOT.jar /app.jar
CMD exec java ${JAVA_OPTS} --add-opens java.base/java.lang=ALL-UNNAMED -Djava.security.egd=file:/dev/./urandom -jar /app.jar
