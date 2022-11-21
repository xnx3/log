springboot 项目中使用  

## 1. 快速使用

#### 1.1 pom.xml 加入

````
<!-- 日志的核心支持 https://github.com/xnx3/log -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>core</artifactId>
	<version>1.0</version>
</dependency>
<!-- 在 SpringBoot 框架中的快速使用。 （在不同的框架中使用，这里引入的framework.xxx也不同） -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>framework.springboot</artifactId>
	<version>1.0</version>
</dependency> 
````

#### 1.2 Java 中使用
写入一行日志，代码： 

````
LogUtil.add(...);
````

## 2. 扩展-配置参数

配置 application.properties (或yml)，加入：  

````
#
# 日志 https://github.com/xnx3/log
#
# 缓存日志的最大条数。当达到这个条数后，将自动提交日志。默认为100
log.cacheMaxNumber=100
# 缓存日志的最大时间，单位为秒。不设置默认是60。超过这个时间后，将会自动提交日志。即每隔多长时间就自动提交日志，然后重新开始累计即时
log.cacheMaxTime=60
````

## 3. 扩展-存储方式-使用ElasticSearch进行存储

默认是直接在控制台将日志打印出来。这里演示使用elasticsearch进行存放时，增加其相关的配置


#### 3.1 pom.xml 额外加入

````
<!-- 加入elasticsearch的实现。 （使用哪种存储方式，这里artifactId就引入的哪里的 datasource.xxx -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>datasource.elasticsearch</artifactId>
	<version>1.0</version>
</dependency>
````

#### 3.2 设置配置文件

配置 application.properties (或yml)，加入：

````
#
# 当前使用的是哪种数据存储方式
# 如果此不设置，默认只是使用控制台输出而已。
# 下面便是具体针对ElasticSearch这种存储方式的配置了
# 索引得名字，类似于数据库得数据表名字。要将数据存储到哪个索引中
log.datasource.elasticsearch.indexName=useraction
# elasticsearch所在得hostname，比如在服务器本身安装，可以传入 127.0.0.1
log.datasource.elasticsearch.hostname=127.0.0.1
# 端口号，不设置默认是 9200
log.datasource.elasticsearch.port=9200
# scheme，如 http 、 https 不设置默认是http
log.datasource.elasticsearch.scheme=http
# 登录得用户名。如果不需要登录，这里注释掉即可，无需配置。
log.datasource.elasticsearch.username=xxxxxx
# 登录得密码。如果不需要登录，这里注释掉即可，无需配置。
log.datasource.elasticsearch.password=xxxxxx
````

#### 3.3 启动，测试
如此，java代码中使用 ```` LogUtil.add(...);```` 就可直接将其存入elasticsearch中了。  
**注意**，日志本身为了避免频繁写日志，而导致阻塞本身请求，使请求时长增加，特使用了缓存机制，您可设置application配置文件的配置项 ```` log.cacheMaxNumber=1 ```` 便是表示最大缓存一条，那便是只要加入一条就立即提交，也就能在elasticsearch中实时看到日志记录了
