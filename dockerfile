FROM openjdk:20-jdk
VOLUME /tmp
COPY target/* Practice/
ENTRYPOINT ["java","-jar","Practice/Practice-0.0.1-SNAPSHOT.jar"]