package ttmp.macrocosmos.gui.widget;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import gregtech.api.gui.resources.IGuiTexture;

import javax.annotation.Nullable;

public class PartyPokemonSlot extends PokemonSlot{
	private final int index;

	public PartyPokemonSlot(PokemonSlotController.PokemonSlotInterface slotInterface, int x, int y, int index, IGuiTexture... slotTextures){
		super(slotInterface, x, y, slotTextures);
		this.index = index;
	}

	@Nullable @Override public Pokemon getPokemonForRender(){
		return ClientStorageManager.party.get(index);
	}
}