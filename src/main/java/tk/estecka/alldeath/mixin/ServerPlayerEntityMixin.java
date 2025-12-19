package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRules;
import tk.estecka.alldeath.AllDeathMessages;
import tk.estecka.alldeath.DeathRules;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin
{
	@Unique static private final GameRule<Boolean> PLAYER_RULE = DeathRules.nameToRule.get("player").death;

	@WrapOperation(
		method = "onDeath",
		at = @At(
			value = "INVOKE",
			ordinal = 0,
			target = "net/minecraft/world/rule/GameRules.getValue(Lnet/minecraft/world/rule/GameRule;)Ljava/lang/Object;"
		)
	)
	private Object CheckPlayerDeathMessages(GameRules rules, GameRule<Boolean> key, Operation<Boolean> original){
		boolean result = original.call(rules, key);
		if (!result)
			; //no-op
		else if (!key.equals(GameRules.SHOW_DEATH_MESSAGES))
			AllDeathMessages.LOGGER.error("Invalid mixin injection point for the Player Death Message gamerule.");
		else
			result &= rules.getValue(PLAYER_RULE);

		return (Boolean)result;
	}
}
