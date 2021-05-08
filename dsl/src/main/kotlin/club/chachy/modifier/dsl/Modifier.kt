package club.chachy.modifier.dsl

import club.chachy.modifier.classloader.TransformableClassLoader
import club.chachy.modifier.dsl.invoker.invokerNode
import club.chachy.modifier.dsl.transformer.ModifierDSLTransformer
import club.chachy.modifier.injection.InjectionPoint
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.aload_0
import codes.som.anthony.koffee.insns.jvm.invokespecial
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode
import kotlin.reflect.KClass

object Modifier {
    private val transformer = ModifierDSLTransformer()

    data class ModifierData(
        val clazz: String,
        val method: String,
        val injectionPoint: InjectionPoint,
        val descriptor: String?,
        val block: () -> Unit
    ) {
        fun perform(node: ClassNode) {
            val blockMethodNode = methodNode(block::class)

            val invokerNode = invokerNode(blockMethodNode)

            node.methods.add(invokerNode)

            val method = findMethod(node) ?: error("INJECTION FAILED! Could not find $method")

            // Inject our method into the other method
            injectionPoint.inject(
                method,
                buildInvoker(invokerNode)
            )
        }

        private fun findMethod(node: ClassNode): MethodNode? {
            return if (descriptor != null) node.methods.find { it.name == method && it.desc == descriptor } else node.methods.find { it.name == method }
        }

        private fun methodNode(clazz: KClass<out () -> Unit>): MethodNode {
            val reader = ClassReader(clazz.java.name)
            val node = ClassNode()

            reader.accept(node, ClassReader.EXPAND_FRAMES)

            return node.methods.find { it.name == "invoke" && it.desc == "()V" } ?: error("...")
        }

        private fun buildInvoker(node: MethodNode): InsnList {
            return assembleBlock {
                aload_0
                invokespecial(clazz.replace(".", "/"), node)
            }.first
        }
    }

    internal val modifiers = ArrayList<ModifierData>()

    fun inject(
        clazz: String,
        method: String,
        injectionPoint: InjectionPoint,
        descriptor: String? = null,
        block: () -> Unit
    ) {
        TransformableClassLoader.addIfAbsent(transformer)
        modifiers.add(ModifierData(clazz, method, injectionPoint, descriptor, block))
    }

    /**
     * Existent for code to compile! Nothing else...
     * The compiler plugin should STOP usage of this
     * and replace it with the full class name
     */
    fun inject(
        clazz: KClass<*>,
        method: String,
        injectionPoint: InjectionPoint,
        descriptor: String? = null,
        block: () -> Unit
    ) {
        throw UnsupportedOperationException()
    }
}