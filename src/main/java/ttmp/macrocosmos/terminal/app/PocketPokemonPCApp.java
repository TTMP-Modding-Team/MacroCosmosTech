package ttmp.macrocosmos.terminal.app;

import gregtech.api.terminal.app.AbstractApplication;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.gui.widget.PokemonBoxSlotController;
import ttmp.macrocosmos.util.PokemonContainerUtil;

public class PocketPokemonPCApp extends AbstractApplication{
	private PokemonContainer pcContainer;

	public PocketPokemonPCApp(){
		super("PC");
	}

	@Override public AbstractApplication initApp(){
		PokemonBoxSlotController controller = new PokemonBoxSlotController();
		pcContainer = PokemonContainerUtil.getPC(gui.entityPlayer);
		addWidget(controller);
		for(int i = 0; i<5; i++){
			for(int j = 0; j<6; j++){
				addWidget(controller.newContainerSlot(5 + 16*j, 5 + 16*i, pcContainer, i*6+j));
			}
		}
		return this;
	}
}
