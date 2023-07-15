# Playtime Limiter
This mod aims to limit the amount of time a player is allowed to play on a server.

## Commands
Check how much time you have left until you are kicked using:
``
/playtime left
``

Tell's the player it's total active time in the server:
``
/playtime total
``

**(OP)** Reset a player's timeout time back to the configured time using:
`` 
/playtime reset timeout {name of player here}
``

**(OP)** Reset a player's total time played to 0:
``
/playtime reset totalTime {name of player here}
``

**(OP)** Reset a player's total time played and timeout time:
``
/playtime reset all {name of player here}
``

*Note: the player needs to be online for it to be reset.*

## Configuration
Available configurations include:
- Playtime length
- Timeout length
- Should the player should be warned X minutes prior to being kicked
- Should the player have their timeout timer reset upon reconnecting
- Should the player timeout timer reset after midnight
- Should the timeout specified run parallel to the midnight reset timeout
- Enable or disable tracking total time played.

On normal circumstances, the default setup has midnight reset and time warnings active.
Default configuration file:
```toml
["Playtime Limiter configuration"]
#The length (IN SECONDS) which a player can play on your server before getting kicked with a timeout. Default is 3 hours
#Range: 1 ~ 604800
playtime_length = 10800
#The length (IN SECONDS) which a player has to wait, after being kicked from the server to be able to join again. Default is 12 hours. 
#Do not leave blank.
#Range: 1 ~ 604800
playtime_timeout = 43200
#If players should get warned before they get kicked. 
#They would get warned: {30, 15, 10, 5, 3, 1} Minute(s) before being Kicked!
playtime_warn_kick = true
#If the playtime should be reset after the Player Reconnects
playtime_reset_reconnect = false
#If enabled, omits the timeout set above. If the playtime timeout should be reset once the server detects that a new (IRL) day has started since the last time the player has joined
playtime_reset_midnight = true
#Allow a timeout to occur between the playtime and midnight reset.
#Example: If you have a timeout of 12 hours and Midnight Reset active, activating this option would compare which timeout is smaller and apply the shortest timeout.
#In this case, if we play at 4 am, the shortest timeout would be 4 pm. But if we play at 6 pm, the shortest timeout would be midnight reset.
#Default is false.
allow_timeout_with_midnight = false
#Enable or disable tracking the player's total play time.
track_total_playtime = true
```

## Original mod
This mod is a version port from the mod *Playtime Limiter* made by [@StunterLetsPlay](https://github.com/StunterLetsPlay) with quality of life updates (such as player time tracking) made by me.
Links to original mod:
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/playtime-limiter)
- [Mod in Github](https://github.com/StunterLetsPlay/Playtime_Limiter)