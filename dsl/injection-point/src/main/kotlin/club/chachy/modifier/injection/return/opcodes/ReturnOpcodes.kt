package club.chachy.modifier.injection.`return`.opcodes

import org.objectweb.asm.Opcodes

object ReturnOpcodes : ArrayList<Int>() {
    init {
        add(Opcodes.RETURN)
        add(Opcodes.IRETURN)
        add(Opcodes.FRETURN)
        add(Opcodes.LRETURN)
        add(Opcodes.DRETURN)
        add(Opcodes.ARETURN)
    }
}