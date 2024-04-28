# Minecraft Code Breaking Changes
### 1.19.4
Current master

### 1.20 - 1.20.2
- **`ServerCommandSource::sendFeedback` takes a Text supplier instead of a Text.**
- `DamageRecord::getAttacker` was removed. No code change is required: use `DamageRecord::getDamageSource` instead.
- An additional LivingEntity name needs to be styled in `DamageTracker::getAttackedFallDeathMessage` and `DamageTracker::getFallDeathMessage`
- An additional Entity name needs to be styled in `DamageTracker::getDisplayName`
- `DamageRecord:getAttackerName` no longer needs to be styled in `DamageTracker::getDeathMessage`

### 1.20.3
- **`TextColor.parse` returns an Optional wrapped inside DataResult; instead of a straight up TextColor**
