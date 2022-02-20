package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.translation.I18n;

public final class NotEgg implements PokemonCondition{
	public static final NotEgg NOT_EGG = new NotEgg();

	private NotEgg(){}

	@Override public boolean test(Pokemon pokemon){
		return !pokemon.isEgg();
	}

	@Override public byte type(){
		return Types.NOT_EGG;
	}

	@Override public void writeAdditional(PacketBuffer buffer){}

	@SuppressWarnings("deprecation") @Override public String localize(){
		return I18n.translateToLocal("pokemon_condition.macrocosmos.not_egg");
	}

	@Override public String toString(){
		return "Not Egg";
	}
}
