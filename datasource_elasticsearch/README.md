elasticsearch 

## pom.xml 中加入：

````
<!-- 加入elasticsearch的实现。 （存储到哪，这里artifactId就引入的哪里的 datasource.xxx 另外，core中默认带控制台将日志实时打印出来） -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>log-datasource-elasticsearch</artifactId>
	<version>1.0</version>
</dependency>
````

## SpringBoot框架中配置

````
#
# 当前使用的是哪种数据存储方式
#
# 如果此不设置，默认只是使用本地文件存储方式。
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