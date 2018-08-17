FROM openjdk:8-jre-alpine

ADD /stream-cep-poc/streaming-cep-data-generator-app/target/streaming-cep-data-generator-app.jar /

CMD ["java", "-Dvertx.cacheDirBase=/tmp", "-Dvertx.disableDnsResolver=true", "-jar", "streaming-cep-data-generator-app.jar"]