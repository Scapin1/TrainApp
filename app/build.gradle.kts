plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

android {
    namespace = "com.tuusuario.trainapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tuusuario.trainapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=androidx.lifecycle.ExperimentalLifecycleApi",
            "-Xopt-in=androidx.compose.runtime.ExperimentalComposeApi",
            "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        )
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.0"
    }

    buildFeatures {
        compose = true
        dataBinding = false
    }

    packagingOptions {
        resources {
            excludes += listOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0"
            )
        }
        jniLibs {
            useLegacyPackaging = true
        }
    }

    kotlin {
        sourceSets {
            all {
                languageSettings {
                    optIn("kotlin.RequiresOptIn")
                    optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                    optIn("androidx.lifecycle.ExperimentalLifecycleApi")
                    optIn("androidx.compose.runtime.ExperimentalComposeApi")
                    optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                    optIn("androidx.compose.foundation.ExperimentalFoundationApi")
                }
            }
        }
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }
}

dependencies {
    // --- Core Android / Jetpack ---
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.fragment:fragment-ktx:1.8.3")

    // --- Compose BOM ---
    val composeBom = platform("androidx.compose:compose-bom:${rootProject.extra.get("composeBomVersion")}")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.compose.runtime:runtime-rxjava3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${rootProject.extra.get("lifecycleVersion")}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra.get("lifecycleVersion")}")
    implementation("androidx.navigation:navigation-compose:${rootProject.extra.get("navigationVersion")}")
    implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.extra.get("navigationVersion")}")

    // --- Room ---
    val room_version = rootProject.extra.get("roomVersion") as String
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-paging:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")

    // --- Hilt (manual DI for now, but keeping Hilt annotations for future) ---
    val hilt_version = rootProject.extra.get("hiltVersion") as String
    implementation("com.google.dagger:hilt-android:$hilt_version")
    ksp("com.google.dagger:hilt-compiler:$hilt_version")

    // --- Coroutines & Flow ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra.get("coroutinesVersion")}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra.get("coroutinesVersion")}")

    // --- Lifecycle & ViewModel ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra.get("lifecycleVersion")}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra.get("lifecycleVersion")}")

    // --- WorkManager ---
    val workmanager_version = rootProject.extra.get("workManagerVersion") as String
    implementation("androidx.work:work-runtime-ktx:$workmanager_version")
    testImplementation("androidx.work:work-testing:$workmanager_version")

    // --- Serialization (for backup JSON) ---
    val serialization_version = rootProject.extra.get("serializationVersion") as String
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")

    // --- Kotlinx Datetime ---
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

    // --- Coil (Image loading) ---
    val coil_version = rootProject.extra.get("coilVersion") as String
    implementation("io.coil-kt:coil-compose:$coil_version")

    // --- Okio (Backup I/O) ---
    val okio_version = rootProject.extra.get("okioVersion") as String
    implementation("com.squareup.okio:okio:$okio_version")

    // --- Gson ---
    implementation("com.google.code.gson:gson:2.11.0")

    // --- Charts: Using Compose Canvas (custom implementation) ---
    // No external chart library needed - custom Canvas charts for heatmap, line, bar

    // --- Accompanist (Permissions, etc.) ---
    val accompanist_version = rootProject.extra.get("accompanistVersion") as String
    implementation("com.google.accompanist:accompanist-permissions:$accompanist_version")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanist_version")

    // --- Material Icons Extended ---
    implementation("androidx.compose.material:material-icons-extended")

    // --- Testing ---
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.0.20")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.1")
    testImplementation("io.mockk:mockk:1.13.14")
    testImplementation("app.cash.turbine:turbine:1.0.1")
    testImplementation("org.robolectric:robolectric:4.11.1")

    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling")
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("hilt.enableAggregatingWorker", "true")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.named("check") {
    dependsOn("lintVitalRelease")
}