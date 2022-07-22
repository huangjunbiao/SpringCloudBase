### binlog日志
binlog是归档日志，属于MySQL server层的日志。可以实现主从复制和数据恢复两个作用。当需要数据恢复时，可以取出某个时间范围内的binlog进行重放恢复即可。
#### binlog日志格式
statement
* statement格式，binlog记录的是SQL原文，它可能会导致主库不一致（主库和从库选的索引不一样时）。假设主库执行删除的SQL（其中 a 和 create_time 都有索引）【delete from t where a > '666' and create_time<'2022-03-01' limit 1;】。数据选择了a索引和选择create_time索引，最后limit 1出来的数据一般不一样。所以就会存在：在binlog=statement格式时主库在执行这条SQL使用的是a索引，而从库在执行这条SQL时使用了B索引，最后主从数据不一致了。

row
* row格式可以解决上述问题，row格式binlog记录的不是SQL原文，而是两个event:Table_map和Delete_rows。Table_map event说明要操作的表，Delete_rows event说明要删除的行为，记录删除的具体行数。row格式binlog记录的就是要删除的主键ID信息，因此不会出现主从不一致的问题。但是如果SQL删除10万行数据，使用row格式会很占空间，写binlog也很耗IO。

mixed
* mixed格式可以解决上述问题。其实就是row和statement格式混合使用，当MySQL判断可能数据不一致时，就用row格式，否则就是用statement格式。