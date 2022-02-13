package ttmp.macrocosmos.mte;

import net.minecraft.util.ResourceLocation;

import static gregtech.common.metatileentities.MetaTileEntities.registerMetaTileEntity;
import static ttmp.macrocosmos.MacroCosmosMod.MODID;

public class ModMetaTileEntities{
	private static final int ID = 18000;

	public static JailMetaTileEntity JAIL;
	public static CombeeApiaryMTE COMBEE_APIARY;
	public static PokeRecipeTestMTE RECIPE_TEST;

	public static void init(){
		JAIL = registerMetaTileEntity(ID, new JailMetaTileEntity(n("jail")));
		COMBEE_APIARY = registerMetaTileEntity(ID+1, new CombeeApiaryMTE(n("combee_apiary")));
		RECIPE_TEST = registerMetaTileEntity(ID+2, new PokeRecipeTestMTE(n("recipe_test")));
	}

	public static ResourceLocation n(String path){
		return new ResourceLocation(MODID, path);
	}
}
