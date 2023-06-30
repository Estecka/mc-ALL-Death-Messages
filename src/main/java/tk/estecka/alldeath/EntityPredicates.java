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
		put("named",      EntityPredicates::NAMED     );
		put("hostile",    EntityPredicates::HOSTILE   );
		put("passive",    EntityPredicates::PASSIVE   );
		put("persistent", EntityPredicates::PERSISTENT);
	}};

	static public boolean	NAMED(Entity e){ return e.hasCustomName() || e.isPlayer(); }
	static public boolean	HOSTILE(Entity e) { return e instanceof HostileEntity; }
	static public boolean	PASSIVE(Entity e) { return e instanceof PassiveEntity; }
	static public boolean	PERSISTENT(Entity e) {
		MobEntity m = (MobEntity)e;
		return (e instanceof MobEntity) 
			&& (m.isPersistent() || m.cannotDespawn() || !m.canImmediatelyDespawn(Double.POSITIVE_INFINITY))
			;
	}

	static public	Predicate<Entity>	getOrDefault(String name){
		return predicates.getOrDefault(name, e -> false);
	}

	static public	Predicate<Entity>	put(String name, Predicate<Entity> p){
		return predicates.put(name, p);
	}

}
