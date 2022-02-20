package ttmp.macrocosmos.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayer;
import ttmp.macrocosmos.capability.EmptyPokemonContainer;
import ttmp.macrocosmos.capability.PlayerPartyContainer;
import ttmp.macrocosmos.capability.PokemonContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PokemonContainerUtil{
	public static PokemonContainer getParty(EntityPlayer player){
		return player.world.isRemote ? EmptyPokemonContainer.EMPTY : new PlayerPartyContainer(player);
	}

	public static int firstEmptySlot(PokemonContainer container){
		for(int i = 0; i<container.size(); i++)
			if(container.getPokemon(i)==null) return i;
		return -1;
	}
	public static boolean hasEmptySlot(PokemonContainer container){
		return firstEmptySlot(container)>=0;
	}

	public static Transaction transferPokemon(PokemonContainer from, int fromIndex, PokemonContainer to, int toIndex){
		Pokemon p1 = from.getPokemon(fromIndex);
		Pokemon p2 = to.getPokemon(toIndex);
		if(p1==null||p2!=null) return Transaction.fail();
		UUID o1 = from.getOwnerId(fromIndex);
		return Transaction.multiple(
				from.setPokemonEmpty(fromIndex),
				to.setPokemon(toIndex, p1, o1));
	}

	public static Transaction swapPokemon(PokemonContainer from, int fromIndex, PokemonContainer to, int toIndex){
		Pokemon p1 = from.getPokemon(fromIndex);
		Pokemon p2 = to.getPokemon(toIndex);
		if(p1==null&&p2==null) return Transaction.success(null);
		UUID o1 = from.getOwnerId(fromIndex);
		UUID o2 = to.getOwnerId(toIndex);
		return Transaction.multiple(
				from.setPokemon(fromIndex, p2, o2),
				to.setPokemon(toIndex, p1, o1));
	}

	public static Pokemon[] toArray(PokemonContainer container, boolean excludeEmptySlot){
		List<Pokemon> l = new ArrayList<>();
		for(int i = 0; i<container.size(); i++){
			Pokemon pokemon = container.getPokemon(i);
			if(!excludeEmptySlot||pokemon!=null) l.add(pokemon);
		}
		return l.toArray(new Pokemon[0]);
	}
}
