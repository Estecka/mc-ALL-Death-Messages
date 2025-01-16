package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.entity.mob.MobEntity;

@Mixin(MobEntity.class)
public interface IMobEntityMixin 
{
	@Invoker boolean callIsDisallowedInPeaceful();
}
