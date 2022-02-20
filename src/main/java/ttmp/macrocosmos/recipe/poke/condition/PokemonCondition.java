package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.combeekeeping.CombeeType;
import ttmp.macrocosmos.util.ByteSerializable;
import ttmp.macrocosmos.util.TypedSerializable;

import java.util.function.Predicate;

public interface PokemonCondition extends TypedSerializable, Predicate<Pokemon>{
	static PokemonCondition always(){
		return Always.ALWAYS;
	}
	static PokemonCondition never(){
		return Never.NEVER;
	}
	static PokemonCondition all(PokemonCondition... conditions){
		return All.of(conditions);
	}
	/**
	 * @deprecated Did you mean {@link PokemonCondition#always() always}?
	 */
	static PokemonCondition any(){
		return never();
	}
	static PokemonCondition any(PokemonCondition... conditions){
		return Any.of(conditions);
	}
	static PokemonCondition not(PokemonCondition condition){
		return new Not(condition);
	}
	static PokemonCondition egg(){
		return Egg.IS_EGG;
	}
	static PokemonCondition notEgg(){
		return NotEgg.NOT_EGG;
	}
	static PokemonCondition species(EnumSpecies species){
		return new Species(species);
	}
	static PokemonCondition type(EnumType type){
		return new Type(type);
	}
	static PokemonCondition vespiquen(CombeeType type){
		return new Vespiquen(type);
	}
	static PokemonCondition combeeType(CombeeType type){
		return new Combee(type);
	}

	static PokemonCondition readCondition(byte[] bytes){
		return readCondition(ByteSerializable.createBufferFromBytes(bytes));
	}
	static PokemonCondition readCondition(PacketBuffer buffer){
		switch(buffer.readByte()){
			case Types.ALL:
				return All.read(buffer);
			case Types.ANY:
				return Any.read(buffer);
			case Types.NOT:
				return Not.read(buffer);
			case Types.EGG:
				return Egg.IS_EGG;
			case Types.NOT_EGG:
				return NotEgg.NOT_EGG;
			case Types.SPECIES:
				return Species.read(buffer);
			case Types.TYPE:
				return Type.read(buffer);
			case Types.VESPIQUEN:
				return Vespiquen.read(buffer);
			case Types.ALWAYS:
				return Always.ALWAYS;
			case Types.NEVER:
				return Never.NEVER;
			case Types.COMBEE:
				return Combee.read(buffer);
		}
		return null;
	}

	/**
	 * Don't change these numbers. The moon will crash into earth and whole world will gonna collapse.
	 */
	interface Types{
		byte ALWAYS = 0;
		byte NEVER = 1;
		byte ALL = 2;
		byte ANY = 3;
		byte NOT = 4;
		byte EGG = 5;
		byte NOT_EGG = 6;
		byte SPECIES = 7;
		byte TYPE = 8;
		byte VESPIQUEN = 9;
		byte COMBEE = 10;
	}
}
