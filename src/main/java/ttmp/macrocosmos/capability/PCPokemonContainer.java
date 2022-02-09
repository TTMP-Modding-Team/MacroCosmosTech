package ttmp.macrocosmos.capability;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import ttmp.macrocosmos.util.Transaction;

import javax.annotation.Nullable;
import java.util.UUID;

public class PCPokemonContainer implements PokemonContainer{
	private PCStorage pcStorage;
	private static final int SIZE = 30;

	public PCPokemonContainer(PCStorage pcStorage) {
		this.pcStorage = pcStorage;
	}

	@Override public int size(){
		return SIZE;
	}
	@Nullable @Override public Pokemon getPokemon(int index){
		return pcStorage.get(index, index);
	}
	@Nullable @Override public UUID getOwnerId(int index){
		return pcStorage.playerUUID;
	}
	@Override public Transaction setPokemon(int index, @Nullable Pokemon pokemon, @Nullable UUID ownerId){
		return index>=0&&index<size()&&(pokemon==null||isOwnerOf(ownerId, index)) ?
				Transaction.success(() -> pcStorage.set(index/30, index%30, pokemon)) : Transaction.fail();
	}

	public StoragePosition boxPosition(int index){
		return new StoragePosition(index/30, index%30);
	}
}
