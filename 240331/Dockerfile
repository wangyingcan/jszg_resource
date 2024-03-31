FROM openjdk:17-alpine

COPY *.jar /opt/240331/loki_demo.jar

WORKDIR /opt/240331

#创建logs目录
RUN mkdir logs

WORKDIR /opt/240331/logs

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/opt/240331/loki_demo.jar"]
