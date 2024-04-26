// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.google.gms) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.baseline.profile) apply false
}