package ttmp.macrocosmos.eventlisteners;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static ttmp.macrocosmos.MacroCosmosMod.MODID;

/**
 * I hate you Pixelmon
 */
@Mod.EventBusSubscriber(modid = MODID, value = Side.CLIENT)
public final class UnicodeFontPatch{
	private UnicodeFontPatch(){}

	@SubscribeEvent
	public static void beforeDrawScreen(GuiScreenEvent.DrawScreenEvent.Pre event){
		Minecraft mc = Minecraft.getMinecraft();
		mc.fontRenderer.setUnicodeFlag(mc.isUnicode());
	}

	@SubscribeEvent
	public static void afterDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event){
		Minecraft mc = Minecraft.getMinecraft();
		mc.fontRenderer.setUnicodeFlag(mc.isUnicode());
	}
}
