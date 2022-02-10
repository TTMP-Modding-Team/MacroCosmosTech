package ttmp.macrocosmos.capability;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import ttmp.macrocosmos.util.Transaction;

import javax.annotation.Nullable;
import java.util.UUID;

public class EmptyPokemonContainer implements PokemonContainer{
	public static final EmptyPokemonContainer EMPTY = new EmptyPokemonContainer();

	@Override public int size(){
		return 0;
	}
	@Nullable @Override public Pokemon getPokemon(int index){
		return null;
	}
	@Nullable @Override public UUID getOwnerId(int index){
		return null;
	}
	@Override public Transaction setPokemon(int index, @Nullable Pokemon pokemon, @Nullable UUID ownerId){
		return Transaction.fail();
	}
	@Override public boolean isValid(Pokemon pokemon, int index){
		return false;
	}
}
