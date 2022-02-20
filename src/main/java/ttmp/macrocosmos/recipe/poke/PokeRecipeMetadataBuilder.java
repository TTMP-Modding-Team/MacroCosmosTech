package ttmp.macrocosmos.recipe.poke;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.pixelmonmod.pixelmon.enums.EnumType;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class PokeRecipeMetadataBuilder{
	private List<PokemonCondition> conditions = Collections.emptyList();
	@Nullable private PokemonValue hpToWorkConversionRate;
	@Nullable private PokeRecipeWorkType.Builder workType;
	@Nullable private PokemonValue progress;
	private Set<PokeRecipeSkillBonus> skillBonus = Collections.emptySet();

	private boolean overrideDefaultCondition;
	private boolean overrideDefaultSkillBonus;

	public PokeRecipeMetadataBuilder conditions(PokemonCondition... conditions){
		this.overrideDefaultCondition = true;
		this.conditions = ImmutableList.copyOf(conditions);
		return this;
	}
	public PokeRecipeMetadataBuilder addidionalConditions(PokemonCondition... conditions){
		this.overrideDefaultCondition = false;
		this.conditions = ImmutableList.copyOf(conditions);
		return this;
	}

	public PokeRecipeMetadataBuilder hpToWorkConversionRate(PokemonValue hpToWorkConversionRate){
		this.hpToWorkConversionRate = hpToWorkConversionRate;
		return this;
	}

	public PokeRecipeMetadataBuilder noWorkType(){
		this.workType = PokeRecipeWorkType.builder();
		return this;
	}

	public PokeRecipeMetadataBuilder workType(EnumType... types){
		if(types.length==0) throw new IllegalArgumentException("Empty types");
		if(this.workType==null) this.workType = PokeRecipeWorkType.builder();
		for(EnumType t : types)
			this.workType.add(1, t);
		return this;
	}

	public PokeRecipeMetadataBuilder workType(int weight, EnumType type){
		if(this.workType==null) this.workType = PokeRecipeWorkType.builder();
		this.workType.add(weight, type);
		return this;
	}

	public PokeRecipeMetadataBuilder progress(PokemonValue progress){
		this.progress = progress;
		return this;
	}

	public PokeRecipeMetadataBuilder skillBonuses(PokeRecipeSkillBonus... skillBonuses){
		this.overrideDefaultSkillBonus = true;
		this.skillBonus = ImmutableSet.copyOf(skillBonuses);
		return this;
	}

	public PokeRecipeMetadataBuilder additionalSkillBonuses(PokeRecipeSkillBonus... skillBonuses){
		this.overrideDefaultSkillBonus = false;
		this.skillBonus = ImmutableSet.copyOf(skillBonuses);
		return this;
	}

	public PokeRecipeMetadata build(){
		return new PokeRecipeMetadata(conditions,
				hpToWorkConversionRate,
				workType!=null ? workType.build() : null,
				progress,
				skillBonus,
				overrideDefaultCondition,
				overrideDefaultSkillBonus);
	}
}
