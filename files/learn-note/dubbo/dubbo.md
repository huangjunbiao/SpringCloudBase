# dubbo
Dubbo是一个分布式、高性能、透明化的RPC服务框架，能提供服务自动注册、自动发现等高效服务治理方案。功能主要包括：高性能的NIO通讯及多协议集成、服务动态寻址与路由、软负载均衡与容错、依赖分析与降级。
Rpc存在一些问题及需求
* rpc调用需要定制，会有额外工作量；
* 分布式服务中，服务数很多，相互之间调用复杂，相互依赖严重；
* 对集群的服务，需要负载策略；
* 对集群的服务，要能动态扩展节点。
## dubbo架构设计分层
* 接口服务层（Service）：该层与业务逻辑相关，根据provider和consumer的业务设计对应的接口和实现；
* 配置层（Config）：对外配置接口，以ServiceConfig和ReferenceConfig为中心；
* 服务代理层（Proxy）：服务接口透明代理，生成服务的客户端Stub和服务端Skeleton，以ServiceProxy为中心，扩展接口为ProxyFactory；
* 服务注册层（Registry）：封装服务地址的注册和发现，以服务URL为中心，扩展接口为RegistryFactory、Registry、RegistryService；
* 路由层（Cluster）：封装多个提供者的路由和负载均衡，并桥接注册中心，以Invoker为中心，扩展接口为Cluster、Directory、Router 和 LoadBlancce；
* 监控层（Monitor）：RPC调用次数和调用时间监控，以Statistics为中心，扩展接口为MonitorFactory、Monitor 和 MonitorService；
* 远程调用层（Protocol）：封装RPC调用，以Invocation和Result为中心，扩展接口为Protocol、Invoker 和 Exporter；
* 信息交换层（Exchange）：封装请求响应模式，同步转异步，以Request和Response为中心，扩展接口为Exchanger、ExchangeChannel、ExchangeClient 和 ExchangeServer；
* 网络传输层（Transport）：抽象mina和netty为统一接口，以Message为中心扩展接口为Channel、Transporter、Client、Server 和 Codec；
* 数据序列化层（Serialize）：可复用的一些工具，扩展接口为Serialization、ObjectInput、ObjectOutput 和 ThreadPool。
## dubbo集群容错方案
* Failover Cluster：失败自动切换，自动重试其他服务（默认）；通常用于读操作，但重试会带来更长延迟。
* Failfast Cluster：快速失败，立即报错，只发起一次调用；通常用于非幂等的写操作，比如新增操作。
* Failsafe Cluster：失败安全，出现异常时，直接忽略；通常用于写入审计日志等操作。
* Failback Cluster：失败自动恢复，记录失败请求，定时重发；通常用于消息通知等操作。
* Forking Cluster：并行调用多个服务器，只要一个成功即可返回；通常用于实时性要求较高的读操作，但需要浪费更多服务资源。可通过forks=2设置最大并行数。
* Broadcast Cluster：广播逐个调用所有提供者，任意一个报错则报错。通常用于通知所有提供者更新缓存或日志等本地资源信息。
### dubbo结主要构和功能
Registry、Consumer、Provider、Container、Monitor
* container负责启动、加载、运行provider；
* provider启动时，向registry注册自己的服务（增加服务节点）；
* consumer启动时，向registry订阅自己的服务；
* registry提供provider列表给consumer，实时推送变动情况；
* consumer根据provider列表，按负载算法选一台provider调用；
* monitor统计rpc的调用频次。
## Dubbo Monitor实现原理
Consumer会在发起调用前先走filter链；provider端在接收到请求时也先走filter链，然后才进行真正的业务逻辑处理。  
默认情况下，在consumer和provider的filter链中都会有Monitorfilter。
1. MonitorFilter向DubboMonitor发送数据；
2. DubboMonitor将数据进行聚合后（默认聚合1min的统计数据）暂存到ConcurrentMap<Statistics, AtomicReference> statisticsMap，然后使用一个含有3个线程的线程池每个1min调用SimpleMonitorService遍历发送statisticsMap中的统计数据，每发送一个，就重置当前的statistics的AtomicReference；
3. SimpleMonitorService将这些聚合数据塞入BlockingQueue中（队列大小为100000）；
4. SimpleMonitorService使用一个后台线程将阻塞queue中的数据写入文件；
5. SimpleMonitorService还会使用一个含有一个线程的线程池每隔5min将文件中的统计数据画成图表。
## Dubbo配置文件如何加载到Spring中
Spring容器在启动时，会读取到Spring默认的一些schema以及Dubbo自定义的schema，每个schema都会对应一个自己的NamespaceHandler，NamespaceHandler里面通过BeanDefinitionParser来解析配置信息并转化为需要加载的bean对象。
## Dubbo超时设置
* 服务提供者端设置超时时间，在Dubbo的用户文档中，推荐如果能够在服务端多配置就尽量多配置，因为服务提供者比消费者更清楚自己提供的服务特性；
* 服务消费者端设置超时时间，如果在消费者端设置了超时时间，以消费者端为主，即优先级更高。因为服务调用方设置超时时间控制性更灵活。如果消费方超时，服务端线程不会定制，会产生警告。
> dubbo在服务调用不成功时，默认会重试两次。
## 通信协议
dubbo默认使用Netty作为通讯架构。
### Dubbo支持的协议
* Dubbo：单一长连接和NIO异步通讯，适合大并发小数据量的服务调用，以及消费者远大于提供者。传输协议TCP，异步Hessian序列化。Dubbo推荐使用dubbo协议。
* RMI：采用JDK标准的RMI协议实现，传输参数和返回参数对象需要实现Serializable接口，使用Java标准序列化机制，使用阻塞式短连接，传输数据包大小混合，消费者和提供者个数差不多，可传文件，传输协议TCP。多个短连接TCP传输协议，同步传输，使用常规的远程服务调用和RMI互操作。
* WebService：基于WebService的远程调用协议，集成CXF实现，提供和原生WebService的互操作。多个短连接，基于HTTP传输，同步传输，适用于系统集成和跨语言调用。
* HTTP：基于HTTP表单提交的远程调用协议，使用Spring的HttpInvoke实现。多个短连接，传输协议HTTP，传入参数大小混合，提供者个数多于消费者，需要给应用程序和浏览器JS调用。
* Hessian：集成Hessian服务，基于HTTP通讯，采用Servlet暴露服务，Dubbo内嵌Jetty作为服务器时默认实现，提供与Hessian服务互操作。多个短连接，同步HTTP传输，Hessian序列化，传入参数较大，提供者大于消费者，提供者压力较大，可传输文件。
* Memcache：基于Memcache实现的RPC协议。
* Redis：基于Redis实现的RPC协议。
## 运维管理
### 服务上线怎么兼容旧版本
可以用版本号version过渡，多个不同版本的服务注册到注册中心，版本号不同的服务相互间不引用，和服务分组概念类似。
### telnet命令
dubbo服务发布之后，可以利用telnet命令进行调试、管理。Dubbo2.0.5以上版本服务提供端口支持telnet命令。
### Dubbo支持服务降级吗
可以通过dubbo:reference中设置mock="return null"。mock值也可修改为true，然后再跟接口同一个路径下实现一个Mock类，命名规则是"接口名称+Mock"后缀，然后在Mock类里实现自己的降级逻辑处理。
### Dubbo如何优雅停机
Dubbo是通过JDK的shutdownHook来停机的，所以如果执行kill -9 PID等强制关闭命令是不会执行优雅停机的，只有通过kill PID时才会执行。
## Dubbo SPI和Java SPI
### JDK SPI
JDK标准的SPI会一次性加载所有的扩展实现，如果有的扩展很耗时，但没用上就会很浪费资源。无法实现只加载某个实现。
### Dubbo SPI
* 对Dubbo进行扩展，不需要改动Dubbo源码；
* 延迟加载，可以一次只加载自己想要加载的扩展实现；
* 增加了扩展点IOC和AOP支持，一个扩展点可以直接setter注入其他扩展点；
* Dubbo的扩展机制能很好的支持第三方IOC容器，默认支持Spring Bean。 
## Dubbo扩展内容
### 支持分布式事务吗
暂不支持事务，可通过tcc-transaction框架实现。（tcc-transaction是开源的TCC补偿性分布式事务框架，通过dubbo隐式传参的功能避免对代码的侵入）
### Dubbo可以对结果进行缓存吗
为了提高数据访问速度，Dubbo提供了声明式缓存，以减少用户加缓存的工作量，<dubbo:reference cache=“true” />。
### Dubbo在安全方面的措施
通过Token令牌防止用户绕过注册中心直连，然后在注册中心上管理授权；还提供服务黑白名单功能，来控制服务所允许的调用方。
### 服务调用是阻塞的吗
默认是阻塞的，可以异步调用，没有返回值的可以这么做。Dubbo是基于NIO的非阻塞实现并行调用，客户端不需要启动多线程即可完成并行调用多个远程服务，相对多个线程开销较小，异步调用会返回一个Future对象。
### 服务提供者失效踢出原理
服务失效踢出基于zookeeper的临时节点原理
### 同一个服务多个注册的情况下是否可直连某个服务
可以点对点直连，修改配置即可，也可以通过telnet直连某个服务。
### Dubbo服务降级，失败重试怎么做
可以通过 dubbo:reference 中设置 mock=“return null”。mock 的值也可以修改为 true，然后再跟接口同一个路径下实现一个 Mock 类，命名规则是 “接口名称+Mock” 后缀。然后在 Mock 类里实现自己的降级逻辑。


项目部署中的dubbo:
* 一台应用服务器（application）内，既有对外提供服务（provider），也有依赖外部服务（consumer）；
* provider涉及：registry/protocol/service/method/provider；
* consumer涉及：registry/reference/method/consumer；
* 每台服务接口的信息，都会反映到monitor。以application的名称标识属于哪个应用。

dubbo标签：
> 1. \<dubbo:application/>：当前应用信息；
> 2. \<dubbo:registry/>：注册中心相关信息；
> 3. \<dubbo:provider/>：服务提供者信息，默认配置，一般不显式声明；
> 4. \<dubbo:protocol/>：配置提供服务的协议信息，消费方被动接受，有继承关系，沿用上层配置；
> 5. \<dubbo:service/>：暴露一个服务，定义服务的元信息；
> 6. \<dubbo:consumer/>：消费方配置，默认继承提供方配置；
> 7. \<dubbo:reference/>：引用服务配置，用于创建一个远程服务代理，会继承service和consumer；
> 8. \<dubbo:method/>：方法配置；
> 8. \<dubbo:argument/>：·方法参数配置。  
timeout、retries、loadbalance消费方未配置沿用服务提供方配置。


# RPC
RPC（Remote Procedure Call Protocol）远程过程调用协议，它是一种通过网络从远程计算机程序上请求服务，而不需要了解底层网络技术的协议。简言之，RPC使得程序能够像访问本地系统资源一样，去访问远端系统资源。
## 调用过程
1. 服务消费者（client客户端）通过调用本地服务的方式调用需要消费的服务；
2. 客户端存根（client stub）接收到调用请求后负责将方法、入参等信息序列化（组装）成能够进行网络传输的消息体；
3. 客户端存根（client stub）找到远程的服务地址，并且将消息通过网络发送给服务端；
4. 服务端存根（server stub）收到消息后进行解码（反序列化操作）；
5. 服务端存根（server stub）根据解码结果调用本地的服务进行相关处理；
6. 本地服务执行具体业务逻辑并将处理结果返回给服务端存根；
7. 服务端存根（server stub）将返回结果重新打包成消息（序列化）并通过网络发送给消费方；
8. 客户端存根（client stub）接收到消息进行解码（反序列化）；
9. 服务消费方得到最终结果。
## RPC、SOA、SOAP、REST的区别
* REST：可以看作是HTTP协议的一种直接应用，默认基于json作为传输格式，使用简单，学习成本低效率高但是安全性较低。
* SOAP：SOAP是一种数据交换协议规范，是一个轻量的、简单的、基于XML的协议的规范。SOAP协议可以简单地理解为：SOAP=RPC+HTTP+XML，即采用HTTP作为通信协议，RPC（Remote Procedure Call Protocol  远程过程调用协议）作为一致性的调用途径，XML作为数据传送的格式，从而允许服务提供者和服务客户经过防火墙在Internet上进行通信交互。
* SOA：面向服务架构，它可以根据需求通过网络对松散耦合的粗粒度应用组件进行分布式部署、组合和使用。服务层是SOA的基础，可以直接被应用调用，从而有效控制系统中与软件代理交互的人为依赖性。SOA是一种粗粒度、松耦合服务架构，服务之间通过简单、精确定义接口进行通讯，不涉及底层编程接口和通讯模型。SOA可以看作是B/S模型、XML/Web Service之后的自然延伸。
