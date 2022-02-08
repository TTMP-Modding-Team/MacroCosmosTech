package ttmp.macrocosmos;

import com.pixelmonmod.pixelmon.Pixelmon;
import gregtech.api.util.GTLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ttmp.macrocosmos.capability.Caps;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;
import ttmp.macrocosmos.mte.ModMetaTileEntities;

@Mod(modid = MacroCosmosMod.MODID,
		name = MacroCosmosMod.NAME,
		version = MacroCosmosMod.VERSION,
		dependencies = "required:gregtech@[2,);required:pixelmon;")
public class MacroCosmosMod{
	public static final String MODID = "macrocosmos";
	public static final String NAME = "Macro Cosmos Tech";
	public static final String VERSION = "0.1.0";

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Pixelmon.LOGGER.error("trollge");
		GTLog.logger.error("trollge2");
		ModMetaTileEntities.init();
		Caps.register();
		CombeeTypes.init();
	}
}
