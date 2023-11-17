plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.experimental.passwordpdfpoc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.experimental.passwordpdfpoc"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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

    implementation("com.google.code.gson:gson:2.10.1")
    //implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")
    //implementation ("com.gemalto.jp2:jp2-android:1.0.3")
    implementation("com.tom-roush:pdfbox-android:2.0.27.0")
    implementation("com.dmitryborodin:pdfview-android:1.1.0")

    //implementation("com.github.barteksc:android-pdf-viewer:2.8.2")
    //implementation("com.github.barteksc:android-pdf-viewer:2.8.2")

    //implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    //implementation("com.android.support:support-core-utils:28.0.0")

}