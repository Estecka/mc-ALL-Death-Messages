package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.text.Text;
import tk.estecka.alldeath.DeathStyles;

@Mixin(DamageTracker.class)
public abstract class DamageTrackerStyler 
{
	@Redirect(
		method = {
			"getDeathMessage",
			"getAttackedFallDeathMessage",
			"getFallDeathMessage"
		},
		at = @At(
			value = "INVOKE",
			target= "net/minecraft/entity/LivingEntity.getDisplayName ()Lnet/minecraft/text/Text;"
		)
	)
	private Text	getLivingStyledName(LivingEntity entity){
		return DeathStyles.getStyledName(entity);
	}

	@Redirect(
		method = {
			"getDisplayName",
		},
		at = @At(
			value = "INVOKE",
			target= "net/minecraft/entity/Entity.getDisplayName ()Lnet/minecraft/text/Text;"
		)
	)
	static private Text	getStyledName(Entity entity){
		return DeathStyles.getStyledName(entity);
	}

	// Needed in 1.19.4, but not 1.20
	// @Redirect(
	// 	method = "getDeathMessage",
	// 	at = @At(
	// 		value = "INVOKE",
	// 		target= "Lnet/minecraft/entity/damage/DamageRecord;getAttackerName ()Lnet/minecraft/text/Text;"
	// 	)
	// )
	// private Text	getAttackerStyledName(DamageRecord record){
	// 	Entity atk = record.damageSource().getAttacker();
	// 	if (atk != null)
	// 		return DeathStyles.getStyledName(record.damageSource().getAttacker());
	// 	else
	// 		return null;
	// }
}
