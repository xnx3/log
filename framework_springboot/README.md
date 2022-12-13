springboot 项目中使用  

## 1. 快速使用

#### 1.1 pom.xml 加入

````
<!-- 日志的核心支持 https://github.com/xnx3/log -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>log-core</artifactId>
	<version>1.0</version>
</dependency>
<!-- 在 SpringBoot 框架中的快速使用。 （在不同的框架中使用，这里引入的framework.xxx也不同） -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>log-framework-springboot</artifactId>
	<version>1.0</version>
</dependency> 
````

#### 1.2 Java 中使用
写入一行日志，代码： 

````
LogUtil.add(...);
````

## 2. 配置参数

#### 2.1 日志提交相关的配置  
配置 application.properties (或yml)，加入：  

````
#
# 日志 https://github.com/xnx3/log
#
# 缓存日志的最大条数。当达到这个条数后，将自动提交日志。默认为100
# 这里方便测试是否提交，直接设为1。正常情况下建议使用默认值即可，不用设置
log.cacheMaxNumber=3
# 缓存日志的最大时间，单位为秒。不设置默认是60。超过这个时间后，将会自动提交日志。即每隔多长时间就自动提交日志，然后重新开始累计即时
# 这里方便测试是否提交，直接设为1。正常情况下建议使用默认值即可，不用设置
log.cacheMaxTime=60
````

#### 2.2 默认写出日志文件方面配置

````
#
# 当前使用的是哪种数据存储方式
# 如果此不设置，默认不做任何动作
# 下面便是具体针对自带默认的写出日志文件这种存储方式的配置了
# 保存日志的路径，格式如 /mnt/tomcat8/logs/ 、或者windows服务器的话路径如 C:\\Users\\Administrator\\Desktop\\log\\  注意最后的斜杠不能拉下。如果不设置此，则不开启，日志不会写出到日志文件，没任何动作。
log.datasource.file.path=C:\\Users\\Administrator\\Desktop\\log\\
#log.datasource.file.path=/mnt/tomcat8/logs/
# 保存日志的文件名，传入如 useraction ，则会自动保存出 useraction_20221206.log  。如果name不设置，默认是 log ，按小时生成的日志文件名字为 log_2022-12-13_14.log
#log.datasource.file.name=log
````

#### 2.3 项目中整体使用示例

[/demo_springboot/](/demo_springboot/) 这里在springboot项目中的整体示例进行演示，可直接以maven项目方式导入、运行 ```` src/main/java/cn/zvo/log/demo/springboot/Application.java  ```` 即可。  
访问测试： 
* localhost:8080/add.json 添加一条日志
* localhost:8080/list.json 查看日志列表

其中的文件相关说明：  
* src/main/resources/application.properties 配置相关
* [src/main/java/cn/zvo/log/demo/springboot/DemoController.java](/demo_springboot/src/main/java/cn/zvo/log/demo/springboot/DemoController.java) add.json、list.json 的实现示例

## 3. 扩展-存储方式-使用ElasticSearch

默认是直接使用文件存储的方式。这里演示使用elasticsearch进行存放时，增加其相关的配置


#### 3.1 pom.xml 额外加入


````
<!-- 加入elasticsearch的实现。 （使用哪种存储方式，这里artifactId就引入的哪里的 datasource.xxx -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>log-datasource-elasticsearch</artifactId>
	<version>1.0</version>
</dependency>
````

#### 3.2 设置配置文件

配置文件 application.properties (或yml) 中，将 log.datasource.file.xxxx 相关的都删掉，加入一下设置:  

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
