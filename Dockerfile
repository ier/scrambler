FROM openjdk:8-alpine

COPY target/uberjar/scrambler.jar /scrambler/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/scrambler/app.jar"]
