FROM openjdk:17-alpine

COPY *.jar /opt/240328/loki_demo_v1.jar

WORKDIR /opt/240328

#创建logs目录
RUN mkdir logs

WORKDIR /opt/240328/logs

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/240328/loki_demo_v.jar"]
