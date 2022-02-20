package ttmp.macrocosmos.terminal.app;

import com.pixelmonmod.pixelmon.api.storage.PCBox;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.resources.IGuiTexture;
import gregtech.api.gui.resources.ItemStackTexture;
import gregtech.api.gui.widgets.AdvancedTextWidget;
import gregtech.api.gui.widgets.ClickButtonWidget;
import gregtech.api.terminal.app.AbstractApplication;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import ttmp.macrocosmos.gui.widget.PokemonBoxSlotController;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

public class PocketPokemonPCApp extends AbstractApplication{
	public PocketPokemonPCApp(){
		super(MODID+".pc");
	}

	@Override public IGuiTexture getIcon(){
		return new ItemStackTexture(new ItemStack(PixelmonBlocks.pc));
	}

	@Override public AbstractApplication initApp(){
		PokemonBoxSlotController controller = new PokemonBoxSlotController(gui.entityPlayer);
		addWidget(controller);

		addWidget(new AdvancedTextWidget(gui.getWidth()/2, 5, c -> {
			String name = controller.getPCBox().getName();
			if(name!=null) c.add(new TextComponentString(name));
			else c.add(new TextComponentTranslation("gui.pc.box", controller.getBoxIndex()+1));
		}, 0xFFFFFFFF));

		for(int i = 0; i<PCBox.POKEMON_PER_BOX; i++)
			addWidget(controller.newBoxSlot(gui.getWidth()/2-(18*3)+18*(i%6), 5+10+18*(i/6), i, GuiTextures.SLOT));

		for(int i = 0; i<PartyStorage.MAX_PARTY; i++)
			addWidget(controller.newPartySlot(gui.getWidth()/2-(18*3)+18*i, 5+10+18*5+15, gui.entityPlayer, i, GuiTextures.SLOT));


		addWidget(new ClickButtonWidget(gui.getWidth()/2-(18*3)-20, 5+10+18*5/2-8, 16, 16, "<-",
				clickData -> controller.moveBoxLeft()).setDisplayFunction(() -> controller.getBoxIndex()>0));
		addWidget(new ClickButtonWidget(gui.getWidth()/2+(18*3)+4, 5+10+18*5/2-8, 16, 16, "->",
				clickData -> controller.moveBoxRight()).setDisplayFunction(() -> controller.getBoxIndex()<controller.getPCStorage().getBoxCount()));
		return this;
	}
}
