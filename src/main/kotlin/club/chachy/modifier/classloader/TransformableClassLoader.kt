package club.chachy.modifier.classloader

import club.chachy.modifier.transformer.ITransformer
import club.chachy.modifier.transformer.Priority
import java.io.File
import java.net.URLClassLoader

/**
 * A custom ClassLoader which provides us with the utility of loading class transformations/modifications.
 *
 * @author ChachyDev
 * @since 1.0.0
 */
class TransformableClassLoader : ClassLoader() {
    private val isDebugOutputEnabled = System.getProperty("modifier.bytecode.output", "false")?.toBoolean() ?: false

    override fun loadClass(name: String?): Class<*> {
        if (name == null) error("You have some big problems... (The classname is null)")

        // If the class is marked as "excluded" we ask the parent ClassLoader to load it instead.
        // In theory this would make the class "unmodifiable".
        // This is needed especially for JDK classes as those
        // cannot be transformed.
        if (exclusions.any { name.startsWith(it) }) return javaClass.classLoader.loadClass(name)

        val classPath = name.replace(".", "/") + ".class"

        val clazz = getResourceAsStream(classPath) ?: throw ClassNotFoundException()

        var bytes = clazz.readBytes()

        transformers.forEach {
            // We then transform a class and return the transformed version of that class
            // for the next transformer to use :)
            bytes = it.transform(name, bytes)
        }

        // Finally define the transformed class. Job done :)
        if (isDebugOutputEnabled) {
            File(".modifier-out/output/$classPath").also {
                it.parentFile.mkdirs()
                it.createNewFile()
                it.writeBytes(bytes)
            }
        }
        return defineClass(name, bytes, 0, bytes.size)
    }

    companion object {
        private var transformers: MutableList<ITransformer> = ArrayList()

        // Default excludes, kotlin, java, and our ClassLoader. If these aren't excluded the ClassLoader will
        // Simply, break.
        private val exclusions = mutableListOf("kotlin.", "java.", TransformableClassLoader::class.java.name, ITransformer::class.java.name)

        // Add class transformers
        fun addClassTransformer(transformer: ITransformer) {
            transformers.add(transformer)

            // Sort the transformers by their Priority annotation
            transformers = transformers.sortedBy {
                // Sort the transformers by their Priority annotation
                runCatching { it::class.java.getDeclaredAnnotation(Priority::class.java).value }.getOrNull() ?: 0
            }.toMutableList()
        }

        fun addIfAbsent(transformer: ITransformer) {
            if (!transformers.contains(transformer)) addClassTransformer(transformer)
        }

        // Add loader exclusions (Read above)
        fun addExclusion(name: String) {
            exclusions.add(name)
        }
    }
}