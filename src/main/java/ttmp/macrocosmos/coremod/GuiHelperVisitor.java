package ttmp.macrocosmos.coremod;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import ttmp.macrocosmos.MacroCosmosMod;

import static org.objectweb.asm.Opcodes.*;

/**
 * Patches {@link com.pixelmonmod.pixelmon.client.gui.GuiHelper#drawPokemonHoverInfo(Pokemon, int, int)}.
 */
public class GuiHelperVisitor extends ClassVisitor{
	public static final String TARGET_CLASS_NAME = "com.pixelmonmod.pixelmon.client.gui.GuiHelper";
	private static final String METHOD_NAME = "drawPokemonHoverInfo";

	private boolean found;

	public GuiHelperVisitor(ClassVisitor cv){
		super(Opcodes.ASM5, cv);
	}

	@Override public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		MethodVisitor v = super.visitMethod(access, name, desc, signature, exceptions);
		if(METHOD_NAME.equals(name)){
			MacroCosmosMod.LOGGER.info("Patching drawPokemonHoverInfo...");
			found = true;
			v.visitVarInsn(ALOAD, 0);
			v.visitVarInsn(ILOAD, 1);
			v.visitVarInsn(ILOAD, 2);
			v.visitMethodInsn(INVOKESTATIC, "ttmp/macrocosmos/client/PokemonHoverTooltip",
					"drawPokemonHoverInfo",
					"(Lcom/pixelmonmod/pixelmon/api/pokemon/Pokemon;II)V", false);
			v.visitInsn(RETURN);
		}
		return v;
	}

	@Override public void visitEnd(){
		super.visitEnd();
		if(!found) throw new RuntimeException("Cannot find method "+METHOD_NAME+".");
	}
}
