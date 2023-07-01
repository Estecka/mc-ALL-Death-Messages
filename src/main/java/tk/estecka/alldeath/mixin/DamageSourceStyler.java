package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import tk.estecka.alldeath.DeathStyles;

@Mixin(DamageSource.class)
public abstract class DamageSourceStyler 
{
	@Redirect(
		method = "getDeathMessage",
		at = @At(
			value = "INVOKE",
			target = "net/minecraft/entity/LivingEntity.getDisplayName()Lnet/minecraft/text/Text;"
		)
	)
	private Text	getLivingStyledName(LivingEntity entity){
		return DeathStyles.getStyledName(entity);
	}

	@Redirect(
		method = "getDeathMessage",
		at = @At(
			value = "INVOKE",
			target = "net/minecraft/entity/Entity.getDisplayName ()Lnet/minecraft/text/Text;"
		)
	)
	private Text	getStyledNamed(Entity entity){
		return DeathStyles.getStyledName(entity);
	}
}
