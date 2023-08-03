import com.junyoung.ha.buildsrc.Libraries

plugins {
    id("com.android.library")
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
    namespace = "com.junyoung.ha.features.orderbook.ui"
}

dependencies {
    implementation(project(":common:bloc"))
    implementation(project(":android:common:bloc_view_model"))

    implementation(project(":features:common:domain"))
    implementation(project(":features:orderbook:domain"))
    implementation(project(":features:orderbook:presentation"))

    implementation(Libraries.Hilt.android)
    kapt(Libraries.Hilt.compiler)
}