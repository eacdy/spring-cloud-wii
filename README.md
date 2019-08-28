# README

Spring Cloud Wii是一个用于快速 **完美整合** Spring Cloud与 **异构微服务** 的框架，基于Spring Cloud Gateway以及Nacos Discovery，灵感来自 [Sidecar](https://github.com/spring-cloud/spring-cloud-netflix/tree/master/spring-cloud-netflix-sidecar) 。



## 术语

### 异构微服务

非Spring Cloud应用，统称异构微服务。比如你的遗留项目，或者非JVM应用。

### 完美整合有三层含义：

* 享受服务发现的优势
* 有负载均衡
* 有断路器



## 为什么要造这个轮子？

原因有两点：

* Spring Cloud有个子项目Spring Cloud Netflix Sidecar，然而Sidecar只支持使用Eureka作为服务发现，而我们的项目需要使用Nacos。
* Sidecar是基于Zuul 1.x的，Spring Cloud官方已经明确声明过，未来将会逐步淘汰Zuul。在今年早些时候，我有给Spring Cloud官方提出需求，希望官方实现一个基于Spring Cloud Gateway的新一代Sidecar，然而官方表示并没有该计划。详见：<https://github.com/spring-cloud/spring-cloud-gateway/issues/735>

既然没有，那我就自己写了。



## 原理

* Wii根据配置的异构微服务的IP、端口等信息，将异构微服务的IP/端口注册到Nacos上
* Wii实现了健康检查，如果异构微服务挂掉，Wii会自动将代表异构微服务的实例下线；如果异构微服务恢复正常，则会自动上线。最长延迟是10秒。



## 使用

* 依赖管理：Spring Cloud Alibaba版本必须是2.1.x，否则会报错。这主要是因为Spring Cloud Alibaba修改了他们的包名命名…

  ```xml
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring-cloud.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
      <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-dependencies</artifactId>
        <version>2.1.0.RELEASE</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
  ```

* 加依赖：

  ```xml
  <dependency>
    <groupId>com.itmuch</groupId>
    <artifactId>spring-cloud-wii</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>
  ```

* 写配置：

  ```yaml
  server:
    port: 8070
  spring:
    cloud:
      nacos:
        discovery:
          server-addr: localhost:8848
      gateway:
        discovery:
          locator:
            enabled: true
    application:
      name: wii-node-service
  wii:
    # 异构微服务的IP
    ip: 127.0.0.1
    # 异构微服务的端口
    port: 8060
    # 异构微服务的健康检查URL
    health-check-url: http://localhost:8060/health.json
  ```

  配置比较简单，就是把Wii注册到Nacos上，然后写了一堆wii的配置就OK了。



## 测试

### 异构微服务

我这里准备了一个NodeJS编写的简单微服务。

```javascript
var http = require('http');
var url = require("url");
var path = require('path');

// 创建server
var server = http.createServer(function(req, res) {
    // 获得请求的路径
    var pathname = url.parse(req.url).pathname;
    res.writeHead(200, { 'Content-Type' : 'application/json; charset=utf-8' });
    // 访问http://localhost:8060/，将会返回{"index":"欢迎来到首页"}
    if (pathname === '/') {
        res.end(JSON.stringify({ "index" : "欢迎来到首页" }));
    }
    // 访问http://localhost:8060/health，将会返回{"status":"UP"}
    else if (pathname === '/health.json') {
        res.end(JSON.stringify({ "status" : "UP" }));
    }
    // 其他情况返回404
    else {
        res.end("404");
    }
});
// 创建监听，并打印日志
server.listen(8060, function() {
    console.log('listening on localhost:8060');
});
```



### 测试1：Spring Cloud完美调用异构微服务

你的Spring Cloud项目整合Ribbon，只需构建 `http://wii-node-service` 就可以请求到异构微服务了。

示例：

Ribbon请求 `http://wii-node-service/` 会请求到 `http://localhost:8060/` 以此类推。

至于断路器，正常为你的Spring Cloud微服务整合Sentinel或者Hystirx、Resilience4J即可 。



### 测试2：异构微服务完美调用Spring Cloud

由于Wii基于Spring Cloud Gateway，而网关自带转发能力啊。

示例：

如果你有一个Spring Cloud微服务叫做 `spring-cloud-microservice` ，那么NodeJS应用只需构建 `http://localhost:8070/spring-cloud-microservice/**` ，Wii就会把请求转发到 `spring-cloud-microservice` 的 `/**` 。

而Spring Cloud Gateway是整合了Ribbon的，所以实现了负载均衡；Spring Cloud Gateway还可以整合Sentinel或者Hystirx、Resilience4J，所以也带有了断路器。