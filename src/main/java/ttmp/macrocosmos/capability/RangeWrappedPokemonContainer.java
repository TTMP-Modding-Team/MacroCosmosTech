package ttmp.macrocosmos.capability;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import ttmp.macrocosmos.util.Transaction;

import javax.annotation.Nullable;
import java.util.UUID;

public class RangeWrappedPokemonContainer implements PokemonContainer{
	private final PokemonContainer delegate;
	private final int startIndex;
	private final int size;

	public RangeWrappedPokemonContainer(PokemonContainer delegate, int startIndex, int size){
		if(startIndex<0) throw new IllegalArgumentException("startIndex < 0");
		if(size<0) throw new IllegalArgumentException("size < 0");
		this.delegate = delegate;
		this.startIndex = startIndex;
		this.size = size;
	}

	@Override public int size(){
		return size;
	}

	@Override @Nullable public Pokemon getPokemon(int index){
		if(index>=size||index<0) throw new IndexOutOfBoundsException(""+index);
		return delegate.getPokemon(startIndex+index);
	}
	@Override @Nullable public UUID getOwnerId(int index){
		if(index>=size||index<0) throw new IndexOutOfBoundsException(""+index);
		return delegate.getOwnerId(startIndex+index);
	}
	@Override public Transaction setPokemon(int index, @Nullable Pokemon pokemon, @Nullable UUID ownerId){
		return index<size&&index>=0&&(pokemon==null||isValid(pokemon, index)) ?
				delegate.setPokemon(startIndex+index, pokemon, ownerId) : Transaction.fail();
	}
	@Override public boolean isValid(Pokemon pokemon, int index){
		return index<size&&index>=0&&delegate.isValid(pokemon, index);
	}
}
