package ttmp.macrocosmos.eventlisteners;

import com.pixelmonmod.pixelmon.api.events.BreedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;

import java.util.Random;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public final class VespiquenMorphHandler{
	private VespiquenMorphHandler(){}

	private static final Random RNG = new Random();

	@SubscribeEvent
	public static void onMakeEgg(BreedEvent.MakeEgg event){
		EnumSpecies sp1 = event.parent1.getSpecies();
		EnumSpecies sp2 = event.parent2.getSpecies();
		if(sp1==EnumSpecies.Vespiquen){
			if(sp2==EnumSpecies.Combee)
				checkForMorph(event.getEgg(), event.parent1, event.parent2);
		}else if(sp1==EnumSpecies.Combee&&
				sp2==EnumSpecies.Vespiquen){
			checkForMorph(event.getEgg(), event.parent2, event.parent1);
		}
	}

	private static void checkForMorph(Pokemon egg, Pokemon queen, Pokemon worker){
		CombeeTypes.setCombeeType(egg, CombeeTypes.generateType(queen, worker, RNG));
	}
}
