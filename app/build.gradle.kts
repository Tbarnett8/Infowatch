plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.openapi.generator")
}

android {
    namespace = "com.example.infowatch"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.infowatch"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Java toolchain for all Java tasks
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    jvmToolchain(21)
}

// === OpenAPI generator config ===
val cleanGeneratedApi by tasks.registering(Delete::class) {
    val openApiOutputDir = layout.buildDirectory.dir("generated/openapi").get().asFile
    delete(openApiOutputDir)
}

tasks.named("openApiGenerate") {
    dependsOn(cleanGeneratedApi)
}

openApiGenerate {
    generatorName.set("kotlin")
    library.set("jvm-okhttp4")

    inputSpec.set("https://overfast-api.tekrop.fr/openapi.json")
    outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath)

    packageName.set("com.example.infowatch.api")
    apiPackage.set("com.example.infowatch.api")
    modelPackage.set("com.example.infowatch.model")

    // 👇 Disable test generation
    globalProperties.set(
        mapOf(
            "modelTests" to "false",
            "apiTests" to "false"
        )
    )
}

// Make generated code visible to Kotlin
android.sourceSets["main"].java.srcDir(
    layout.buildDirectory.dir("generated/openapi").get().asFile
)

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.material3)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // OkHttp 4
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Moshi (used by okhttp4 generator)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //Coil
    implementation(libs.coil.compose)

    //Icons
    implementation(libs.androidx.material.icons.extended)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}