package club.chachy.modifier.dsl

import org.objectweb.asm.tree.LocalVariableNode
import org.objectweb.asm.tree.MethodNode

class Injection(private val method: MethodNode, private val locals: List<LocalVariableNode>) {
    inline fun <reified T> local(index: Int) {
    }
}