package net.fabricmc.alldeath.mixin;

import net.fabricmc.alldeath.AllDeathMessages;
import net.fabricmc.alldeath.DeathRules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.GameRules.BooleanRule;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class Thanatos 
{
	private static boolean	ShouldHandleEntityDeath(LivingEntity entity){
		return !entity.world.isClient
			&& GetRule(entity.world, GameRules.SHOW_DEATH_MESSAGES)
			&& !(entity instanceof TameableEntity && ((TameableEntity)entity).getOwner() != null)
			;
	}

	private static boolean	HasDeathRule(LivingEntity entity){
		return (GetRule(entity.world, DeathRules.NAMED_DEATH) && entity.hasCustomName())
			|| (GetRule(entity.world, DeathRules.OTHER_DEATH))
			;
	}
	private static boolean	HasKillRule(Entity entity){
		if (entity == null)
			return false;
		return (GetRule(entity.world, DeathRules.NAMED_KILL) && (entity.hasCustomName() || entity.isPlayer()))
			|| (GetRule(entity.world, DeathRules.OTHER_KILL))
			;
	}

	private static boolean	GetRule(World world, GameRules.Key<BooleanRule> key){
		BooleanRule rule = world.getGameRules().get(key);
		if (rule != null)
			return rule.get();
		else {
			AllDeathMessages.LOGGER.error("The rule {} doesn't exist", key.getName());
			return false;
		}
	}


	@Inject(
		method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V",
		at = @At("HEAD")
	)
	private void InterceptDeathMessage(CallbackInfo info) {
		LivingEntity dyingEntity = (LivingEntity)(Object)this;

		if (!ShouldHandleEntityDeath(dyingEntity))
			return;

		DamageTracker damages = dyingEntity.getDamageTracker();
		Entity killer = damages.getMostRecentDamage().getAttacker();
		LivingEntity assist = damages.getBiggestAttacker();
		if (HasDeathRule(dyingEntity)
		||  HasKillRule(killer)
		||  HasKillRule(assist)
		){
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(damages.getDeathMessage());
		}
	}
}
