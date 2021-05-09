package club.chachy.modifier

import Test
import club.chachy.modifier.classloader.TransformableClassLoader
import club.chachy.modifier.dsl.Modifier
import club.chachy.modifier.injection.InjectionPoint
import club.chachy.modifier.injection.`return`.ReturnInjectionPoint
import club.chachy.modifier.injection.head.HeadInjectionPoint
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

fun main() {
    val t = "club.chachy.modifier.ModifierTestKt"

    val clazz = Class.forName(t)
    if (clazz.classLoader !is TransformableClassLoader) {
        with("Test" to "test") {
            Modifier.inject(first, second, HeadInjectionPoint) {
                println("Injected at HEAD! :)")
            }

            Modifier.inject(first, second, ReturnInjectionPoint) {
                println("Injected before return!")
            }

            Modifier.inject(first, second, object : InjectionPoint {
                override fun inject(methodNode: MethodNode, instructions: InsnList) {
                    methodNode.instructions.insert(methodNode.instructions.first.next.next.next, instructions)
                }
            }) {
                println("Injected at a custom place")
            }
        }

        val classLoader = TransformableClassLoader()

        Thread.currentThread().contextClassLoader = classLoader
        val c = classLoader.loadClass(t)
        c.getDeclaredMethod("main").invoke(null)
    } else {
        ModifierTest().main()
    }
}

class ModifierTest {
    fun main() {
        Test().test()
    }
}