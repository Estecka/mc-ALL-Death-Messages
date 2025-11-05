# All Death Messages

## Overview
Enables death messages for several categories of mobs, and add colours to their names in death messages. Messages use the same texts as vanilla death messages.

For each category, separate `kill` and `death` gamerules are generated. 

Custom mob categories must be defined in a config file. There is currently no config screen.


## Message Triggers

Built-in categories based on mob properties. Custom categories are based on entity types.

A death message will appear if its victim belongs to an enabled death rule, or if any of its recent assailant has an enabled kill rule.
A mob may belong to multiple categories; it will trigger a death message if any of them has its rule enabled.


### Built-in categories
- **`all`**: Applies to everything. When enabled, this overrules all other categories.
- **`player`**: Players are excluded from every other built-in categories below.
- **`named`**: Mobs that have been given a custom name.
- **`tamed`**: Includes both tamed pets and tamed mounts. Messages will still be broadcast to all players regardless of ownership.
- **`ephemeral`**/**`persistent`**: Mobs that will/won't naturally despawn.
- **`passive`**/**`hostile`**: Mobs that are allowed/disallowed in Peaceful difficulty.

### Custom categories
Custom categories are defined in `.minecraft/config/alldeath-rules.json`.

Each key in the root object is used as a category name. The associated value is an array of strings, representing the entity types that can trigger the gamerule.

This example is provided as the default config file:
```json
{
	"utility": [
		"minecraft:allay",
		"minecraft:iron_golem",
		"minecraft:snow_golem",
		"minecraft:villager",
		"minecraft:wandering_trader"
	],
	"boss": [
		"minecraft:ender_dragon",
		"minecraft:elder_guardian",
		"minecraft:warden",
		"minecraft:wither"
	]
}
```

## Name Styling

Mob names in death messages can have different colours and styling applied. Styles can be based on the same categories used in gamerules (both built-in and user-defined), or can be based on anonymous lists of entity types that don't correspond to any gamerule.

The way styles are applied is similar to CSS stylesheets, except in Json and in reverse order.
When multiple styles match a mob, they will all be combined, and the topmost style takes priority in case of conflicts.

Styles are defined in the config file `.minecraft/config/alldeath-styles.json`, as an array of Json Object.  
Each object may contain the following properties:
- _(Mandatory)_ **`rule`**: The mobs that will have the style applied
	- If it is a string, it will be treated as the name of a mob category. (Either built-in or custom.)
	- If it is an array of strings, it will be treated as a list of entity types.
- _(Optional)_ **`color`**: A string. Can be either a hex code formatted as `#rrggbb`, or a built-in colour name.
- _(Optional)_ **`bold`**: A boolean
- _(Optional)_ **`italic`**: A boolean
- _(Optional)_ **`underline`**: A boolean
- _(Optional)_ **`strikethrough`**: A boolean
- _(Optional)_ **`obfuscated`**: A boolean

![Styles](./doc/Style.gif)  
![Colour Names](./doc/Colours.jpg)

### Styling Example
```json
[
	{
		"rule": [
			"minecraft:wolf"
		],
		"color": "yellow",
		"italic": false
	},
	{
		"rule": "named",
		"italic": true,
		"underline": true
	}
]
```
#### Walkthrough
The topmost style defines an anonymous category for entities of type "wolf". The bottom one refers to the built-in "named" category.

If a wolf is given a name, both styles will be applied:  
The "italic" property is taken from the topmost style; its names will not be italicized.  
The topmost style does not define a "underline" property, so the lower property is used, and its name will be underlined.

![Example](./doc/Style%20example.png)

## Commands

`/alldeathmsg` is the root for all subcommands. It requires a permision level of at least 2.

- `test <entities>` Checks which rules apply to the given entities, and preview their styled names.
- `see-enabled` Lists all currently enabled rules
- `disable-all <confirm>` Disables all death message rules.
- `set <rule name> <rule type> <boolean>` Equivalent to the `gamerule` command, but with a more convenient auto-complete.
- `reload-styles` Reloads styles from the config file.
