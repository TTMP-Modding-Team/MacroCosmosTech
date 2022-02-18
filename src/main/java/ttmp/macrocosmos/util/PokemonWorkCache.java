package ttmp.macrocosmos.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import it.unimi.dsi.fastutil.ints.Int2FloatArrayMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ttmp.macrocosmos.mte.trait.PokemonRecipeLogic;

public class PokemonWorkCache{
	private final PokemonRecipeLogic logic;
	protected final Int2FloatMap works = new Int2FloatArrayMap();

	public PokemonWorkCache(PokemonRecipeLogic logic){
		this.logic = logic;
	}

	/**
	 * Consumes {@code workToConsume} of work from internal buffer.<br>
	 * If internal buffer is insufficient for operation, part of the HP of provided pokemon will be converted into work.
	 *
	 * @param index         Index of internal work buffer to use
	 * @param pokemon       Pokemon to provide additional work
	 * @param workToConsume Work to consume
	 * @return Range of 0 ~ {@code workToConsume} indicating consumed work
	 */
	public float consumeWork(int index, Pokemon pokemon, float workToConsume){
		float w = works.get(index);
		if(w<workToConsume){
			w += drawWork(pokemon, w-workToConsume);
			if(w<workToConsume){
				works.remove(index);
				return w;
			}
		}
		works.put(index, w-workToConsume);
		return workToConsume;
	}

	protected float drawWork(Pokemon pokemon, float requiredWork){
		float hp = logic.getDefaultMetadata().getHpToWorkConversionRate(logic.getRecipeMetadata()).getValue(pokemon);
		if(hp<=0) return 0;
		PokeRecipeWorkType workType = logic.getDefaultMetadata().getWorkType(logic.getRecipeMetadata());
		float averageEffectiveness = workType.getAverageEffectiveness(
				pokemon.getBaseStats().getTypeList(), EnumEffectivenessCalculationLogic.get(pokemon));
		if(averageEffectiveness==0) return requiredWork;

		if(averageEffectiveness>0){
			int minimumHpConsumption = (int)Math.ceil(requiredWork/hp);
			if(minimumHpConsumption>=pokemon.getHealth()){
				float work = pokemon.getHealth()*hp;
				pokemon.setHealth(0);
				return work;
			}
			pokemon.setHealth(pokemon.getHealth()-minimumHpConsumption);
			return hp*minimumHpConsumption;
		}else{
			int minimumHpGen = (int)Math.ceil(requiredWork/hp);
			pokemon.setHealth(pokemon.getHealth()+minimumHpGen);
			return hp*minimumHpGen;
		}
	}

	public NBTTagList write(){
		NBTTagList list = new NBTTagList();
		for(Int2FloatMap.Entry e : this.works.int2FloatEntrySet()){
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("i", e.getIntKey());
			tag.setFloat("w", e.getFloatValue());
		}
		return list;
	}

	public void read(NBTTagList list){
		this.works.clear();
		for(int i = 0; i<list.tagCount(); i++){
			NBTTagCompound tag = list.getCompoundTagAt(i);
			int index = tag.getInteger("i");
			float work = tag.getFloat("w");
			this.works.put(index, work);
		}
	}
}
