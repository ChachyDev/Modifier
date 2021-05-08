package club.chachy.modifier.dsl.transformer

import club.chachy.modifier.dsl.Modifier
import club.chachy.modifier.transformer.ITransformer
import club.chachy.modifier.transformer.Priority
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode

@Priority(-1)
class ModifierDSLTransformer : ITransformer {
    override fun transform(name: String, clazz: ByteArray): ByteArray {
        val modifiers = Modifier.modifiers.filter { it.clazz == name }

        val reader = ClassReader(clazz)
        val node = ClassNode()
        reader.accept(node, ClassReader.EXPAND_FRAMES)

        for (modifier in modifiers) {
            modifier.perform(node)
        }

        return ClassWriter(ClassWriter.COMPUTE_FRAMES).also { node.accept(it) }.toByteArray()
    }
}