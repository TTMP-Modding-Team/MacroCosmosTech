package ttmp.macrocosmos.capability;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import ttmp.macrocosmos.util.Transaction;

import javax.annotation.Nullable;
import java.util.UUID;

public class CombinedPokemonContainer implements PokemonContainer{
	public static PokemonContainer create(PokemonContainer... containers){
		switch(containers.length){
			case 0:
				return EmptyPokemonContainer.EMPTY;
			case 1:
				return containers[0];
			default:
				return new CombinedPokemonContainer(containers);
		}
	}

	private final PokemonContainer[] containers;

	protected CombinedPokemonContainer(PokemonContainer... containers){
		this.containers = containers;
	}

	@Override public int size(){
		int sum = 0;
		for(PokemonContainer c : containers){
			sum += c.size();
		}
		return sum;
	}
	@Nullable @Override public Pokemon getPokemon(int index){
		for(int i = 0; index>=0&&i<containers.length; i++){
			PokemonContainer c = containers[i];
			if(index<c.size()) return c.getPokemon(index);
			index -= c.size();
		}
		return null;
	}
	@Nullable @Override public UUID getOwnerId(int index){
		for(int i = 0; index>=0&&i<containers.length; i++){
			PokemonContainer c = containers[i];
			if(index<c.size()) return c.getOwnerId(index);
			index -= c.size();
		}
		return null;
	}
	@Override public Transaction setPokemon(int index, @Nullable Pokemon pokemon, @Nullable UUID ownerId){
		for(int i = 0; index>=0&&i<containers.length; i++){
			PokemonContainer c = containers[i];
			if(index<c.size()) return c.setPokemon(index, pokemon, ownerId);
			index -= c.size();
		}
		return null;
	}

	@Override public boolean isValid(Pokemon pokemon, int index){
		for(int i = 0; index>=0&&i<containers.length; i++){
			PokemonContainer c = containers[i];
			if(index<c.size()) return c.isValid(pokemon, index);
			index -= c.size();
		}
		return false;
	}
}
