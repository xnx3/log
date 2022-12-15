## 1. pom.xml 加入

````
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



