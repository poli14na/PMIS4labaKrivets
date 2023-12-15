// Top-level build file where you can add configuration options common to all sub-projects/modules.



import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false


}
tasks {
    wrapper {
        distributionType = Wrapper.DistributionType.BIN
    }
}

allprojects {
    tasks {
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
                languageVersion.set(KotlinVersion.KOTLIN_1_9)
            }
        }
    }
}

