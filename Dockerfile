# Test私有镜像
FROM swr.cn-north-4.myhuaweicloud.com/oomall-javaee/openjdk:17-alpine
# 官方镜像
#FROM openjdk:17-alpine
MAINTAINER mingqiu mingqiu@xmu.edu.cn
WORKDIR /work

# 安装maven和git
RUN apk update
RUN apk add wget git unzip bash curl
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.zip
RUN unzip apache-maven-3.9.5-bin.zip
RUN rm -f apache-maven-3.9.5-bin.zip
RUN git config --global credential.helper store

# 设置环境变量
ENV PATH /work/apache-maven-3.9.5/bin:$PATH

