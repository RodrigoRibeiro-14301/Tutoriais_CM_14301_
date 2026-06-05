package annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Greeting(val message: String)
annotation class Extract(val regex: String)