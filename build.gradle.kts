// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.google.gms.google.services) apply false

}
buildscript {
    val nav_version = "2.8.3"
    repositories {
        google()
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}

