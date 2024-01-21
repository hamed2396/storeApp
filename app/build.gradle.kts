plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //kapt
    kotlin("kapt")

    //nav component
    id("androidx.navigation.safeargs.kotlin")

    //hilt
    id("com.google.dagger.hilt.android")

    //parcelable
    id("kotlin-parcelize")

    //OTP
    id("com.google.gms.google-services")
}

android {
    viewBinding.enable = true
    dataBinding.enable = true
    buildFeatures.buildConfig = true
    namespace = "com.example.storeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.storeapp"
        minSdk = 22
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    //OkHTTP client
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    //coil
    implementation("io.coil-kt:coil:2.3.0")

    //room
    val room_version = "2.6.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    //navigation component
    val navVersion = "2.5.3"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    //hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    //dataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Calligraphy
    implementation("io.github.inflationx:calligraphy3:3.1.1")
    implementation("io.github.inflationx:viewpump:2.0.3")
    val lifecycle_version = "2.6.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //pinView
    implementation("com.github.GoodieBag:Pinview:v1.5")

    //circleIndicator
    implementation("me.relex:circleindicator:2.1.6")

    //MPChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //readMore
    implementation("kr.co.prnd:readmore-textview:1.0.0")

    //persian Date Picker
    implementation("com.github.aliab:Persian-Date-Picker-Dialog:1.8.0")

    //image picker
    implementation("com.github.SimformSolutionsPvtLtd:SSImagePicker:2.0")

    //carousel recyclerView
    implementation("com.github.sparrow007:carouselrecyclerview:1.2.6")
    //Receive OTP
    implementation("com.google.android.gms:play-services-base:18.2.0")
    implementation("com.google.android.gms:play-services-auth-api-phone:18.0.1")

    //Other
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.todkars:shimmer-recyclerview:0.4.1")
    implementation("com.airbnb.android:lottie:5.2.0")
    implementation("com.github.MrNouri:DynamicSizes:1.0")

    //extension
    val utilsVersion = "2.2.10"
    //coroutines
    implementation("com.github.FunkyMuse.KAHelpers:coroutines:$utilsVersion")

    //glide
    implementation("com.github.FunkyMuse.KAHelpers:glide:$utilsVersion")
    //gson
    implementation("com.github.FunkyMuse.KAHelpers:gson:$utilsVersion")

    //the most basic extensions that rely on the basic Android APIs such as context, content providers etc...
    implementation("com.github.FunkyMuse.KAHelpers:kotlinextensions:$utilsVersion")

    //recyclerview
    implementation("com.github.FunkyMuse.KAHelpers:recyclerview:$utilsVersion")



    //retrofit
    implementation("com.github.FunkyMuse.KAHelpers:retrofit:$utilsVersion")


    //viewbinding
    implementation("com.github.FunkyMuse.KAHelpers:viewbinding:$utilsVersion")
    //extension
}