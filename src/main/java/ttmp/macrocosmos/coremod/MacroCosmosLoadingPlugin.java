package ttmp.macrocosmos.coremod;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

import javax.annotation.Nullable;
import java.util.Map;

@Name("MacroCosmosLoadingPlugin")
@MCVersion(ForgeVersion.mcVersion)
@TransformerExclusions("ttmp.macrocosmos.coremod.")
@SortingIndex(1002)
public class MacroCosmosLoadingPlugin implements IFMLLoadingPlugin{
	@Override public String[] getASMTransformerClass(){
		return new String[]{
				"ttmp.macrocosmos.coremod.MacroCosmosTransformer"
		};
	}
	@Override public String getModContainerClass(){
		return null;
	}
	@Nullable @Override public String getSetupClass(){
		return null;
	}
	@Override public void injectData(Map<String, Object> data){}
	@Override public String getAccessTransformerClass(){
		return null;
	}
}
