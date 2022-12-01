# 日志
日志存储及读取，快速实现写日志、读取日志，大量日志数据的持久化存储。

## 快速使用
#### 1. pom.xml 中加入：

如果你只是单纯本地用，用不到像是elasticsearch、阿里云日志服务、Springboot框架的，那你可以只使用 ```` <artifactId>log-core</artifactId> ```` 这一个核心实现即可

````
<!-- 日志的核心支持 https://github.com/xnx3/log -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>log-core</artifactId>
	<version>1.0</version>
</dependency>
````

#### 2. 代码中调用

````
Log log = new Log();

Map<String, Object> params = new HashMap<String, Object>();
params.put("author", "管雷鸣");
params.put("url", "https://github.com/xnx3/log");
log.add(params);
````

#### 3. 更多设置方式


#### 4. Demo示例
[demo_javase/README.md](demo_javase/)

## 日志存储方式
默认使用的是控制台打印出日志。可以切换成采用elasticsearch、阿里云sls日志服务、华为云lts日志服务  
比如使用elasticsearch，则pom.xml 中额外加入：

````
<!-- 加入elasticsearch的实现。 （存储到哪，这里artifactId就引入的哪里的 datasource.xxx 另外，core中默认带控制台将日志实时打印出来） -->
<dependency> 
	<groupId>cn.zvo.log</groupId>
	<artifactId>log-datasource-elasticsearch</artifactId>
	<version>1.0</version>
</dependency>
````

代码中的变动,仅仅只是增加一行 setLogInterface 设置： 

````
//设置使用elasticsearch 。这里是设置日志实现的接口，存储到哪，后端时对接的哪里，比如elasticsearch、阿里云sls日志服务、华为云lts日志服务等
log.setLogInterface(new ElasticSearchDataSource("127.0.0.1", 9200, "http", null, null)); 
````

如此，便将日志服务切换为了使用elasticsearch  


## SpringBoot框架中使用
在springboot项目中使用时，pom.xml 中再加入以下：

````
<!-- 在 SpringBoot 框架中的快速使用。 （在不同的框架中使用，这里引入的framework.xxx也不同） -->
<dependency> 
    <groupId>cn.zvo.log</groupId>
    <artifactId>log-framework-springboot</artifactId>
    <version>1.0</version>
</dependency> 
````

具体SpringBoot的使用，可参考： [framework_springboot/README.md](framework_springboot/)

## 交流及参与贡献
作者：管雷鸣  
微信：xnx3com  
QQ交流群：:579544729  