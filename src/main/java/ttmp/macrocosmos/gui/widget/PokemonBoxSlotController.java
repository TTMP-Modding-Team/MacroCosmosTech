package ttmp.macrocosmos.gui.widget;

import gregtech.api.gui.resources.IGuiTexture;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.capability.PokemonContainer;

import java.util.ArrayList;
import java.util.List;

public class PokemonBoxSlotController extends PokemonSlotController{
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	private final List<SlotData> slot = new ArrayList<>();

	public PokemonSlot newContainerSlot(int x, int y, PokemonContainer container, int index, IGuiTexture... slotTextures){
		PokemonSlotInterface i = new PokemonSlotInterface(slot.size());
		slot.add(new SlotData(container, index, false));
		return new PokemonSlot.PCSlot(i, x, y, container, index, slotTextures);
	}

	@Override public void handleClientAction(int id, PacketBuffer buffer){
		if(id==MOVE||id==QUICK_MOVE) super.handleClientAction(id, buffer);
		else if(id==LEFT){

		}else if(id==RIGHT){

		}
	}
}
