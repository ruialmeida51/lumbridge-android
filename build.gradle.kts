// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("kapt") version "1.9.23"
    id("com.android.application") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.46" apply false
    id("com.android.library") version "8.3.1" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.firebase.appdistribution") version "4.2.0" apply false
}
