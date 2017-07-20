# lhh-frame-spring-boot
spring-boot框架
运营平台

## 代码结构

### 工程目录结构
 * lib：maven仓库之外的jar包，目前有ojdbc14.jar
 * sql：数据库初始化脚本
 * src/main/java：java代码目录
 * src/main/resource：资源目录
    * config：配置目录  
    * static：静态资源目录（css、js、images、lib等）
    * template：thymeleaf页面模版目录（页面文件都放在这里）
    * application.yml：spring-boot默认配置文件，yaml格式。
    * logback-spring.xml：logback日志配置文件
 
 ### 包结构
  * com.lhh.base：系统核心功能，包含系统基础类,日志记录
  * com.lhh.core：系统核心功能，包含系统用户，角色，权限管理
 
 ## 测试启动
  com.lhh.Application.main(String[] args):启动spring-boot，自带tomcat
 
 ## 涉及到的技术
  spring-boot
  shiro
  jpa
  hibernate
  mysql、oracle
  thymeleaf
  layui

 ## 初始化数据库
  sql/init-mysql.sql
