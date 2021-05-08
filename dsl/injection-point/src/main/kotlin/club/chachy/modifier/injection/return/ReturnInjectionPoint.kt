package club.chachy.modifier.injection.`return`

import club.chachy.modifier.injection.InjectionPoint
import club.chachy.modifier.injection.`return`.opcodes.ReturnOpcodes
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

/**
 * Looks for all nodes with the RETURN opcode and inserts the instructions
 * before it is returned.
 */
object ReturnInjectionPoint : InjectionPoint {
    override fun inject(methodNode: MethodNode, instructions: InsnList) {
        val list = methodNode.instructions
        for (item in list.iterator()) {
            if (ReturnOpcodes.contains(item.opcode)) {
                list.insertBefore(item, instructions)
            }
        }
    }
}