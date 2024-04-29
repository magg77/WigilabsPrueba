plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("androidx.navigation.safeargs.kotlin")

    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

    id("kotlin-parcelize")
}

android {
    namespace = "com.maggiver.wigilabspruebamaggiver"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.maggiver.wigilabspruebamaggiver"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            manifestPlaceholders["cleartextTrafficPermitted"] = true
            buildConfigField("String", "BUILD_TIME_NOW", "\"${rootProject.extra["API_PRUEBA_MAGGIVER"]}\"")
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        /*sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17*/

    }

    kotlinOptions {
        jvmTarget = "1.8"
        //jvmTarget = ("17")
        //freeCompilerArgs += listOf("-Xjsr305=strict", "-Xskip-runtime-version-check", "-Xskip-metadata-version-check")
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    //core-android
        implementation("androidx.core:core-ktx:1.13.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //coroutines
        implementation("androidx.activity:activity-ktx:1.9.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["coroutines_android"]}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra["coroutines_android"]}")


    //live-data & view-model
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["liveData"]}")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["liveData"]}")

    //navigation components
        implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.extra["nav_version"]}")
        implementation("androidx.navigation:navigation-ui-ktx:${rootProject.extra["nav_version"]}")
        //feature module Support
        implementation("androidx.navigation:navigation-dynamic-features-fragment:${rootProject.extra["nav_version"]}") //navegacion dinamica fragments

    //hilt
        implementation("com.google.dagger:hilt-android:${rootProject.extra["hilt_version"]}")
        implementation("androidx.legacy:legacy-support-v4:1.0.0")
        annotationProcessor("com.google.dagger:hilt-compiler:${rootProject.extra["hilt_version"]}")
        //kapt "com.google.dagger:hilt-compiler:$rootProject.hilt_version"
        ksp("com.google.dagger:hilt-compiler:${rootProject.extra["hilt_version"]}")

    //retrofit
        implementation("com.squareup.retrofit2:retrofit:${rootProject.extra["retrofit"]}")
        implementation("com.google.code.gson:gson:${rootProject.extra["retrofit"]}")
        implementation("com.squareup.retrofit2:converter-gson:${rootProject.extra["retrofit"]}")
        implementation("com.squareup.retrofit2:converter-scalars:${rootProject.extra["retrofit"]}")
        implementation ("com.squareup.okhttp3:okhttp:4.11.0")
        implementation("com.squareup.okhttp3:logging-interceptor:${rootProject.extra["logging_interceptor"]}") //log interceptor

    //room
        implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")           //compatibilidad para corrutinas destinada a las transacciones de base de datos
        implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")       //relacionado con la compilacion
        annotationProcessor("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
        //to use Kotlin annotation processing tool (kapt)
        //kapt "androidx.room:room-compiler:$room_version"
        //to use Kotlin Symbol Processing (KSP)
        ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")         //relacionado con las anotaciones @quer - @select - @delete ..
        implementation("androidx.room:room-paging:${rootProject.extra["room_version"]}")

    //recyclerView : dependencias para implementar merge adapter
        implementation("androidx.recyclerview:recyclerview:${rootProject.extra["recyclerView"]}")
        implementation("androidx.recyclerview:recyclerview-selection:${rootProject.extra["recyclerview_selection"]}") //For control over item selection of both touch and mouse driven selection
        implementation("androidx.swiperefreshlayout:swiperefreshlayout:${rootProject.extra["recyclerview_selection"]}")   //Implementa el patrón de deslizar para actualizar la IU.

    //show-images
        implementation("com.github.bumptech.glide:glide:4.15.1")

    //style layout
        implementation("androidx.core:core-splashscreen:1.0.1")
        implementation("com.google.android.material:material:${rootProject.extra["materialDesing"]}")

    //test
        //kotlinx-coroutines-debug
        /*testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:1.4.0")*/
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}