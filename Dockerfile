FROM eclipse-temurin:21.0.5_11-jdk
WORKDIR /gilded-sentinel-server-client-aggregator
COPY target/*.jar gilded-sentinel-server-client-aggregator.jar
EXPOSE 15560
ENTRYPOINT ["java", "-jar", "gilded-sentinel-server-client-aggregator.jar"]
