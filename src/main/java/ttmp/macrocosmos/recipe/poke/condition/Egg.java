package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.translation.I18n;

public final class Egg implements PokemonCondition{
	public static final Egg IS_EGG = new Egg();

	private Egg(){}

	@Override public boolean test(Pokemon pokemon){
		return pokemon.isEgg();
	}

	@Override public byte type(){
		return Types.EGG;
	}

	@Override public void writeAdditional(PacketBuffer buffer){}

	@SuppressWarnings("deprecation") @Override public String localize(){
		return I18n.translateToLocal("pokemon_condition.macrocosmos.egg");
	}

	@Override public String toString(){
		return "Egg";
	}
}
