package ttmp.macrocosmos.item;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ClientChangeOpenPC;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import ttmp.macrocosmos.MacroCosmosMod;

public class PocketPokemonPC extends Item{
	public PocketPokemonPC() {
		super();
		this.setRegistryName(new ResourceLocation(MacroCosmosMod.MODID, "pocketpokemonpc"));
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.setTranslationKey(MacroCosmosMod.MODID + "." + "pocketpokemonpc");
	}

	@Override public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
		ItemStack heldItem = playerIn.getHeldItem(handIn);
		if(worldIn.isRemote) return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
		if(!(playerIn.openContainer instanceof ContainerPlayer)){
			return new ActionResult<>(EnumActionResult.FAIL, heldItem);
		}
		if(playerIn instanceof EntityPlayerMP){
			PCStorage pc = Pixelmon.storageManager.getPCForPlayer(playerIn.getUniqueID());
			Pixelmon.network.sendTo(new ClientChangeOpenPC(pc.uuid), (EntityPlayerMP)playerIn);
			OpenScreen.open(playerIn, EnumGuiScreen.PC);
			worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, PixelSounds.pc, SoundCategory.BLOCKS, 0.7F, 1.0F);
			return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
	}
}
