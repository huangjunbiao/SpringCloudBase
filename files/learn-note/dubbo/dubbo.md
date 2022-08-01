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
* Failover Cluster：失败自动切换，自动重试其他服务（默认）；
* Failfast Cluster：快速失败，立即报错，只发起一次调用；
* Failsafe Cluster：失败安全，出现异常时，直接忽略；
* Failback Cluster：失败自动恢复，记录失败请求，定时重发；
* Forking Cluster：并行调用多个服务器，只要一个成功即可返回；
* Broadcast Cluster：广播逐个调用所有提供者，任意一个报错则报错。
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
## Dubbo SPI和Java SPI
### JDK SPI
JDK标准的SPI会一次性加载所有的扩展实现，如果有的扩展很耗时，但没用上就会很浪费资源。无法实现只加载某个实现。
### Dubbo SPI
* 对Dubbo进行扩展，不需要改动Dubbo源码；
* 延迟加载，可以一次只加载自己想要加载的扩展实现；
* 增加了扩展点IOC和AOP支持，一个扩展点可以直接setter注入其他扩展点；
* Dubbo的扩展机制能很好的支持第三方IOC容器，默认支持Spring Bean。 


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
