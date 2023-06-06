FROM openjdk:20
ADD target/user-profile-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "app.jar"]