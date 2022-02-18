package ttmp.macrocosmos.util;

import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class PokeRecipeWorkType implements ByteSerializable{
	public static final class DefaultWorkTypeHolder{
		private static final PokeRecipeWorkType defaultWorkType = new PokeRecipeWorkType(Collections.emptyList());
	}

	public static PokeRecipeWorkType defaultWorkType(){
		return DefaultWorkTypeHolder.defaultWorkType;
	}

	public static PokeRecipeWorkType.Builder builder(){
		return new Builder();
	}
	public static PokeRecipeWorkType read(byte[] bytes){
		return read(ByteSerializable.createBufferFromBytes(bytes));
	}
	public static PokeRecipeWorkType read(PacketBuffer buffer){
		List<Entry> entries = new ArrayList<>();
		for(int i = buffer.readVarInt(); i>0; i--){
			entries.add(new Entry(buffer.readVarInt(), buffer.readEnumValue(EnumType.class)));
		}
		return new PokeRecipeWorkType(entries);
	}

	private final List<Entry> entries = new ArrayList<>();
	private final long weightSum;

	public PokeRecipeWorkType(List<Entry> entries){
		this.entries.addAll(entries);
		int sum = 0;
		for(Entry e : this.entries)
			sum += e.weight;
		this.weightSum = sum;
	}

	public EnumType pickType(Random random){
		if(entries.isEmpty()) return EnumType.Mystery;
		long rand = random.nextLong()%weightSum;
		for(Entry e : entries){
			if(e.weight>=rand) rand -= e.weight;
			else return e.type;
		}
		return EnumType.Mystery;
	}


	public float getAverageEffectiveness(List<EnumType> types, EffectivenessCalculationLogic calculationLogic){
		if(entries.isEmpty()) return 1;
		double avgEffectiveness = 0;
		for(Entry e : entries)
			avgEffectiveness += calculationLogic.getEffectiveness(types, e.type)*e.weight;
		return (float)(avgEffectiveness/weightSum);
	}

	@Override public void write(PacketBuffer buffer){
		buffer.writeVarInt(entries.size());
		for(Entry e : entries){
			buffer.writeVarInt(e.weight);
			buffer.writeEnumValue(e.type);
		}
	}

	public static final class Builder{
		private final List<Entry> entries = new ArrayList<>();

		public Builder add(int weight, EnumType type){
			if(weight>0) entries.add(new Entry(weight, type));
			return this;
		}

		public PokeRecipeWorkType build(){
			return new PokeRecipeWorkType(entries);
		}
	}

	public static final class Entry{
		public final int weight;
		public final EnumType type;

		public Entry(int weight, EnumType type){
			if(weight<=0) throw new IllegalArgumentException("weight");
			this.weight = weight;
			this.type = Objects.requireNonNull(type);
		}
	}
}
