package ttmp.macrocosmos.recipe.poke;

import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PokeRecipeMetadataBuilder{
	private List<PokemonCondition> conditions = new ArrayList<>();
	private PokemonValue hpToWorkConversionRate = PokemonValue.one();
	private PokemonValue progress = PokemonValue.one();
	private Set<PokeRecipeSkillBonus> skillBonus = new HashSet<>();

	private boolean overrideDefaultCondition;
	private boolean overrideDefaultSkillBonus;

	public PokeRecipeMetadataBuilder condition(PokemonCondition condition){
		this.conditions.add(condition);
		return this;
	}

	public PokeRecipeMetadataBuilder hpToWorkConversionRate(PokemonValue hpToWorkConversionRate){
		this.hpToWorkConversionRate = hpToWorkConversionRate;
		return this;
	}

	public PokeRecipeMetadataBuilder progress(PokemonValue progress){
		this.progress = progress;
		return this;
	}

	public PokeRecipeMetadataBuilder skillBonus(PokeRecipeSkillBonus skillBonus){
		this.skillBonus.add(skillBonus);
		return this;
	}

	public PokeRecipeMetadataBuilder overrideDefaultCondition(){
		this.overrideDefaultCondition = true;
		return this;
	}

	public PokeRecipeMetadataBuilder overrideDefaultSkillBonus(){
		this.overrideDefaultSkillBonus = true;
		return this;
	}

	public PokeRecipeMetadata build(){
		return new PokeRecipeMetadata(conditions, hpToWorkConversionRate, progress, skillBonus, overrideDefaultCondition, overrideDefaultSkillBonus);
	}
}
