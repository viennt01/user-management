FROM openjdk:8-jre
EXPOSE 8080

# add the backend jar
ADD target/dist/backend-*.jar /opt/

# add config files
ADD target/dist/application*.properties /opt/
ADD target/dist/logback-spring.xml /opt/logback-spring.xml

# rename to backend.jar
RUN mv /opt/backend-*.jar /opt/backend.jar

ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", \
"-jar", "/opt/backend.jar", \
"--spring.config.additional-location=/opt/"]