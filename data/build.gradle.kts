import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}
val STRING = "String"

//Base Url
val BASE_URL_AUTH = "BASE_URL_AUTH"
//Base Url Dev
val BASE_URL_AUTH_LINK = "\"eip-dev.byteachers.in/authentication-services/\""
android {
    namespace = "com.ranjit.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }

        var secretsExists:File = File("secrets.properties")
        if (secretsExists.exists()) {
            val secretsFile = rootProject.file("secrets.properties")
            val secrets = Properties()
            secrets.load(FileInputStream(secretsFile))
            buildConfigField("String", "CLIENT_ID", secrets["CLIENT_ID"].toString())
            buildConfigField("String", "CLIENT_SECRET",secrets["CLIENT_SECRET"].toString())
        }
        buildConfigField("String", "API_URL", "\"https://api.imgur.com/3/\"")
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
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
    //Logging
    implementation("com.jakewharton.timber:timber:5.0.1")

}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}