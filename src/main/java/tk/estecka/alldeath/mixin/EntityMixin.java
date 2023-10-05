package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.minecraft.entity.Entity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import tk.estecka.alldeath.AllDeathMessages;

@Mixin(Entity.class)
public abstract class EntityMixin 
{
	@ModifyArg( method="getHoverEvent", index=2, at=@At(value="INVOKE", target="net/minecraft/text/HoverEvent$EntityContent.<init> (Lnet/minecraft/entity/EntityType;Ljava/util/UUID;Lnet/minecraft/text/Text;)V"))
	Text	alldeath$PosInsertion(Text entityName){
		Entity entity = (Entity)(Object)this;
		if (!entity.getWorld().getGameRules().getBoolean(AllDeathMessages.COORD_RULE))
			return entityName;

		MutableText text = Text.empty();
		BlockPos pos = entity.getBlockPos();

		text.append(entityName);
		text.append(
			Text.literal(String.format(" (%s)", pos.toShortString()))
			    .formatted(Formatting.GRAY)
		);

		return text;
	}
	
}
