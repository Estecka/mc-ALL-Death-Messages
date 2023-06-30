package tk.estecka.alldeath;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;

public class EntityPredicates {
	static public final Map<String, Predicate<Entity>>	predicates = new HashMap<String, Predicate<Entity>>(){{
		put("named", e -> e.hasCustomName() || e.isPlayer());
		put("hostile", e -> e instanceof HostileEntity);
		put("passive", e -> e instanceof PassiveEntity);
		put("persistent", e -> {
			MobEntity m = (MobEntity)e;
			return (e instanceof MobEntity) 
			    && (m.isPersistent() || m.cannotDespawn() || !m.canImmediatelyDespawn(Double.POSITIVE_INFINITY))
			    ;
		});
	}};

}
