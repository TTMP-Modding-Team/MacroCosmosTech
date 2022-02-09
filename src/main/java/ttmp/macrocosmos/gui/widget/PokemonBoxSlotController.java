package ttmp.macrocosmos.gui.widget;

import gregtech.api.gui.resources.IGuiTexture;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.capability.PCPokemonContainer;
import ttmp.macrocosmos.capability.PokemonContainer;

public class PokemonBoxSlotController extends PokemonSlotController{
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	private int boxIndex = 0;
	private int maxBoxIndex;

	public PokemonSlot newBoxSlot(int x, int y, PokemonContainer container, int index, IGuiTexture... slotTextures){
		PokemonSlotInterface i = new PokemonSlotInterface(slot.size());
		if(container instanceof PCPokemonContainer) maxBoxIndex = ((PCPokemonContainer)container).getBoxIndex();
		else maxBoxIndex = 32;
		slot.add(new SlotData(container, index, false));
		return new PokemonSlot.PCSlot(i, x, y, container, index, this, slotTextures);
	}

	@Override public void handleClientAction(int id, PacketBuffer buffer){
		if(id==MOVE||id==QUICK_MOVE) super.handleClientAction(id, buffer);
		else if(id==LEFT){
			moveBoxLeft();
		}else if(id==RIGHT){
			moveBoxRight();
		}
	}

	public int getBoxIndex(){
		return this.boxIndex;
	}

	public void moveBoxLeft(){
		if(boxIndex == 0) boxIndex = maxBoxIndex;
		else boxIndex--;
	}

	public void moveBoxRight() {
		if(boxIndex == maxBoxIndex) boxIndex = 0;
		else boxIndex++;
	}
}
