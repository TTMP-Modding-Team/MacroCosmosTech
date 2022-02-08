package ttmp.macrocosmos.mte.trait;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import gregtech.api.metatileentity.MTETrait;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import ttmp.macrocosmos.capability.Caps;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.util.Transaction;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PokemonContainerTrait extends MTETrait implements PokemonContainer{
	private static final int SYNC_POKEMON = 1;

	private final Pokemon[] pokemons;
	private final UUID[] pokemonOwners;

	public PokemonContainerTrait(MetaTileEntity mte, int size){
		super(mte);
		this.pokemons = new Pokemon[size];
		this.pokemonOwners = new UUID[size];
	}

	@Override public int size(){
		return pokemons.length;
	}

	@Nullable @Override public Pokemon getPokemon(int index){
		return pokemons[index];
	}
	@Nullable @Override public UUID getOwnerId(int index){
		return pokemonOwners[index];
	}

	@Override public Transaction setPokemon(int index, @Nullable Pokemon pokemon, @Nullable UUID ownerId){
		return index>=0&&index<size()&&(pokemon==null||(isOwnerOf(ownerId, index)&&isValid(pokemon, index))) ?
				Transaction.success(() -> {
					getStorage().set(index, pokemon);
					pokemonOwners[index] = ownerId;
				}) : Transaction.fail();
	}

	@Nullable private WrappedPokemonStorage storage;

	private WrappedPokemonStorage getStorage(){
		if(storage==null) storage = new WrappedPokemonStorage();
		return storage;
	}

	@Override public String getName(){
		return "pokemon_trait";
	}
	@Override public int getNetworkID(){
		return 5;
	}

	@Override public <T> T getCapability(Capability<T> capability){
		return capability==Caps.POKEMON_CONTAINER ? Caps.POKEMON_CONTAINER.cast(this) : null;
	}

	@Override public NBTTagCompound serializeNBT(){
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(int i = 0; i<pokemons.length; i++){
			if(pokemons[i]==null) continue;
			NBTTagCompound pokemonTag = pokemons[i].writeToNBT(new NBTTagCompound());
			pokemonTag.setInteger("_i", i);
			if(pokemonOwners[i]!=null)
				pokemonTag.setUniqueId("_owner", pokemonOwners[i]);
			list.appendTag(pokemonTag);
		}
		tag.setTag("Pokemon", list);
		return tag;
	}

	@Override public void deserializeNBT(NBTTagCompound tag){
		Arrays.fill(this.pokemons, null);
		if(tag.hasKey("Pokemon", Constants.NBT.TAG_LIST)){
			NBTTagList pokemon = tag.getTagList("Pokemon", Constants.NBT.TAG_COMPOUND);
			for(int i = 0; i<pokemon.tagCount(); i++){
				NBTTagCompound pokemonTag = pokemon.getCompoundTagAt(i);
				int at = pokemonTag.getInteger("_i");
				this.pokemons[at] = Pixelmon.pokemonFactory.create(pokemonTag);
				this.pokemonOwners[at] = pokemonTag.hasUniqueId("_owner") ? pokemonTag.getUniqueId("_owner") : null;
			}
		}
	}

	@Override public void writeInitialData(PacketBuffer buf){
		for(Pokemon p : this.pokemons){
			buf.writeBoolean(p!=null);
			if(p!=null) p.writeToByteBuffer(buf, EnumUpdateType.CLIENT);
		}
	}
	@Override public void receiveInitialData(PacketBuffer buf){
		for(int i = 0; i<this.pokemons.length; i++){
			this.pokemons[i] = buf.readBoolean() ?
					Pixelmon.pokemonFactory.create(UUID.randomUUID()).readFromByteBuffer(buf, EnumUpdateType.CLIENT) :
					null;
		}
	}

	private void syncPokemon(){
		writeCustomData(SYNC_POKEMON, this::writeInitialData);
	}
	@Override public void receiveCustomData(int id, PacketBuffer buffer){
		if(id!=SYNC_POKEMON) return;
		receiveInitialData(buffer);
	}

	private class WrappedPokemonStorage extends PokemonStorage{
		public WrappedPokemonStorage(){
			super(UUID.randomUUID());
		}

		public StoragePosition pos(int index){
			return new StoragePosition(-1, index);
		}

		@Override public Pokemon[] getAll(){
			return pokemons.clone();
		}
		@Nullable @Override public StoragePosition getFirstEmptyPosition(){
			for(int i = 0; i<pokemons.length; i++)
				if(pokemons[i]==null) return pos(i);
			return null;
		}
		public void set(int index, Pokemon pokemon){
			set(pos(index), pokemon);
		}
		@Override public void set(StoragePosition pos, Pokemon pokemon){
			pokemons[pos.order] = pokemon;
			if(pokemon!=null) pokemon.setStorage(this, pos);
			this.notifyListeners(pos, pokemon);
		}
		@Nullable @Override public Pokemon get(StoragePosition p){
			return pokemons[p.order];
		}
		@Override public void swap(StoragePosition p1, StoragePosition p2){
			Pokemon temp = pokemons[p1.order];
			pokemons[p1.order] = pokemons[p2.order];
			pokemons[p2.order] = temp;

			if(pokemons[p1.order]!=null) pokemons[p1.order].setStorage(this, p1);
			if(pokemons[p2.order]!=null) pokemons[p2.order].setStorage(this, p2);

			this.notifyListeners(p1, pokemons[p1.order]);
			this.notifyListeners(p2, pokemons[p2.order]);
		}
		@Override public NBTTagCompound writeToNBT(NBTTagCompound tag){
			return tag;
		}
		@Override public PokemonStorage readFromNBT(NBTTagCompound tag){
			return this;
		}
		@Override public StoragePosition getPosition(Pokemon pokemon){
			for(int i = 0; i<pokemons.length; i++)
				if(pokemons[i]!=null&&pokemons[i].getUUID().equals(pokemon.getUUID()))
					return pos(i);
			return null;
		}
		@Override public List<EntityPlayerMP> getPlayersToUpdate(){
			return Collections.emptyList();
		}
		@Override public File getFile(){
			return new File("literally what the fucking shit is this/"+
					"why should a getFile method be in pokemon storage/"+
					"i have no idea/"+
					"im just writing this in order "+
					"to prevent some stupid NPEs from nowhere so yeah fuck off i.guess");
		}

		@Override public void notifyListeners(StoragePosition position, Pokemon pokemon, EnumUpdateType... dataTypes){
			super.notifyListeners(position, pokemon, dataTypes);
			if(this.getShouldSendUpdates()&&dataTypes!=null) syncPokemon();
		}
	}
}
