@file:Suppress("unused")

object AppVersion {
    const val applicationId = "dev.ohoussein.restos"
    const val versionCode = 1
    const val versionName = "1.0"
}

object BuildPlugins {
    object Versions {
        const val androidGradlePlugin = "4.0.2"
        const val kotlinVersion = "1.4.10"
        const val daggerHiltVersion = "2.28-alpha"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val kotlinAllOpenPlugin = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlinVersion}"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.daggerHiltVersion}"
    const val gradleVersionsTrackerPlugin = "com.github.ben-manes:gradle-versions-plugin:+"
    const val testLoggerPlugin = "com.adarshr:gradle-test-logger-plugin:2.1.1"
    const val detektPlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.14.2"

    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val kotlinAndroid = "kotlin-android"
    const val kotlin = "kotlin"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val kotlinKapt = "kotlin-kapt"
    const val daggerHilt = "dagger.hilt.android.plugin"
    const val kotlinAllOpen = "kotlin-allopen"
    const val gradleVersionsTracker = "com.github.ben-manes.versions"
    const val testLogger = "com.adarshr.test-logger"
    const val detekt = "io.gitlab.arturbosch.detekt"
}

object AndroidSdk {
    const val minSdk = 21
    const val compileSdk = 30
    const val targetSdk = compileSdk
}

object Libs {
    object Versions {
        const val retrofit = "2.9.0"
        const val daggerHiltJetpack = "1.0.0-alpha02"
        const val lifecycle = "2.2.0"
        const val coroutines = "1.3.9"
        const val fragment = "1.2.5"

        const val stetho = "1.5.1"

        const val androidXTest = "1.3.0"
        const val androidJUnit = "1.1.2"
        const val espresso = "3.3.0"
        const val mockito = "2.2.0"
    }

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${BuildPlugins.Versions.kotlinVersion}"
    const val appcompat = "androidx.appcompat:appcompat:1.2.0"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0"
    const val material = "com.google.android.material:material:1.2.1"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"

    const val retofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retofitConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"

    const val hiltAndroid = "com.google.dagger:hilt-android:${BuildPlugins.Versions.daggerHiltVersion}"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${BuildPlugins.Versions.daggerHiltVersion}"
    const val hiltLifecycle = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.daggerHiltJetpack}"
    const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.daggerHiltJetpack}"

    const val timber = "com.jakewharton.timber:timber:4.7.1"

    const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
}

object DebugLibs {
    const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:4.7.2"
    const val stetho = "com.facebook.stetho:stetho:${Libs.Versions.stetho}"
    const val stethoOkhttp = "com.facebook.stetho:stetho-okhttp3:${Libs.Versions.stetho}"
}

//Android and unit tests
object TestLibs {
    const val archCoreTesting = "androidx.arch.core:core-testing:2.1.0"
    const val hiltAndroid = "com.google.dagger:hilt-android-testing:${BuildPlugins.Versions.daggerHiltVersion}"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${BuildPlugins.Versions.daggerHiltVersion}"
    const val mockito = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Libs.Versions.mockito}"
    const val mockitoInline = "org.mockito:mockito-inline:2.21.0"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Libs.Versions.coroutines}"
    const val junit = "junit:junit:4.12"
    const val mockwebserver = "com.squareup.okhttp3:mockwebserver:4.9.0"
}

object AndroidTestLibs {
    const val testCore = "androidx.test:core:${Libs.Versions.androidXTest}"
    const val testCoreKtx = "androidx.test:core-ktx:${Libs.Versions.androidXTest}"

    const val androidJUnit = "androidx.test.ext:junit:${Libs.Versions.androidJUnit}"
    const val androidJUnitKtx = "androidx.test.ext:junit-ktx:${Libs.Versions.androidJUnit}"

    const val rules = "androidx.test:rules:1.3.0"
    const val espresso = "androidx.test.espresso:espresso-core:${Libs.Versions.espresso}"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Libs.Versions.espresso}"
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Libs.Versions.fragment}"
    const val mockitoAndroid = "org.mockito:mockito-android:3.3.3"
}
