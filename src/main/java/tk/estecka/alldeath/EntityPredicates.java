package tk.estecka.alldeath;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;

public class EntityPredicates {
	static public final Map<String, Predicate<Entity>>	predicates = new LinkedHashMap<String, Predicate<Entity>>(8){{
		put( "all", e->true );
		put( "named",      EntityPredicates::NAMED      );
		put( "persistent", EntityPredicates::PERSISTENT );
		put( "hostile",    EntityPredicates::HOSTILE    );
		put( "passive",    EntityPredicates::PASSIVE    );
		put( "ephemeral",  EntityPredicates::EPHEMERAL  );
	}};

	static public boolean	NAMED(Entity e) { return e.hasCustomName() || e.isPlayer(); }
	static public boolean	HOSTILE(Entity e) { return e instanceof HostileEntity; }
	static public boolean	PASSIVE(Entity e) { return e instanceof PassiveEntity; }
	static public boolean	EPHEMERAL(Entity e) { return !PERSISTENT(e); }

	static public boolean	PERSISTENT(Entity e) {
		if (!(e instanceof MobEntity))
			return false;

		MobEntity m = (MobEntity)e;
		return (m.isPersistent() || m.cannotDespawn() || !m.canImmediatelyDespawn(Double.POSITIVE_INFINITY));
	}

	static public	Predicate<Entity>	getOrDefault(String name){
		return predicates.getOrDefault(name, e->false);
	}

	static public	Predicate<Entity>	put(String name, Predicate<Entity> p){
		return predicates.put(name, p);
	}

}
