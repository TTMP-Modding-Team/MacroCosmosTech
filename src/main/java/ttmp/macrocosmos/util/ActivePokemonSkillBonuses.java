package ttmp.macrocosmos.util;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ttmp.macrocosmos.MacroCosmosMod;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.recipe.poke.PokeRecipeSkillBonus;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ActivePokemonSkillBonuses{
	protected final Int2ObjectMap<Map<String, Entry>> entries = new Int2ObjectOpenHashMap<>();

	public void tickActiveSkillBonus(PokemonContainer container){
		for(int i = 0; i<container.size(); i++){
			Pokemon pokemon = container.getPokemon(i);

			if(pokemon==null){
				entries.remove(i);
				continue;
			}
			Map<String, Entry> m = this.entries.get(i);
			if(m!=null){
				for(Iterator<Map.Entry<String, Entry>> it = m.entrySet().iterator(); it.hasNext(); ){
					Map.Entry<String, Entry> e = it.next();
					Entry e2 = e.getValue();
					if(--e2.remainingTick<=0)
						it.remove();
				}
			}
		}
	}

	public float getSkillBonus(int index, Pokemon pokemon, Collection<PokeRecipeSkillBonus> skillBonuses){
		float skillBonusSum = 1;
		Map<String, Entry> m = this.entries.get(index);
		for(PokeRecipeSkillBonus skillBonus : skillBonuses){
			if(m!=null){
				Entry e = m.get(skillBonus.getSkillName());
				if(e!=null){
					skillBonusSum += e.additionalModifier.getValue(pokemon);
					continue;
				}
			}
			Attack attack = getAttack(pokemon, skillBonus.getSkillName());
			if(attack==null||attack.pp<=0) continue;
			attack.pp--;
			if(m==null) this.entries.put(index, m = new HashMap<>());
			m.put(skillBonus.getSkillName(), new Entry(skillBonus.getAdditionalModifier(), skillBonus.getEffectDuration()));
			skillBonusSum += skillBonus.getAdditionalModifier().getValue(pokemon);
		}
		return skillBonusSum;
	}

	@Nullable private static Attack getAttack(Pokemon pokemon, String name){
		for(Attack a : pokemon.getMoveset())
			if(a!=null&&a.isAttack(name)) return a;
		return null;
	}

	public float getModifier(int pokemonIndex, Pokemon pokemon){
		Map<String, Entry> m = entries.get(pokemonIndex);
		if(m==null) return 0;
		float modifier = 0;
		for(Entry e : m.values())
			modifier += e.additionalModifier.getValue(pokemon);
		return modifier;
	}

	public NBTTagList write(){
		NBTTagList list = new NBTTagList();
		for(Int2ObjectMap.Entry<Map<String, Entry>> e : this.entries.int2ObjectEntrySet()){
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("i", e.getIntKey());
			for(Map.Entry<String, ActivePokemonSkillBonuses.Entry> e2 : e.getValue().entrySet()){
				if(e2.getKey().equals("i")){
					MacroCosmosMod.LOGGER.warn("Who the fuck names their skill 'i'?");
					continue;
				}
				tag.setTag(e2.getKey(), e2.getValue().write());
			}
		}
		return list;
	}

	public void read(NBTTagList list){
		this.entries.clear();
		for(int i = 0; i<list.tagCount(); i++){
			NBTTagCompound tag = list.getCompoundTagAt(i);
			HashMap<String, Entry> m = new HashMap<>();
			this.entries.put(tag.getInteger("i"), m);
			for(String s : tag.getKeySet()){
				if("i".equals(s)) continue;
				m.put(s, new Entry(tag.getCompoundTag(s)));
			}
		}
	}

	@Override public String toString(){
		StringBuilder stb = new StringBuilder("ActivePokemonSkillBonuses{");
		boolean first = true;
		for(Int2ObjectMap.Entry<Map<String, Entry>> e : entries.int2ObjectEntrySet()){
			if(e.getValue().isEmpty()) continue;
			if(first) first = false;
			else stb.append(", ");
			stb.append(e.getIntKey()).append(":").append("{");
			boolean first2 = true;
			for(Map.Entry<String, Entry> e2 : e.getValue().entrySet()){
				if(first2) first2 = false;
				else stb.append(", ");
				stb.append(e2.getKey()).append(":").append(e2.getValue());
			}
			stb.append("}");
		}
		return stb.append("}").toString();
	}

	public static final class Entry{
		public final PokemonValue additionalModifier;
		public int remainingTick;

		public Entry(PokemonValue additionalModifier, int remainingTick){
			this.additionalModifier = additionalModifier;
			this.remainingTick = remainingTick;
		}
		public Entry(NBTTagCompound nbt){
			this.additionalModifier = PokemonValue.readValue(nbt.getByteArray("modifier"));
			this.remainingTick = nbt.getInteger("ticks");
		}

		public NBTTagCompound write(){
			NBTTagCompound tag = new NBTTagCompound();
			tag.setByteArray("modifier", additionalModifier.writeToByteArray());
			tag.setInteger("ticks", remainingTick);
			return tag;
		}

		@Override public String toString(){
			return "Entry{"+
					"additionalModifier="+additionalModifier+
					", remainingTick="+remainingTick+
					'}';
		}
	}
}
