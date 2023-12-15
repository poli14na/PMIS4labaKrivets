import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)


}


android {
    namespace = "com.example.markiyanova.bookhouse"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.markiyanova.bookhouse"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }



    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.9"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {

            kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get().toString()
            useLiveLiterals = true
    }
    packaging {
        resources {
            excludes += setOf(
                "DebugProbesKt.bin",
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/versions/9/previous-compilation-data.bin"
            )
        }
    }




}

dependencies {


    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.material)
    ksp(libs.androidx.room.compiler)
    ksp(libs.compose.destinations.ksp)
    implementation(libs.koin.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.serialization)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.lifecycle)
    implementation(libs.okhttp.logging)
    implementation(libs.paging.compose)
    implementation(libs.paging.common)
    implementation(libs.work)
    implementation(libs.datastore)
    implementation(libs.coil.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.core)
    implementation(libs.androidx.collection)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.destinations)

    debugImplementation(libs.compose.tooling)

    implementation(libs.material.icons.extended)




}


