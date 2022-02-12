package ttmp.macrocosmos.gui.widget;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import gregtech.api.gui.resources.IGuiTexture;
import ttmp.macrocosmos.capability.PokemonContainer;

import javax.annotation.Nullable;

public class PCPokemonSlot extends PokemonSlot{
		private final PokemonContainer container;
		private final PokemonBoxSlotController controller;
		private final int index;

		public PCPokemonSlot(PokemonSlotController.PokemonSlotInterface slotInterface, int x, int y, PokemonContainer container, PokemonBoxSlotController controller, int index, IGuiTexture... slotTextures){
			super(slotInterface, x, y, slotTextures);
			this.index = index;
			this.container = container;
			this.controller = controller;
		}

		@Nullable @Override public Pokemon getPokemonForRender(){
			return ClientStorageManager.pcs.get(controller.getPcUUID()).get(index/30, index%30);
		}
}
