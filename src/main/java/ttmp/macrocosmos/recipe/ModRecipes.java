package ttmp.macrocosmos.recipe;

import com.pixelmonmod.pixelmon.enums.EnumType;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.PrimitiveRecipeBuilder;
import gregtech.api.util.ValidationResult;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ttmp.macrocosmos.MacroCosmosMod;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;
import ttmp.macrocosmos.recipe.poke.PokeRecipe;
import ttmp.macrocosmos.recipe.poke.PokeRecipeConditionIngredient;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMetadata;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMetadataBuilder;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;
import static ttmp.macrocosmos.recipe.poke.condition.PokemonCondition.queen;
import static ttmp.macrocosmos.recipe.poke.condition.PokemonCondition.type;

@Mod.EventBusSubscriber(modid = MODID)
public class ModRecipes{
	@SuppressWarnings("SameParameterValue")
	private static <T extends RecipeBuilder<T>> RecipeMap<T> newPokeRecipeMap(String id, int minInputs, int maxInputs, int minOutputs, int maxOutputs,
	                                                                          int minFluidInputs, int maxFluidInputs, int minFluidOutputs, int maxFluidOutputs,
	                                                                          T defaultRecipe, boolean isHidden){
		// Need one more item input because pokemon container is transferred as an item
		return new RecipeMap<>(MODID+"."+id, minInputs, maxInputs+1, minOutputs, maxOutputs, minFluidInputs, maxFluidInputs, minFluidOutputs, maxFluidOutputs, defaultRecipe, isHidden);
	}

	public static final RecipeMap<PrimitiveRecipeBuilder> COMBEE = newPokeRecipeMap("combee",
			0, 9, 0, 6, 0, 3, 0, 3, new PrimitiveRecipeBuilder(), false);
	public static final RecipeMap<PrimitiveRecipeBuilder> TEST = newPokeRecipeMap("test",
			1, 1, 1, 1, 0, 0, 0, 0, new PrimitiveRecipeBuilder(), false);

	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event){
		ModRecipes.registerWithMetadata(COMBEE, COMBEE.recipeBuilder()
						.duration(10000)
						.input(Blocks.DIRT)
						.output(Blocks.BEDROCK),
				PokeRecipeMetadata.builder()
						.condition(queen(CombeeTypes.NORMAL)));

		registerWithMetadata(TEST, TEST.recipeBuilder()
						.duration(10)
						.input(Blocks.DIRT)
						.output(Blocks.BEDROCK),
				PokeRecipeMetadata.builder());
		registerWithMetadata(TEST, TEST.recipeBuilder()
						.duration(10)
						.input(Items.IRON_INGOT)
						.output(Items.IRON_INGOT, 2),
				PokeRecipeMetadata.builder()
						.condition(type(EnumType.Normal)));
	}

	public static void registerWithMetadata(RecipeMap<?> recipeMap, RecipeBuilder<?> recipe, PokeRecipeMetadataBuilder metadata){
		PokeRecipeMetadata meta = metadata.build();
		if(meta.hasCondition()) recipe.notConsumable(new PokeRecipeConditionIngredient(meta));
		ValidationResult<Recipe> r = recipe.build();
		Recipe result = r.getResult();
		if(result!=null) result = new PokeRecipe(result, meta);
		MacroCosmosMod.LOGGER.warn("YOU PICEECE OF SHIT {}", result);
		recipeMap.addRecipe(new ValidationResult<>(r.getType(), result));
	}
}
