package tk.estecka.alldeath;

import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class TypeEntityPredicate 
implements Predicate<Entity>
{
	private final String[] types;

	public TypeEntityPredicate(String[] typesIds){
		this.types = typesIds;
	}

	public boolean	test(Entity entity){
		for (var t : this.types)
			if (EntityType.getKey(entity.getType()).toString().equals(t))
				return true;
		return false;
	}
	
	public boolean	IsEmpty(){
		return types.length <= 0;
	}
}
