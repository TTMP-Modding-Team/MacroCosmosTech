package ttmp.macrocosmos.recipe.loaders;

import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import net.minecraft.init.Blocks;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;
import ttmp.macrocosmos.recipe.ModRecipes;
import ttmp.macrocosmos.recipe.poke.PokeRecipeCondition;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMetadata;

import static ttmp.macrocosmos.recipe.poke.condition.PokemonCondition.vespiquen;

public class ModBeeRecipes{
	public static void load(){
		ModRecipes.registerWithMetadata(ModRecipes.COMBEE,
				new SimpleRecipeBuilder()
						.duration(10000)
						.input(Blocks.DIRT)
						.notConsumable(PokeRecipeCondition.builder()
								.add(vespiquen(CombeeTypes.NORMAL))
								.ingredient())
						.output(Blocks.BEDROCK), PokeRecipeMetadata.builder());
	}
}
