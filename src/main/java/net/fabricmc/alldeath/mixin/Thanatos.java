package net.fabricmc.alldeath.mixin;

import net.fabricmc.alldeath.AllDeathMessages;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class Thanatos {
	@Inject(
		method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V",
		at = @At("HEAD")
	)
	private void InterceptDeathMessage(CallbackInfo info) {
		LivingEntity dyingEntity = (LivingEntity)(Object)this;
		if (dyingEntity instanceof TameableEntity && ((TameableEntity)dyingEntity).getOwner() != null)
			return;

		Text message = dyingEntity.getDamageTracker().getDeathMessage();
		if (!dyingEntity.world.isClient)
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
	}
}
