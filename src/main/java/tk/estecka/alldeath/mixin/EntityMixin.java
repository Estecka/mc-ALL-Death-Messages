package tk.estecka.alldeath.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tk.estecka.alldeath.AllDeathMessages;
import tk.estecka.alldeath.DeathRules;

@Unique
@Mixin(Entity.class)
public abstract class EntityMixin 
{
	@ModifyArg( method="createHoverEvent", index=2, at=@At(value="INVOKE", target="net/minecraft/network/chat/HoverEvent$EntityTooltipInfo.<init>(Lnet/minecraft/world/entity/EntityType;Ljava/util/UUID;Lnet/minecraft/network/chat/Component;)V"))
	private Component	alldeath$PosInsertion(Component entityName){
		Entity entity = (Entity)(Object)this;
		if (entity.level().isClientSide() || !DeathRules.IsRuleEnabled(entity, AllDeathMessages.COORD_RULE))
			return entityName;

		BlockPos pos = entity.blockPosition();
		var world = entity.level().dimension().identifier();
		MutableComponent addendum = Component.translatableWithFallback("gui.alldeath.entity_tooltip.location", "(%s) in %s", pos.toShortString(), world.toString());
			
		MutableComponent text = Component.empty();
		text.append(entityName);
		text.append(" ");
		text.append(addendum.withStyle(ChatFormatting.GRAY));

		return text;
	}
	
}
