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
