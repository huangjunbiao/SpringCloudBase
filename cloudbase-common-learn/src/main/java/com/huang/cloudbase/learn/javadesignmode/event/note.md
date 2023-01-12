# 渠道账号
> com.cdv.qi.publisher.model.ChannelAccount
>
> 还需对应补充所属集团信息stationId、divisionId、groupId？，appId等渠道个性化数据到extraData

账号model的基础增删改查已有缺少的增加、findByChannel、findByGroupIdOrDivisionId(即在发布的时候应该是根据当前所处的集团或部门来查询出的渠道账号)--依据所属和渠道的联动账号查询;  

目前的渠道：三网、微信、微博、抖音、快手、B战、西瓜视频、知乎、百家号、报纸杂志APP。
快手、西瓜视频、B站需要看下官方开放文档确定对接方式  
抖音、B站、快手视频分片上传处理

# 渠道稿发布信息
> com.cdv.qi.publisher.model.CommonChannelTitleInfo
* new 新建状态
* publishing 发布中，调用publish成功即对应渠发布API调用成功
* publishingFailed 发布调用失败，调用publish失败即调用对应渠道发布API失败或异常（可重试?）
* publishingFinished 发布完成 即后续通过对应渠道API查询发布状态为审核中等?
* publishedFailed 发布失败 即后续通过对应渠道API查询发布状态为失败、审核未通过等情况
* publishedSuccessful 发布成功 即后续通过对应渠道API查询发布状态为成功

CommonChannelTitleInfo基础的增删改查已有缺少的增加，findPubDetail(获取发布详情)、  findByMoId(根据通稿id获取所有已发布的账号，与发布详情是否可以复用)

通稿下面关联的渠道稿id回填是否需要至通稿里？以及对应发布账号渠道稿的状态是否也回填？

发布库的通稿查询query：

> 发布时候多数账号会有授权token时效，若过期则再次重新获取相关信息从而继续发布；抖音好像比较特殊需要处理重新授权？
>
> 预计对应渠道真实的发布情况交由后台线程或定时任务去处理，只处理state=publishing的渠道稿，同时提供一个内部接口，用于每次看发布详情时查询一次暂未得到结果的渠道稿。
>
> 目前先处理publish.handle，
> 发布publish直接先更改状态后返回将调用API发布任务的处理交给其他线程或是同步/异步、并行/消息通知处理
## REST
1. save（前端还是后端处理）：发布中心稿件库里的保存，依据通稿以及渠道账号生成渠道稿和渠道发布数据，渠道发布数据初建后state为new 新建状态；
2. publish：发布到渠道，依据CommonChannelTitleInfo.channelAccountId发布到对应的渠道，只处理发布调用api，不处理后续实际发布状态查询。调用publish成功后，默认状态为publishing 发布中；
3. delete：删除，撤回后才允许删除，相应关联的渠道稿和CommonChannelTitleInfo均删除处理；
4. cancel：撤回，调用渠道API进行对应的撤回，部分渠道不支持；
5. queryPubDetail：查询发布详情，在查询时对publishing状态的调用第三方API查询真实状态并更新；
6. republish：重新发布，对于发布失败或调用发布失败的可重新发布，部分渠道不支持；

## 方法
* API方式的抽象调用的方法；
* 相同属性等的公共处理？
* 发布失败的处理
* 视频分片上传的处理


resttemplate调用切面统一处理调用接口返回的错误异常，格式化处理后交给publish相关处理错误信息到info表



* 虚线指向依赖；
* 实心三角实线表示继承，指向父类；
* 绿色三角虚线表示实现，指向被实现的接口；
* 实心菱形的白色实线表示聚合，菱形的一端为聚合的类；
* 泛化：空心箭头实线；
* 组合：实心菱形直线；