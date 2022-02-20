package ttmp.macrocosmos.gui.widget;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCBox;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import gregtech.api.gui.resources.IGuiTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.util.Transaction;

import javax.annotation.Nullable;
import java.util.UUID;

import static ttmp.macrocosmos.util.PokemonContainerUtil.swapPokemon;

public class PokemonBoxSlotController extends PokemonSlotController{
	private static final int UPDATE_BOX_INDEX = 2;
	private static final int MOVE_BETWEEN_BOX = 3;

	private final PCStorage pc;
	private int boxIndex;

	private final PokemonContainer wrappedBox = new WrappedBox(){
		@Override protected int boxIndex(){
			return boxIndex;
		}
	};

	public PokemonBoxSlotController(EntityPlayer player){
		this.pc = Pixelmon.storageManager.getPCForPlayer(player.getUniqueID());
	}

	public int getBoxIndex(){
		return this.boxIndex;
	}
	public UUID getPcUUID(){
		return pc.uuid;
	}
	public PCBox getPCBox(){
		return this.pc.getBox(this.boxIndex);
	}
	public PCStorage getPCStorage(){
		return this.pc;
	}

	public PokemonSlot newBoxSlot(int x, int y, int index, IGuiTexture... slotTextures){
		return new PCPokemonSlot(registerNewSlot(wrappedBox, index), x, y, this, index, slotTextures);
	}

	@Override public void handleClientAction(int id, PacketBuffer buffer){
		if(id==MOVE_BETWEEN_BOX){
			SlotData from = slotData(buffer.readVarInt());
			int fromBox = buffer.readVarInt();
			SlotData to = slotData(buffer.readVarInt());
			int toBox = buffer.readVarInt();
			if(from==null||to==null) return;
			swapPokemon(
					from.container==this.wrappedBox ? new Box(fromBox) : from.container, from.index,
					to.container==this.wrappedBox ? new Box(toBox) : to.container, to.index)
					.run();
		}else super.handleClientAction(id, buffer);
	}

	@Override public void readUpdateInfo(int id, PacketBuffer buffer){
		if(id==UPDATE_BOX_INDEX){
			this.boxIndex = buffer.readVarInt();
		}else super.readUpdateInfo(id, buffer);
	}

	public void moveBoxLeft(){
		if(boxIndex>0){
			boxIndex--;
			writeUpdateInfo(UPDATE_BOX_INDEX, b -> b.writeVarInt(boxIndex));
		}
	}
	public void moveBoxRight(){
		if(boxIndex<pc.getBoxCount()-1){
			boxIndex++;
			writeUpdateInfo(UPDATE_BOX_INDEX, b -> b.writeVarInt(boxIndex));
		}
	}

	private int boxIndexCache;

	@Override protected void selectSlot(int selectedSlot){
		super.selectSlot(selectedSlot);
		this.boxIndexCache = this.boxIndex;
	}
	@Override protected void performMove(int selectedSlot){
		if(this.selectedSlot==null) return;
		if(selectedSlot!=this.selectedSlot||this.boxIndexCache!=this.boxIndex)
			writeClientAction(MOVE_BETWEEN_BOX, buffer -> buffer
					.writeVarInt(this.selectedSlot).writeVarInt(this.boxIndexCache)
					.writeVarInt(selectedSlot).writeVarInt(this.boxIndex));
		this.selectedSlot = null;
	}

	@Override protected boolean isSlotSelected(int slot){
		return super.isSlotSelected(slot)&&this.boxIndex==this.boxIndexCache;
	}

	protected class Box extends WrappedBox{
		private final int boxIndex;

		public Box(int boxIndex){
			this.boxIndex = boxIndex;
		}

		@Override protected int boxIndex(){
			return boxIndex;
		}
	}

	protected abstract class WrappedBox implements PokemonContainer{
		protected abstract int boxIndex();

		@Override public int size(){
			return PCBox.POKEMON_PER_BOX;
		}
		@Nullable @Override public Pokemon getPokemon(int index){
			return pc.get(boxIndex(), index);
		}
		@Nullable @Override public UUID getOwnerId(int index){
			return pc.playerUUID;
		}
		@Override public Transaction setPokemon(int index, @Nullable Pokemon pokemon, @Nullable UUID ownerId){
			return Transaction.success(() -> pc.set(boxIndex(), index, pokemon));
		}
		@Override public boolean isValid(Pokemon pokemon, int index){
			return true;
		}
	}
}
