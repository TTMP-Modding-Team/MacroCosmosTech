package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import ttmp.macrocosmos.combeekeeping.CombeeType;

import javax.annotation.Nullable;

public class Vespiquen implements PokemonCondition{
	@Nullable private final CombeeType combeeType;
	public Vespiquen(@Nullable CombeeType combeeType){
		this.combeeType = combeeType;
	}
	@Override public boolean test(Pokemon pokemon){
		return !pokemon.isEgg()&&pokemon.getSpecies()==EnumSpecies.Vespiquen&&
				(combeeType==null||combeeType==CombeeType.getCombeeType(pokemon));
	}
}
