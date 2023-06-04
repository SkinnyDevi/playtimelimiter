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
- Should the player timeout timer have the timer reset after midnight
- Enable or disable tracking total time played.

On normal circumstances, the default setup has midnight reset and time warnings active.
Default configuration file:
```toml
["Playtime Limiter configuration"]
#The Length (IN SECONDS) which a Player can play on your Server before getting Kicked with a Timeout. Default is 3 Hours
#Range: 1 ~ 604800
playtime_length = 10800
#The Length (IN SECONDS) which a Player has to wait, after being kicked from the server to be able to join again. Default is 12 Hours
#Range: 1 ~ 604800
playtime_timeout = 43200
#If Players should get warned before they get kicked.
#They would get warned: {30, 15, 10, 5, 3, 1} Minute(s) before being Kicked!
playtime_warn_kick = true
#If the Playtime should be reset after the Player Reconnects
playtime_reset_reconnect = false
#If the Playtime should be reset after once the Server detects that a new (IRL) Day has started since the last time the Player has joined
playtime_reset_midnight = true
#Enable or disable tracking the player's total play time.
track_total_playtime = true
```

## Original mod
This mod is a version port from the mod *Playtime Limiter* made by [@StunterLetsPlay](https://github.com/StunterLetsPlay) with quality of life updates (such as player time tracking) made by me.
Links to original mod:
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/playtime-limiter)
- [Mod in Github](https://github.com/StunterLetsPlay/Playtime_Limiter)