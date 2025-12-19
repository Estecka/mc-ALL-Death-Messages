package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Dynamic;
import net.minecraft.datafixer.fix.GameRuleRegistryFix;
import tk.estecka.alldeath.DeathRules;
import tk.estecka.alldeath.DeathRules.MobCategory;

@Mixin(GameRuleRegistryFix.class)
public abstract class GameRuleRegistryFixMixin
{
	@Shadow static private Dynamic<?> isTrue (Dynamic<?> dynamic) { throw new AssertionError(); }

	@ModifyReturnValue(
		method = "method_76071",
		at = @At("RETURN")
	)
	static private <T> Dynamic<T> RenameGamerules(Dynamic<T> original)
	{
		for (MobCategory cat : DeathRules.nameToRule.values()){
			String oldDeath = cat.death.getId().getPath().replace("show_death_messages", "showDeathMessages");
			String oldKill  = cat.kill .getId().getPath().replace("show_death_messages", "showDeathMessages");

			original = original
				.renameAndFixField(oldDeath, cat.death.getId().toString(), GameRuleRegistryFixMixin::isTrue)
				.renameAndFixField(oldKill , cat.kill .getId().toString(), GameRuleRegistryFixMixin::isTrue)
				;
		}

		return original
			.renameAndFixField("showDeathCoordinates", "alldeathmessages:show_death_coordinates", GameRuleRegistryFixMixin::isTrue)
			;
	}
}
