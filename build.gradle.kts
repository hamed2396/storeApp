// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    //nav component
    id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false

    //hilt
    id("com.google.dagger.hilt.android") version "2.44" apply false

    //OTP
    id("com.google.gms.google-services") version "4.3.14" apply false
}