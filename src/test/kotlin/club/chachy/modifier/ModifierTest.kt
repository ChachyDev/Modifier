package club.chachy.modifier

import Test
import club.chachy.modifier.classloader.TransformableClassLoader
import club.chachy.modifier.dsl.Modifier
import club.chachy.modifier.injection.`return`.ReturnInjectionPoint
import club.chachy.modifier.injection.head.HeadInjectionPoint

fun main() {
    val t = "club.chachy.modifier.ModifierTestKt"

    val clazz = Class.forName(t)
    if (clazz.classLoader !is TransformableClassLoader) {
        with("Test" to "test") {
            Modifier.inject(first, second, HeadInjectionPoint, "()V") {
                println("Injected at HEAD! :)")
            }

            Modifier.inject(first, second, ReturnInjectionPoint, "()V") {
                println("Injected before return!")
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