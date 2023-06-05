package net.fabricmc.alldeath.mixin;

import net.fabricmc.alldeath.AllDeathMessages;
import net.fabricmc.alldeath.DeathRules;
import net.fabricmc.alldeath.DeathRules.MobCategory;
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
	private static boolean	IsRuleEnabled(World world, GameRules.Key<BooleanRule> key){
		BooleanRule rule = world.getGameRules().get(key);
		if (rule != null)
			return rule.get();
		else {
			AllDeathMessages.LOGGER.error("The rule {} doesn't exist", key.getName());
			return false;
		}
	}

	private static boolean	ShouldHandleEntityDeath(LivingEntity entity){
		return !entity.world.isClient
			&& IsRuleEnabled(entity.world, GameRules.SHOW_DEATH_MESSAGES)
			&& !(entity instanceof TameableEntity && ((TameableEntity)entity).getOwner() != null)
			;
	}

	private static GameRules.Key<BooleanRule>	HasDeathRule(LivingEntity entity){
		for (DeathRules.MobCategory cat : DeathRules.GetCategories(entity))
			if (IsRuleEnabled(entity.world, cat.death))
				return cat.death;
		return null;
	}
	private static GameRules.Key<BooleanRule>	HasKillRule(Entity entity){
		if (entity == null)
			return null;
		for (DeathRules.MobCategory cat : DeathRules.GetCategories(entity))
			if (IsRuleEnabled(entity.world, cat.kill))
				return cat.kill;
		return null;
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

		GameRules.Key<BooleanRule> rule;
		if (null != (rule=HasDeathRule(dyingEntity))
		|| 	null != (rule=HasKillRule(killer))
		|| 	null != (rule=HasKillRule(assist))
		){
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(damages.getDeathMessage());
			AllDeathMessages.LOGGER.info("Death message triggered by rule {}", rule);
		}
	}
}
