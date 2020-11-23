// Top-level build file where you can add configuration options common to all sub-projects/modules.
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
                // Use the secret token you stored in gradle.properties as the password
                password = if (project.hasProperty("MAPBOX_DOWNLOADS_TOKEN")) {
                    project.property("MAPBOX_DOWNLOADS_TOKEN") as String
                } else {
                    System.getenv("MAPBOX_DOWNLOADS_TOKEN")
                } ?: throw IllegalArgumentException("SDK  key is not specified")
            }
        }
    }
}

tasks.register("clean").configure {
    delete("build")
}

val detektAll by tasks.registering(io.gitlab.arturbosch.detekt.Detekt::class) {
    buildUponDefaultConfig = true
    autoCorrect = true
    parallel = true
    setSource(files(projectDir))
    config.setFrom(files("$rootDir/config/detekt.yml"))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/build/**")
    exclude("**/buildSrc/**")
    exclude("**/test/**/*.kt")
    reports {
        xml.enabled = true
        html.enabled = false
        txt.enabled = false
    }
}

tasks {
    withType<io.gitlab.arturbosch.detekt.Detekt> {
// Target version of the generated JVM bytecode. It is used for type resolution.
        this.jvmTarget = "1.8"
    }
}
