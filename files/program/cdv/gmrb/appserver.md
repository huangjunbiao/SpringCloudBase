# App Server
&emsp;主要是直接给gmrb的APP客户端提供服务，包括内容、频道、流、评论和推送等服务。目前都在一个代码库中，根据功能业务划分了多个module，一个模块一般拆分为3部分，如gmrb-content、gmrb-content-api、gmrb-content-svc，将接口和实现进行了拆分。  
&emsp;除了一些必要的common等包的依赖之外，app server大多还依赖cms、authserver。同时还有些中间件等技术的依赖如rocket MQ、es等。
&emsp;