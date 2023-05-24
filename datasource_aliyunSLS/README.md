SpringBoot中使用的示例： （也可以在普通java项目或别的框架项目中使用）
## 1. pom.xml 加入

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
<!-- 阿里云 sls 支持 -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>log-datasource-aliyunSLS</artifactId>
	<version>1.0</version>
</dependency> 
````

## 2. 配置参数

配置文件 application.properties (或yml) 中，将 

````
log.tableName=useraction
````
将此 log.tableName 设置为sls的 logstore  
另外  
log.datasource.file.xxxx 相关的都删掉，加入一下设置:  

````
#
# 当前使用的是哪种数据存储方式
#
# 如果此不设置，默认只是使用控制台输出而已。
# 下面便是具体针对 AliyunSLS 这种存储方式的配置了
# 阿里云的 accessKeyId
log.datasource.aliyunSLS.accessKeyId=xxxxxxxxxxx
# 阿里云的 accessKeySecret
log.datasource.aliyunSLS.accessKeySecret=xxxxxxxxxxx
# sls日志服务的endpoint
log.datasource.aliyunSLS.endpoint=cn-hongkong.log.aliyuncs.com
# sls日志服务中建的项目名
log.datasource.aliyunSLS.project=requestlog
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
# 如果此不设置，默认只是使用控制台输出而已。
# 下面便是具体针对 AliyunSLS 这种存储方式的配置了
# 阿里云的 accessKeyId
log.datasource.aliyunSLS.accessKeyId=xxxxxxxxxxx
# 阿里云的 accessKeySecret
log.datasource.aliyunSLS.accessKeySecret=xxxxxxxxxxx
# sls日志服务的endpoint
log.datasource.aliyunSLS.endpoint=cn-hongkong.log.aliyuncs.com
# sls日志服务中建的项目名
log.datasource.aliyunSLS.project=requestlog
````

