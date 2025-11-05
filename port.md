# Minecraft Versions Breaking Changes

## 1.19.4
Initial Release

## 1.20 - 1.20.2
### No workaround:
- `ServerCommandSource::sendFeedback` takes a Text supplier instead of a Text.
- `DamageRecord::getAttacker` was removed. No code change is required: use `DamageRecord::getDamageSource` instead.
- An additional LivingEntity name needs to be styled in `DamageTracker::getAttackedFallDeathMessage` and `DamageTracker::getFallDeathMessage`
- An additional Entity name needs to be styled in `DamageTracker::getDisplayName`
- `DamageRecord::getAttackerName` no longer needs to be styled in `DamageTracker::getDeathMessage`
#### Worked around:
- `DamageRecord::getAttacker` was removed. Use `DamageRecord::getDamageSource` instead.

## 1.20.3
### Worked around:
- `TextColor.parse`'s return is now wrapped inside an Optional Wrapped inside a Dataresult. Use its codec instead.

## 1.20.5
### No Workaround:
- `DataResult` is now an interface instead of a class. No code change needed, but requires recompilation.

## 1.21.2
### Worked around:
- `World.getGamerules()` was moved to `ServerWorld`. Use `MinecraftServer.getGamerules()` instead.
- `ServerPlayerEntity` no longer implements `CommandOutput`, `sendMessage(Text)` is effectively a different method. Use `sendMessageToClient()` instead.

## 1.21.5
- `Tameable` no longer has a `getOwnerUUID` method.

## 1.21.9
- `Entity::getWorld` was replaced with `HeldItemContext::getEntityWorld`.
- `Entity::getServer` was removed.
- `MobEntity::isDisallowedInPeaceful` was replaced with `EntityType::isAllowedInPeaceful`.
