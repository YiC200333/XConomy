##About XConomyAPI

For XConomy 2.8.2

Chinese
```xml
XConomyAPI xcapi = new XConomyAPI;

xcapi.getversion()
获取XConomy版本号，返回String

xcapi.isbungeecordmode()
是否启用BC模式，返回Boolean

xcapi.translateUUID(String playername)
将玩家名称转换为UUID，返回UUID

xcapi.formatdouble(String amount)
格式化amount，返回BigDecimal

xcapi.getdisplay(BigDecimal balance)
将金额转为显示消息，返回String
比如 5,000 元

xcapi.getbalance(UUID uid)
获取玩家金额，返回BigDecimal

xcapi.ismaxnumber(BigDecimal amount)
检查金额是否为最大值，返回Boolean

xcapi.changebalance(UUID u, String playername, BigDecimal amount, Boolean isadd)
对金额进行修改，返回Integer
0 表示成功
1 表示BC模式且没有玩家存在，失败

xcapi.getbalancetop()
获取TOP10名单，返回List<String>

xcapi.getsumbalance()
获取服务器金额总数，返回BigDecimal
```

English
```xml
XConomyAPI xcapi = new XConomyAPI;

xcapi.getversion()
Gets the xconomy version number, return String

xcapi.isbungeecordmode()
Check whether BC mode is enabled, return Boolean

xcapi.translateUUID(String playername)
Convert the player name to UUID, return UUID

xcapi.formatdouble(String amount)
Format amount, return BigDecimal

xcapi.getdisplay(BigDecimal balance)
Convert the amount to display message, return String
For example: 5,000 dollars

xcapi.getbalance(UUID uid)
Get player amount, return BigDecimal

xcapi.ismaxnumber(BigDecimal amount)
Check whether the amount is the maximum value, return Boolean

xcapi.changebalance(UUID u, String playername, BigDecimal amount, Boolean isadd)
Modify the amount, return Integer
0 means success
1 means failure, BungeeCord mode is enabled and no player is online

xcapi.getbalancetop()
Get the list of TOP10, return List<String>

xcapi.getsumbalance()
Get the total amount of the server, return BigDecimal
```