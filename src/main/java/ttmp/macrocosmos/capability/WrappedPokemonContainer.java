package ttmp.macrocosmos.capability;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import ttmp.macrocosmos.util.Transaction;

import javax.annotation.Nullable;
import java.util.UUID;

public class WrappedPokemonContainer implements PokemonContainer{
	private final PokemonContainer delegate;

	public WrappedPokemonContainer(PokemonContainer delegate){
		this.delegate = delegate;
	}

	@Override public int size(){
		return delegate.size();
	}
	@Override @Nullable public Pokemon getPokemon(int index){
		return delegate.getPokemon(index);
	}
	@Override @Nullable public UUID getOwnerId(int index){
		return delegate.getOwnerId(index);
	}
	@Override public Transaction setPokemon(int index, @Nullable Pokemon pokemon, @Nullable UUID ownerId){
		return pokemon==null||isValid(pokemon, index) ? delegate.setPokemon(index, pokemon, ownerId) : Transaction.fail();
	}
	@Override public boolean isValid(Pokemon pokemon, int index){
		return delegate.isValid(pokemon, index);
	}
}
