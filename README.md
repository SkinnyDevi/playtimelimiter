# Playtime Limiter
This mod aims to limit the amount of time a player is allowed to play on a server.

*At the moment, it does not track how much time a player has played.*

## Commands
Check how much time you have left until you are kicked using:
``
/playtime left
``

**(OP)** Reset a player's timeout time back to the configured time using:
`` 
/playtime reset {name of player here}
``

*Note: the player needs to be online for it to be reset.*

## Configuration
Available configurations include:
- Playtime length
- Timeout length
- Should the player should be warned X minutes prior to being kicked
- Should the player have their timeout timer reset upon reconnecting
- Should the player timeout timer have the timer reset after midnight

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

```

## Original mod
This mod is a version and quality upgrade port from the mod *Playtime Limiter* made by @StunterLetsPlay.
Links to original mod:
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/playtime-limiter)
- [Github](https://github.com/StunterLetsPlay/Playtime_Limiter)
- [StunterLetsPlay (Original Author)](https://github.com/StunterLetsPlay)