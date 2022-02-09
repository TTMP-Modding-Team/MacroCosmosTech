package ttmp.macrocosmos.gui.widget;

import gregtech.api.gui.Widget;
import gregtech.api.gui.resources.IGuiTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.capability.PokemonContainer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static ttmp.macrocosmos.util.PokemonContainerUtil.*;

public class PokemonSlotController extends Widget{
	public static final int MOVE = 0;
	public static final int QUICK_MOVE = 1;

	protected final List<SlotData> slot = new ArrayList<>();

	@Nullable private Integer selectedSlot;

	public PokemonSlotController(){
		super(0, 0, 0, 0);
		setVisible(false);
	}

	@Nullable public Integer getSelectedSlot(){
		return selectedSlot;
	}

	public PokemonSlot newContainerSlot(int x, int y, PokemonContainer container, int index, IGuiTexture... slotTextures){
		PokemonSlotInterface i = new PokemonSlotInterface(slot.size());
		slot.add(new SlotData(container, index, false));
		return new ContainerPokemonSlot(i, x, y, container, index, slotTextures);
	}

	public PokemonSlot newPartySlot(int x, int y, EntityPlayer player, int index, IGuiTexture... slotTextures){
		PokemonSlotInterface i = new PokemonSlotInterface(slot.size());
		slot.add(new SlotData(getParty(player), index, true));
		return new PartyPokemonSlot(i, x, y, index, slotTextures);
	}

	@Override public void handleClientAction(int id, PacketBuffer buffer){
		switch(id){
			case MOVE:{
				SlotData from = slotData(buffer.readVarInt());
				SlotData to = slotData(buffer.readVarInt());
				if(from==null||to==null) return;
				swapPokemon(from.container, from.index, to.container, to.index).run();
				return;
			}
			case QUICK_MOVE:{
				SlotData from = slotData(buffer.readVarInt());
				if(from==null||from.container.getPokemon(from.index)==null) return;
				for(SlotData s : slot){
					if(s.isParty!=from.isParty&&
							s.container.getPokemon(s.index)==null&&
							transferPokemon(from.container, from.index, s.container, s.index).runIfSuccess()){
						return;
					}
				}
			}
		}
	}

	@Nullable protected SlotData slotData(int id){
		if(id>=0&&id<slot.size()) return slot.get(id);
		else return null;
	}

	protected static final class SlotData{
		public final PokemonContainer container;
		public final int index;
		public final boolean isParty;

		SlotData(PokemonContainer container, int index, boolean isParty){
			this.container = container;
			this.index = index;
			this.isParty = isParty;
		}
	}

	public class PokemonSlotInterface{
		private final int id;

		public PokemonSlotInterface(int id){
			this.id = id;
		}

		public void select(){
			if(selectedSlot==null) selectedSlot = id;
			else{
				if(selectedSlot!=id)
					writeClientAction(MOVE, buffer -> buffer.writeVarInt(selectedSlot).writeVarInt(id));
				selectedSlot = null;
			}
		}

		public void quickMove(){
			writeClientAction(QUICK_MOVE, buffer -> buffer.writeVarInt(id));
			selectedSlot = null;
		}

		public boolean isSelected(){
			return selectedSlot!=null&&selectedSlot==id;
		}
	}
}
