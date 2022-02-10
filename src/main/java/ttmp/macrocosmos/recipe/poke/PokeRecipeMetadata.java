package ttmp.macrocosmos.recipe.poke;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class PokeRecipeMetadata{
	public static Builder builder(){
		return new Builder();
	}

	private final EnumSet<WorkType> workStats;
	private final List<PokeRecipeModifier> modifiers;

	public PokeRecipeMetadata(EnumSet<WorkType> workStats, List<PokeRecipeModifier> modifiers){
		this.workStats = workStats;
		this.modifiers = modifiers;
	}

	public EnumSet<WorkType> workStats(){
		return workStats;
	}
	public List<PokeRecipeModifier> modifiers(){
		return modifiers;
	}

	public double calculateProgress(Pokemon pokemon){
		double stats = 0;
		for(WorkType t : workStats)
			stats += pokemon.getStat(t.pokemonStat);
		double progressMod = 1;
		for(PokeRecipeModifier m : modifiers())
			progressMod += m.getOperationSpeed();
		return stats/workStats.size()/100.0*progressMod;
	}

	public static final class Builder{
		private final EnumSet<WorkType> workStats = EnumSet.noneOf(WorkType.class);
		private final List<PokeRecipeModifier> modifiers = new ArrayList<>();

		public Builder workStats(WorkType... types){
			Collections.addAll(workStats, types);
			return this;
		}
		public Builder modifier(PokeRecipeModifier modifier){
			modifiers.add(modifier);
			return this;
		}

		public PokeRecipeMetadata build(){
			return new PokeRecipeMetadata(workStats, modifiers);
		}
	}
}
