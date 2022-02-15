package ttmp.macrocosmos.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;

import java.util.List;

public class PokeRecipeMatch{
	public static boolean test(List<PokemonCondition> conditions, PokemonContainer container){
		if(container.size()<conditions.size()) return false;
		if(container.size()>=Long.SIZE){
			// I don't fucking care. I bet this already will be enough for 97% of the use case
			throw new UnsupportedOperationException("Input too large");
		}
		LongArrayList incompleteConditions = new LongArrayList();

		// Match each pokemon input to each condition.
		long excludedPokemonFlag = 0;
		for(PokemonCondition condition : conditions){
			int count = 0;
			long pokemonFlag = 0;
			for(int i = 0; i<container.size(); i++){
				if(BitMask.get(excludedPokemonFlag, i)) continue;
				Pokemon pokemon = container.getPokemon(i);
				if(pokemon==null||!condition.test(pokemon)) continue;
				count++;
				pokemonFlag = BitMask.set(pokemonFlag, i, true);
			}
			switch(count){
				case 0: // input is invalid
					return false;
				case 1: // we don't have to track it, so we exclude it
					if(exclude(incompleteConditions, excludedPokemonFlag |= pokemonFlag))
						return false;
					break;
				default: // otherwise shove it into temp list and think about it later
					incompleteConditions.add(pokemonFlag);
					break;
			}
		}

		if(incompleteConditions.size()<=1) return true; // don't even have to enter brute force mode

		// Remove conditions that is positive for all pokemon, since there's no possibility of them being false positive
		long usedPokemon = 0;
		for(int i = 0; i<incompleteConditions.size(); i++)
			usedPokemon |= incompleteConditions.getLong(i);
		for(int i = 0; i<incompleteConditions.size(); i++){
			if(usedPokemon==incompleteConditions.getLong(i)){
				incompleteConditions.removeLong(i);
				i--;
			}
		}

		if(incompleteConditions.size()<=1) return true; // check again before (possibly) the most expensive part of the algorithm

		return bruteForce(incompleteConditions, 0, 0, container.size());
	}

	/**
	 * @return {@code true} if input is invalid
	 */
	private static boolean exclude(LongArrayList entries, long excluded){
		for(int i = 0; i<entries.size(); i++){
			long bits = entries.getLong(i);
			long modifiedBits = BitMask.removeAll(bits, excluded);
			if(modifiedBits!=bits){
				if(modifiedBits==0) return true; // input is invalid
				entries.set(i, modifiedBits);
			}
		}
		return false;
	}

	// no sundae
	private static boolean bruteForce(LongArrayList entries, int index, long usedPokemonFlags, int pokemonSize){
		if(entries.size()<=index) return true;
		long pokemon = entries.getLong(index);
		for(int i = 0; i<pokemonSize; i++)
			if(!BitMask.get(usedPokemonFlags, i)&& // not used in previous conditions
					BitMask.get(pokemon, i)&& // matches this condition
					bruteForce(entries, index+1, BitMask.set(usedPokemonFlags, i, true), pokemonSize)) // can match all the following conditions
				return true; // lfg
		return false;
	}
}
