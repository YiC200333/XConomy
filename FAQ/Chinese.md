![img.png](../img.png)
## 中文社区文档  
Itz_Dr_Li 编写/维护   
https://xc.itzdrli.com/intro/readme
***
## FAQ
基于 2.21 版本
***
Q - 怎么同步数据(BungeeCord 通道)?  
A - 1 在 BungeeCord 或 Velocity 端安装 XConomy (Velocity 的 modern 模式不支持)。  
&nbsp; &nbsp; &nbsp; 2 在 spigot.yml 中设置 bungeecord 为 true。  
&nbsp; &nbsp; &nbsp; 3 在 config.yml 中设置 SyncData.enable 为 true。  
&nbsp; &nbsp; &nbsp; 4 在 config.yml 中设置 SyncData.channel-type 为 BungeeCord。  
&nbsp; &nbsp; &nbsp; 5 保证需要同步的子服务器中，XConomy的配置文件全部一样。 (推荐直接复制配置文件)

Q - 怎么同步数据(Redis 通道)?  
A - 1 在 config.yml 中设置 SyncData.enable 为 true。  
&nbsp; &nbsp; &nbsp; 2 在 config.yml 中设置 SyncData.channel-type 为 Redis。   
&nbsp; &nbsp; &nbsp; 3 在 database.yml 中完成 Redis 的配置。   
&nbsp; &nbsp; &nbsp; 4 保证需要同步的子服务器中，XConomy的配置文件全部一样。 (推荐直接复制配置文件)

Q - config.yml 中 bungeecord.sign 是什么意思?  
A - 对需要同步数据的服务器进行分组，只有sign相同的服务器才会同步数据。

Q - 我完成了同步配置，但仍然无法同步数据   
A - 如果你服务器有安装其他 BungeeCord 同步插件，请在安装本插件前，确认他们的 Vault 同步功能已关闭。

Q - 如何移除余额的小数？  
A - 在 config.yml 中设置 initial-bal 为 true, 但科学计数仍会有小数 (比如: 234561 -> 234.56K)

Q - 我在 config.yml 中设置了语言。但是不生效。  
A - 请删除旧的 message.yml 后，重新生成新的语言文件. 


***
### 不同的UUID模式分别有什么特点?

1 - Default - 支持大部分情况，从数据库中获取玩家的UUID.  
2 - Online - 通过网络获取玩家的UUID. 更好地兼容玩家会修改名称的需求.  
3 - Offline - 不通过数据库或网络获取玩家UUID, 所以响应更快，但此模式不支持忽略大小写.  
4 - SemiOnline - 支持一个玩家拥有多个UUID. 从数据库中获取玩家的UUID.

***

### 关于 GeyserMC

1 - 如果您没有在Geyser的配置中设置 auth-type 为 floodgate，请使用XConomy的默认UUID模式  
2 - 如果您使用 floodgate 并在 floodgat 配置中将用户名前缀设置为空，请使用XConomy的SemiOnline UUID模式，否则使用默认的UUID模式。
