package tk.estecka.alldeath;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class DamageStyler 
{
	public static Text	getStyledName(LivingEntity entity)
	{
		AllDeathMessages.LOGGER.warn("Name of: {}", entity);
		Text name = entity.getDisplayName();
		Style style = name.getStyle().withUnderline(true).withColor(0xff0000).withItalic(true);

		return MutableText.of(name.getContent()).setStyle(style);
	}

	public static Text	getStyledName(Entity entity){
		if (entity instanceof LivingEntity)
			return DamageStyler.getStyledName((LivingEntity)entity);
		else if (entity != null)
			return entity.getDisplayName();
		else
			return null;
	}
}
