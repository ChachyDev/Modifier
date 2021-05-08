package club.chachy.modifier.transformer

interface ITransformer {
    fun transform(name: String, clazz: ByteArray) : ByteArray
}