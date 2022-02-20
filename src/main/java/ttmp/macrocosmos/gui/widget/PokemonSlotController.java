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
	private static final int MOVE = 0;
	private static final int QUICK_MOVE = 1;

	protected final List<SlotData> slot = new ArrayList<>();

	@Nullable protected Integer selectedSlot;

	public PokemonSlotController(){
		super(0, 0, 0, 0);
		setVisible(false);
	}

	@Nullable public Integer getSelectedSlot(){
		return selectedSlot;
	}

	public PokemonSlot newContainerSlot(int x, int y, PokemonContainer container, int index, IGuiTexture... slotTextures){
		return new ContainerPokemonSlot(registerNewSlot(container,index), x, y, container, index, slotTextures);
	}

	public PokemonSlot newPartySlot(int x, int y, EntityPlayer player, int index, IGuiTexture... slotTextures){
		return new PartyPokemonSlot(registerNewSlot(getParty(player), index), x, y, index, slotTextures);
	}

	protected PokemonSlotInterface registerNewSlot(PokemonContainer container, int index){
		PokemonSlotInterface i = new PokemonSlotInterface(slot.size());
		slot.add(new SlotData(container, index, true));
		return i;
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

	protected void selectSlot(int selectedSlot){
		this.selectedSlot = selectedSlot;
	}

	protected void performMove(int selectedSlot){
		if(this.selectedSlot==null) return;
		if(selectedSlot!=this.selectedSlot)
			writeClientAction(MOVE, buffer -> buffer
					.writeVarInt(this.selectedSlot)
					.writeVarInt(selectedSlot));
		this.selectedSlot = null;
	}

	protected void performQuickMove(int selectedSlot){
		writeClientAction(QUICK_MOVE, buffer -> buffer.writeVarInt(selectedSlot));
		this.selectedSlot = null;
	}

	protected boolean isSlotSelected(int slot){
		return selectedSlot!=null&&selectedSlot==slot;
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
			if(selectedSlot==null) selectSlot(id);
			else performMove(id);
		}

		public void quickMove(){
			performQuickMove(id);
		}

		public boolean isSelected(){
			return isSlotSelected(id);
		}
	}
}
