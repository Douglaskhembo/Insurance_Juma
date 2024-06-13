FROM maven:3.6-jdk-8-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY Takaful.pfx /home/app/Takaful.pfx
RUN mvn -f /home/app/pom.xml clean package

FROM tomcat:8.0.20-jre8
EXPOSE 8080
COPY --from=build /home/app/Takaful.pfx /usr/local/tomcat/Takaful.pfx
COPY --from=build /home/app/target/broker.war /usr/local/tomcat/webapps/SIS.war