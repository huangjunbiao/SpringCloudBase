# Elasticsearch
全文搜索引擎，底层是开源库Lucene，提供了 REST API 的操作接口，开箱即用。可以快速地储存、搜索和分析海量数据。
## 基本概念
### Node和Cluster
ES本质上是一个分布式数据库，允许多台服务器协同工作，每台服务器可以运行多个es实例。单个es实例称为一个节点node，一组节点构成一个集群cluster。
### Index
es会索引所有字段，经过处理后写入一个反向索引。查找数据时，直接查找该索引。所以es数据管理的顶层单位就叫做index索引。它是单个数据库的同义词。每个index(数据库)的名字必须是小写。
### Document
index里的单条记录称为document文档，许多条document构成一个index。document使用json格式表示。同一个index里的document不要求有相同的结构schema，但最好保持相同，这样有利于提高搜索效率。
### Type
document可以分组，不同分组就叫做type，它是虚拟的分组，用来过滤document。不同的type应该有相似的结构schema，如id不能在这个组是字符串，在其他组是数值。这是与关系型数据库的表的一个区别。性质完全不同的数据应该存成两个index，而不是一个index里的两个type（尽管可以）。根据规划，Elastic 6.x 版只允许每个 Index 包含一个 Type，7.x 版将会彻底移除 Type。