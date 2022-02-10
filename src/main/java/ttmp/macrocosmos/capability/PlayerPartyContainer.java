package ttmp.macrocosmos.capability;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayer;
import ttmp.macrocosmos.util.Transaction;

import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerPartyContainer implements PokemonContainer{
	private final PlayerPartyStorage storage;

	public PlayerPartyContainer(EntityPlayer player){
		this(Pixelmon.storageManager.getParty(player.getUniqueID()));
	}
	public PlayerPartyContainer(PlayerPartyStorage storage){
		this.storage = storage;
	}

	@Override public int size(){
		return PlayerPartyStorage.MAX_PARTY;
	}
	@Nullable @Override public Pokemon getPokemon(int index){
		return storage.get(index);
	}
	@Nullable @Override public UUID getOwnerId(int index){
		return storage.getPlayerUUID();
	}

	@Override public Transaction setPokemon(int index, @Nullable Pokemon pokemon, @Nullable UUID ownerId){
		return index>=0&&index<size()&&(pokemon==null||isOwnerOf(ownerId, index)) ?
				Transaction.success(() -> storage.set(index, pokemon)) : Transaction.fail();
	}
	@Override public boolean isValid(Pokemon pokemon, int index){
		return true;
	}
}
