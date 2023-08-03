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
    namespace = "com.junyoung.hafeatures.recenttrade.ui"
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":common:bloc"))
    implementation(project(":android:common:bloc_view_model"))

    implementation(project(":features:common:domain"))
    implementation(project(":features:recenttrades:domain"))
    implementation(project(":features:recenttrades:presentation"))

    implementation(Libraries.Hilt.android)
    kapt(Libraries.Hilt.compiler)
}