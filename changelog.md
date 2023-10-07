# v1
## 1.0
Initial release

## 1.1
- Added separate gamerules for named/unnamed mob deathes/kills
- Vanilla `showDeathMessages` rule is now being respected
- Added multiplayer support

## 1.2
- Kill rules now take all recent attackers into account, not just the biggest and the latest ones.


## 1.3
### 1.3.0
- Added the ability to create custom gamerules, regrouping mobs based on their type.
### 1.3.1
- Fix default config using regular Guardian instead of Elder Guardians in the boss category.


## 1.4
- Added the ability to apply colours and styles to the mob names

## 1.5
- Added the ability to see entity coordinates in the Advanced Tooltips.
- Added `persistent`, `ephemeral`, `hostile` and `passive` as built-in rules
- Removed the `other` rule, now replaced with `all`.
- Death messages for tamed entities are handled more reliably in multiplayer.
- Builtin gamerules may now be overwritten by the rules in the config.
- Installed MixinExtras and refactored all mixins.