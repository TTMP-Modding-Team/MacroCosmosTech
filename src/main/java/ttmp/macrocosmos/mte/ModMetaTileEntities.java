package ttmp.macrocosmos.mte;

import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.util.ResourceLocation;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

public class ModMetaTileEntities{
	private static final int ID = 18000;

	public static JailMetaTileEntity JAIL;
	public static CombeeApiaryMTE COMBEE_APIARY;

	public static void init(){
		JAIL = MetaTileEntities.registerMetaTileEntity(ID, new JailMetaTileEntity(n("jail")));
		COMBEE_APIARY = MetaTileEntities.registerMetaTileEntity(ID+1, new CombeeApiaryMTE(n("combee_apiary")));
	}

	public static ResourceLocation n(String path){
		return new ResourceLocation(MODID, path);
	}
}
