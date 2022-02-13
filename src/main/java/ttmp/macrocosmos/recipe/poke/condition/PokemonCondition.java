package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.network.PacketBuffer;
import ttmp.macrocosmos.combeekeeping.CombeeType;

public interface PokemonCondition{
	boolean test(Pokemon pokemon);

	byte type();
	void writeAdditional(PacketBuffer buffer);

	default void write(PacketBuffer buffer){
		buffer.writeByte(type());
		writeAdditional(buffer);
	}

	static PokemonCondition always(){
		return Always.ALWAYS;
	}
	static PokemonCondition never(){
		return Never.NEVER;
	}
	static PokemonCondition all(PokemonCondition... conditions){
		return All.of(conditions);
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
	static PokemonCondition queen(CombeeType type){
		return new Vespiquen(type);
	}
	static PokemonCondition combeeType(CombeeType type){
		return new Combee(type);
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
