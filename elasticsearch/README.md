# Elasticsearch 使用说明和安装文档

## 环境准备

### 1. **更新服务器节点标签**
将 Elasticsearch 服务指定运行在 `OOMALL-Search` 节点：
```shell
docker node update --label-add server=es OOMALL-Search
```

---

## 安装 Elasticsearch 和 Kibana

### 2. **部署 Elasticsearch 服务**
运行以下命令启动 Elasticsearch 容器：

启动前需要把对应目录建好，并且赋予读写权限。

```shell
mkdir /root/elastic/log
sudo chmod -r 777 /root/elastic/log
sudo chmod -r 777 /root/oomall/es/backups
```

```shell
docker service create \
  --name es \
  --with-registry-auth \
  --constraint 'node.labels.server==es' \
  --env "ES_JAVA_OPTS=-Xms1g -Xmx1g" \
  --env "discovery.type=single-node" \
  --mount type=bind,source=/root/elastic/log,target=/usr/share/elasticsearch/logs \
  --mount type=bind,source=/root/oomall/conf/es/conf/elasticsearch.yml,target=/usr/share/elasticsearch/config/elasticsearch.yml \
  --mount type=bind,source=/root/oomall/conf/es/plugins,target=/usr/share/elasticsearch/plugins \
  --mount type=bind,source=/root/oomall/es/backups,target=/usr/share/elasticsearch/backups \
  --network my-net \
  --publish 9200:9200 \
  -d \
  swr.cn-north-4.myhuaweicloud.com/oomall-javaee/elasticsearch:8.5.3
```

- **参数说明**：
  - `--env "ES_JAVA_OPTS=-Xms1g -Xmx1g"`：限制 Elasticsearch JVM 最小和最大内存为 1GB。
  - `--mount`：
    - `source=/root/elastic/log`：挂载存储 Elasticsearch 的日志文件目录。
    - `source=/root/oomall/conf/es/conf/elasticsearch.yml`：指定自定义配置文件。
    - `source=/root/oomall/conf/es/plugins`：挂载插件目录。
    - `source=/root/oomall/es/backups`：挂载快照或数据备份目录。
  - `--network my-net`：加入自定义 Docker 网络。
  - `--publish`：映射 9200。

### 3. **部署 Kibana 服务（可选）**
如果服务器内存大于 4GB，可以安装 Kibana：

```shell
docker service create \
  --name kibana \
  --constraint 'node.labels.server==es' \
  --env ELASTICSEARCH_HOSTS=http://es:9200 \
  --memory="256m" \
  --memory-swap="512m" \
  --env "NODE_OPTIONS=--max-old-space-size=512" \
  --network my-net \
  --publish 5601:5601 \
  -d \
  kibana:8.5.3
```

- **参数说明**：
  - `ELASTICSEARCH_HOSTS`：指定 Elasticsearch 地址。
  - `--memory` 和 `--memory-swap`：限制内存使用，避免因资源不足导致服务崩溃。
  - 如果服务器内存不足，可以选择不安装 Kibana，不影响 Elasticsearch 的正常使用。

---

## 数据和索引导入

### 1. **使用快照进行数据导入**(推荐且方便)

Elasticsearch 允许通过快照恢复索引数据，适用于大规模数据迁移或备份恢复。

> 快照仓库的配置已经在elasticsearch.yml中完成，所以此处不需要做。

#### 1.1 **创建快照仓库**

首先，创建一个快照仓库，用于存储索引的快照。以下命令创建一个文件系统类型的快照仓库：

```shell
curl -X PUT "localhost:9200/_snapshot/my_backup" -H 'Content-Type: application/json' -d '{
  "type": "fs",
  "settings": {
    "location": "/usr/share/elasticsearch/backups"
  }
}'
```

这里的 `/usr/share/elasticsearch/backups` 是容器挂载到宿主机的目录，存放备份文件。

> 如果 curl 无法执行的话，可以用 postman 等工具发请求，将localhost替换为你自己的公网ip。

#### 1.2 **恢复快照**

恢复/root/oomall/es/backups 下的 oomall 快照。

```shell
curl -X POST "localhost:9200/_snapshot/my_backup/oomall/_restore"
```

### 2. 使用 elasticdump 工具导入和导出数据(速度慢而且繁琐)

#### 2.1 **安装 nvm**

使用 `nvm` 安装和管理 Node.js 版本。

```shell
git clone https://gitee.com/mirrors/nvm	# 使用gitee上的镜像源
cd nvm
bash install.sh
source ~/.bashrc
```

#### 2.2 **安装 Node.js**
```shell
nvm install 14	# 由于Ubuntu内核版本限制只能安装14版本
```

#### 2.3 **配置 npm 镜像源**

```shell
npm config set registry https://registry.npmmirror.com
```
#### 2.5 **安装 `elasticdump` 工具**
`elasticdump` 是用于导入和导出 Elasticsearch 数据的工具：

```shell
npm install elasticdump -g
```

#### 2.6 **导入索引 Mapping**
将 `mapping` 文件导入 Elasticsearch：
```shell
elasticdump \
  --input=product_index_mapping.json \
  --output=http://localhost:9200/product_index \
  --type=mapping

elasticdump \
  --input=order_item_name_index_mapping.json \
  --output=http://localhost:9200/order_item_name_index \
  --type=mapping

elasticdump \
  --input=order_index_mapping.json \
  --output=http://localhost:9200/order_index \
  --type=mapping
```

#### 2.7 **导入索引数据**
导入索引的初始数据：
```shell
elasticdump \
  --input=product_index_data.json \
  --output=http://localhost:9200/product_index \
  --type=data

elasticdump \
  --input=order_item_name_index_data.json \
  --output=http://localhost:9200/order_item_name_index \
  --type=data

elasticdump \
  --input=order_index_data.json \
  --output=http://localhost:9200/order_index \
  --type=data
```

---

## 验证安装

### 1. **验证 Elasticsearch 服务**
运行以下命令检查 Elasticsearch 是否启动：
```shell
curl -X GET "http://localhost:9200"
```

预期返回：
```json
{
  "name": "your-node-name",
  "cluster_name": "elasticsearch",
  "cluster_uuid": "xxxxxxxxx",
  "version": {
    "number": "8.5.3",
    ...
  },
  "tagline": "You Know, for Search"
}
```

### 2. **验证 Kibana 服务**
在浏览器访问 Kibana：
```
http://<your-server-ip>:5601
```
### 2. **验证快照数据是否导入成功**

```shell
curl -X GET "http://localhost:9200/_cat/indices?v"
```

预期返回：

```shell
health status index                 uuid                   pri rep docs.count docs.deleted store.size pri.store.size
yellow open   order_item_name_index HlMDnsd8TCKgRO88RtSxgQ   1   1       3652            0    620.4kb        620.4kb
yellow open   order_index           SOdv8EFPQf-VRe6NHzrY4w   1   1      79980            0      4.5mb          4.5mb
yellow open   product_index         rVbdTe_tRM2W_QuXuDoijg   1   1       3912            0    381.2kb        381.2kb
```

---

## 常见问题排查

### 1. **Elasticsearch 启动失败**
- 检查日志：
  ```shell
  docker service logs es
  ```

## 附录

如果想导出自己数据，也可以使用 Snapshot 完成。下面介绍如何用快照导出数据。

### 创建快照仓库

在开始导出数据之前，首先需要创建一个用于存储快照的仓库。仓库类型可以是文件系统（`fs`），并指定存储位置。假设快照存储在 `/usr/share/elasticsearch/backups` 目录下。

> 在快照创建前必须对快照仓库进行，配置可以在docker 命令里指定也可以在 elasticsearch.yml 指定，但在创建快照仓库前必须要做这一步。
>
> 在本项目中是在 elasticsearch.yml 中配置，然后用 mount 将它挂载出来，这样我们可以很方便地通过文件系统在不同的服务器上(即使非同一个集群)来导入导出数据。

```shell
curl -X PUT "localhost:9200/_snapshot/my_backup" -H 'Content-Type: application/json' -d '{
  "type": "fs",
  "settings": {
    "location": "/usr/share/elasticsearch/backups"
  }
}'
```

### **创建数据快照**

一旦创建了快照仓库，你可以创建快照。假设你要创建名为 `oomall` 的快照，并导出数据，使用以下命令：

```shell
curl -X PUT "http://localhost:9200/_snapshot/my_backup/oomall?wait_for_completion=true"
```

