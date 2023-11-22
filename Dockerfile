FROM openjdk:17
COPY build/libs/wanted2023.jar wanted2023.jar
ENTRYPOINT ["java", "-jar", "wanted2023.jar"]