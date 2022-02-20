package ttmp.macrocosmos.recipe.poke;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;
import ttmp.macrocosmos.util.BitMask;
import ttmp.macrocosmos.util.ByteSerializable;
import ttmp.macrocosmos.util.CombinedList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ttmp.macrocosmos.recipe.poke.value.PokemonValue.*;

public final class PokeRecipeMetadata implements ByteSerializable{
	// HP is omitted because they already contribute to operation and it uses different formula for calculation
	// defence stats were also omitted because they already contribute to operation
	private static final PokemonValue defaultProgress = mul(
			div(
					constant(600),
					sum(constant(500), stat(StatsType.Attack, StatsType.SpecialAttack, StatsType.Speed))
			),
			sum(
					degradation(),
					ability()
			)
	);
	public static PokemonValue defaultProgress(){
		return defaultProgress;
	}

	public static final PokemonValue defaultHpToWorkConversionRate = div(
			constant(4000),
			sum(
					constant(200),
					stat(StatsType.Defence, StatsType.SpecialDefence)
			)
	);
	public static PokemonValue defaultHpToWorkConversionRate(){
		return defaultHpToWorkConversionRate;
	}

	private static final class DefaultMetadataHolder{
		private static final PokeRecipeMetadata defaultMetadata = builder().build();
	}

	public static PokeRecipeMetadata defaultMetadata(){
		return DefaultMetadataHolder.defaultMetadata;
	}

	public static PokeRecipeMetadataBuilder builder(){
		return new PokeRecipeMetadataBuilder();
	}
	public static PokeRecipeMetadata read(byte[] bytes){
		return read(ByteSerializable.createBufferFromBytes(bytes));
	}
	public static PokeRecipeMetadata read(PacketBuffer buffer){
		byte b = buffer.readByte();
		List<PokemonCondition> conditions = new ArrayList<>();
		for(int i = buffer.readVarInt(); i>0; i--)
			conditions.add(PokemonCondition.readCondition(buffer));

		PokemonValue hpToWork = BitMask.get(b, 2) ? PokemonValue.readValue(buffer) : null;
		PokeRecipeWorkType workType = BitMask.get(b, 4) ? PokeRecipeWorkType.read(buffer) : null;
		PokemonValue progressModifier = BitMask.get(b, 3) ? PokemonValue.readValue(buffer) : null;

		Set<PokeRecipeSkillBonus> skillBonuses = new HashSet<>();
		for(int i = buffer.readVarInt(); i>0; i--)
			skillBonuses.add(PokeRecipeSkillBonus.read(buffer));

		return new PokeRecipeMetadata(conditions, hpToWork, workType, progressModifier, skillBonuses,
				BitMask.get(b, 0), BitMask.get(b, 1));
	}

	private final List<PokemonCondition> conditions;
	@Nullable private final PokemonValue hpToWorkConversionRate;
	@Nullable private final PokeRecipeWorkType workType;
	@Nullable private final PokemonValue progress;
	private final Set<PokeRecipeSkillBonus> skillBonus;
	private final boolean overrideDefaultCondition;
	private final boolean overrideDefaultSkillBonus;

	public PokeRecipeMetadata(List<PokemonCondition> conditions,
	                          @Nullable PokemonValue hpToWorkConversionRate,
	                          @Nullable PokeRecipeWorkType workType,
	                          @Nullable PokemonValue progress,
	                          Set<PokeRecipeSkillBonus> skillBonus,
	                          boolean overrideDefaultCondition,
	                          boolean overrideDefaultSkillBonus){
		this.conditions = ImmutableList.copyOf(conditions);
		this.hpToWorkConversionRate = hpToWorkConversionRate;
		this.workType = workType;
		this.progress = progress;
		this.skillBonus = ImmutableSet.copyOf(skillBonus);
		this.overrideDefaultCondition = overrideDefaultCondition;
		this.overrideDefaultSkillBonus = overrideDefaultSkillBonus;
	}

	public List<PokemonCondition> getConditions(){
		return conditions;
	}
	@Nullable public PokemonValue getHpToWorkConversionRate(){
		return hpToWorkConversionRate;
	}
	@Nullable public PokeRecipeWorkType getWorkType(){
		return workType;
	}
	@Nullable public PokemonValue getProgress(){
		return progress;
	}
	public Set<PokeRecipeSkillBonus> getSkillBonus(){
		return skillBonus;
	}

	public boolean overrideDefaultCondition(){
		return overrideDefaultCondition;
	}
	public boolean overrideDefaultSkillBonus(){
		return overrideDefaultSkillBonus;
	}

	public List<PokemonCondition> getConditions(@Nullable PokeRecipeMetadata override){
		if(override==null) return getConditions();
		if(override.overrideDefaultCondition()) return override.getConditions();
		return CombinedList.of(getConditions(), override.getConditions());
	}

	public PokemonValue getHpToWorkConversionRate(@Nullable PokeRecipeMetadata override){
		return override!=null&&override.getHpToWorkConversionRate()!=null ?
				override.getHpToWorkConversionRate() : getHpToWorkConversionRate()!=null ?
				getHpToWorkConversionRate() : defaultHpToWorkConversionRate();
	}

	public PokeRecipeWorkType getWorkType(@Nullable PokeRecipeMetadata override){
		return override!=null&&override.getWorkType()!=null ?
				override.getWorkType() : this.getWorkType()!=null ?
				this.getWorkType() : PokeRecipeWorkType.defaultWorkType();
	}

	public PokemonValue getProgress(@Nullable PokeRecipeMetadata override){
		return override!=null&&override.getProgress()!=null ?
				override.getProgress() : getProgress()!=null ?
				getProgress() : defaultProgress();
	}

	public Set<PokeRecipeSkillBonus> getSkillBonus(@Nullable PokeRecipeMetadata override){
		if(override==null) return getSkillBonus();
		if(override.overrideDefaultSkillBonus()) return override.getSkillBonus();
		HashSet<PokeRecipeSkillBonus> set = new HashSet<>(getSkillBonus());
		set.addAll(override.getSkillBonus());
		return set;
	}

	@Override public void write(PacketBuffer buffer){
		buffer.writeByte(BitMask.toByteMask(
				this.overrideDefaultCondition, // 0
				this.overrideDefaultSkillBonus, // 1
				this.hpToWorkConversionRate!=null, // 2
				this.progress!=null, // 3
				this.workType!=null // 4
		));
		buffer.writeVarInt(this.conditions.size());
		for(PokemonCondition c : this.conditions)
			c.write(buffer);
		if(this.hpToWorkConversionRate!=null) this.hpToWorkConversionRate.write(buffer);
		if(this.workType!=null) this.workType.write(buffer);
		if(this.progress!=null) this.progress.write(buffer);

		buffer.writeVarInt(this.skillBonus.size());
		for(PokeRecipeSkillBonus b : this.skillBonus)
			b.write(buffer);
	}

	@Override public String toString(){
		return "PokeRecipeMetadata{"+
				"conditions="+conditions+
				", hpToWorkConversionRate="+hpToWorkConversionRate+
				", workType="+workType+
				", progress="+progress+
				", skillBonus="+skillBonus+
				", overrideDefaultCondition="+overrideDefaultCondition+
				", overrideDefaultSkillBonus="+overrideDefaultSkillBonus+
				'}';
	}
}
