package tk.estecka.alldeath.mixin;

import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.damage.DamageTracker;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DamageTracker.class)
public interface IDamageTrackerMixin {
	@Accessor public List<DamageRecord>	getRecentDamage();
}
