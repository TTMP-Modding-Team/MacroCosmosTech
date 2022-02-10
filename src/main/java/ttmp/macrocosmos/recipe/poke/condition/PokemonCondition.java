package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import ttmp.macrocosmos.combeekeeping.CombeeType;

public interface PokemonCondition{
	boolean test(Pokemon pokemon);

	static PokemonCondition all(PokemonCondition... conditions){
		return new All(conditions);
	}
	static PokemonCondition any(PokemonCondition... conditions){
		return new Any(conditions);
	}
	static PokemonCondition egg(){
		return Egg.IS_EGG;
	}
	static PokemonCondition not(PokemonCondition condition){
		return new Not(condition);
	}
	static PokemonCondition notEgg(){
		return NotEgg.NOT_EGG;
	}
	static PokemonCondition species(EnumSpecies species){
		return new Species(species);
	}
	static PokemonCondition type(EnumType type){
		return new Type(type);
	}
	static PokemonCondition vespiquen(CombeeType type){
		return new Vespiquen(type);
	}
}
