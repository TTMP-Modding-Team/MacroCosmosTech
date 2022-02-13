package ttmp.macrocosmos.recipe.poke;

import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public final class PokeRecipeMetadataBuilder{
	private final List<PokeRecipeMetadata.ConditionEntry> conditions = new ArrayList<>();
	private final EnumSet<WorkType> workStats = EnumSet.noneOf(WorkType.class);
	private final List<PokeRecipeModifier> modifiers = new ArrayList<>();

	public PokeRecipeMetadataBuilder condition(int index, PokemonCondition condition){
		conditions.add(new PokeRecipeMetadata.ConditionEntry(index, condition));
		return this;
	}
	public PokeRecipeMetadataBuilder condition(PokemonCondition condition){
		conditions.add(new PokeRecipeMetadata.ConditionEntry(null, condition));
		return this;
	}

	public PokeRecipeMetadataBuilder workStats(WorkType... types){
		Collections.addAll(workStats, types);
		return this;
	}
	public PokeRecipeMetadataBuilder modifier(PokeRecipeModifier modifier){
		modifiers.add(modifier);
		return this;
	}

	public PokeRecipeMetadata build(){
		return new PokeRecipeMetadata(conditions, workStats, modifiers);
	}
}