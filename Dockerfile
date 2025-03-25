FROM eclipse-temurin:21.0.6_7-jdk

WORKDIR /gilded-sentinel-server-client-aggregator

# Download OpenTelemetry Java Agent
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar /otel/opentelemetry-javaagent.jar

# Copy project JAR
COPY target/*.jar gilded-sentinel-server-client-aggregator.jar

EXPOSE 15560

ENTRYPOINT ["java", "-javaagent:/otel/opentelemetry-javaagent.jar", "-jar", "gilded-sentinel-server-client-aggregator.jar"]