package tk.estecka.alldeath;

import java.util.function.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public class TypeEntityPredicate 
implements Predicate<Entity>
{
	private final String[] types;

	public TypeEntityPredicate(String[] typesIds){
		this.types = typesIds;
	}

	public boolean	test(Entity entity){
		for (var t : this.types)
			if (EntityType.getId(entity.getType()).toString().equals(t))
				return true;
		return false;
	}
	
	public boolean	IsEmpty(){
		return types.length <= 0;
	}
}
