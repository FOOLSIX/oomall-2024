# OOMALL

2024 Tutorial Project for course "Object-Oriented Analysis and Design“ and "JavaEE Platform Technologies"

<br>2023-10-12：[API 1.3.1版](https://app.swaggerhub.com/apis/mingqcn/OOMALL/1.3.1#/) 2023年第一版API<br>


## 测试结果
系统每天**5:00 21:00**会自动进行一次单元测试，这是[历史测试结果](http://116.205.123.125/unit-test/)
<br>系统每天**2:00**会自动进行一次集成测试，这是[测试结果](http://116.205.123.125/integration-test/required/), 目录是按照测试时间产生的，在目录下存在public.log文件，可以看到测试时的完整输出
<br>[各模块的日志](http://116.205.123.125/logs/)会在**4:00**收集到服务器上

## 工程编译，调试的顺序
所有module都依赖于core模块，先要将core安装到maven的本地仓库，才能编译运行其他模块。方法如下：
1. 首先将OOMALL下的pom.xml文件中除·<module>core</module>·以外的module注释掉，<br>
2. 在maven的中跑install phase，将core和oomall安装到maven的本地仓库<br>
3. 将OOMALL下的pom.xml文件中注释掉的部分修改回来<br>
4. 编译打包其他部分<br>
5. 以后修改了core的代码，只需要单独install core到maven本地仓库，无需重复上述步骤<br>

## 环境安装
### 安装docker
如果没有安装docker需要先安装docker
`apt-get update`
`apt-get install docker.io`

## docker swarm环境下项目的安装指南
### docker swarm安装
如果在docker swarm上部署，需要先安装docker swarm，参考以下网页
[https://docs.docker.com/engine/swarm/swarm-tutorial/create-swarm/
](https://docs.docker.com/engine/swarm/swarm-tutorial/create-swarm/)

### 创建docker swarm overlay network
`docker network create --driver overlay my-net`

### 安装MySQL镜像和服务
#### 在目标服务器上安装MySQL镜像
`docker pull mysql:latest`

#### 更新目标服务器的label
为目标服务器定义label，方便docker swarm在创建Service时，将Service部署在目标服务器上，以下我们在node6上定义了一个label `server=mysql`<br>
`docker node update --label-add server=mysql node6`<br>

#### 在docker swarm中创建服务
<br>MySQL的配置文件目录conf.d和数据库初始化脚本sql.zip都在OOMALL的conf/mysql目录下，需要把sql.zip文件解压到conf/mysql/sql目录，并这些文件拷贝到运行mysql的节点上，并映射到容器中<br>
<br>用以下命令创建mysql
<br>`docker service create --name mysql --with-registry-auth --constraint node.labels.server==mysql --publish published=3306,target=3306 --mount type=bind,src=/root/oomall-2023/conf/mysql/sql,dst=/sql,readonly --mount type=bind,src=/root/oomall-2023/conf/mysql/conf.d,dst=/etc/mysql/conf.d,readonly --mount type=bind,src=/root/mysql/log,dst=/var/log/mysql --mount type=bind,src=/root/mysql/data,dst=/var/lib/mysql  --network my-net -e MYSQL_ROOT_PASSWORD=123456  -d swr.cn-north-4.myhuaweicloud.com/oomall-javaee/mysql:latest`
<br>其中 `--mount type=bind,src=/root/oomall-2023/mysql/sql,dst=/sql,readonly`是将OOMALL的数据SQL脚本mount到容器中
<br>`--mount type=bind,src=/root/OOMALL/mysql/conf.d,dst=/etc/mysql/conf.d,readonly`是将OOMALL中的MySQL设置mount到容器中
<br>`--mount type=bind,src=/root/mysql/log,dst=/var/log/mysql`是将操作系统的`/root/mysql/log`mount到容器中的MySQL日志目录，这样在操作系统里就能看到mysql的日志
<br>`--mount type=bind,src=/root/mysql/data,dst=/var/lib/mysql`是将操作系统的`/root/mysql/data`mount到容器中的MySQL数据目录，这样将数据库数据存储在操作系统的目录下
<br>`-e MYSQL_ROOT_PASSWORD=123456`是设定数据库root账户密码。建议自行修改密码，特别是在暴露端口的情况下
<br>如果需要将mysql的端口暴露出来 加上--publish published=3306,target=3306
<br>mysql:latest为官方docker镜像，swr.cn-north-4.myhuaweicloud.com/oomall-javaee/mysql:latest为课程用私服镜像，如果用私有镜像需要加--with-registry-auth，官方镜像去掉此选项


#### 在运行mysql服务的节点上运行sql脚本
看一下mysql的服务运行在哪台服务器<br>
`docker service ps mysql`<br>
切换到运行mysql服务的机器，看一下mysql容器在这台机器的container id，将容器的CONTAINER ID拷贝替换下述命令中[CONTAINER ID],用这个容器运行mysql的命令<br>
`docker exec -it [CONTAINER ID] mysql -uroot -p`<br>
用root账号登录mysql服务器，在运行起来的mysql命令行中用`source /sql/database.sql`建立oomall各模块数据库<br>

#### 分别初始化各模块的数据
以product模块为例，用`use product`切换数据库
<br>用`source /sql/product.sql`插入初始数据
<br>其他模块数据库的安装类似

### 安装带布隆过滤器的Redis

#### 在目标服务器上安装Redis镜像
`docker pull redis/redis-stack-server:latest `
<br>在管理机上，更新目标服务器的label
<br>为目标服务器定义label，方便docker swarm在创建Service时，将Service部署在目标服务器上，以下我们在node5上定义了一个label `server=redis`
<br>`docker node update --label-add server=redis node5`

#### 在docker swarm中创建Redis服务
将机器上的Redis配置目录映射到docker容器中，并把redis的服务加入到my-net网络
<br>`docker service create --name redis --with-registry-auth --constraint node.labels.server==redis --mount type=bind,source=/root/oomall-2023/conf/redis,destination=/etc/redis,readonly --network my-net -e CONFFILE=/etc/redis/redis.conf  -d swr.cn-north-4.myhuaweicloud.com/oomall-javaee/redis/redis-stack-server:latest `
<br>其中`-e CONFFILE=/etc/redis/redis.conf`是指定redis的配置文件，在配置文件中我们设定了redis的连接密码为123456, redis的配置文件目录redis映射到容器中，建议自行修改密码
<br>redis/redis-stack-server:latest为官方docker镜像，swr.cn-north-4.myhuaweicloud.com/oomall-javaee/redis/redis-stack-server:latest为课程用私服镜像，如果用私有镜像需要加--with-registry-auth，官方镜像去掉此选项

#### 在运行redis服务的节点上查看redis的服务是否正常运行
看一下redis的服务运行在哪台服务器
<br>`docker service ps redis`
<br>切换到运行redis服务的机器，看一下redis容器在这台机器的container id，将容器的CONTAINER ID拷贝替换下述命令中[CONTAINER ID],用这个容器运行redis的命令行工具redis-cli
<br>`docker exec -it [CONTAINER ID] redis-cli`
<br>进入redis-cli后，先运行下面的命令输入密码
<br>`auth 123456`
<br>再测试Bloom过滤器是否正常
`BF.ADD testFilter hello`
`BF.EXISTS testFilter hello`
<br>如果均返回(integer) 1则可以正常使用redis了
<br>如果需要将redis的端口暴露出来 加上--publish published=6379,target=6379


### 安装Mongo镜像
#### 在目标服务器上安装Mongo镜像
`docker pull mongo:latest `
<br>更新目标服务器的label, 为目标服务器定义label，方便docker swarm在创建Service时，将Service部署在目标服务器上，以下我们在node4上定义了一个label `server=mongo`
<br>`docker node update --label-add server=mongo1 node4`

### 安装Mongo副本集
如果需要mongo支持事务，则需要安装至少3台mongo服务器，形成mongo的副本集。具体可以参考[https://www.mongodb.com/docs/manual/administration/replica-set-deployment/](https://www.mongodb.com/docs/manual/administration/replica-set-deployment/)
#### 安装mongo的primary
<br>`docker service create --name mongo1 --with-registry-auth --constraint node.labels.server==mongo1 --mount type=bind,source=/root/oomall-2023/conf/mongo,destination=/mongo --mount type=bind,source=/root/mongo-data,destination=/data/db --publish 27017:27017 --network my-net -d swr.cn-north-4.myhuaweicloud.com/oomall-javaee/mongo:latest mongod --replSet rs0 --oplogSize 128`
<br>其中`--replSet rs0`定义了副本集的名称 `--oplogSize 128`定义了副本日志的大小
<br>如果需要将mongo的端口暴露出来 加上--publish 27017:27017
<br>redis/redis-stack-server:latest为官方docker镜像，swr.cn-north-4.myhuaweicloud.com/oomall-javaee/mongo:latest为课程用私服镜像，如果用私有镜像需要加--with-registry-auth，官方镜像去掉此选项


#### 安装mongo的secondary
secondary需要安装两台，如果在同一台物理机上无需将端口映射出来
<br>`docker service create --name mongo2 --with-registry-auth --constraint node.labels.server==mongo2 --mount type=bind,source=/root/oomall-2023/conf/mongo,destination=/mongo --mount type=bind,source=/root/mongo-data,destination=/data/db --publish published=27018,target=27017 --network my-net -d swr.cn-north-4.myhuaweicloud.com/oomall-javaee/mongo:latest  mongod --replSet rs0 --oplogSize 128`
<br>`docker service create --name mongo3 --with-registry-auth --constraint node.labels.server==mongo3 --mount type=bind,source=/root/oomall-2023/conf/mongo,destination=/mongo  --mount type=bind,source=/root/mongo-data,destination=/data/db --publish published=27019,target=27017 --network my-net -d swr.cn-north-4.myhuaweicloud.com/oomall-javaee/mongo:latest mongod --replSet rs0 --oplogSize 128`

#### 在primary上创建副本
<br>看一下mongo1的服务运行在哪台服务器
<br>`docker service ps mongo1`
<br>切换到运行mongo1服务的机器，看一下mongo1容器在这台机器的container id，将容器的CONTAINER ID拷贝替换下述命令中[CONTAINER ID],用这个容器运行mongo的命令行工具mongosh
<br>`docker exec -it [CONTAINER ID] mongosh`
<br>进入mongosh后，先运行以下命令初始化副本集，其中`priority: 2`表示该节点优先成为主节点
<br>`rsconf = {_id: "rs0",members: [{_id: 0, host: "mongo1:27017",priority: 2},{_id: 1,host: "mongo2:27017",priority: 1},{_id: 2,host: "mongo3:27017",priority: 1}]}`
<br>`rs.initiate( rsconf )`

<br>用`rs.conf()`即可以查看配置情况
<br>用`rs.status()`即可以查看哪个mongo是primary，目前的设置是mongo1是primary
<br>在primary mongo上用相同的步骤[创建mongo用户]和[restore mongo的数据库]
<br>在工程中将配置指向primary的mongo服务器

#### 创建mongo的用户
在运行mongo1服务的节点上查看mongo的服务是否正常运行
<br>`docker exec -it [CONTAINER ID] mongosh`
<br>进入mongosh后，先运行下面的命令切换database
<br>`use oomall`
<br>再在oomall建立demouser用户，给用户赋予读写和数据库管理员的角色
<br>`db.createUser({user:"demouser",pwd:"123456",roles:[{role:"readWrite",db:"oomall"},{role:"dbAdmin",db:"oomall"}]})`
<br>如果均返回{ok:1}则可以用demouser用户正常使用mongo的oomall数据库了
`docker exec -it [CONTAINER ID] mongosh -u demouser -p 123456 --authenticationDatabase oomall`

#### restore mongo的数据库
`docker exec -it [CONTAINER ID] mongorestore -u demouser -p 123456 --authenticationDatabase oomall -d oomall --dir /mongo/oomall --drop --preserveUUID`
<br>此命令会先删除所有数据再用户mongo/oomall下的数据恢复数据库

### 安装Rocketmq
#### 在node2 安装rocketmq的nameserver和dashboard，在node3安装rocketmq的broker
在管理机上执行以下命令
<br>`docker node update --label-add server=rocketmq-namesrv node2`
<br>`docker node update --label-add server=rocketmq-broker node3`
<br>在node2上pull docker镜像
<br>`docker pull apache/rocketmq`
<br>`docker pull apacherocketmq/rocketmq-dashboard:latest`
<br>在node3上pull docker镜像
<br>`docker pull apache/rocketmq`

#### 创建RocketMQ的NameServer 服务
`docker service create --name rocketmq-namesrv --with-registry-auth --constraint node.labels.server==rocketmq-namesrv --publish published=9876,target=9876 --network my-net  -d swr.cn-north-4.myhuaweicloud.com/oomall-javaee/apache/rocketmq:latest ./mqnamesrv`

#### 创建RocketMQ的Broker 服务
`docker service create --name rocketmq-broker --with-registry-auth --constraint node.labels.server==rocketmq-broker -p 10911:10911 --mount type=bind,source=/root/oomall-2023/conf/rocketmq,destination=/rocketmq  --network my-net -e "HEAP_OPTS=-Xms512m -Xmx512m -Xmn256m -XX:MaxDirectMemorySize=512m" -d swr.cn-north-4.myhuaweicloud.com/oomall-javaee/apache/rocketmq:latest ./mqbroker -n rocketmq-namesrv:9876 -c /rocketmq/broker.conf`
<br>其中`-e "HEAP_OPTS=-Xms1g -Xmx1g -Xmn512m -XX:MaxDirectMemorySize=1g"`限制了内存最大使用1G，建议服务器内存为2G

#### 安装Rocket Dashboard
`docker service create --name rocketmq-dashboard --with-registry-auth --constraint node.labels.server==rocketmq-namesrv  --network my-net  -d -e "JAVA_OPTS=-Drocketmq.namesrv.addr=rocketmq-namesrv:9876" -p 8100:8080 -d swr.cn-north-4.myhuaweicloud.com/oomall-javaee/apacherocketmq/rocketmq-dashboard:latest`
<br>然后访问集群的任何的8100端口就可以看到服务器了

### 安装oomall的模块
在此节点上需要安装JDK-17和Maven，才能编译工程
<br>Maven可以从[https://repo.huaweicloud.com/apache/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz](https://repo.huaweicloud.com/apache/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz)下载
<br>用`apt-get install git openjdk-17-jdk`安装git和jdk17

选择一个节点安装打包OOMALL，例如node2
<br>`git clone `
<br>修改OOMALL下的pom文件，将除core以外的模块注释掉
<br>在OOMALL目录用以下命令安装core和oomall的pom
<br>`mvn clean install`
<br>如果第二次打包不用重复上述过程，直接在core目录下运行
<br>`mvn clean install`
<br>在OOMALL目录下运行
<br>`git checkout pom.xml`
<br>将pom.xml恢复原样,
<br>在模块目录下运行
<br>`mvn clean pre-integration-test -Dmaven.test.skip=true`
<br>在node2的/root/上建立logs目录
<br>将模块的docker push到华为swr服务中
<br>在管理机上创建服务
<br>`docker node update --label-add server=product node2`
<br>`docker service create --name product --with-registry-auth  --network my-net --constraint node.labels.server==goods --publish published=8080,target=8080 --mount type=bind,source=/root/logs,destination=/app/logs -d [华为swr私服镜像]`

### 安装nacos

#### 设定在node2 安装nacos
在管理机上执行
<br>`docker node update --label-add server=nacos node2`
<br>在node2上pull docker镜像
<br>`docker pull swr.cn-north-4.myhuaweicloud.com/oomall-javaee/nacos/nacos-server:latest`

#### 启动nacos
`docker service create --name nacos --with-registry-auth --constraint node.labels.server==nacos  --network my-net --publish published=8848,target=8848 --publish published=9848,target=9848 --publish published=9849,target=9849  -e MODE=standalone  -e PREFER_HOST_MODE=hostname -d swr.cn-north-4.myhuaweicloud.com/oomall-javaee/nacos/nacos-server:latest`
<br>其中MODE表示用standalone模式启动， PREFER_HOST_MODE表示支持hostname方式，因为用的是swarn，需要用服务名查询
<br>设置集中配置的application.yaml
<br> 8848为nacos访问端口， 9848为客户端gRPC请求服务端端口，用于客户端向服务端发起连接和请求， 9849为服务端gRPC请求服务端端口，用于服务间同步等
<br>在浏览器中访问http://[IP]:8848/nacos, IP为集群中任意一台服务器ip
<br>输入默认用户名/密码: nacos/nacos
<br>即可进入nacos的控制台
<br>在ConfigurationManagement->Configurations中增加一项配置Create Configuration
<br>Data Id的格式为 ${spring.application.name}.yaml, 如商品模块为goods-service.yaml，商铺模块为shop-service.yaml，支付模块为payment-service.yaml
<br>Group：为默认的DEFAULT_GROUP
<br>Format：选Yaml
<br>Configuration Content：将对应模块的application.yaml内容拷贝进来，注意不能有中文注释
<br>按publish即可

### 安装ElasticSearch服务器
#### **更新服务器节点标签**
将 Elasticsearch 服务指定运行在 `OOMALL-Search` 节点：
```shell
docker node update --label-add server=es OOMALL-Search
```

####  **部署 Elasticsearch 服务**
运行以下命令启动 Elasticsearch 容器：
```shell
docker service create \
  --name es \
  --with-registry-auth \
  --constraint 'node.labels.server==es' \
  --env "ES_JAVA_OPTS=-Xms1g -Xmx1g" \
  --env "discovery.type=single-node" \
  --mount type=bind,source=/root/elastic/data,target=/usr/share/elasticsearch/data \
  --mount type=bind,source=/root/elastic/log,target=/usr/share/elasticsearch/logs \
  --mount type=bind,source=/root/oomall-2023/conf/es/conf/elasticsearch.yml,target=/usr/share/elasticsearch/config/elasticsearch.yml \
  --mount type=bind,source=/root/oomall-2023/conf/es/plugins,target=/usr/share/elasticsearch/plugins \
  --network my-net \
  --publish 9200:9200 \
  -d \
  swr.cn-north-4.myhuaweicloud.com/oomall-javaee/elasticsearch:8.5.3
```

- **参数说明**：
    - `--env "ES_JAVA_OPTS=-Xms1g -Xmx1g"`：限制 Elasticsearch JVM 最小和最大内存为 1GB。
    - `--mount`：
        - `source=/root/elastic/data`：挂载数据目录。
        - `source=/root/elastic/log`: 日志文件目录
        - `source=/root/oomall-2023/conf/es/conf/elasticsearch.yml`：指定自定义配置文件。
        - `source=/root/oomall-2023/conf/es/plugins`：挂载插件目录。
    - `--network my-net`：加入自定义 Docker 网络。
    - `--publish`：映射 9200。

## docker环境下项目的安装指南

### 安装MySQL镜像和服务
#### 在目标服务器上安装MySQL镜像
MySQL的配置文件目录conf.d和数据库初始化脚本都在OOMALL的mysql目录下，需要把这些文件拷贝到运行mysql的节点上，并映射到容器中
<br>用以下命令创建mysql
<br>`docker run --name mysql  -v /root/OOMALL/mysql/sql:/sql:ro -v /root/OOMALL/mysql/conf.d:/etc/mysql/conf.d:ro -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456  -d mysql:latest`
<br>其中`-e MYSQL_ROOT_PASSWORD=123456`是设定数据库root账户密码, 3306端口绑定到主机端口

#### 在运行mysql的docker容器上运行sql脚本
`docker exec -it mysql mysql -uroot -p`
<br>用root账号登录mysql服务器，在运行起来的mysql命令行中用`source /sql/database.sql`建立oomall各模块数据库

#### 分别初始化各模块的数据
以goods模块为例，用`use goods`切换数据库
<br>用`source /sql/product.sql`插入初始数据
<br>其他模块数据库的安装类似

### 安装带布隆过滤器的Redis

#### 在目标服务器上安装Redis镜像
将机器上的Redis配置目录映射到docker容器中
<br>`docker run --name redis -v /root/OOMALL/conf/redis:/etc/redis:ro -p 6379:6379 -e CONFFILE=/etc/redis/redis.conf  -d redis/redis-stack-server:latest`
<br>其中`-e CONFFILE=/etc/redis/redis.conf`是指定redis的配置文件，在配置文件中我们设定了redis的连接密码为123456, redis的配置文件目录redis映射到容器中

#### 在运行redis的容器上查看redis的服务是否正常运行
`docker exec -it redis redis-cli`
<br>进入redis-cli后，先运行下面的命令输入密码
<br>`auth 123456`
<br>再测试Bloom过滤器是否正常
<br>`BF.ADD testFilter hello`
<br>`BF.EXISTS testFilter hello`
<br>如果均返回(integer) 1则可以正常使用redis了

### 安装Mongo镜像
#### 在目标服务器上安装Mongo镜像
将机器上的Mongo配置目录映射到docker容器中
<br>`docker run --name mongo -v /root/OOMALL/conf/mongo:/mongo -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=123456  -d mongo:latest mongod --auth`
<br>其中` -e MONGO_INITDB_ROOT_USERNAME=root`是默认的用户名 `-e MONGO_INITDB_ROOT_PASSWORD=123456`是连接密码为123456

#### 创建mongo的用户
`docker exec -it mongo mongosh -u root -p 123456`
<br>进入mongosh后，先运行下面的命令切换database
<br>`use oomall`
<br>再在oomall建立demouser用户，给用户赋予读写和数据库管理员的角色
<br>`db.createUser({user:"demouser",pwd:"123456",roles:[{role:"readWrite",db:"oomall"},{role:"dbAdmin",db:"oomall"}]})`
<br>如果均返回{ok:1}则可以用demouser用户正常使用mongo的oomall数据库了
<br>`docker exec -it mongo mongosh -u demouser -p 123456 --authenticationDatabase oomall`

#### restore mongo的数据库
`docker exec -it mongo mongorestore -u demouser -p 123456 --authenticationDatabase oomall -d oomall --dir /mongo/oomall --drop`
<br>此命令会先删除所有数据再用户mongo/oomall下的数据恢复数据库

### 安装Mongo副本集
如果需要mongo支持事务，则需要安装至少3台mongo服务器，形成mongo的副本集。以下所列步骤是在一台物理机上安装三个docker，如需要在多台物理机上安装，可以使用docker swarm。具体可以参考[https://www.mongodb.com/docs/manual/administration/replica-set-deployment/](https://www.mongodb.com/docs/manual/administration/replica-set-deployment/)
#### 安装mongo的primary
<br>`docker run --name mongo1 -v /root/OOMALL/conf/mongo:/mongo -p 27017:27017 -d mongo:latest mongod --replSet rs0 --oplogSize 128`
<br>其中`--replSet rs0`定义了副本集的名称 `--oplogSize 128`定义了副本日志的大小

#### 安装mongo的secondary
secondary需要安装两台，如果在同一台物理机上无需将端口映射出来
<br>`docker run --name mongo2 -v /root/OOMALL/conf/mongo:/mongo -p 27018:27017 -d mongo:latest mongod --replSet rs0 --oplogSize 128`
<br>`docker run --name mongo3 -v /root/OOMALL/conf/mongo:/mongo -p 27019:27017 -d mongo:latest mongod --replSet rs0 --oplogSize 128`

#### #### 在primary上创建副本
查看三个容器的ip<br>
`docker network inspect bridge`<br>
从中找到mongo1, mongo2和mongo3的ip地址
登录上mongo1<br>
`docker exec -it mongo1 mongosh`
运行以下命令初始化副本集<br>
`rsconf = {_id: "rs0",members: [{_id: 0, host: "<mongo1_ip>:27017"},{_id: 1,host: "<mongo2_ip>:27017"},{_id: 2,host: "<mongo3_ip>:27017"}]}`
`rs.initiate( rsconf )`<br>
其中<mongo1_ip>，<mongo2_ip>和<mongo3_ip>分别用三个容器的ip替代<br>
用`rs.conf()`即可以查看配置情况
用`rs.status()`即可以查看哪个mongo是primary
在primary mongo上用相同的步骤[创建mongo用户]和[restore mongo的数据库]
在工程中将配置指向primary的mongo服务器

### 安装RocketMQ镜像
#### 创建RocketMQ的NameServer容器
`docker run -d --name rocketmq-namesrv -d -p 9876:9876 apache/rocketmq ./mqnamesrv`

#### 创建RocketMQ的Broker容器
修改原有的broker.conf文件，将其中的brokerIP1=rockermq-broker改为运行broker容器的IP。
<br>`docker run -d --name rocketmq-broker -d -p 10911:10911 -p 10909:10909 -v /home/mingqiu/OOMALL/conf/rocketmq:/rocketmq apache/rocketmq ./mqbroker -n [IP]:9876 -c /rocketmq/broker.conf`
<br>其中IP为rocketmq nameserver的ip

#### 安装Rocket Dashboard容器
`docker run -d --name rocketmq-dashboard -d  -e "JAVA_OPTS=-Drocketmq.namesrv.addr=[IP]:9876" -p 8100:8080 -t apacherocketmq/rocketmq-dashboard:latest`
<br>其中IP为rocketmq nameserver的ip，然后访问该机器的8100端口就可以看到Rocketmq的dashboard了

### 安装oomall的模块
安装打包oomall-2022，例如node2
<br>`git clone https://codehub.devcloud.cn-north-4.huaweicloud.com/OOMALL00024/OOMALL.git`
<br>修改OOMALL下的pom文件，将除core以外的模块删除
<br>在OOMALL目录用以下命令安装core和oomall的pom
<br>`mvn clean install`
<br>如果第二次打包不用重复上述过程，直接在core目录下运行
<br>`mvn clean install`
<br>在OOMALL目录下运行
<br>`git checkout pom.xml`
<br>将pom.xml恢复原样,
<br>在goods目录下运行
<br>`mvn clean pre-integration-test -Dmaven.test.skip=true`
<br>在node2的/root/上建立logs目录
<br>创建容器
<br>`docker run  --name goods  -p 8080:8080 -v /root/logs:/app/logs -d xmu-oomall/goods:0.0.1-SNAPSHOT`
<br>其中8080为模块的端口，注意每个模块的application.yaml中端口设置

### 安装nacos
`docker run --name nacos  -p 8848:8848 -e MODE=standalone  -e PREFER_HOST_MODE=hostname -d nacos/nacos-server:v2.1.2`
<br>其中MODE表示用standalone模式启动， PREFER_HOST_MODE表示支持hostname方式，因为用的是swarn，需要用服务名查询
<br>设置集中配置的application.yaml
<br>在浏览器中访问http://[IP]:8848/nacos, IP为服务器ip
<br>输入默认用户名/密码: nacos/nacos
<br>即可进入nacos的控制台
<br>在ConfigurationManagement->Configurations中增加一项配置Create Configuration
<br>Data Id的格式为 ${spring.application.name}.yaml, 如商品模块为goods-service.yaml，商铺模块为shop-service.yaml，支付模块为payment-service.yaml
<br>Group：为默认的DEFAULT_GROUP
<br>Format：选Yaml
<br>Configuration Content：将对应模块的application.yaml内容拷贝进来，注意不能有中文注释
<br>按publish即可
