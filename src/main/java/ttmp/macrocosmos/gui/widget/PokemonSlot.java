package ttmp.macrocosmos.gui.widget;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import gregtech.api.gui.IRenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.resources.IGuiTexture;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import ttmp.macrocosmos.capability.PokemonContainer;
import ttmp.macrocosmos.gui.widget.PokemonSlotController.PokemonSlotInterface;

import javax.annotation.Nullable;

public abstract class PokemonSlot extends Widget{
	private final PokemonSlotInterface slotInterface;
	private final IGuiTexture[] slotTextures;

	public PokemonSlot(PokemonSlotInterface slotInterface, int x, int y, IGuiTexture... slotTextures){
		super(x, y, 18, 18);
		this.slotInterface = slotInterface;
		this.slotTextures = slotTextures;
	}

	@Nullable public abstract Pokemon getPokemonForRender();

	@Override public void drawInBackground(int mouseX, int mouseY, float partialTicks, IRenderContext context){
		Position pos = this.getPosition();
		Size size = this.getSize();

		GlStateManager.disableBlend();
		GlStateManager.disableDepth();
		GlStateManager.colorMask(true, true, true, false);

		for(IGuiTexture t : slotTextures)
			t.draw(pos.x, pos.y, size.width, size.height);

		if(slotInterface.isSelected()){
			Minecraft.getMinecraft().renderEngine.bindTexture(GuiResources.pcResources);
			GuiHelper.drawImageQuad(pos.x+1.5, pos.y+1.5, 15, 15, 0, 0.11328125D, 0.11328125D, 0.2265625D, 0); // I have no idea what these numbers mean
		}

		Pokemon pokemon = getPokemonForRender();
		if(pokemon!=null){
			GuiHelper.bindPokemonSprite(pokemon, Minecraft.getMinecraft());
			GuiHelper.drawImageQuad(pos.getX()+1, pos.getY()+1, 16, 16, 0, 0, 1, 1, 0);
		}

		if(this.isActive()){
			if(this.isMouseOverElement(mouseX, mouseY))
				drawSolidRect(pos.x+1, pos.y+1, 16, 16, 0x80ffffff);
		}else drawSolidRect(pos.x+1, pos.y+1, 16, 16, 0xbf000000);

		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.enableDepth();
		GlStateManager.enableBlend();
	}

	@Override public void drawInForeground(int mouseX, int mouseY){
		if(this.isActive()&&this.isMouseOverElement(mouseX, mouseY)){
			Pokemon pokemon = getPokemonForRender();
			if(pokemon==null) return;
			GuiHelper.drawPokemonHoverInfo(pokemon, mouseX, mouseY);
		}
	}

	@Override public boolean mouseClicked(int mouseX, int mouseY, int button){
		if(!this.isMouseOverElement(mouseX, mouseY)||this.gui==null||button!=0) return false;
		if(isShiftDown()) slotInterface.quickMove();
		else slotInterface.select();
		return true;
	}

	public static class ContainerSlot extends PokemonSlot{
		private final PokemonContainer container;
		private final int index;

		public ContainerSlot(PokemonSlotInterface slotInterface, int x, int y, PokemonContainer container, int index, IGuiTexture... slotTextures){
			super(slotInterface, x, y, slotTextures);
			this.container = container;
			this.index = index;
		}

		@Nullable @Override public Pokemon getPokemonForRender(){
			return container.getPokemon(index);
		}
	}

	public static class PartySlot extends PokemonSlot{
		private final int index;

		public PartySlot(PokemonSlotInterface slotInterface, int x, int y, int index, IGuiTexture... slotTextures){
			super(slotInterface, x, y, slotTextures);
			this.index = index;
		}

		@Nullable @Override public Pokemon getPokemonForRender(){
			return ClientStorageManager.party.get(index);
		}
	}
}
