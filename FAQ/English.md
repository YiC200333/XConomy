![img.png](../img.png)
## FAQ
For 2.21 version
***
Q - How to sync data(BungeeCord Channel)?  
A - 1 Install XConomy on BungeeCord or Velocity (Does not support synchronizing data in modern mode of Velocity).  
&nbsp; &nbsp; &nbsp; 2 Set bungeecord to true in the spigot.yml.  
&nbsp; &nbsp; &nbsp; 3 Set SyncData.enable to true in the config.yml.  
&nbsp; &nbsp; &nbsp; 4 Set SyncData.channel-type to BungeeCord in the config.yml.  
&nbsp; &nbsp; &nbsp; 5 keep every configuration of XConomy is the same in your subserver. (It is recommended to copy the configuration file directly)

Q - How to sync data(Redis Channel)?  
A - 1 Set SyncData.enable to true in the config.yml.  
&nbsp; &nbsp; &nbsp; 2 Set SyncData.channel-type to Redis in the config.yml.  
&nbsp; &nbsp; &nbsp; 3 Complete Redis configuration in database.yml.  
&nbsp; &nbsp; &nbsp; 4 keep every configuration of XConomy is the same in your subserver. (It is recommended to copy the configuration file directly)

Q - What does bungeecord.sign mean in config.yml?  
A - Group the servers that need to synchronize data. Only servers with the same sign can synchronize data.

Q - I finished the synchronization configuration, but still failed to synchronize data.  
A - 1 If your server install other Bungeecord sync plugins, please set their Vault function to false.

Q - How to cancel the decimal of the balance?  
A - Set initial-bal to true in config.yml, but scientific counting still has decimals. (eg: 234561 -> 234.56K)

Q - I set the language in config.yml, but it still doesn't work.  
A - Delete the old message.yml file and regenerate it. 


***
### What are the advantages of different UUID-Mode?

1 - Default - Support most cases, get the player's UUID through database.  
2 - Online - Get player's UUID through the network. Better compatible with the requirement of changing player's name.  
3 - Offline - Get the player UUID not through the network or database, but this mode does not support ignoring case.  
4 - SemiOnline - Support one player to have multiple UUIDs. Get the player's UUID through database.

***

### About GeyserMC

1 - If you do not set auth-type: floodgate in the configuration of Geyser, please use the default UUID-mode of XConomy  
2 - If you use floodgate and set username-prefix to empty in the configuration of floodgate, please use the SemiOnline UUID-mode of XConomy, Otherwise, use default.
