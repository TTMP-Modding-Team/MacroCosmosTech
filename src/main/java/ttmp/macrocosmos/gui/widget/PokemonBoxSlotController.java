package ttmp.macrocosmos.gui.widget;

import gregtech.api.gui.resources.IGuiTexture;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.capability.PCPokemonContainer;
import ttmp.macrocosmos.capability.PokemonContainer;

import java.util.UUID;

public class PokemonBoxSlotController extends PokemonSlotController{
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	private int boxIndex = 0;
	private int maxBoxIndex;
	private UUID pcUUID;

	public PokemonSlot newBoxSlot(int x, int y, PokemonContainer container, int index, IGuiTexture... slotTextures){
		PokemonSlotInterface i = new PokemonSlotInterface(slot.size());
		pcUUID = container.getOwnerId(1);
		if(container instanceof PCPokemonContainer) {
			maxBoxIndex = ((PCPokemonContainer)container).getBoxIndex();
		}
		else maxBoxIndex = 32;
		slot.add(new SlotData(container, index, false));
		return new PCPokemonSlot(i, x, y, container,this, index, slotTextures);
	}

	@Override public void handleClientAction(int id, PacketBuffer buffer){
		if(id==MOVE||id==QUICK_MOVE) super.handleClientAction(id, buffer);
		else if(id==LEFT){
			if(boxIndex == 0) boxIndex = maxBoxIndex;
			else boxIndex--;
		}else if(id==RIGHT){
			System.out.println("DOSOME");
			if(boxIndex == maxBoxIndex) boxIndex =0;
			else boxIndex++;
		}
	}

	public int getBoxIndex(){
		return this.boxIndex;
	}

	public void moveBoxLeft(){
		writeClientAction(LEFT, packetBuffer -> {});
		//if(boxIndex == 0) boxIndex = maxBoxIndex;
		//else boxIndex--;
	}

	public void moveBoxRight() {
		writeClientAction(RIGHT, packetBuffer -> {});
	}

	public UUID getPcUUID(){
		return pcUUID;
	}
}
