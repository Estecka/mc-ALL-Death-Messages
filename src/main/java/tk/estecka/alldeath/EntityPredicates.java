package tk.estecka.alldeath;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import tk.estecka.alldeath.mixin.IMobEntityMixin;

public class EntityPredicates {
	static public final Map<String, Predicate<Entity>>	predicates = new LinkedHashMap<String, Predicate<Entity>>(8){{
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

	static public boolean	PLAYER(Entity e) { return e instanceof PlayerEntity; }
	static public boolean	NAMED(Entity e) { return e.hasCustomName(); }
	static public boolean	TAMED(Entity entity) { return entity instanceof Tameable tameable && tameable.getOwnerUuid() != null; }
	static public boolean	HOSTILE(Entity e) { return e instanceof MobEntity mob && ((IMobEntityMixin)mob).callIsDisallowedInPeaceful(); }
	static public boolean	PASSIVE(Entity e) { return !HOSTILE(e); }
	static public boolean	EPHEMERAL(Entity e) { return !PERSISTENT(e); }
	static public boolean	SEMIPERSISTENT(Entity e) { return e instanceof MobEntity mob && mob.cannotDespawn(); }

	static public boolean	PERSISTENT(Entity entity) {
		if (entity instanceof WitherEntity || entity instanceof EnderDragonEntity)
			return true;

		if (!(entity instanceof MobEntity mob))
			return false;

		return mob.isPersistent()
			//|| m.cannotDespawn() 
			|| !mob.canImmediatelyDespawn(Double.POSITIVE_INFINITY)
			;
	}

	static public	Predicate<Entity>	getOrDefault(String name){
		return predicates.getOrDefault(name, e->false);
	}

	static public	Predicate<Entity>	put(String name, Predicate<Entity> p){
		return predicates.put(name, p);
	}

}
