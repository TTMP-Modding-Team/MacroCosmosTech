package ttmp.macrocosmos.terminal;

import gregtech.api.terminal.TerminalRegistry;
import ttmp.macrocosmos.terminal.app.PocketPokemonPCApp;

public class ModTerminalRegistry{
	public static void init() {
		TerminalRegistry.registerApp(new PocketPokemonPCApp());
	}
}
