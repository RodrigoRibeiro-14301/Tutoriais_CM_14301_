plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "GreetingProcessorProject2"
include("annotations")
include("processor")
include("app")