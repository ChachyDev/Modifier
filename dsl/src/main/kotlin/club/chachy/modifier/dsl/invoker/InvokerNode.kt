package club.chachy.modifier.dsl.invoker

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodNode

private var count = 0

fun invokerNode(method: MethodNode): MethodNode {
    val node = MethodNode()
    method.accept(node)
    node.name = "generated\$injection\$$count"
    node.access = Opcodes.ACC_PRIVATE
    node.desc = method.desc
    count++
    return node
}