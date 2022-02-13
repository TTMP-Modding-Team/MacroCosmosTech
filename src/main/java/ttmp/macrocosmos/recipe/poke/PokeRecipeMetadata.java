package ttmp.macrocosmos.recipe.poke;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;
import ttmp.macrocosmos.recipe.poke.condition.PokemonConditionSerializer;
import ttmp.macrocosmos.util.Join;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public final class PokeRecipeMetadata{
	public static PokeRecipeMetadataBuilder builder(){
		return new PokeRecipeMetadataBuilder();
	}
	public static PokeRecipeMetadata read(NBTTagCompound tag){
		List<ConditionEntry> conditions = new ArrayList<>();
		NBTTagList nbtConditions = tag.getTagList("c", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i<nbtConditions.tagCount(); i++){
			conditions.add(ConditionEntry.read(nbtConditions.getCompoundTagAt(i)));
		}
		EnumSet<WorkType> workStats = fromBitMask(tag.getByte("s"));
		List<PokeRecipeModifier> modifiers = new ArrayList<>();
		NBTTagList nbtModifiers = tag.getTagList("m", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i<nbtModifiers.tagCount(); i++){
			modifiers.add(PokeRecipeModifier.read(nbtModifiers.getCompoundTagAt(i)));
		}
		return new PokeRecipeMetadata(conditions, workStats, modifiers);
	}

	private final List<ConditionEntry> conditions;
	private final EnumSet<WorkType> workStats;
	private final List<PokeRecipeModifier> modifiers;

	public PokeRecipeMetadata(List<ConditionEntry> conditions, EnumSet<WorkType> workStats, List<PokeRecipeModifier> modifiers){
		this.conditions = conditions;
		this.workStats = workStats;
		this.modifiers = modifiers;
	}

	public List<ConditionEntry> getConditions(){
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
		for(ConditionEntry c : conditions)
			if(!c.test(container)) return false;
		return true;
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
		for(ConditionEntry c : this.conditions){
			conditions.appendTag(c.write());
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

	public static final class ConditionEntry{
		public static ConditionEntry read(NBTTagCompound tag){
			return new ConditionEntry(tag.hasKey("i", Constants.NBT.TAG_INT) ? tag.getInteger("i") : null,
					PokemonConditionSerializer.readCondition(tag.getByteArray("c")));
		}

		@Nullable private final Integer index;
		private final PokemonCondition condition;

		public ConditionEntry(@Nullable Integer index, PokemonCondition condition){
			this.index = index;
			this.condition = condition;
		}
		public boolean test(PokemonContainer container){
			if(index==null){
				for(int i = 0; i<container.size(); i++)
					if(test(container.getPokemon(i))) return true;
				return false;
			}
			return index>=0&&index<container.size()&&test(container.getPokemon(index));
		}
		public boolean test(@Nullable Pokemon pokemon){
			return pokemon!=null&&condition.test(pokemon);
		}

		@Override public String toString(){
			return index!=null ? index+"="+condition.toString() : condition.toString();
		}

		public NBTTagCompound write(){
			NBTTagCompound tag = new NBTTagCompound();
			if(index!=null) tag.setInteger("i", index);
			tag.setTag("c", PokemonConditionSerializer.writeToNBT(condition));
			return tag;
		}
	}
}
