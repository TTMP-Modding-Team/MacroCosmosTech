package ttmp.macrocosmos.capability;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayer;
import ttmp.macrocosmos.util.Transaction;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public interface PokemonContainer{
	int size();

	@Nullable Pokemon getPokemon(int index);
	@Nullable UUID getOwnerId(int index);

	Transaction setPokemon(int index, @Nullable Pokemon pokemon, @Nullable UUID ownerId);

	boolean isValid(Pokemon pokemon, int index);

	///////// DO NOT OVERRIDE BELOW THIS

	default boolean isOwnerOf(@Nullable EntityPlayer player, int index){
		return isOwnerOf(player!=null ? player.getUniqueID() : null, index);
	}
	default boolean isOwnerOf(@Nullable UUID id, int index){
		if(getPokemon(index)==null) return true;
		UUID ownerId = getOwnerId(index);
		return ownerId==null||Objects.equals(ownerId, id);
	}

	default Transaction setPokemon(int index, @Nullable Pokemon pokemon, @Nullable EntityPlayer owner){
		return setPokemon(index, pokemon, owner!=null ? owner.getUniqueID() : null);
	}
	default Transaction setPokemonEmpty(int index){
		return setPokemon(index, null, (UUID)null);
	}

	interface Notifiable{
		void addListener(Listener listener);
	}

	@FunctionalInterface
	interface Listener{
		void onPokemonChange(int index, PokemonContainer container);

		default void onStartListening(Runnable eventRemover){}
	}
}
