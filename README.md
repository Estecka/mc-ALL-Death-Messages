# All Death Messages

Displays a death message for any and all mob deathes.

Death messages are split across several gamerules, based on the mob's category. For each category, a separate "kill" and "death" gamerule is created.
A mob may belong to multiple categories; it will trigger a death message if at least one of them has its rule enabled.

The built-in category "named" regroups all mobs that have been given a custom name, including all players.  
The built-in category "other" regroups mobs that do not belong in any other category, including custom categories.  

Custom categories can be created to regroup entities based on their type. They are defined in the config file `.minecraft/config/AllDeathMessages-rules.json`.  
For example: 
```json
{
	"utility": [
		"minecraft:allay",
		"minecraft:iron_golem",
		"minecraft:villager",
		"minecraft:wandering_trader"
	],
	"boss": [
		"minecraft:ender_dragon",
		"minecraft:guardian",
		"minecraft:warden",
		"minecraft:wither"
	]
}
```

## Version

- Minecraft 1.20
- Fabric Loader 0.14.21 or above
- Fabric API 0.83.0 or above
