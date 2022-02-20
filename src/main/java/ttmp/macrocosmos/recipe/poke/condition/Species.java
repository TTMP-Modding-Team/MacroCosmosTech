package ttmp.macrocosmos.recipe.poke.condition;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.network.PacketBuffer;

public class Species implements PokemonCondition{
	public static PokemonCondition read(PacketBuffer buffer){
		return new Species(EnumSpecies.getFromDex(buffer.readVarInt()));
	}

	private final EnumSpecies species;

	public Species(EnumSpecies species){
		this.species = species;
	}

	@Override public boolean test(Pokemon pokemon){
		return pokemon.getSpecies()==species;
	}

	@Override public byte type(){
		return Types.SPECIES;
	}

	@Override public void writeAdditional(PacketBuffer buffer){
		buffer.writeVarInt(species.getNationalPokedexInteger());
	}

	@Override public String localize(){
		return species.getLocalizedName();
	}

	@Override public String toString(){
		return species.toString();
	}
}
