FROM dockerfile/java
MAINTAINER Brady Olsen, bradyaolsen@gmail.com

# copy jar into image and expose on port 8080
WORKDIR /
USER daemon
ADD target/scala-2.11/config-spray-assembly-0.1.jar /app/server.jar

ENTRYPOINT [ "java", "-jar", "/app/server.jar" ]
EXPOSE 8080