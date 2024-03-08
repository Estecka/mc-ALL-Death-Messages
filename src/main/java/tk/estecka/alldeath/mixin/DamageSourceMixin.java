package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import tk.estecka.alldeath.DeathStyles;

@Unique
@Mixin(DamageSource.class)
public abstract class DamageSourceMixin 
{
	@WrapOperation( method="getDeathMessage", at=@At( value="INVOKE", target="net/minecraft/entity/LivingEntity.getDisplayName ()Lnet/minecraft/text/Text;") )
	private Text	allDeath$getLivingStyledName(LivingEntity entity, Operation<Text> original){
		return DeathStyles.getStyledName(entity, original.call(entity));
	}

	@WrapOperation( method="getDeathMessage", at=@At(value="INVOKE", target="net/minecraft/entity/Entity.getDisplayName ()Lnet/minecraft/text/Text;") )
	private Text	alldeath$getStyledNamed(Entity entity, Operation<Text> original){
		return DeathStyles.getStyledName(entity, original.call(entity));
	}
}
