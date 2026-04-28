package tk.estecka.alldeath;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;

public class EntityPredicates {
	static public final Map<String, Predicate<Entity>>	predicates = new LinkedHashMap<String, Predicate<Entity>>(11){{
		put( "all", e->true );
		put( "player",     EntityPredicates::PLAYER                );
		put( "named",      NonPlayer(EntityPredicates::NAMED)      );
		put( "tamed",      NonPlayer(EntityPredicates::TAMED)      );
		put( "persistent", NonPlayer(EntityPredicates::PERSISTENT) );
		put( "hostile",    NonPlayer(EntityPredicates::HOSTILE)    );
		put( "passive",    NonPlayer(EntityPredicates::PASSIVE)    );
		put( "ephemeral",  NonPlayer(EntityPredicates::EPHEMERAL)  );
		put( "semi-persistent",  NonPlayer(EntityPredicates::SEMIPERSISTENT));
	}};

	static private Predicate<Entity> NonPlayer(Predicate<Entity> base){
		return e -> !PLAYER(e) && base.test(e);
	}

	static public boolean	PLAYER(Entity e) { return e instanceof Player; }
	static public boolean	NAMED(Entity e) { return e.hasCustomName(); }
	static public boolean	TAMED(Entity entity) { return entity instanceof OwnableEntity tameable && tameable.getOwnerReference() != null; }
	static public boolean	HOSTILE(Entity e) { return e instanceof Mob mob && !mob.getType().isAllowedInPeaceful(); }
	static public boolean	PASSIVE(Entity e) { return !HOSTILE(e); }
	static public boolean	EPHEMERAL(Entity e) { return !PERSISTENT(e); }
	static public boolean	SEMIPERSISTENT(Entity e) { return e instanceof Mob mob && mob.requiresCustomPersistence(); }

	static public boolean	PERSISTENT(Entity entity) {
		if (entity instanceof WitherBoss || entity instanceof EnderDragon)
			return true;

		if (!(entity instanceof Mob mob))
			return false;

		return mob.isPersistenceRequired()
			//|| m.cannotDespawn() 
			|| !mob.removeWhenFarAway(Double.POSITIVE_INFINITY)
			;
	}

	static public	Predicate<Entity>	getOrDefault(String name){
		return predicates.getOrDefault(name, e->false);
	}

	static public	Predicate<Entity>	put(String name, Predicate<Entity> p){
		return predicates.put(name, p);
	}

}
