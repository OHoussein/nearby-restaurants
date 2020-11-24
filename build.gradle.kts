// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.jetbrains.kotlin.konan.properties.hasProperty
import java.util.Properties
apply("checkDependencies.gradle")

buildscript {
    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(BuildPlugins.androidGradlePlugin)
        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.hiltGradlePlugin)
        classpath(BuildPlugins.gradleVersionsTrackerPlugin)
        classpath(BuildPlugins.testLoggerPlugin)
        classpath(BuildPlugins.detektPlugin)
    }
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                // This should always be `mapbox` (not your username).
                username = "mapbox"
                val secureProp= readProperties(project.rootDir.resolve("secure.properties"))
                password = if (secureProp.hasProperty(ApiKeys.MAP_BOX_SECRET_PROP_KEY)) {
                    secureProp.getProperty(ApiKeys.MAP_BOX_SECRET_PROP_KEY) as String
                } else {
                    System.getenv(ApiKeys.MAP_BOX_SECRET_PROP_KEY)
                } ?: throw IllegalArgumentException("SDK  key is not specified")
            }
        }
    }
}

tasks.register("clean").configure {
    delete("build")
}

tasks {
    withType<io.gitlab.arturbosch.detekt.Detekt> {
// Target version of the generated JVM bytecode. It is used for type resolution.
        this.jvmTarget = "1.8"
    }
}
