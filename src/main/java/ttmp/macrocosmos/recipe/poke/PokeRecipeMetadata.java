package ttmp.macrocosmos.recipe.poke;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;
import ttmp.macrocosmos.recipe.poke.value.PokemonValue;
import ttmp.macrocosmos.util.BitMask;
import ttmp.macrocosmos.util.ByteSerializable;
import ttmp.macrocosmos.util.Join;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PokeRecipeMetadata implements ByteSerializable{
	public static PokeRecipeMetadataBuilder builder(){
		return new PokeRecipeMetadataBuilder();
	}
	public static PokeRecipeMetadata read(byte[] bytes){
		return read(new PacketBuffer(Unpooled.wrappedBuffer(bytes)));
	}
	public static PokeRecipeMetadata read(PacketBuffer buffer){
		byte b = buffer.readByte();
		List<PokemonCondition> conditions = new ArrayList<>();
		for(int i = buffer.readVarInt(); i>0; i--)
			conditions.add(PokemonCondition.readCondition(buffer));

		PokemonValue hpToWork = BitMask.get(b, 2) ? PokemonValue.readValue(buffer) : null;
		PokemonValue progressModifier = BitMask.get(b, 3) ? PokemonValue.readValue(buffer) : null;

		Set<PokeRecipeSkillBonus> skillBonuses = new HashSet<>();
		for(int i = buffer.readVarInt(); i>0; i--)
			skillBonuses.add(PokeRecipeSkillBonus.read(buffer));

		return new PokeRecipeMetadata(conditions, hpToWork, progressModifier, skillBonuses,
				BitMask.get(b, 0), BitMask.get(b, 1));
	}

	private final List<PokemonCondition> conditions;
	@Nullable private final PokemonValue hpToWorkConversionRate;
	@Nullable private final PokemonValue progress;
	private final Set<PokeRecipeSkillBonus> skillBonus;
	private final boolean overrideDefaultCondition;
	private final boolean overrideDefaultSkillBonus;

	public PokeRecipeMetadata(List<PokemonCondition> conditions,
	                          @Nullable PokemonValue hpToWorkConversionRate,
	                          @Nullable PokemonValue progress,
	                          Set<PokeRecipeSkillBonus> skillBonus,
	                          boolean overrideDefaultCondition,
	                          boolean overrideDefaultSkillBonus){
		this.conditions = conditions;
		this.hpToWorkConversionRate = hpToWorkConversionRate;
		this.progress = progress;
		this.skillBonus = skillBonus;
		this.overrideDefaultCondition = overrideDefaultCondition;
		this.overrideDefaultSkillBonus = overrideDefaultSkillBonus;
	}

	public List<PokemonCondition> getConditions(){
		return conditions;
	}
	@Nullable public PokemonValue getHpToWorkConversionRate(){
		return hpToWorkConversionRate;
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

	@Override public void write(PacketBuffer buffer){
		buffer.writeByte(BitMask.toByteMask(
				this.overrideDefaultCondition, // 0
				this.overrideDefaultSkillBonus, // 1
				this.hpToWorkConversionRate!=null, // 2
				this.progress!=null // 3
		));
		buffer.writeVarInt(this.conditions.size());
		for(PokemonCondition c : this.conditions)
			c.write(buffer);
		if(this.hpToWorkConversionRate!=null){
			this.hpToWorkConversionRate.write(buffer);
		}
		if(this.progress!=null){
			this.progress.write(buffer);
		}

		for(PokeRecipeSkillBonus b : this.skillBonus)
			b.write(buffer);

	}

	@Override public String toString(){
		return "PokeRecipeMetadata{"+
				"conditions="+Join.join(conditions)+
				", hpToWorkConversionRate="+hpToWorkConversionRate+
				", progress="+progress+
				", skillBonus="+Join.join(skillBonus)+
				", overrideDefaultCondition="+overrideDefaultCondition+
				", overrideDefaultSkillBonus="+overrideDefaultSkillBonus+
				'}';
	}
}
