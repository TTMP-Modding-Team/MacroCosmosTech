package ttmp.macrocosmos.gui.widget;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import gregtech.api.gui.resources.IGuiTexture;
import ttmp.macrocosmos.capability.PokemonContainer;

import javax.annotation.Nullable;

public class ContainerPokemonSlot extends PokemonSlot{
	private final PokemonContainer container;
	private final int index;

	public ContainerPokemonSlot(PokemonSlotController.PokemonSlotInterface slotInterface, int x, int y, PokemonContainer container, int index, IGuiTexture... slotTextures){
		super(slotInterface, x, y, slotTextures);
		this.container = container;
		this.index = index;
	}

	@Nullable @Override public Pokemon getPokemonForRender(){
		return container.getPokemon(index);
	}

}
