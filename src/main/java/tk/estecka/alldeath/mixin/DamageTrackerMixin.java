package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import tk.estecka.alldeath.DeathStyles;

@Unique
@Mixin(CombatTracker.class)
public abstract class DamageTrackerMixin 
{
	@WrapOperation(
		method={ "getDeathMessage", "getMessageForAssistedFall", "getFallMessage" },
		at=@At( value="INVOKE", target="net/minecraft/world/entity/LivingEntity.getDisplayName()Lnet/minecraft/network/chat/Component;" )
	)
	private Component	alldeath$getLivingStyledName(LivingEntity entity, Operation<Component> original){
		return DeathStyles.getStyledName(entity, original.call(entity));
	}

	@WrapOperation( method="getDisplayName", at=@At(value="INVOKE", target="net/minecraft/world/entity/Entity.getDisplayName()Lnet/minecraft/network/chat/Component;") )
	static private Component	alldeath$getStyledName(Entity entity, Operation<Component> original){
		return DeathStyles.getStyledName(entity, original.call(entity));
	}

}
