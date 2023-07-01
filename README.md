# All Death Messages

Displays a death message for any and all mob deathes.

Messages can be enabled or disabled for specific categories of mobs, and their names in the feed can be styled with different colours and effects.  
Styles and categories can be customized in the config files. You can have look at the default config files [here](https://github.com/Estecka/mc-ALL-Death-Messages/tree/HEAD/src/main/resources/config).

----

## Message Triggers
Death messages are spread across several gamerules, based on the mob's category. Each category then has separate `kill` and `death` gamerules.

There is a couple of built-in categories, and custom ones can be created in the config based on entity types.
A mob may belong to multiple categories; it will trigger a death message if at least one of them has its rule enabled.

Custom categories are defined in `.minecraft/config/alldeath-rules.json`.  
Each key in the root object is used as a category name. The associated value is an array of strings, representing the entity types that can trigger the gamerule.

### Built-in trigger categories
- "named" regroups all mobs that have been given a custom name, including all players.
- "other" regroups mobs that do not belong in any category, including custom categories.

Those names mat not be used in the config to create custom categories.

----

## Name Styling

Mob names can be made to show different colours and styling in the chat log. Those styles may use the same categories as gamerules, but are otherwise independent from them. They can as well define anonymous categories, which do not match any gamerule.

When multiple styles match a mob, they will all be combined. Where properties conflict, the topmost style takes priority over the lower ones.  
Multiple styles can be created for the same category, allowing different style properties to have different priorities.


Styles are defined in the config file `.minecraft/config/alldeath-styles.json`, as an array of Json Object.  
Each object may contain the following properties:
- _(Mandatory)_ `rule`:  
	- If it an array of strings, it will be treated as a list of entity types.
	- If it is a string, it will be treated as the name of a gamerule or built-in category.   
      I'm expmerimenting with a new category system, so styling categories contain more built-in options than are available for gamerules (see below). These may become available as trigger categories in the future.
- _(Optional)_ `color`: A string. Can be either a hex code formatted as `#rrggbb`, or a built-in colour name.
- _(Optional)_ `bold`: A boolean
- _(Optional)_ `italic`: A boolean
- _(Optional)_ `underline`: A boolean
- _(Optional)_ `strikethrough`: A boolean
- _(Optional)_ `obfuscated`: A boolean

![Styles](./doc/Style.gif)  
![Colour Names](./doc/Colours.jpg)

### Built-in styling categories
- `all`: Applies to everything. Best used as the lowest priority, in order to provide a default style.
- `named`: Same as the "named" gamerule.
- `hostile`: Mob types treated as hostile by the game's code.
- `passive`: Mob types treated as passive by the game's code.
- `ephemeral`: Any mob that has the ability to despawn.
- `persistent`: The negation of "ephemeral"  

The "other" gamerule is not available as a styling category.

The game's code does not provide a very reliable way to tell which mob can and cannot despawn, so `persistent` and `ephemeral` may yield some unexpected results. In the future, I may need to handle a few exceptions for them to work as expected.

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
