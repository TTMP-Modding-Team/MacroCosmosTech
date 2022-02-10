package ttmp.macrocosmos.recipe.poke;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class PokeRecipeCondition{
	public static Builder builder(){
		return new Builder();
	}

	private final List<Entry> entries;

	private PokeRecipeCondition(List<Entry> entries){
		this.entries = entries;
	}

	public boolean test(PokemonContainer container){
		for(Entry e : entries)
			if(!e.test(container)) return false;
		return true;
	}

	public static final class Builder {
		private final List<Entry> entries = new ArrayList<>();

		public Builder add(int index, PokemonCondition condition){
			entries.add(new Entry(index, condition));
			return this;
		}
		public Builder add(PokemonCondition condition){
			entries.add(new Entry(null, condition));
			return this;
		}

		public PokeRecipeCondition build(){
			return new PokeRecipeCondition(entries);
		}
		public PokeRecipeConditionIngredient ingredient(){
			return new PokeRecipeConditionIngredient(build());
		}
	}

	private static final class Entry{
		@Nullable private final Integer index;
		private final PokemonCondition condition;

		private Entry(@Nullable Integer index, PokemonCondition condition){
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
	}
}
