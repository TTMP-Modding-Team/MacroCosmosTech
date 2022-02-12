package ttmp.macrocosmos.terminal.app;

import com.pixelmonmod.pixelmon.Pixelmon;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.widgets.ClickButtonWidget;
import gregtech.api.terminal.app.AbstractApplication;
import ttmp.macrocosmos.capability.PCPokemonContainer;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.gui.widget.PokemonBoxSlotController;
import ttmp.macrocosmos.util.PokemonContainerUtil;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

public class PocketPokemonPCApp extends AbstractApplication{

	public PocketPokemonPCApp(){
		super(MODID + ".pc");
	}

	@Override public AbstractApplication initApp(){
		PokemonBoxSlotController controller = new PokemonBoxSlotController();
		PokemonContainer pcContainer = PokemonContainerUtil.getPC(gui.entityPlayer);
		addWidget(controller);
		for(int i = 0; i<5; i++){
			for(int j = 0; j<6; j++){
				addWidget(controller.newBoxSlot(5 + 16*j, 5 + 16*i, pcContainer, i*6+j, GuiTextures.SLOT));
			System.out.println(i*6+j);
			}
		}
		for(int i=0; i<6; i++) addWidget(controller.newPartySlot(5+16*i, 5+16*5, gui.entityPlayer, i, GuiTextures.SLOT));
		addWidget(new ClickButtonWidget(5+16*8, 5+16*5, 16, 16, "->", clickData -> {
			System.out.println("Clicked");
			writeClientAction(PokemonBoxSlotController.RIGHT, packetBuffer -> {});
			controller.moveBoxRight();
			System.out.println(controller.getBoxIndex());
		}));
		return this;
	}
}
