package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.text.Text;
import tk.estecka.alldeath.DeathStyles;

@Unique
@Mixin(DamageTracker.class)
public abstract class DamageTrackerMixin 
{
	@WrapOperation(
		method={ "getDeathMessage", "getAttackedFallDeathMessage", "getFallDeathMessage" },
		at=@At( value="INVOKE", target="net/minecraft/entity/LivingEntity.getDisplayName()Lnet/minecraft/text/Text;" )
	)
	private Text	alldeath$getLivingStyledName(LivingEntity entity, Operation<Text> original){
		return DeathStyles.getStyledName(entity, original.call(entity));
	}

	@WrapOperation( method="getDisplayName", at=@At(value="INVOKE", target="net/minecraft/entity/Entity.getDisplayName ()Lnet/minecraft/text/Text;") )
	static private Text	alldeath$getStyledName(Entity entity, Operation<Text> original){
		return DeathStyles.getStyledName(entity, original.call(entity));
	}

}
