// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        // Version catalog handled by settings.gradle.kts
    }
}

// Version definitions - accessible via rootProject.extra["key"]
extra["composeBomVersion"] = "2024.10.00"
extra["roomVersion"] = "2.6.1"
extra["hiltVersion"] = "2.55"
extra["kspVersion"] = "2.0.21-1.0.28"
extra["navigationVersion"] = "2.7.7"
extra["workManagerVersion"] = "2.9.0"
extra["coroutinesVersion"] = "1.8.0"
extra["lifecycleVersion"] = "2.7.0"
extra["material3Version"] = "1.2.1"
extra["accompanistVersion"] = "0.32.0"
extra["serializationVersion"] = "1.6.3"
extra["coilVersion"] = "2.5.0"
extra["okioVersion"] = "3.6.0"

allprojects {
    group = "com.tuusuario.trainapp"
    version = "1.0.0"
}