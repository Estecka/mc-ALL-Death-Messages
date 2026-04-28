package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import tk.estecka.alldeath.AllDeathMessages;
import tk.estecka.alldeath.DeathRules;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin
{
	@Unique static private final GameRule<Boolean> PLAYER_RULE = DeathRules.nameToRule.get("player").death;

	@WrapOperation(
		method = "die",
		at = @At(
			value = "INVOKE",
			ordinal = 0,
			target = "net/minecraft/world/level/gamerules/GameRules.get(Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;"
		)
	)
	private Object CheckPlayerDeathMessages(GameRules rules, GameRule<Boolean> key, Operation<Boolean> original){
		boolean result = original.call(rules, key);
		if (!result)
			; //no-op
		else if (!key.equals(GameRules.SHOW_DEATH_MESSAGES))
			AllDeathMessages.LOGGER.error("Invalid mixin injection point for the Player Death Message gamerule.");
		else
			result &= rules.get(PLAYER_RULE);

		return (Boolean)result;
	}
}
