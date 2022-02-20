package ttmp.macrocosmos.client;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.pc.GuiPC;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiUtils;
import ttmp.macrocosmos.combeekeeping.CombeeType;
import ttmp.macrocosmos.combeekeeping.CombeeTypes;
import ttmp.macrocosmos.jei.ingredient.PreviewPokemonFactory;
import ttmp.macrocosmos.util.Join;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.text.TextFormatting.*;

public final class PokemonHoverTooltip{
	private PokemonHoverTooltip(){}

	/**
	 * Patched version of {@link com.pixelmonmod.pixelmon.client.gui.GuiHelper#drawPokemonHoverInfo(Pokemon, int, int)}.
	 */
	@SuppressWarnings("unused")
	public static void drawPokemonHoverInfo(Pokemon pokemon, int x, int y){
		List<String> tooltips = getPokemonTooltip(pokemon);
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		boolean isPcOpen = Minecraft.getMinecraft().currentScreen instanceof GuiPC; // Need to do this I guess
		GuiUtils.drawHoveringText(tooltips, isPcOpen ? x+6 : x, y, res.getScaledWidth(), res.getScaledHeight(), -1, Minecraft.getMinecraft().fontRenderer);
	}

	public static List<String> getPokemonTooltip(Pokemon pokemon){
		List<String> tooltips = new ArrayList<>();
		boolean previewPokemon = PreviewPokemonFactory.isPreviewPokemon(pokemon);
		String genderString = "";
		if(!previewPokemon&&!pokemon.isEgg())
			switch(pokemon.getGender()){
				case Male:
					genderString = " "+BLUE+BOLD+"♂"+RESET;
					break;
				case Female:
					genderString = " "+LIGHT_PURPLE+BOLD+"♀"+RESET;
					break;
			}
		tooltips.add(pokemon.getDisplayName()+genderString);

		if(!pokemon.isEgg()){
			String form = "";
			if(!pokemon.getCustomTexture().isEmpty()){
				form = pokemon.getCustomTexture().substring(0, 1).toUpperCase()+pokemon.getCustomTexture().substring(1);
				String key = "customtexture."+pokemon.getCustomTexture()+".name";
				if(I18n.hasKey(key)){
					form = I18n.format(key);
				}

				form = I18n.format("gui.screenpokechecker.form", form);
			}else if(I18n.hasKey(pokemon.getFormEnum().getUnlocalizedName())&&
					!I18n.format(pokemon.getFormEnum().getUnlocalizedName()).equals(I18n.format("gui.trainereditor.normal"))&&
					!I18n.format(pokemon.getFormEnum().getUnlocalizedName()).equals(I18n.format("pixelmon.generic.form.noform"))){
				form = I18n.format("gui.screenpokechecker.form", I18n.format(pokemon.getFormEnum().getUnlocalizedName()));
			}
			if(!form.isEmpty()) tooltips.add(GOLD+form+RESET);

			if(pokemon.getSpecies()==EnumSpecies.Vespiquen||pokemon.getSpecies()==EnumSpecies.Combee){
				CombeeType combeeType = CombeeTypes.getCombeeType(pokemon);
				if(combeeType!=CombeeTypes.NORMAL){
					tooltips.add(GREEN+combeeType.getLocalizedName());
				}
			}

			if(!previewPokemon){
				String level = YELLOW+I18n.format("gui.screenpokechecker.lvl")+RESET+" "+pokemon.getLevel();
				String health = pokemon.getHealth()>0 ? YELLOW+I18n.format("nbt.hp.name")+RESET+" "+pokemon.getHealth()+"/"+pokemon.getMaxHealth() :
						DARK_GRAY+I18n.format("gui.creativeinv.fainted")+RESET;
				tooltips.add(level+"  "+health);
			}else{
				tooltips.add("Gen "+pokemon.getSpecies().getGeneration()+" - "+Join.join(" ", pokemon.getBaseStats().getTypeList()));
				// TODo
			}

			if(!pokemon.getHeldItem().isEmpty()){
				tooltips.add(GREEN+I18n.format("tooltip.macrocosmos.pokemon.holding", pokemon.getHeldItem().getDisplayName()));
			}
		}
		return tooltips;
	}
}
