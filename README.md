# All Death Messages

## Overview
Enables death messages for any entity you want.

Specific categories of mobs can be defined in a config file, and their names in the messages can be styled with different colours and effects.

For each category, separate `kill` and `death` gamerules or generated.

## Message Triggers

There is a couple of built-in categories, and custom ones can be created in the config based on entity types.

A mob may belong to multiple categories; it will trigger a death message if at least one of them has its rule enabled.

### Built-in categories
- `all`: Applies to everything. When enabled, this effectively overule all other categories.
- `named`: Mobs that have been given a custom name, including all players.
- `persistent`: Mobs that will not naturally despawn.
- `ephemeral`: The negation of `persistent`
- `hostile`: Mobs treated as hostile by the game's code.
- `passive`: Mobs treated as passive by the game's code. This is _not_ the negation of `hostile; some mobs are neither hostile nor passive.

(The game's builtin way of checking whether a mob can despawn is quite unreliable, so `persistent` and `ephemeral` may yield some unexpected results. In the future, I will need to handle a few exceptions for them to work as expected.)

### Custom categories
Custom categories are defined in `.minecraft/config/alldeath-rules.json`.

Each key in the root object is used as a category name. The associated value is an array of strings, representing the entity types that can trigger the gamerule.

### Categories example
This example is provided as the default config file.
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
		"minecraft:elder_guardian",
		"minecraft:warden",
		"minecraft:wither"
	]
}
```

## Name Styling

Mob names in death messages can have different colours and styling applied. Those styles can be based on same categories used in as gamerules (both built-in and user-defined), but can also be independent from them. They can define anonymous categories which do not match any gamerule.

When multiple styles match a mob, they will all be combined. Where properties conflict, the topmost style takes priority over the lower ones
Multiple styles can be created for the same category, allowing different style properties to have different priorities.


Styles are defined in the config file `.minecraft/config/alldeath-styles.json`, as an array of Json Object.  
Each object may contain the following properties:
- _(Mandatory)_ `rule`:
	- If it is a string, it will be treated as the name of a gamerule.
	- If it is an array of strings, it will be treated as a list of entity types, similar to how gamerules are defined.
- _(Optional)_ `color`: A string. Can be either a hex code formatted as `#rrggbb`, or a built-in colour name.
- _(Optional)_ `bold`: A boolean
- _(Optional)_ `italic`: A boolean
- _(Optional)_ `underline`: A boolean
- _(Optional)_ `strikethrough`: A boolean
- _(Optional)_ `obfuscated`: A boolean

![Styles](./doc/Style.gif)  
![Colour Names](./doc/Colours.jpg)

### Styling Example
```json
[
	{
		"rule": [
			"minecraft:player"
		],
		"color": "yellow",
		"italic": false,
	},
	{
		"rule": "named",
		"italic": true,
		"underline": true
	}
]
```

The topmost style defines an anonymous category for entities of type "player". The bottom one refers to the built-in "named" category.

Players also belong to the "named" category, so both styles will be applied to a Player's name:  
The "italic" property is taken from the topmost style; player names will not be italicized.  
The topmost style does not define a "underline" property, so the lower property is used, and player names will be underlined.
