# v1
## 1.0
Initial release

## 1.1
- Added separate gamerules for named/unnamed mob deathes/kills
- Vanilla `showDeathMessages` rule is now being respected
- Added multiplayer support

## 1.2
- _(MC1.20 onward)_ Kill rules now take all recent attackers into account, not just the biggest and the latest ones.


## 1.3
### 1.3.0
- Added the ability to create custom gamerules based on mob types.
### 1.3.1
- Fix default config using regular Guardian instead of Elder Guardians in the boss category.


## 1.4
- Added the ability to apply colours and styles to mob names

## 1.5
### 1.5.0
- Added the ability to see entity coordinates in the Advanced Tooltips.
- Added `persistent`, `ephemeral`, `hostile` and `passive` as built-in rules
- Removed the `other` rule, now replaced with `all`.
- Death messages for tamed entities are handled more reliably in multiplayer.
- Installed MixinExtras and refactored all mixins.
###	1.5.1
- Added `/alldeathmsg test` command to check a mob's categories.
- Jockeys, mobs in vehicles, and Endermen carrying blocks are no longer considered persistent.
- The Wither and the Ender Dragon are no longer considered ephemeral.
### 1.5.2
- Coordinate tooltip will also show the dimension type.
- Added `set`, `see-enabled` and `disable-all` commands.
- _Regression: Kill messages are controlled by death rules_
### 1.5.3
- Fixed Kill messages being wrongly associated with Death rules.
### 1.5.4
- Updated for MC 1.20.3
### 1.5.5
- Fixed text-siblings being stripped from mob names upon styling.
### 1.5.6
- Changed gamerule categories to better fit Cloth-Gamerules
### 1.5.7
- Updated for MC 1.20.5
### 1.5.8
- Updated for MC 1.21
### 1.5.9
- Updated for MC 1.21.2

## 1.6
### 1.6.0
- Players are no longer included in any built-in mob category.
- Added a dedicated "Player" category.
- Added a "Tamed" category, including both tameable pets and tameable mounts.
- Changed the definition of "Hostile" and "Passive". They are now based on whether the mob is allowed in Peaceful difficulty. "Passive" is now the complementary of "Hostile".
	- Phantoms, Slimes, and Magma Cubes are now considered hostile.
	- Squids, fish, and Snow Golems are now considered passive.
	- Piglins (except brutes) are now passive instead of hostile.
- "Utility" now includes snow golems.
- Added a `reload-styles` command.
### 1.6.1
- Updated for MC 1.21.5
### 1.6.2
- Updated for MC 1.21.6 and 1.21.9
### 1.6.3
- Marked as incompatible with MC 1.21.10
### 1.6.4
- Added translations for rules in the default config file.
- Added ja_jp language
