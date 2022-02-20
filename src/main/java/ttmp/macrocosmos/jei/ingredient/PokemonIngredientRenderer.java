package ttmp.macrocosmos.jei.ingredient;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import ttmp.macrocosmos.client.PokemonHoverTooltip;

import javax.annotation.Nullable;
import java.util.List;

public class PokemonIngredientRenderer implements IIngredientRenderer<Pokemon>{
	@Override public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable Pokemon ingredient){
		if(ingredient==null) return;
		GlStateManager.enableBlend();// idk
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GuiHelper.bindPokemonSprite(ingredient, Minecraft.getMinecraft());
		GuiHelper.drawImageQuad(xPosition, yPosition, 16, 16, 0, 0, 1, 1, 0);
	}

	@Override public List<String> getTooltip(Minecraft minecraft, Pokemon pokemon, ITooltipFlag tooltipFlag){
		return PokemonHoverTooltip.getPokemonTooltip(pokemon);
	}
}
