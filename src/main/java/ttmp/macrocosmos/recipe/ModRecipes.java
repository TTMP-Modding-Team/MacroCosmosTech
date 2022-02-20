package ttmp.macrocosmos.recipe;

import com.pixelmonmod.pixelmon.enums.EnumType;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.builders.PrimitiveRecipeBuilder;
import gregtech.api.util.ValidationResult;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import static ttmp.macrocosmos.recipe.poke.condition.PokemonCondition.*;

@Mod.EventBusSubscriber(modid = MODID)
public class ModRecipes{
	/**
	 * Recipes for all the beekeeping nonsense.<br>
	 * Expected size of pokemon input: 1, bunch of combees also help (6 of them in base apiary, each with 33%~50% of the efficiency)
	 */
	public static final PokeRecipeMap<PrimitiveRecipeBuilder> COMBEE = new PokeRecipeMap<>(MODID+".combee",
			0, 9, 0, 6, 0, 3, 0, 3,
			new PrimitiveRecipeBuilder());
	public static final PokeRecipeMap<PrimitiveRecipeBuilder> TEST_1 = new PokeRecipeMap<>(MODID+".test",
			0, 1, 0, 1, 0, 0, 0, 0,
			new PrimitiveRecipeBuilder());

	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event){
		registerWithMetadata(COMBEE, COMBEE.recipeBuilder()
						.duration(10000)
						.input(Blocks.DIRT)
						.output(Blocks.BEDROCK),
				PokeRecipeMetadata.builder()
						.conditions(vespiquen(CombeeTypes.NORMAL)));

		registerWithMetadata(TEST_1, TEST_1.recipeBuilder()
						.duration(100)
						.input(Blocks.DIRT)
						.output(Blocks.BEDROCK),
				PokeRecipeMetadata.builder()
						.conditions(always()));
		registerWithMetadata(TEST_1, TEST_1.recipeBuilder()
						.duration(100)
						.input(Items.IRON_INGOT)
						.output(Blocks.BEDROCK),
				PokeRecipeMetadata.builder()
						.conditions(always(), type(EnumType.Normal)));
	}

	public static void registerWithMetadata(PokeRecipeMap<?> recipeMap, RecipeBuilder<?> recipe, PokeRecipeMetadataBuilder metadata){
		PokeRecipeMetadata meta = metadata.build();
		recipe.notConsumable(new PokeRecipeConditionIngredient(recipeMap.getDefaultMetadata(), meta));
		ValidationResult<Recipe> r = recipe.build();
		Recipe result = r.getResult();
		if(result!=null) result = new PokeRecipe(result, meta);
		recipeMap.addRecipe(new ValidationResult<>(r.getType(), result));
	}
}
