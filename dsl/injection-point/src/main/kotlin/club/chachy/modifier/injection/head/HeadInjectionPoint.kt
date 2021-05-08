package club.chachy.modifier.injection.head

import club.chachy.modifier.injection.InjectionPoint
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.LineNumberNode
import org.objectweb.asm.tree.MethodNode

object HeadInjectionPoint : InjectionPoint {
    override fun inject(methodNode: MethodNode, instructions: InsnList) {
        val list = methodNode.instructions
        val node = list.firstOrNull { it is LineNumberNode } ?: list.first
        list.insert(node, instructions)
    }
}