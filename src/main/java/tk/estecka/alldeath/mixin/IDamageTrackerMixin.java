package tk.estecka.alldeath.mixin;

import java.util.List;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.CombatTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CombatTracker.class)
public interface IDamageTrackerMixin
{
	@Accessor public List<CombatEntry>	getEntries();
}
