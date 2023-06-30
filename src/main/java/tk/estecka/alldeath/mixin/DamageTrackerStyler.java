package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.text.Text;
import tk.estecka.alldeath.DeathStyles;

@Mixin(DamageTracker.class)
public abstract class DamageTrackerStyler 
{
	@Redirect(
		method = "getDeathMessage",
		at = @At(
			value = "INVOKE",
			target= "net/minecraft/entity/LivingEntity.getDisplayName()Lnet/minecraft/text/Text;"
		)
	)
	private Text	getVictimeStyledName(LivingEntity entity){
		return DeathStyles.getStyledName(entity);
	}

	@Redirect(
		method = "getDeathMessage",
		at = @At(
			value = "INVOKE",
			target= "Lnet/minecraft/entity/damage/DamageRecord;getAttackerName ()Lnet/minecraft/text/Text;"
		)
	)
	private Text	getAttackerStyledName(DamageRecord record){
		return DeathStyles.getStyledName(record.getAttacker());
	}
}
