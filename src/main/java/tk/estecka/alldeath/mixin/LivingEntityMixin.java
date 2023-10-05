package tk.estecka.alldeath.mixin;

import tk.estecka.alldeath.AllDeathMessages;
import tk.estecka.alldeath.DeathRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin 
{
	private static boolean	alldeath$ShouldHandleEntityDeath(LivingEntity entity){
		return !entity.getWorld().isClient()
			&& DeathRules.IsRuleEnabled(entity.getWorld(), GameRules.SHOW_DEATH_MESSAGES)
			;
	}


	@Inject( method="onDeath", at=@At("HEAD") )
	private void alldeath$TriggerMessage(CallbackInfo info) {
		final LivingEntity dyingEntity = (LivingEntity)(Object)this;
		final DamageTracker damages = dyingEntity.getDamageTracker();

		if (!alldeath$ShouldHandleEntityDeath(dyingEntity))
			return;

		Entity	rulingEntity = dyingEntity;
		LivingEntity owner = null;
		GameRules.Key<BooleanRule> rule = DeathRules.HasDeathRule(dyingEntity);

		if (dyingEntity instanceof TameableEntity tamedEntity)
			owner = tamedEntity.getOwner();

		if (rule == null) {
			for (DamageRecord dmg : ((IDamageTrackerMixin)damages).getRecentDamage())
				if ((rulingEntity=dmg.getAttacker()) != null && (rule=DeathRules.HasKillRule(rulingEntity)) != null)
					break;
		}

		if (rule != null){
			Text msg = damages.getDeathMessage();
			dyingEntity.getWorld().getServer().sendMessage(msg);
			for (ServerPlayerEntity player : dyingEntity.getWorld().getServer().getPlayerManager().getPlayerList())
			if  (player != owner)
				player.sendMessage(msg);
			AllDeathMessages.LOGGER.info("Death message triggered by {} ({}) using rule {}", rulingEntity.getName().getString(), rulingEntity.getType(), rule);
		}
	}
}
