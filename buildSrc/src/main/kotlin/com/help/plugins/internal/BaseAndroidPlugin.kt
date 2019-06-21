package com.help.plugins.internal

import com.android.build.gradle.TestedExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

internal abstract class BaseAndroidPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        pluginManager.apply("kotlin-android")
        pluginManager.apply(ConfigurationPlugin::class.java)

        extensions.getByType(TestedExtension::class.java).apply {
            compileSdkVersion(COMPILE_SDK_VERSION)
            defaultConfig.apply {
                minSdkVersion(MIN_SDK_VERSION)
                targetSdkVersion(COMPILE_SDK_VERSION)
                setTestInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
            }
            dexOptions {
                it.setPreDexLibraries(true)
            }

            configureSourceSets()

            compileOptions.apply {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
        Unit
    }

    private fun TestedExtension.configureSourceSets() {
        sourceSets.all { set ->
            val withKotlin = set.java.srcDirs.map { it.path.replace("java", "kotlin") }
            set.java.setSrcDirs(set.java.srcDirs + withKotlin)
        }
    }

    companion object {
        private const val COMPILE_SDK_VERSION = 28
        private const val MIN_SDK_VERSION = 26
    }
}
