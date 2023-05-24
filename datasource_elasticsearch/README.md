SpringBoot中使用的示例： （也可以在普通java项目或别的框架项目中使用）

## pom.xml 中加入：

````
<!-- 日志的核心支持 https://github.com/xnx3/log -->
<dependency> 
    <groupId>cn.zvo.log</groupId>
    <artifactId>log-core</artifactId>
    <version>1.1</version>
</dependency>
<!-- 在 SpringBoot 框架中的快速使用。 （在不同的框架中使用，这里引入的framework.xxx也不同） -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>log-framework-springboot</artifactId>
	<version>1.0</version>
</dependency> 
<!-- 加入elasticsearch的实现。 （存储到哪，这里artifactId就引入的哪里的 datasource.xxx 另外，core中默认带控制台将日志实时打印出来） -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>log-datasource-elasticsearch</artifactId>
	<version>1.0</version>
</dependency>
````

## 配置文件改动

#### 在原本配置的基础上增加一下配置
````
#
# 当前使用的是哪种数据存储方式
#
# 如果此不设置，默认只是使用本地文件存储方式。
# 下面便是具体针对ElasticSearch这种存储方式的配置了
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


#### 全部的配置如下


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
# 设置日志存储到哪个日志仓库中【此项必填】
# 这里以数据库为例解释，以便于理解。数据库有多个表，每个表都会存储不同的数据（结构）
# 这里便是每个表代表一个数据仓库。通过设置此，可切换将数据存入不同的数据仓库
# 例如 ：
#     elasticsearch ： 这里便是设置索引的名字，可以将不同的数据存入不同的索引中
#     阿里云SLS日志服务 ： 这里便是设置的日志库的名字，可将不同的数据存入不同的日志库中
#     华为云LTS日志服务 ： 这里便是设置的日志流的名字
log.tableName=useraction
#
# 当前使用的是哪种数据存储方式
#
# 如果此不设置，默认只是使用本地文件存储方式。
# 下面便是具体针对ElasticSearch这种存储方式的配置了
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