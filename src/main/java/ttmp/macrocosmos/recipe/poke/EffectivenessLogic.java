package ttmp.macrocosmos.recipe.poke;

import com.pixelmonmod.pixelmon.enums.EnumType;

import java.util.List;

@FunctionalInterface
public interface EffectivenessLogic{
	/**
	 * @return Effectiveness of the attack against provided type of pokemon.<br>
	 * Return value of 0 indicates the attack is completely nullified.<br>
	 * Negative return value indicates the attack is nullified and absorbed(by certain ability).
	 */
	float getEffectiveness(List<EnumType> types, EnumType attackType);
}
