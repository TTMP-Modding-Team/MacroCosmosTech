package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class NotEgg implements PokemonCondition{
	public static final NotEgg NOT_EGG = new NotEgg();
	@Override public boolean test(Pokemon pokemon){
		return !pokemon.isEgg();
	}
}
