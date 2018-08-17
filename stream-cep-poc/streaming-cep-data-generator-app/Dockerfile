FROM openjdk18-openshift:1.4

ADD target/streaming-cep-data-generator-app.jar /

CMD ["java", "-Dvertx.cacheDirBase=/tmp", "-jar", "streaming-cep-data-generator-app.jar"]