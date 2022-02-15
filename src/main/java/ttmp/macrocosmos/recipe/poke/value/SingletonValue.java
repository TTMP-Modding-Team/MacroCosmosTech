package ttmp.macrocosmos.recipe.poke.value;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;

public enum SingletonValue implements PokemonValue{
	ZERO(Types.ZERO){
		@Override public float getValue(Pokemon pokemon){
			return 0;
		}
		@Override public String toString(){
			return "0";
		}
	},
	ONE(Types.ONE){
		@Override public float getValue(Pokemon pokemon){
			return 1;
		}
		@Override public String toString(){
			return "1";
		}
	},
	LEVEL(Types.LEVEL){
		@Override public float getValue(Pokemon pokemon){
			return pokemon.getLevel();
		}
		@Override public String toString(){
			return "Level";
		}
	},
	DEGRADATION(Types.DEGRADATION){
		@Override public float getValue(Pokemon pokemon){
			return (float)Math.sqrt((double)pokemon.getHealth()/pokemon.getMaxHealth());
		}
		@Override public String toString(){
			return "Degradation";
		}
	};

	private final byte type;

	SingletonValue(byte type){
		this.type = type;
	}

	@Override public abstract float getValue(Pokemon pokemon);
	@Override public byte type(){
		return type;
	}
	@Override public void writeAdditional(PacketBuffer buffer){}
}
