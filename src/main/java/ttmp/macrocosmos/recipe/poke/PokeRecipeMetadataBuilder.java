package ttmp.macrocosmos.recipe.poke;

import com.pixelmonmod.pixelmon.enums.EnumType;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PokeRecipeMetadataBuilder{
	private final List<PokemonCondition> conditions = new ArrayList<>();
	@Nullable private PokemonValue hpToWorkConversionRate;
	@Nullable private PokeRecipeWorkType.Builder workType;
	@Nullable private PokemonValue progress;
	private final Set<PokeRecipeSkillBonus> skillBonus = new HashSet<>();

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
		return new PokeRecipeMetadata(conditions, hpToWorkConversionRate, workType!=null ? workType.build() : null, progress, skillBonus, overrideDefaultCondition, overrideDefaultSkillBonus);
	}
}
