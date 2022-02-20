package ttmp.macrocosmos.mte;

import net.minecraft.util.ResourceLocation;
import ttmp.macrocosmos.recipe.ModRecipes;

import static gregtech.common.metatileentities.MetaTileEntities.registerMetaTileEntity;
import static ttmp.macrocosmos.MacroCosmosMod.MODID;

public class ModMetaTileEntities{
	private static final int ID = 18000;

	public static JailMetaTileEntity JAIL;
	public static CombeeApiaryMTE COMBEE_APIARY;
	public static PokeRecipeMTE POKE_RECIPE_TEST_1;
	public static PokeRecipeMTE POKE_RECIPE_TEST_2;
	public static PokeRecipeMTE POKE_RECIPE_TEST_3;

	public static void init(){
		JAIL = registerMetaTileEntity(ID, new JailMetaTileEntity(n("jail")));
		COMBEE_APIARY = registerMetaTileEntity(ID+1, new CombeeApiaryMTE(n("combee_apiary")));
		POKE_RECIPE_TEST_1 = registerMetaTileEntity(ID+2, new PokeRecipeMTE(n("poke_recipe_test_1"), ModRecipes.TEST_1, 1));
		POKE_RECIPE_TEST_2 = registerMetaTileEntity(ID+3, new PokeRecipeMTE(n("poke_recipe_test_2"), ModRecipes.TEST_1, 2));
		POKE_RECIPE_TEST_3 = registerMetaTileEntity(ID+4, new PokeRecipeMTE(n("poke_recipe_test_3"), ModRecipes.TEST_1, 3));
	}

	public static ResourceLocation n(String path){
		return new ResourceLocation(MODID, path);
	}
}
