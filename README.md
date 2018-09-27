# springmvc-demo

# SpringMVC

## 一、SpringMVC简介

### 1. 什么是MVC

	M:Model 数据模型

	V:View 视图

	C:Controller 控制器

	MVC是一种Web开发应用架构，是一种代码设计思想

	核心思想：将所有客户端的请求（Request）都交由控制器，由控制器进行分发，并将结果响应给客户端

### 2. 常见的MVC框架

	使用原生Servlet实现MVC的缺点：

- 配置比较复杂

- 数据处理麻烦

  实现开发中一般都会使用MVC框架，如Struts1、Struts2、SpringMVC等 

  对比：

  - 效率：Struts1>SpringMVC>Struts2

    Struts2是多例的，效率的

    Struts1和SpringMVC是单例的，两者效率基本差不多

  - 配置：SpringMVC>Struts2>Struts1

    SpringMVC最简单，全注解配置

### 3. 为什么使用SpringMVC

- 简单，使用注解配置来替代XML配置
- 效率高，单例的，将Controller将由IOC容器管理
- 扩展好，方便用户自定义
- SpringMVC和Spring无缝对应，亲生的

## 二、SpringMVC实现原理

### 1. 流程图

![springMVC](springMVC.JPG)



### 2. 执行过程

	分六步：

- DispatcherServlet

  SpringMVC核心控制器，主要作用是用来分发，不进行任何处理

- HandlerMapping

  映射处理器：根据请求的url映射到具体的Handler

  Handler就是Controllern层的实现对象，也称为Controller或Action

- HandlerAdapter

  适配器：用来适配不同的处理器Handler

  处理器有两种实现方式：实现接口、基于注解，所以在执行Handler之前需要先适配，这样才知道如何执行

- Handler

  处理器：执行具体的业务，返回数据模型Model和视图View

  Handler会将数据模型和视图封装成一个对象ModelAndView并返回

- ViewResolver

  视图解析器：根据视图名解析为具体的视图对象，一般多为jsp，然后封装对View对象

- View

  视图：使用具体的视图技术进行渲染，结合Model进行数据展示

  视图有很多种技术：jsp、html、freemarker、velocity、excel、pdf等

  ​

## 三、第一个SpringMVC程序

### 1. 添加jar包

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-webmvc</artifactId>
</dependency>
```

### 2. 配置核心控制器

	在web.xml文件中配置

```xml
<!-- 1. 配置DispatcherServlet核心控制器，本质上就是一个Servlet-->
<servlet>
  <servlet-name>springmvc</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
  <servlet-name>springmvc</servlet-name>
  <url-pattern>/</url-pattern>
</servlet-mapping>
```

### 3. 核心配置文件

	两种定义方式：

- 使用默认位置，默认在WEB-INF目录下，名称为：`Servlet名称-servlet.xml`
- 自定义位置，名称自定义，需要指定配置文件的路径

        SpringMVC控制器两种实现方式：

- 实现接口
- 基于注解

#### 3.1 实现接口的Controller

	HelloController

```java
public class HelloController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        ModelAndView mav = new ModelAndView();
        mav.addObject("msg","hello "+name);
        mav.setViewName("hello");

        return mav;
    }
}

```

	springmvc-servlet.xml

```xml
<!--2.配置HandlerMapping-->
<bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>

<!--3.配置HandlerAdapter-->
<bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>

<!--4.配置Handler-->
<bean name="/hello" class="com.itany.controller.HelloController"/>

<!--5.配置ViewResolver-->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
  <property name="prefix" value="/WEB-INF/view/"/>  <!-- /WEB-INF/view/hello.jsp -->
  <property name="suffix" value=".jsp"/>
  <!--6.配置View，使用jsp视图技术渲染页面-->
  <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
</bean>
```



#### 3.2 基于注解的Controller

	HelloAnnotationController

```java
@Controller
public class HelloAnnotationController {

    @RequestMapping(value = "/aaa")
    public ModelAndView abc(String name){
        ModelAndView mav = new ModelAndView();
        mav.addObject("msg","你好 "+name);
        mav.setViewName("hello");
        return mav;
    }
}
```

	springmvc.xml

```xml
 <!--2.配置HandlerMapper-->
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"/>

<!--3.配置HandlerAdapter-->
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"/>

<!--4.配置Handler-->
<context:component-scan base-package="com.itany.controller"/>

<!--5.配置ViewResolver-->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
  <property name="prefix" value="/WEB-INF/view/"/>
  <property name="suffix" value=".jsp"/>
  <!--6.配置View-->
  <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
</bean>
```

#### 3.3 简化注解方式的配置

```xml
 <!--
        注解驱动，用来简化注解配置的
        会自动加载RequestMappingHandlerMapping和RequestMappingHandlerAdapter
    -->
<mvc:annotation-driven/>

<!--4.配置Handler-->
<context:component-scan base-package="com.itany.controller"/>

<!--5.配置ViewResolver-->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
  <property name="prefix" value="/WEB-INF/view/"/>
  <property name="suffix" value=".jsp"/>
</bean>
```

## 四、案例

### 1. 用户登陆

	login.jsp  success.jsp  LoginController

### 2. 静态资源处理

	当配置DispatcherServlet的urlpattern为/时，会拦截所有请求，也包括静态资源，导致所有静态资源都无法访问

	两种处理方式：

- 使用tomcat提供的默认Servlet

  查看tomcat7.../conf/web.xml，提供DefaultServlet，用来处理所有静态资源的访问

  ```xml
  <mvc:default-servlet-handler/>
  ```

  缺点：

  1. 如果使用的不是Tomcat，可能就不生效了，与tomcat耦合
  2. 只能读取webapp下的资源，无法读取WEB-INF下的资源

- 使用SpringMVC提供的处理方式

  ```xml
  <mvc:resources mapping="/js/**" location="/js/"/>
  <!--当访问/imgs/时映射访问/WEB-INF/images/目录-->
  <mvc:resources mapping="/imgs/**" location="/WEB-INF/images/"/>
  <mvc:resources mapping="/css/**" location="/WEB-INF/css/"/>
  ```

### 3. 直接访问jsp页面

	WEB-INF下的jsp页面默认不能直接访问，一般都是通过Controller进行跳转

## 五、Controller详解

### 1. 方法的返回值

	有四种类型：

- ModelAndView 表示返回的是数据模型和视图

- String 表示返回的是视图

  三种形式（写法）：

  - 普通字符串——>表示视图名，直接跳转到页面
  - forward:url——>转发
  - redirect:url——>重定向

- void 表示返回的是视图

  将url请求路径作为视图名，很少使用

- Object 表示返回的是数据模型（一般返回json数据）

### 2. SpringMVC注解

| 注解                 | 解释                                 |
| ------------------ | ---------------------------------- |
| @Controller        | 将类映射为Controller层，添加到IoC容器中         |
| @RequestMapping    | 配置请求映射路径，即URL                      |
| @RequestParam      | 表示参数来源于请求参数                        |
| @PathVariable      | 表示参数来源于URL                         |
| @RequestHeader     | 表示参数来源于请求头                         |
| @CookieValue       | 表示参数来源于Cookie                      |
| @RequestBody       | 表示参数来源于请求体（只有POST请求）               |
| @ModelAttribute    | 将请求参数转换为对象                         |
| @Valid             | 后台校验                               |
| @InitBinder        | 类型转换                               |
| @SessionAttributes | 将数据存储到Session作用域                   |
| @ControllerAdvice  | 统一异常处理                             |
| @ExceptionHander   | 异常处理器                              |
| @ResponseBody      | 处理ajax请求，结合返回为Object的方法，用来返回json数据 |
| @RestController    | 相当于@Controller+@ResponseBody       |

### 3. @RequestMapping

#### 3.1 基本用法

	该注解可以标注在方法上，也可以标注在类上，表示层次关系

	配置URL时以/开头和不以/开头的区别：

- 添加时表示从项目名称开始查找
- 不添加时表示从当前方法所有在层次开始查找

#### 3.2 URL的多种写法

	请求映射路径有三种写法：

- Ant风格(使用不多)

  `*`表示单层目录，匹配任意字符，可以没有字符，但正斜杠必须有

  `**`表示多层目录，匹配任意字符，可以没有字符，同时正斜杠可以没有

  `?`匹配单个字符，必须有一个字符

- Rest风格

  可以使用占位符：  {变量:正则}表示URL中的占位符

- 普通写法

  value、path

#### 3.3 根据请求方式访问

	限定请求的方式：GET、POST、PUT、DELETE等

#### 3.4 其他

- params

  限定请求参数，必须符合指定的条件

- headers

  限定请求头，必须符合指定的条件

  ​

## 补充：

### 1. 配置JDK环境

```bash
vi .bashrc  # 在文件的最后添加如下内容
	export JAVA_HOME=/home/soft01/jdk1.8.0_171
	export CLASSPATH=.:$JAVA_HOME/lib
	export PATH=$JAVA_HOME/bin:$PATH
source .bashrc  # 立即生效    
```



	
