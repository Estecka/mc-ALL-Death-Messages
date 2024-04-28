# Minecraft Code Breaking Changes
### 1.19.4
Current master

### 1.20
#### No workaround:
- **`ServerCommandSource::sendFeedback` takes a Text Supplier instead of a Text.**
- An additional LivingEntity name needs to be styled in `DamageTracker::getAttackedFallDeathMessage` and `DamageTracker::getFallDeathMessage`
- An additional Entity name needs to be styled in `DamageTracker::getDisplayName`
- `DamageRecord::getAttackerName` no longer needs to be styled in `DamageTracker::getDeathMessage`
#### Worked around:
- `DamageRecord::getAttacker` was removed. Use `DamageRecord::getDamageSource` instead.

### 1.20.3
#### Worked around: 
- `TextColor.parse` now returns a DataResult Optional instead of a straight up TextColor. Use `TextColor.CODEC` instead.

### 1.20.5
#### No Workaround:
- `DataResult` is now an interface instead of a class. Requires re-compilation, but no code change.
