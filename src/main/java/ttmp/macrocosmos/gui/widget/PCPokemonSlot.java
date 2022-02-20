package ttmp.macrocosmos.gui.widget;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import gregtech.api.gui.resources.IGuiTexture;
import ttmp.macrocosmos.gui.widget.PokemonSlotController.PokemonSlotInterface;

import javax.annotation.Nullable;

public class PCPokemonSlot extends PokemonSlot{
	private final PokemonBoxSlotController controller;
	private final int index;

	public PCPokemonSlot(PokemonSlotInterface slotInterface, int x, int y, PokemonBoxSlotController controller, int index, IGuiTexture... slotTextures){
		super(slotInterface, x, y, slotTextures);
		this.controller = controller;
		this.index = index;
	}

	@Nullable @Override public Pokemon getPokemonForRender(){
		PCStorage pc = ClientStorageManager.pcs.get(controller.getPcUUID());
		return pc!=null ? pc.get(controller.getBoxIndex(), index) : null;
	}
}
