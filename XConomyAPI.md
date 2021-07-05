##About XConomyAPI

For XConomy 2.10.5

Chinese
```xml
XConomyAPI xcapi = new XConomyAPI;
```

```xml
xcapi.getversion()
```
获取XConomy版本号，返回String

```xml
xcapi.isbungeecordmode()
```
是否启用BC模式，返回boolean

```xml
xcapi.translateUUID(String playername)
```
将玩家名称转换为UUID，返回UUID  
如果玩家不存在，返回null

```xml
xcapi.formatdouble(String amount)
```
格式化amount，返回BigDecimal

```xml
xcapi.getdisplay(BigDecimal balance)
```
将金额转为显示消息，返回String  
比如 5,000 元

```xml
xcapi.getbalance(UUID uid)
```
获取玩家金额，返回BigDecimal

```xml
xcapi.ismaxnumber(BigDecimal amount)
```
检查金额是否为最大值，返回boolean

```xml
xcapi.changebalance(UUID u, String playername, BigDecimal amount, Boolean isadd)
```
对金额进行修改，返回Integer  
isadd = true 为增加金额  
isadd = false 为扣除金额  
isadd = null 为设置金额  
返回0 表示成功  
返回1 失败，表示BC模式且没有玩家存在  
返回2 失败,表示玩家余额不足  
返回3 失败,表示玩家余额超出最大值

```xml
xcapi.getbalancetop()
```
获取TOP10名单，返回List<String>

```xml
xcapi.getsumbalance()
```
获取服务器金额总数，返回BigDecimal

```xml    
xcapi.getglobalpermission(String permission)
```
获取全局权限状态，返回boolean  
eg: boolean value = xcapi.getglobalpermission("pay");

```xml
xcapi.setglobalpermission(String permission, boolean vaule)
```
设置全局权限状态  
eg: xcapi.setglobalpermission("pay", true);

```xml 
xcapi.getpaymentpermission(UUID uid)
```
获取玩家pay指令权限状态，返回Boolean
返回null表示无数据

```xml
xcapi.setglobalpermission(String permission, boolean vaule)
```
设置玩家pay指令权限状态
value为null时表示移除数据


****


English
```xml
XConomyAPI xcapi = new XConomyAPI;
```

```xml  
xcapi.getversion()
```
Gets the xconomy version number, return String

```xml  
xcapi.isbungeecordmode()
```
Check whether BC mode is enabled, return boolean

```xml
xcapi.translateUUID(String playername)
```
Convert the player name to UUID, return UUID  
If the player does not exist, return null

```xml
xcapi.formatdouble(String amount)
```
Format amount, return BigDecimal

```xml
xcapi.getdisplay(BigDecimal balance)
```
Convert the amount to display message, return String  
For example: 5,000 dollars

```xml
xcapi.getbalance(UUID uid)
```
Get player amount, return BigDecimal

```xml
xcapi.ismaxnumber(BigDecimal amount)
```
Check whether the amount is the maximum value, return boolean

```xml
xcapi.changebalance(UUID u, String playername, BigDecimal amount, Boolean isadd)
```
Modify the amount, return Integer  
isadd = true, add amount to balacne  
isadd = false, take amount from balance  
isadd = null, set amount to balance  
return 0 means success  
return 1 means failure that BungeeCord mode is enabled and no player is online  
return 2 means failure that the player's balance is insufficient  
return 3 means failure that the player's balance exceeds the maximum value

```xml
xcapi.getbalancetop()
```
Get the list of TOP10, return List<String>

```xml
xcapi.getsumbalance()
```
Get the total amount of the server, return BigDecimal

```xml    
xcapi.getglobalpermission(String permission)
```
Get global permission state, return boolean  
eg: boolean value = xcapi.getglobalpermission("pay");

```xml
xcapi.setglobalpermission(String permission, boolean vaule)
```
Set global permission state  
eg: xcapi.setglobalpermission("pay", true);

```xml 
xcapi.getpaymentpermission(UUID uid)
```
Get player pay command permission status, return Boolean  
Return null to indicate no data

```xml
xcapi.setglobalpermission(String permission, boolean vaule)
```
Set player pay command permission status  
When value is null, the data is removed