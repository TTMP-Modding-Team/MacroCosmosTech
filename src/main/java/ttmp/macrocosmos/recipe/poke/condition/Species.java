package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class Species implements PokemonCondition{
	private final EnumSpecies species;
	public Species(EnumSpecies species){
		this.species = species;
	}
	@Override public boolean test(Pokemon pokemon){
		return pokemon.getSpecies()==species;
	}
}
