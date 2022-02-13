package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;

public class All extends MultipleCondition{
	public static PokemonCondition of(PokemonCondition... conditions){
		switch(conditions.length){
			case 0:
				return Always.ALWAYS;
			case 1:
				return conditions[0];
			default:
				return new All(conditions);
		}
	}
	public static All read(PacketBuffer buffer){
		return new All(readConditions(buffer));
	}

	public All(PokemonCondition... conditions){
		super(conditions);
	}

	@Override public boolean test(Pokemon pokemon){
		for(PokemonCondition c : conditions)
			if(!c.test(pokemon)) return false;
		return true;
	}

	@Override public byte type(){
		return Types.ALL;
	}

	@Override protected String delimiter(){
		return " and ";
	}
}
