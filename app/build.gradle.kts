plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.craig.weatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.craig.weatherapp"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation (libs.retrofit)
    implementation(libs.com.squareup.retrofit2.converter.gson) //(used to replaced both:
    implementation (libs.picasso)
    implementation(libs.gson)

    implementation (libs.javax.annotation.api)//added for the generate code in the "Creating the Model Class" Lec 138

   /* implementation(libs.retrofit)
    implementation (libs.converter.gson) //causes the App to crash; look for other solution
    implementation (libs.retrofit2.converter.gson) //causes the App to crash; look for other solution
    implementation (libs.picasso)
    implementation (libs.gson)
    */

//   implementation("com.library:library:1.0") {
//        exclude(group = "conflicting.group", module = "conflicting.module")
//    }



}