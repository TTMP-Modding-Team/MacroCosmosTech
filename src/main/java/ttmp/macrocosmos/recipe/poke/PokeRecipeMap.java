package ttmp.macrocosmos.recipe.poke;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;
import ttmp.macrocosmos.util.CombinedList;
import ttmp.macrocosmos.util.PokeRecipeMatch;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ttmp.macrocosmos.recipe.poke.value.PokemonValue.*;

public class PokeRecipeMap<R extends RecipeBuilder<R>> extends RecipeMap<R>{
	public static PokemonValue defaultProgress(){
		// HP is omitted because they already contribute to operation and it uses different formula for calculation
		// defence stats were also omitted because they already contribute to operation
		return mul(
				div(
						constant(600),
						sum(constant(500), stat(StatsType.Attack, StatsType.SpecialAttack, StatsType.Speed))
				),
				sum(
						degradation(),
						ability()
				)
		);
	}

	public static PokemonValue defaultHpToWorkConversionRate(){
		return div(
				constant(4000),
				sum(
						constant(200),
						stat(StatsType.Defence, StatsType.SpecialDefence)
				)
		);
	}

	@Nullable private final PokeRecipeMetadata defaultMetadata;

	public PokeRecipeMap(String unlocalizedName,
	                     int minInputs,
	                     int maxInputs,
	                     int minOutputs,
	                     int maxOutputs,
	                     int minFluidInputs,
	                     int maxFluidInputs,
	                     int minFluidOutputs,
	                     int maxFluidOutputs,
	                     R defaultRecipe,
	                     boolean isHidden){
		this(unlocalizedName, minInputs, maxInputs, minOutputs, maxOutputs, minFluidInputs, maxFluidInputs, minFluidOutputs, maxFluidOutputs, defaultRecipe, isHidden,
				PokeRecipeMetadata.builder()
						.progress(defaultProgress())
						.hpToWorkConversionRate(defaultHpToWorkConversionRate()));
	}
	public PokeRecipeMap(String unlocalizedName,
	                     int minInputs,
	                     int maxInputs,
	                     int minOutputs,
	                     int maxOutputs,
	                     int minFluidInputs,
	                     int maxFluidInputs,
	                     int minFluidOutputs,
	                     int maxFluidOutputs,
	                     R defaultRecipe,
	                     boolean isHidden,
	                     @Nullable PokeRecipeMetadataBuilder defaultMetadata){
		// Need one more item input because pokemon container is transferred as an item
		super(unlocalizedName, minInputs, maxInputs+1, minOutputs, maxOutputs, minFluidInputs, maxFluidInputs, minFluidOutputs, maxFluidOutputs, defaultRecipe, isHidden);
		this.defaultMetadata = defaultMetadata!=null ? defaultMetadata.build() : null;
	}

	@Nullable public PokeRecipeMetadata getDefaultMetadata(){
		return this.defaultMetadata;
	}

	public List<PokemonCondition> getDefaultCondition(){
		return defaultMetadata!=null ? defaultMetadata.getConditions() : Collections.emptyList();
	}

	public PokemonValue getDefaultHpToWorkConversionRate(){
		if(defaultMetadata!=null){
			PokemonValue v = defaultMetadata.getHpToWorkConversionRate();
			if(v!=null) return v;
		}
		return PokemonValue.one();
	}

	public PokemonValue getDefaultProgress(){
		if(defaultMetadata!=null){
			PokemonValue v = defaultMetadata.getProgress();
			if(v!=null) return v;
		}
		return PokemonValue.one();
	}

	public Set<PokeRecipeSkillBonus> getDefaultSkillBonus(){
		return defaultMetadata!=null ? defaultMetadata.getSkillBonus() : Collections.emptySet();
	}

	public List<PokemonCondition> getConditions(@Nullable PokeRecipeMetadata meta){
		if(meta==null) return getDefaultCondition();
		if(meta.overrideDefaultCondition()) return meta.getConditions();
		return CombinedList.of(getDefaultCondition(), meta.getConditions());
	}

	public PokemonValue getHpToWorkConversionRateValue(@Nullable PokeRecipeMetadata meta){
		return meta==null||meta.getHpToWorkConversionRate()==null ?
				getDefaultHpToWorkConversionRate() : meta.getHpToWorkConversionRate();
	}

	public PokemonValue getProgressValue(@Nullable PokeRecipeMetadata meta){
		return meta==null||meta.getProgress()==null ?
				getDefaultProgress() : meta.getProgress();
	}

	public Set<PokeRecipeSkillBonus> getSkillBonus(@Nullable PokeRecipeMetadata meta){
		if(meta==null) return getDefaultSkillBonus();
		if(meta.overrideDefaultSkillBonus()) return meta.getSkillBonus();
		HashSet<PokeRecipeSkillBonus> set = new HashSet<>(getDefaultSkillBonus());
		set.addAll(meta.getSkillBonus());
		return set;
	}

	public boolean test(PokemonContainer container, @Nullable PokeRecipeMetadata explicitMetadata){
		return PokeRecipeMatch.test(getConditions(explicitMetadata), container);
	}
	public float getHpToWorkConversionRate(Pokemon pokemon, @Nullable PokeRecipeMetadata explicitMetadata){
		return getHpToWorkConversionRateValue(explicitMetadata).getValue(pokemon);
	}
	public float getProgress(Pokemon pokemon, @Nullable PokeRecipeMetadata explicitMetadata){
		return getProgressValue(explicitMetadata).getValue(pokemon);
	}

	// TODO jei shit?
}
