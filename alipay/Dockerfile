# 私有镜像
FROM swr.cn-north-4.myhuaweicloud.com/oomall-javaee/openjdk:17-alpine
# 官方镜像
#FROM openjdk:17-alpine
MAINTAINER mingqiu mingqiu@xmu.edu.cn
WORKDIR /app

#ARG JAR_FILE
#ADD ${JAR_FILE} /app/app.jar
#如果在服务器上部署docker，需要uncomment上面两行，并将下面的copy注释掉
COPY ./alipay/target/alipay-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["--spring.datasource.url=jdbc:mysql://mysql:3306/alipay?serverTimezone=Asia/Shanghai","--spring.datasource.username=demouser", "--spring.datasource.password=123456"]