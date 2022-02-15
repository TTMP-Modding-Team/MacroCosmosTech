package ttmp.macrocosmos.recipe;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.builders.PrimitiveRecipeBuilder;
import gregtech.api.util.ValidationResult;
import net.minecraft.init.Blocks;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;
import ttmp.macrocosmos.recipe.poke.PokeRecipe;
import ttmp.macrocosmos.recipe.poke.PokeRecipeConditionIngredient;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMap;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMetadata;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMetadataBuilder;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;
import static ttmp.macrocosmos.recipe.poke.condition.PokemonCondition.queen;
import static ttmp.macrocosmos.recipe.poke.value.PokemonValue.*;

@Mod.EventBusSubscriber(modid = MODID)
public class ModRecipes{
	public static final PokeRecipeMap<PrimitiveRecipeBuilder> COMBEE = new PokeRecipeMap<>(MODID+"."+"combee",
			0, 9, 0, 6, 0, 3, 0, 3,
			new PrimitiveRecipeBuilder(), false);

	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event){
		registerWithMetadata(COMBEE, COMBEE.recipeBuilder()
						.duration(10000)
						.input(Blocks.DIRT)
						.output(Blocks.BEDROCK),
				PokeRecipeMetadata.builder()
						.condition(queen(CombeeTypes.NORMAL))
						.hpToWorkConversionRate(one())
						.progress(mul(
								overallStat(),
								degradation()
						)));
	}

	public static void registerWithMetadata(PokeRecipeMap<?> recipeMap, RecipeBuilder<?> recipe, PokeRecipeMetadataBuilder metadata){
		PokeRecipeMetadata meta = metadata.build();
		recipe.notConsumable(new PokeRecipeConditionIngredient(recipeMap, meta));
		ValidationResult<Recipe> r = recipe.build();
		Recipe result = r.getResult();
		if(result!=null) result = new PokeRecipe(result, meta);
		recipeMap.addRecipe(new ValidationResult<>(r.getType(), result));
	}
}
