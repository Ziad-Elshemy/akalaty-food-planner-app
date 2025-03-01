plugins {
    alias(libs.plugins.android.application)
    id("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
}

android {
    namespace = "eg.iti.mad.akalaty"
    compileSdk = 35

    defaultConfig {
        applicationId = "eg.iti.mad.akalaty"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.google.code.gson:gson:2.12.1")

    //lottie
    implementation ("com.airbnb.android:lottie:6.6.2")

    //okhttp logging interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    //Room
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")

    // Declare the dependency for the Cloud Firestore library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-firestore")

    // youtube
    // Youtube Player
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.1")

    //RXJava
    implementation("io.reactivex.rxjava3:rxjava:3.1.10")
    //RXJava with android to be lifecycle aware
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

    //retrofit with RXJava
    implementation("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    //room with RXJava
    implementation("androidx.room:room-rxjava3:2.6.1")

    //google auth
    implementation("com.google.android.gms:play-services-auth:21.3.0")






}