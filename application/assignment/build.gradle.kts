import com.junyoung.ha.buildsrc.Libraries

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

apply {
    from("$rootDir/buildCommonDependencies/android-common-dependencies.kts")
    from("$rootDir/buildCommonDependencies/android-ui-dependencies.kts")
    from("$rootDir/buildCommonDependencies/test-dependencies.kts")
    from("$rootDir/buildCommonDependencies/test-android-dependencies.kts")
}

android {
    namespace = "com.junyoung.ha.assignment"

    defaultConfig {
        applicationId = "com.junyoung.ha.assignment"
        versionCode = 1
        versionName = "1.0"
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
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(project(":common:bloc"))
    implementation(project(":android:common:bloc_view_model"))

    implementation(project(":features:orderbook:domain"))
    implementation(project(":features:orderbook:presentation"))
    implementation(project(":features:orderbook:repository"))
    implementation(project(":features:orderbook:datasource"))
    implementation(project(":features:orderbook:ui"))

    implementation(project(":features:recenttrades:domain"))
    implementation(project(":features:recenttrades:presentation"))
    implementation(project(":features:recenttrades:repository"))
    implementation(project(":features:recenttrades:datasource"))
    implementation(project(":features:recenttrades:ui"))

    implementation(Libraries.Hilt.android)
    kapt(Libraries.Hilt.compiler)

}
