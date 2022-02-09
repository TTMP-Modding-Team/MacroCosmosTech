package ttmp.macrocosmos;

import com.pixelmonmod.pixelmon.Pixelmon;
import gregtech.api.terminal.TerminalRegistry;
import gregtech.api.util.GTLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ttmp.macrocosmos.capability.Caps;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;
import ttmp.macrocosmos.mte.ModMetaTileEntities;
import ttmp.macrocosmos.terminal.ModTerminalRegistry;

@Mod(modid = MacroCosmosMod.MODID,
		name = MacroCosmosMod.NAME,
		version = MacroCosmosMod.VERSION,
		dependencies = "required:gregtech@[2,);required-after:pixelmon;")
public class MacroCosmosMod{
	public static final String MODID = "macrocosmos";
	public static final String NAME = "Macro Cosmos Tech";
	public static final String VERSION = "0.1.0";

	public static final Logger LOGGER = LogManager.getLogger(NAME);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		Pixelmon.LOGGER.error("trollge");
		GTLog.logger.error("trollge2");
		ModMetaTileEntities.init();
		Caps.register();
		CombeeTypes.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event){
		ModTerminalRegistry.init();
	}
}
