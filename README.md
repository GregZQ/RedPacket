### 简介  

1。该项目实现用户增删改查，同时实现用户数据缓存  
2。实现核心功能抢红包   

```
抢红包流程 : 
1.redis限流进入
2。分布式锁获取红包service,防止用户重复提交
3。当能进入抢红包服务后，进行抢红包功能  
    先判断当前用户是否抢过红包（缓存） 
    判断红包是否还存在
    两个条件都满足表示可以抢红包，将红包内容记录到缓存中。 
    并通过事件监听更新用户信息，红包日志，红包内容
    
```  


### 目的 


学习redis的实际应用