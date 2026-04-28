package tk.estecka.alldeath.mixin;

import tk.estecka.alldeath.AllDeathMessages;
import tk.estecka.alldeath.DeathRules;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Unique
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin 
{
	private static boolean	alldeath$ShouldHandleEntityDeath(LivingEntity entity){
		return !entity.level().isClientSide()
			&& DeathRules.IsRuleEnabled(entity, GameRules.SHOW_DEATH_MESSAGES)
			;
	}


	@Inject( method="die", at=@At("HEAD") )
	private void alldeath$TriggerMessage(CallbackInfo info) {
		final LivingEntity dyingEntity = (LivingEntity)(Object)this;
		final CombatTracker damages = dyingEntity.getCombatTracker();

		if (!alldeath$ShouldHandleEntityDeath(dyingEntity))
			return;

		Entity	rulingEntity = dyingEntity;
		LivingEntity owner = null;
		GameRule<Boolean> rule = DeathRules.HasDeathRule(dyingEntity);

		if (dyingEntity instanceof TamableAnimal tamedEntity)
			owner = tamedEntity.getOwner();

		if (rule == null) {
			for (CombatEntry dmg : ((IDamageTrackerMixin)damages).getEntries())
				if ((rulingEntity=dmg.source().getEntity()) != null && (rule=DeathRules.HasKillRule(rulingEntity)) != null)
					break;
		}

		if (rule != null){
			Component msg = damages.getDeathMessage();
			dyingEntity.level().getServer().sendSystemMessage(msg);
			for (ServerPlayer player : dyingEntity.level().getServer().getPlayerList().getPlayers())
			if  (player != owner)
				player.sendSystemMessage(msg, false);
			AllDeathMessages.LOGGER.info("Death message triggered by {} ({}) using rule {}", rulingEntity.getName().getString(), rulingEntity.getType(), rule);
		}
	}
}
