package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.recipe.poke.condition.PokemonCondition;
import ttmp.macrocosmos.util.TypedSerializable;

import java.text.DecimalFormat;

public interface PokemonValue extends TypedSerializable{
	DecimalFormat decimalFormat = new DecimalFormat("0.####");

	float getValue(Pokemon pokemon);

	void writeAdditional(PacketBuffer buffer);

	static PokemonValue zero(){
		return SingletonValue.ZERO;
	}
	static PokemonValue one(){
		return SingletonValue.ONE;
	}
	static PokemonValue constant(float value){
		return new ConstantValue(value);
	}
	static PokemonValue sum(PokemonValue... values){
		switch(values.length){
			case 0:
				return zero();
			case 1:
				return values[0];
			default:
				return new SumValue(values);
		}
	}
	static PokemonValue mul(PokemonValue... values){
		switch(values.length){
			case 0:
				return one();
			case 1:
				return values[0];
			default:
				return new MulValue(values);
		}
	}
	static PokemonValue avg(PokemonValue... values){
		switch(values.length){
			case 0:
				return zero();
			case 1:
				return values[0];
			default:
				return new AvgValue(values);
		}
	}
	static PokemonValue random(PokemonValue... values){
		switch(values.length){
			case 0:
				return zero();
			case 1:
				return values[0];
			default:
				return new RandomValue(values);
		}
	}
	static PokemonValue condition(PokemonCondition condition, PokemonValue onTrue){
		return new ConditionedValue(condition, onTrue);
	}
	static PokemonValue condition(PokemonCondition condition, PokemonValue onTrue, PokemonValue onFalse){
		return new SelectedValue(condition, onTrue, onFalse);
	}
	static PokemonValue stat(){
		return RawStatValue.all();
	}
	static PokemonValue stat(StatsType... stats){
		return stats.length==0 ? zero() : new RawStatValue(stats);
	}
	static PokemonValue overallStat(){
		// HP is omitted because they already contribute to operation and it uses different formula for calculation
		// defence stats were also omitted because they already contribute to operation
		return overallStat(StatsType.Attack, StatsType.SpecialAttack, StatsType.Speed);
	}
	static PokemonValue overallStat(StatsType... stats){
		return stats.length==0 ? zero() : new AdjustedStatValue(stats);
	}
	static PokemonValue statsSum(StatsType... stats){
		return stats.length==0 ? zero() : new RawStatValue(stats);
	}
	static PokemonValue level(){
		return SingletonValue.LEVEL;
	}
	static PokemonValue degradation(){
		return SingletonValue.DEGRADATION;
	}
	static PokemonValue effectiveness(EnumType type){
		return new EffectivenessValue(type);
	}

	static PokemonValue readValue(byte[] bytes){
		return readValue(new PacketBuffer(Unpooled.wrappedBuffer(bytes)));
	}
	static PokemonValue readValue(PacketBuffer buffer){
		switch(buffer.readByte()){
			case Types.ZERO:
				return SingletonValue.ZERO;
			case Types.ONE:
				return SingletonValue.ONE;
			case Types.CONST:
				return ConstantValue.read(buffer);
			case Types.SUM:
				return SumValue.read(buffer);
			case Types.MUL:
				return MulValue.read(buffer);
			case Types.AVG:
				return AvgValue.read(buffer);
			case Types.RANDOM:
				return RandomValue.read(buffer);
			case Types.CONDITION:
				return ConditionedValue.read(buffer);
			case Types.SELECT:
				return SelectedValue.read(buffer);
			case Types.RAW_STAT:
				return RawStatValue.read(buffer);
			case Types.ADJUSTED_STAT:
				return AdjustedStatValue.read(buffer);
			case Types.LEVEL:
				return SingletonValue.LEVEL;
			case Types.DEGRADATION:
				return SingletonValue.DEGRADATION;
			case Types.EFFECTIVENESS:
				return EffectivenessValue.read(buffer);
		}
		return null;
	}

	/**
	 * bla bla dont touch it bla bla
	 */
	interface Types{
		byte ZERO = 0;
		byte ONE = 1;
		byte CONST = 2;
		byte SUM = 3;
		byte MUL = 4;
		byte AVG = 5;
		byte RANDOM = 6;
		byte CONDITION = 7;
		byte SELECT = 8;
		byte RAW_STAT = 9;
		byte ADJUSTED_STAT = 10;
		byte LEVEL = 11;
		byte DEGRADATION = 12;
		byte EFFECTIVENESS = 13;
	}
}
