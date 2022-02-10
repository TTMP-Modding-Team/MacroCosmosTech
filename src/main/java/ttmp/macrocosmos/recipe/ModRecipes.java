package ttmp.macrocosmos.recipe;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.builders.SimpleRecipeBuilder;
import gregtech.api.util.ValidationResult;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ttmp.macrocosmos.recipe.loaders.ModBeeRecipes;
import ttmp.macrocosmos.recipe.poke.PokeRecipe;
import ttmp.macrocosmos.recipe.poke.PokeRecipeMetadata;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class ModRecipes{
	public static final RecipeMap<SimpleRecipeBuilder> COMBEE = new RecipeMap<>(MODID+".combee",
			1, 16, 1, 16, 1, 16, 1, 16, new SimpleRecipeBuilder(), false);

	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event){
		ModBeeRecipes.load();
	}

	public static void registerWithMetadata(RecipeMap<?> recipeMap, RecipeBuilder<?> recipe, PokeRecipeMetadata.Builder metadata){
		ValidationResult<Recipe> r = recipe.build();
		Recipe result = r.getResult();
		if(result!=null) result = new PokeRecipe(result, metadata.build());
		recipeMap.addRecipe(new ValidationResult<>(r.getType(), result));
	}
}
