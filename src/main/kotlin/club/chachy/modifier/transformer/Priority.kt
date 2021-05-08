package club.chachy.modifier.transformer

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Priority(val value: Int)
