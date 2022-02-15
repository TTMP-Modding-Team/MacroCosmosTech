package ttmp.macrocosmos.recipe.poke;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;
import ttmp.macrocosmos.util.BitMask;
import ttmp.macrocosmos.util.Join;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public final class PokeRecipeMetadata{
	public static PokeRecipeMetadataBuilder builder(){
		return new PokeRecipeMetadataBuilder();
	}
	public static PokeRecipeMetadata read(NBTTagCompound tag){
		List<PokemonCondition> conditions = new ArrayList<>();
		NBTTagList nbtConditions = tag.getTagList("c", Constants.NBT.TAG_BYTE_ARRAY);
		for(int i = 0; i<nbtConditions.tagCount(); i++){
			NBTBase c = nbtConditions.get(i);
			if(c instanceof NBTTagByteArray)
				conditions.add(PokemonCondition.readCondition(((NBTTagByteArray)c).getByteArray()));
		}
		EnumSet<WorkType> workStats = fromBitMask(tag.getByte("s"));
		List<PokeRecipeModifier> modifiers = new ArrayList<>();
		NBTTagList nbtModifiers = tag.getTagList("m", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i<nbtModifiers.tagCount(); i++){
			modifiers.add(PokeRecipeModifier.read(nbtModifiers.getCompoundTagAt(i)));
		}
		return new PokeRecipeMetadata(conditions, workStats, modifiers);
	}

	private final List<PokemonCondition> conditions;
	private final EnumSet<WorkType> workStats;
	private final List<PokeRecipeModifier> modifiers;

	public PokeRecipeMetadata(List<PokemonCondition> conditions, EnumSet<WorkType> workStats, List<PokeRecipeModifier> modifiers){
		this.conditions = conditions;
		this.workStats = workStats;
		this.modifiers = modifiers;
	}

	public List<PokemonCondition> getConditions(){
		return conditions;
	}
	public EnumSet<WorkType> workStats(){
		return workStats;
	}
	public List<PokeRecipeModifier> modifiers(){
		return modifiers;
	}

	public boolean hasCondition(){
		return !getConditions().isEmpty();
	}

	public boolean test(PokemonContainer container){
		if(container.size()<conditions.size()) return false;
		if(container.size()>=Long.SIZE){
			// TODO I don't fucking care. I bet this already will be enough for 97% of the use case
			throw new UnsupportedOperationException("Input too large");
		}
		LongArrayList incompleteConditions = new LongArrayList();

		// Match each pokemon input to each condition.
		long excludedPokemonFlag = 0;
		for(PokemonCondition condition : conditions){
			int count = 0;
			long pokemonFlag = 0;
			for(int i = 0; i<container.size(); i++){
				if(BitMask.get(excludedPokemonFlag, i)) continue;
				Pokemon pokemon = container.getPokemon(i);
				if(pokemon==null||!condition.test(pokemon)) continue;
				count++;
				pokemonFlag = BitMask.set(pokemonFlag, i, true);
			}
			switch(count){
				case 0: // input is invalid
					return false;
				case 1: // we don't have to track it, so we exclude it
					if(exclude(incompleteConditions, excludedPokemonFlag |= pokemonFlag))
						return false;
					break;
				default: // otherwise shove it into temp list and think about it later
					incompleteConditions.add(pokemonFlag);
					break;
			}
		}

		if(incompleteConditions.size()<=1) return true; // don't even have to enter brute force mode

		// Remove conditions that is positive for all pokemon, since there's no possibility of them being false positive
		long usedPokemon = 0;
		for(int i = 0; i<incompleteConditions.size(); i++)
			usedPokemon |= incompleteConditions.getLong(i);
		for(int i = 0; i<incompleteConditions.size(); i++){
			if(usedPokemon==incompleteConditions.getLong(i)){
				incompleteConditions.removeLong(i);
				i--;
			}
		}

		if(incompleteConditions.size()<=1) return true; // check again before (possibly) the most expensive part of the algorithm

		return bruteForce(incompleteConditions, 0, 0, container.size());
	}

	/**
	 * @return {@code true} if input is invalid
	 */
	private static boolean exclude(LongArrayList entries, long excluded){
		for(int i = 0; i<entries.size(); i++){
			long bits = entries.getLong(i);
			long modifiedBits = BitMask.removeAll(bits, excluded);
			if(modifiedBits!=bits){
				if(modifiedBits==0) return true; // input is invalid
				entries.set(i, modifiedBits);
			}
		}
		return false;
	}

	// no sundae
	private static boolean bruteForce(LongArrayList entries, int index, long usedPokemonFlags, int pokemonSize){
		if(entries.size()<=index) return true;
		long pokemon = entries.getLong(index);
		for(int i = 0; i<pokemonSize; i++)
			if(!BitMask.get(usedPokemonFlags, i)&& // not used in previous conditions
					BitMask.get(pokemon, i)&& // matches this condition
					bruteForce(entries, index+1, BitMask.set(usedPokemonFlags, i, true), pokemonSize)) // can match all the following conditions
				return true; // lfg
		return false;
	}

	public double calculateProgress(Pokemon pokemon){
		if(pokemon.getHealth()<=0) return 0;
		double stats;
		if(workStats.isEmpty()){
			stats = pokemon.getLevel()/100.0;
		}else{
			stats = 0;
			for(WorkType t : workStats)
				stats += pokemon.getStat(t.pokemonStat);
			stats /= workStats.size()/100.0;
		}
		double progressMod = 1;
		for(PokeRecipeModifier m : modifiers())
			progressMod += m.getOperationSpeed();
		return stats*progressMod;
	}

	@Override public String toString(){
		return "PokeRecipeMetadata{"+
				"conditions="+conditions+
				"workStats="+Join.join(workStats)+
				", modifiers="+Join.join(modifiers)+
				'}';
	}

	public NBTTagCompound write(){
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList conditions = new NBTTagList();
		for(PokemonCondition c : this.conditions){
			conditions.appendTag(new NBTTagByteArray(c.writeToByteArray()));
		}
		tag.setTag("c", conditions);
		tag.setByte("s", toBitMask(workStats));
		NBTTagList modifiers = new NBTTagList();
		for(PokeRecipeModifier m : this.modifiers){
			modifiers.appendTag(m.write());
		}
		tag.setByte("m", toBitMask(workStats));
		return tag;
	}

	private static byte toBitMask(EnumSet<WorkType> workTypes){
		byte bitMask = 0;
		for(WorkType t : workTypes)
			bitMask |= 1<<t.ordinal();
		return bitMask;
	}

	private static EnumSet<WorkType> fromBitMask(byte bitMask){
		EnumSet<WorkType> workTypes = EnumSet.noneOf(WorkType.class);
		for(WorkType t : WorkType.values())
			if((bitMask&1<<t.ordinal())!=0)
				workTypes.add(t);
		return workTypes;
	}
}
