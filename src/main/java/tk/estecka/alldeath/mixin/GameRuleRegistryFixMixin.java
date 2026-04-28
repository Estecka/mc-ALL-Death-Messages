package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.GameRuleRegistryFix;
import tk.estecka.alldeath.DeathRules;
import tk.estecka.alldeath.DeathRules.MobCategory;

@Mixin(GameRuleRegistryFix.class)
public abstract class GameRuleRegistryFixMixin
{
	@Shadow static private Dynamic<?> convertBoolean (Dynamic<?> dynamic) { throw new AssertionError(); }

	@ModifyReturnValue(
		method = "lambda$makeRule$2",
		at = @At("RETURN")
	)
	static private <T> Dynamic<T> RenameGamerules(Dynamic<T> original)
	{
		for (MobCategory cat : DeathRules.nameToRule.values()){
			String oldDeath = cat.death.getIdentifier().getPath().replace("show_death_messages", "showDeathMessages");
			String oldKill  = cat.kill .getIdentifier().getPath().replace("show_death_messages", "showDeathMessages");

			original = original
				.renameAndFixField(oldDeath, cat.death.getIdentifier().toString(), GameRuleRegistryFixMixin::convertBoolean)
				.renameAndFixField(oldKill , cat.kill .getIdentifier().toString(), GameRuleRegistryFixMixin::convertBoolean)
				;
		}

		return original
			.renameAndFixField("showDeathCoordinates", "alldeathmessages:show_death_coordinates", GameRuleRegistryFixMixin::convertBoolean)
			;
	}
}
