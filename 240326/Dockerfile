# docker容器构建的基础镜像，本地docker仓库没有centos:7镜像时，会自动从docker配置文件指定的docker远程仓库中拉取
FROM centos:7

# 将当前目录下的jar文件复制到镜像中，相当于/opt/2024.03.26/test_wyc_v1.jar是可运行jar包
COPY *.jar /opt/2024.03.26/test_wyc_v1.jar

# 暴露端口
EXPOSE 8080

# 容器运行的执行命令
ENTRYPOINT ["java","-jar","/opt/2024.03.26/test_wyc_v1.jar","--server.port=8080"]