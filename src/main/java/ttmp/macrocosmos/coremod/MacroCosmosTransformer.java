package ttmp.macrocosmos.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

@SuppressWarnings("unused")
public class MacroCosmosTransformer implements IClassTransformer{
	@Override public byte[] transform(String name, String transformedName, byte[] basicClass){
		if(GuiHelperVisitor.TARGET_CLASS_NAME.equals(transformedName)){
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
			classReader.accept(new GuiHelperVisitor(classWriter), 0);
			return classWriter.toByteArray();
		}
		return basicClass;
	}
}
