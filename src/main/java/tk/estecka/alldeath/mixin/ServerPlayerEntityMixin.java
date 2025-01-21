package tk.estecka.alldeath.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import tk.estecka.alldeath.AllDeathMessages;
import tk.estecka.alldeath.DeathRules;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin
{
	@Unique static private final GameRules.Key<BooleanRule> PLAYER_RULE = DeathRules.nameToRule.get("player").death;

	@WrapOperation(method="onDeath", at=@At(value="INVOKE", ordinal=0, target=" net/minecraft/world/GameRules.getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
	private boolean CheckPlayerDeathMessages(GameRules rules, GameRules.Key<BooleanRule> key, Operation<Boolean> original){
		boolean result = original.call(rules, key);
		if (!result)
			; //no-op
		else if (!key.equals(GameRules.SHOW_DEATH_MESSAGES))
			AllDeathMessages.LOGGER.error("Invalid mixin injection point for the Player Death Message gamerule.");
		else
			result &= rules.getBoolean(PLAYER_RULE);

		return result;
	}
}
