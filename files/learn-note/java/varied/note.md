### 三目运算符注意的问题    
condition?表达式1:表达式2，表达式1和2在类型对齐时可能会抛出因自动拆箱导致的NPE异常。  
场景：表达式1或2的值有一个是原始类型；表达式1或2的值的类型不一致，会强制拆箱升级成表示范围更大的类型。

restTemplate默认post等方法会对url做encode，对某些带参数或验签等地址会存在问题，建议直接使用exchange。

没有收到消息如何排查？
生产者发送了一条消费者收到重复的如何排查？
字符集？统计字符长度？
线程阻塞或程序死了如何排查？
mysql索引排查，优化，存哪些类型数据？


https://www.jianshu.com/p/629672c41115
gradle项目Initialization script运行异常，.idea下gradle.xml修改<option name="delegatedBuild" value="false" />

https://xuliugen.blog.csdn.net/article/details/52702659?spm=1001.2101.3001.6650.14&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-14-52702659-blog-119042064.235%5Ev27%5Epc_relevant_3mothn_strategy_recovery&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-14-52702659-blog-119042064.235%5Ev27%5Epc_relevant_3mothn_strategy_recovery&utm_relevant_index=21
nacos clientWorker get changed dataId error, code: 404。spring在发送请求时会自动在nacos上拼接nacos后缀，因此不用加后缀不然会找不到；并且dubbo使用nacos注册中心，一般情况为内网访问调用。

https://blog.csdn.net/m0_51269961/article/details/123709195
git拉取代码报错信息：OpenSSL SSL_read: Connection was reset, errno 10054，检查邮箱和用户名，接触SSL认证：git config --global http.sslVerify "false"。并且刷新DNS缓存：ipconfig /flushdns。
      
