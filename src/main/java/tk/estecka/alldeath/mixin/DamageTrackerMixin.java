package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.text.Text;
import tk.estecka.alldeath.DeathStyles;

@Mixin(DamageTracker.class)
public abstract class DamageTrackerMixin 
{
	@WrapOperation( method="getDeathMessage", at=@At(value="INVOKE", target="net/minecraft/entity/LivingEntity.getDisplayName()Lnet/minecraft/text/Text;") )
	private Text	alldeath$getLivingStyledName(LivingEntity entity, Operation<Text> original){
		return DeathStyles.getStyledName(entity, original.call(entity));
	}

	@WrapOperation( method="getDeathMessage", at=@At(value="INVOKE", target="Lnet/minecraft/entity/damage/DamageRecord;getAttackerName ()Lnet/minecraft/text/Text;") )
	private Text	alldeath$getAttackerStyledName(DamageRecord record, Operation<Text> original){
		Text name = original.call(record);
		Entity atk = record.getAttacker();
		if (atk != null)
			name = DeathStyles.getStyledName(record.getAttacker(), original.call(record));
		return name;
	}
}
