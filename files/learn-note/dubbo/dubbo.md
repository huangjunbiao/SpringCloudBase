# dubbo
Dubbo是一个分布式、高性能、透明化的RPC服务框架，能提供服务自动注册、自动发现等高效服务治理方案。功能主要包括：高性能的NIO通讯及多协议集成、服务动态寻址与路由、软负载均衡与容错、依赖分析与降级。
Rpc存在一些问题及需求
* rpc调用需要定制，会有额外工作量；
* 分布式服务中，服务数很多，相互之间调用复杂，相互依赖严重；
* 对集群的服务，需要负载策略；
* 对集群的服务，要能动态扩展节点。
## dubbo结构和功能
Registry、Consumer、Provider、Container、Monitor
* container负责启动、加载、运行provider；
* provider启动时，向registry注册自己的服务（增加服务节点）；
* consumer启动时，向registry订阅自己的服务；
* registry提供provider列表给consumer，实时推送变动情况；
* consumer根据provider列表，按负载算法选一台provider调用；
* monitor统计rpc的调用频次。  

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
