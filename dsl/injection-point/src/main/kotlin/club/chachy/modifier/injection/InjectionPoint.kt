package club.chachy.modifier.injection

import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

/**
 * InjectionPoint API
 *
 * The InjectionPoint API allows developers to create their own custom injection points or use the default ones
 * provided by the library.
 *
 * @author ChachyDev
 * @since 0.0.1
 */
interface InjectionPoint {
    /**
     * You simply use the instructions provided to you and place them inside
     * the method node that is provided to you.
     *
     * @param methodNode The method to insert the instructions into
     * @param instructions The instructions that need to be inserted into the method.
     *
     * @author ChachyDev
     * @since 0.0.1
     */
    fun inject(methodNode: MethodNode, instructions: InsnList)
}